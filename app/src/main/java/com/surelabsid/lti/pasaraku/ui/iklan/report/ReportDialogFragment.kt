package com.surelabsid.lti.pasaraku.ui.iklan.report

import android.app.AlertDialog
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.pixplicity.easyprefs.library.Prefs
import com.surelabsid.lti.pasaraku.R
import com.surelabsid.lti.pasaraku.databinding.FragmentReportDialogBinding
import com.surelabsid.lti.pasaraku.network.NetworkModule
import com.surelabsid.lti.pasaraku.response.GeneralResponse
import com.surelabsid.lti.pasaraku.utils.Constant
import kotlinx.android.parcel.Parcelize
import kotlinx.coroutines.*


private const val IDADS = "idAds"

class ReportDialogFragment : DialogFragment() {

    private var idAds: String? = null
    private lateinit var binding: FragmentReportDialogBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            idAds = it.getString(IDADS)
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentReportDialogBinding.bind(
            inflater.inflate(
                R.layout.fragment_report_dialog,
                container,
                false
            )
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val reportModel = ReportModel()


        binding.rgReport.setOnCheckedChangeListener { group, _ ->
            val selectedId: Int = group.checkedRadioButtonId
            val radioButton = view.findViewById<RadioButton>(selectedId)

            reportModel.laporan = radioButton.text.toString()
        }

        binding.kirimLaporan.setOnClickListener {
            if (binding.komentar.text.toString().isEmpty() || reportModel.laporan == null) {
                Toast.makeText(
                    requireActivity(),
                    "Pilih Laporan dan tambahkan komentar terlebih dahulu",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                reportModel._id_ads = idAds
                reportModel.komentar = binding.komentar.text.toString()
                reportModel.added_by = Prefs.getString(Constant.EMAIL)

                sendReport(reportModel)
            }
        }
    }

    private fun sendReport(reportModel: ReportModel) {
        CoroutineScope(Dispatchers.IO).launch {
            withContext(Dispatchers.IO) {
                try {
                    val data = NetworkModule.getService().sendReport(reportModel)
                    MainScope().launch {
                        updateUI(data)
                    }
                } catch (t: Throwable) {
                    t.printStackTrace()
                }
            }
        }
    }

    private fun updateUI(data: GeneralResponse) {
        if (data.code == 200) {
            AlertDialog.Builder(requireActivity())
                .setMessage(data.message)
                .setTitle("informasi")
                .setPositiveButton("tutup") { d, i ->
                    d.dismiss()
                    this@ReportDialogFragment.dismiss()
                }
                .create().show()
        } else {
            Toast.makeText(requireActivity(), data.message, Toast.LENGTH_SHORT).show()
        }
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

@Parcelize
data class ReportModel(
    var _id_ads: String? = null,
    var laporan: String? = null,
    var komentar: String? = null,
    var added_by: String? = null,
) : Parcelable
