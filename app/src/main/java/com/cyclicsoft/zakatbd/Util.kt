package com.cyclicsoft.zakatbd

import android.app.Activity
import android.content.Intent

object Util {

    fun gotoActivity(fromActivity: Activity, toActivity: Activity, keepAlive: Boolean) {
        val intent = Intent(fromActivity, toActivity::class.java)
        fromActivity.startActivity(intent)
        if (!keepAlive) {
            fromActivity.finish()
        }
    }
}
