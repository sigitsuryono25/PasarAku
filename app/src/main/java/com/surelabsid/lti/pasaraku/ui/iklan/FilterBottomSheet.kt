package com.surelabsid.lti.pasaraku.ui.iklan

import android.app.Dialog
import android.content.Context
import android.content.res.Resources
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.surelabsid.lti.pasaraku.R


class FilterBottomSheet : BottomSheetDialogFragment() {
    private var behavior: BottomSheetBehavior<View>? = null
    private lateinit var model: FilterViewModel
    private var kondisi = "N"
    override fun onAttach(context: Context) {
        super.onAttach(context)

        model = ViewModelProvider(requireActivity()).get(FilterViewModel::class.java)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val bottomSheets = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        val view = View.inflate(context, R.layout.fragment_filter_bottom_sheet, null)
        val root = view.findViewById<RelativeLayout>(R.id.root)
        val params = LinearLayout.LayoutParams(
            RelativeLayout.LayoutParams.MATCH_PARENT,
            RelativeLayout.LayoutParams.MATCH_PARENT
        )
        root.layoutParams = params

        params.height = getScreenHeight() - 20
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




        this.initViews(view)
        return bottomSheets
    }

    private fun initViews(view: View) {
        val apply = view.findViewById<Button>(R.id.terapkan)
        val rgKondisi = view.findViewById<RadioGroup>(R.id.rgKondisi)
        rgKondisi.setOnCheckedChangeListener { group, checkedId ->
            if(checkedId == R.id.baru){
                kondisi = "Y"
            }

            if(checkedId == R.id.bekas){
                kondisi = "N"
            }
            if(checkedId == R.id.semua){
                kondisi = ""
            }
        }

        model.kondisi.observe(this){
            if(it == "Y"){
             rgKondisi.check(R.id.baru)
            }
            if(it == "N"){
                rgKondisi.check(R.id.bekas)
            }
            if(it.isEmpty()){
                rgKondisi.check(R.id.semua)
            }
        }

        apply.setOnClickListener {
            model.setKondisi(kondisi)
            dismiss()
        }
    }

    private fun getScreenHeight(): Int {
        return Resources.getSystem().displayMetrics.heightPixels
    }
}