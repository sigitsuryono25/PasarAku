package com.surelabsid.lti.pasaraku.ui.login

import android.app.Dialog
import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import android.view.View
import android.widget.*
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.surelabsid.lti.pasaraku.R
import com.surelabsid.lti.pasaraku.network.NetworkModule
import com.surelabsid.lti.pasaraku.ui.WebBrowserActivity
import com.surelabsid.lti.pasaraku.ui.register.RegisterPhoneActivity


class LoginBottomSheet : BottomSheetDialogFragment() {

    //    R.layout.fragment_login_bottom_sheet
    private var behavior: BottomSheetBehavior<View>? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val bottomSheets = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        val view = View.inflate(context, R.layout.fragment_login_bottom_sheet, null)
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
        val finish = view.findViewById<ImageView>(R.id.close)
        finish.setOnClickListener {
            dismiss()
        }

        val terms = view.findViewById<TextView>(R.id.terms)
        terms.setOnClickListener {
            val url = NetworkModule.BASE_URL + "index.php/welcome/privacy"
            Intent(requireActivity(), WebBrowserActivity::class.java).apply {
                putExtra(WebBrowserActivity.URL, url)
                startActivity(this)
            }
        }

        view.findViewById<Button>(R.id.continueWithPhone).setOnClickListener {
            Intent(requireActivity(), RegisterPhoneActivity::class.java).apply {
                startActivity(this)
            }
        }
    }

    private fun getScreenHeight(): Int {
        return Resources.getSystem().displayMetrics.heightPixels
    }
}