package com.metechvn.call.service

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.Icon
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.metechvn.call.R
import com.metechvn.call.Utils
import org.linphone.core.*
import org.linphone.mediastream.Version
import java.io.File
import java.io.IOException
import java.util.*

class LinphoneService : Service() {
    private val TAG = "LinphoneService"
    private val START_LINPHONE_LOGS: String = " ==== Device information dump ===="
    private val CHANNEL_ID = "12345"
    private val NOTIFICATION_ID = 0

    private var mCore: Core? = null
    private var mCoreListener: CoreListenerStub? = null

    private var mTimer: Timer? = null
    private var mHandler: Handler? = null

    companion object {
        var sInstance: LinphoneService? = null

        fun isReady(): Boolean {
            return sInstance != null
        }

        fun getInstance(): LinphoneService {
            return sInstance!!
        }

        fun getCore(): Core? {
            return sInstance?.mCore
        }
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        val basePath = filesDir.absolutePath
        Factory.instance().setLogCollectionPath(basePath)
        Factory.instance().enableLogCollection(LogCollectionState.Enabled)
        Factory.instance().setDebugMode(true, "sip-call")
        dumDeviceInformation()
        dumInstalledLinphoneInformation()
        mHandler = Handler(Looper.getMainLooper())
//        val notificationManager =
//            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//
//        mCoreListener = object : CoreListenerStub() {
//            @RequiresApi(Build.VERSION_CODES.O)
//            @SuppressLint("WrongConstant")
//            override fun onCallStateChanged(
//                lc: Core?,
//                call: Call?,
//                cstate: Call.State?,
//                message: String?
//            ) {
//                Log.e(TAG, "onCallStateChanged: $cstate - $message")
//
//                when (cstate) {
//                    Call.State.IncomingReceived -> {
//                        Utils.OUTCOMING = false
//                        //accept
//                        val intentAccept =
//                            Intent(this@LinphoneService, IncomingReceiver::class.java)
//                        intentAccept.action = "RECEIVE_CALL"
//                        val pendingIntentAccept = PendingIntent.getBroadcast(
//                            this@LinphoneService,
//                            0,
//                            intentAccept,
//                            PendingIntent.FLAG_UPDATE_CURRENT
//                        )
//
//                        val acceptAction = Notification.Action.Builder(
//                            Icon.createWithResource(this@LinphoneService,R.drawable.ic_phone_accept),
//                            "Accept",
//                            pendingIntentAccept
//                        ).build()
//
//                        //deny
//                        val intentDeny = Intent(this@LinphoneService, IncomingReceiver::class.java)
//                        intentDeny.action = "CANCEL_CALL"
//
//                        val pendingIntentDeny = PendingIntent.getBroadcast(
//                            this@LinphoneService, 0, intentDeny, PendingIntent.FLAG_UPDATE_CURRENT
//                        )
//
//                        val denyAction = Notification.Action.Builder(
//                            Icon.createWithResource(this@LinphoneService, R.drawable.ic_phone_deny),
//                            "Deny",
//                            pendingIntentDeny
//                        ).build()
//
//                        val notificationBuilder =
//                            Notification.Builder(this@LinphoneService, CHANNEL_ID)
//                                .setSmallIcon(R.drawable.ic_call)
//                                .setContentTitle("New Call")
//                                .setContentText("From ${getCore()?.currentCallRemoteAddress?.asStringUriOnly()}")
//                                .setAutoCancel(true)
//                                .addAction(acceptAction)
//                                .addAction(denyAction)
//                                .setCategory(NotificationCompat.CATEGORY_CALL)
//                                .setColor(Color.BLUE)
//                                .setDefaults(Notification.DEFAULT_ALL)
//                                .setPriority(NotificationCompat.PRIORITY_MAX)
//
//                        notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build())
//
//                    }
//                    Call.State.Connected -> {
//////                        if (!Utils.OUTCOMING) {
//////                            val intent = Intent(this@LinphoneService, CallActivity::class.java)
//////                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//////                            startActivity(intent)
//////                        }
//////                        notificationManager.cancel(0)
//                        notificationManager.cancel(0)
//                    }
//                    Call.State.Released -> {
//                        notificationManager.cancel(0)
//                    }
//                    else -> {
//                        Log.e(TAG, "onCallStateChanged1: $cstate" )}
//                }
//            }
//        }

        try {
            copyIfNotExist(R.raw.linphonerc_default, "$basePath/.linphonerc")
            copyFromPackage(R.raw.linphonerc_factory, "linphonerc")
        } catch (ioe: IOException) {
            ioe.printStackTrace()
        }
        mCore = Factory.instance().createCore("$basePath/.linphonerc", "$basePath/linphonerc", this)
        mCore?.addListener(mCoreListener)
        configureCore()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        if (sInstance != null) return START_STICKY
        sInstance = this
        mCore?.start()
        val lTask = object : TimerTask() {
            override fun run() {
                Handler(Looper.getMainLooper()).post {
                    if (mCore != null) mCore!!.iterate()
                }
            }
        }

        mTimer = Timer("Linphone scheduler")
        mTimer?.schedule(lTask, 0, 20)
        return START_STICKY
    }

    private fun dumDeviceInformation() {
        val sb = StringBuilder()
        sb.append("DEVICE=").append(Build.DEVICE).append("\n")
        sb.append("MODEL=").append(Build.MODEL).append("\n")
        sb.append("MANUFACTURER=").append(Build.MANUFACTURER).append("\n")
        sb.append("SDK=").append(Build.VERSION.SDK_INT).append("\n")
        sb.append("Supported ABIs=")

        for (abi in Version.getCpuAbis()) {
            sb.append(abi).append(", ")
        }
        sb.append("\n")
    }

    private fun dumInstalledLinphoneInformation() {
        var info: PackageInfo? = null
        try {
            info = packageManager.getPackageInfo(packageName, 0)
        } catch (nnfe: PackageManager.NameNotFoundException) {
            nnfe.printStackTrace()
        }
        if (info != null) {
            Log.e(
                TAG,
                "[Service] Linphone version is ${info.versionName} ${info.versionCode}"
            )
        } else {
            Log.e(TAG, "[Service] Linphone version is unknown")
        }
    }

    @Throws(IOException::class)
    private fun copyIfNotExist(ressourceId: Int, target: String) {
        val lFileToCopy = File(target)
        if (!lFileToCopy.exists()) {
            copyFromPackage(ressourceId, lFileToCopy.name)
        }
    }

    @Throws(IOException::class)
    private fun copyFromPackage(ressourceId: Int, target: String) {
        val lOutputStream = openFileOutput(target, 0)
        val lInputStream = resources.openRawResource(ressourceId)
        var readByte: Int
        val buff = ByteArray(8048)
        while (lInputStream.read(buff).also { readByte = it } != -1) {
            lOutputStream.write(buff, 0, readByte)
        }

        lOutputStream.flush()
        lOutputStream.close()
        lInputStream.close()
    }

    private fun configureCore() {
        // We will create a directory for user signed certificates if needed
        val basePath = filesDir.absolutePath
        val userCerts = "$basePath/user-certs"
        val f = File(userCerts)
        if (!f.exists()) {
            if (!f.mkdir()) {
                Log.e(TAG, "$userCerts can't be created.")
            }
        }
        mCore?.userCertificatesPath = userCerts
    }

    override fun onDestroy() {
        mCore?.removeListener(mCoreListener)
        mTimer?.cancel()
        mCore?.stop()
        mCore = null
        sInstance = null
        super.onDestroy()
    }
}