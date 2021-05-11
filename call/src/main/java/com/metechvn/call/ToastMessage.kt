package com.metechvn.call

import android.content.Context
import android.widget.Toast

class ToastMessage {
    companion object {

    }
    fun show(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}