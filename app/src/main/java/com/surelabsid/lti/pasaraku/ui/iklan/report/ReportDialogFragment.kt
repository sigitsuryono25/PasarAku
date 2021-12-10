package com.surelabsid.lti.pasaraku.ui.iklan.report

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.DialogFragment
import com.surelabsid.lti.pasaraku.R


private const val IDADS = "idAds"

class ReportDialogFragment : DialogFragment() {

    private var idAds: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            idAds = it.getString(IDADS)
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_report_dialog, container, false)
    }

    override fun onResume() {
        super.onResume()
        // Sets the height and the width of the DialogFragment
        val width = LinearLayout.LayoutParams.MATCH_PARENT
        val height = LinearLayout.LayoutParams.MATCH_PARENT
        dialog?.window?.setLayout(width, height)
    }

    companion object {

        @JvmStatic
        fun newInstance(idAds: String?) =
            ReportDialogFragment().apply {
                arguments = Bundle().apply {
                    putString(IDADS, idAds)
                }
            }
    }
}
