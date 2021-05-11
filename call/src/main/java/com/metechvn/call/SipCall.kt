package com.metechvn.call

import android.app.Activity
import android.content.Intent
import com.metechvn.call.service.LinphoneService
import org.linphone.core.ProxyConfig
import org.linphone.core.TransportType

class SipCall {
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

}