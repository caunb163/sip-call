package com.metechvn.call.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.text.TextUtils
import android.util.Log
import org.linphone.core.Call
import org.linphone.core.Reason

class IncomingReceiver : BroadcastReceiver() {
    private  val TAG = "IncomingReceiver"
    override fun onReceive(context: Context?, intent: Intent?) {
        Log.e(TAG, "onReceive: ${intent?.action}" )
//        if (TextUtils.equals("RECEIVE_CALL", intent?.action)) {
//            val acceptIntent = Intent(context, CallActivity::class.java)
//            acceptIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
//            context?.startActivity(acceptIntent)
//        } else if (TextUtils.equals("CANCEL_CALL", intent?.action)) {
//            val core = LinphoneService.getCore()
//            if (core != null) {
//                val call: Call? = core.currentCall
//                call?.decline(Reason.Declined)
//            }
//        } else if (TextUtils.equals("CONECTED_CALL", intent?.action)) {
//            val acceptIntent = Intent(context, CallActivity::class.java)
//            acceptIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
//            context?.startActivity(acceptIntent)
//        }
        val it = Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)
        context?.sendBroadcast(it)
    }
}