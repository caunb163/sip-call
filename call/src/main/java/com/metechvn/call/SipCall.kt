package com.metechvn.call

import android.app.Activity
import android.content.Intent
import android.util.Log
import com.metechvn.call.service.LinphoneService
import org.linphone.core.*

class SipCall {

    fun login(username: String, password: String, domain: String) {
        val mAccountCreator = LinphoneService.getCore()?.createAccountCreator(null)
        mAccountCreator?.username = username
        mAccountCreator?.password = password
        mAccountCreator?.domain = domain
        mAccountCreator?.transport = TransportType.Tcp
        val cfg: ProxyConfig? = mAccountCreator?.createProxyConfig()
        LinphoneService.getCore()?.defaultProxyConfig = cfg
    }

    fun call(number: String) {
            val addressToCall: Address? =  LinphoneService.getCore()?.interpretUrl(number)
            val params: CallParams? =  LinphoneService.getCore()?.createCallParams(null)
            params?.enableAudio(true)
            if (addressToCall != null)
                LinphoneService.getCore()?.inviteAddressWithParams(addressToCall, params)
    }

}