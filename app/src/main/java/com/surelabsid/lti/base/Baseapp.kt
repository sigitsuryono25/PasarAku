package com.surelabsid.lti.base

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.surelabsid.lti.pasaraku.R

open class Baseapp : AppCompatActivity() {
    private lateinit var pd: Dialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    fun showLoading() {
        pd = Dialog(this)
        pd.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
        pd.setContentView(R.layout.progress_view)
        if (!pd.isShowing)
            pd.show()
    }

    fun dismissLoading() {
        if (pd.isShowing)
            pd.dismiss()
    }
}