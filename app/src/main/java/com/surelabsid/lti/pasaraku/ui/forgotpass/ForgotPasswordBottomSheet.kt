package com.surelabsid.lti.pasaraku.ui.forgotpass

import android.app.AlertDialog
import android.app.Dialog
import android.app.ProgressDialog
import android.content.res.Resources
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.RelativeLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.textfield.TextInputEditText
import com.surelabsid.lti.pasaraku.R
import com.surelabsid.lti.pasaraku.model.EmailRequest
import com.surelabsid.lti.pasaraku.network.NetworkModule
import com.surelabsid.lti.pasaraku.utils.HourToMillis
import com.vanillaplacepicker.utils.ToastUtils
import kotlinx.coroutines.*
import retrofit2.HttpException
import java.util.*


class ForgotPasswordBottomSheet : BottomSheetDialogFragment() {
    private var behavior: BottomSheetBehavior<View>? = null
    private var pd: ProgressDialog? = null
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val bottomSheets = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        val view = View.inflate(context, R.layout.fragment_forgot_pass_bottom_sheet, null)
        val root = view.findViewById<RelativeLayout>(R.id.root)
        val params = RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.MATCH_PARENT,
            RelativeLayout.LayoutParams.MATCH_PARENT
        )
        root.layoutParams = params
        params.height = getScreenHeight()
        bottomSheets.setContentView(view)
        behavior = BottomSheetBehavior.from(view.parent as View)

        // prevent from dragging
        bottomSheets.setOnShowListener {
            val bottomSheet =
                (it as BottomSheetDialog).findViewById<View>(com.google.android.material.R.id.design_bottom_sheet) as FrameLayout?
            val behavior = BottomSheetBehavior.from(bottomSheet!!)
            behavior.state = BottomSheetBehavior.STATE_EXPANDED

            behavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
                override fun onStateChanged(bottomSheet: View, newState: Int) {
                    if (newState == BottomSheetBehavior.STATE_DRAGGING) {
                        behavior.state = BottomSheetBehavior.STATE_EXPANDED
                    }
                }

                override fun onSlide(bottomSheet: View, slideOffset: Float) {}
            })
        }

        initViews(view)

        return bottomSheets
    }

    private fun initViews(view: View) {
        val emailInput = view.findViewById<TextInputEditText>(R.id.email)
        val btnSend = view.findViewById<Button>(R.id.sendMail)
        val closeBtn = view.findViewById<ImageView>(R.id.close)

        closeBtn.setOnClickListener {
            dismiss()
        }

        btnSend.setOnClickListener {
            if (emailInput.text.toString().isNotEmpty()) {
                checkedEmail(emailInput.text.toString())
            } else {
                ToastUtils.showToast(requireActivity(), "Email is required")
            }
        }
    }

    private fun checkedEmail(email: String) {
        pd = ProgressDialog.show(requireActivity(), "", "Mengecek data....", true, false)
        CoroutineScope(Dispatchers.IO).launch {
            withContext(Dispatchers.IO) {
                try {
                    val res = NetworkModule.getService().checkedEmail(email)
                    if (res.code == 200) {
                        MainScope().launch {
                            pd?.dismiss()
                            sendEmailForgotPass(email)
                        }
                    }
                } catch (e: Throwable) {
                    when (e) {
                        is HttpException -> {
                            if (e.code() == 404) {
                                MainScope().launch {
                                    ToastUtils.showToast(requireActivity(), e.message())
                                }
                            } else if (e.code() == 500) {
                                MainScope().launch {
                                    ToastUtils.showToast(requireActivity(), e.message())
                                }
                            }
                        }
                        else -> {
                            MainScope().launch {
                                ToastUtils.showToast(requireActivity(), e.message)
                            }
                        }
                    }
                }
            }
        }
    }

    private fun sendEmailForgotPass(email: String) {
        pd?.dismiss()
        pd = ProgressDialog.show(
            requireActivity(),
            "",
            "Mengirimkan permintaan anda...",
            true,
            false
        )
        CoroutineScope(Dispatchers.IO).launch {
            withContext(Dispatchers.IO) {
                try {
                    val emailRequest = EmailRequest()
                    emailRequest.email = email
                    emailRequest.token = UUID.randomUUID().toString()
                    emailRequest.expired = HourToMillis.millisToCustomFormat(
                        HourToMillis.addExpired(2),
                        "yyyy-MM-dd HH:mm:ss"
                    )
                    emailRequest.added_on = HourToMillis.millisToCustomFormat(
                        HourToMillis.millis(),
                        "yyyy-MM-dd HH:mm:ss"
                    )
                    val res = NetworkModule.getService().sendEmail(emailRequest)
                    if (res.code == 200) {
                        MainScope().launch {
                            pd?.dismiss()
                            AlertDialog.Builder(requireActivity())
                                .setMessage(res.message.toString())
                                .setTitle("Konfirmasi")
                                .setPositiveButton("Tutup") { _, _ ->this@ForgotPasswordBottomSheet.dismiss()}
                                .create().show()
                        }
                    }
                } catch (e: Throwable) {
                    e.printStackTrace()
                    var mesg = ""
                    when (e) {
                        is HttpException -> {
                            if (e.code() == 500) {
                                mesg = e.message()
                            } else if (e.code() == 400) {
                                mesg = e.message()
                            }
                        }
                        else -> {
                            mesg = e.message.toString()
                        }
                    }

                    MainScope().launch {
                        pd?.dismiss()
                        ToastUtils.showToast(requireActivity(), mesg)
                    }
                }
            }
        }
    }

    private fun getScreenHeight(): Int {
        return Resources.getSystem().displayMetrics.heightPixels
    }
}