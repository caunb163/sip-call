package com.metechvn.sipcall

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.metechvn.call.SipCall

class MainActivity : AppCompatActivity() {
//    private val TAG = "MainActivity"
//    private lateinit var sipCall: SipCall
//    private lateinit var mReceiver: BroadcastReceiver
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        sipCall = SipCall()
//        sipCall.startService(this)
//
//        mReceiver = object : BroadcastReceiver(){
//            override fun onReceive(context: Context?, intent: Intent?) {
//                Log.e(TAG, "onReceive: ${intent?.action}" )
//                when(intent?.action){
//                    "Call.State.IncomingReceived" -> {
//                        Log.e(TAG, "onReceive: zzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzz" )
//                    }
//
//                    "RegistrationState.Ok" -> {
//                        Log.e(TAG, "onReceive: zxcasdfhagsjdjashfvgjasf")
//                    }
//                }
//            }
//        }

//        val handler = Handler()
//        handler.postDelayed({
//            sipCall.login("1005", "d8nTkX66*yqSc6Ut1Xfc", "call.metechvn.com:5090")
//            Log.e("TAG", "onCreate: xxxxx")
//        }, 3000)
//        handler.postDelayed({
//            sipCall.call("1006")
//            Log.e("TAG", "onCreate: cccccc")
//        }, 6000)

    }

//    override fun onStart() {
//        super.onStart()
//        val filter = IntentFilter()
//        filter.addAction("Call.State.IncomingReceived")
//        filter.addAction("RegistrationState.Ok")
//        LocalBroadcastManager.getInstance(this).registerReceiver(mReceiver, filter)
//
//    }
//
//    override fun onDestroy() {
//        super.onDestroy()
//        LocalBroadcastManager.getInstance(this).unregisterReceiver(mReceiver)
//    }
}