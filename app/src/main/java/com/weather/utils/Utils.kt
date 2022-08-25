package com.weather.utils

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import com.kaopiz.kprogresshud.KProgressHUD

class Utils {

    companion object {

        @SuppressLint("StaticFieldLeak")
        private var hud: KProgressHUD? = null

        // Show Progress Dialog
        fun showProgressDialog(context: Context) {
            if (hud == null) {
                hud = KProgressHUD(context)
                hud = KProgressHUD.create(context)
                    .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                    .setBackgroundColor(Color.parseColor("#008b8a"))
                    .setLabel("Please wait...")
                    .setCancellable(false)
                    .show()
            }
        }

        // Stop Progress Dialog
        fun dismissDialog() {
            if (hud != null) {
                if (hud!!.isShowing) {
                    hud!!.dismiss()
                    hud = null
                }
            }
        }
    }
}