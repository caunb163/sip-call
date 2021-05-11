package com.metechvn.call

import android.app.Activity
import android.content.Intent
import android.util.Log
import com.metechvn.call.service.LinphoneService
import org.linphone.core.*

class SipCall {
    private val core: Core? = LinphoneService.getCore()

    fun configCore(activity: Activity) {
        activity.startService(Intent(activity, LinphoneService::class.java))
    }

    fun login(username: String, password: String, domain: String) {
        var mAccountCreator = LinphoneService.getCore()?.createAccountCreator(null)
        mAccountCreator?.username = username
        mAccountCreator?.password = password
        mAccountCreator?.domain = domain
        mAccountCreator?.transport = TransportType.Tcp
        val cfg: ProxyConfig = mAccountCreator!!.createProxyConfig()
        LinphoneService.getCore()?.defaultProxyConfig = cfg
    }

    fun call(number: String) {
        if (core != null) {
            val addressToCall: Address? = core.interpretUrl(number)
            val params: CallParams = core.createCallParams(null)
            params.enableAudio(true)
            if (addressToCall != null)
                core.inviteAddressWithParams(addressToCall, params)
        }
    }

}