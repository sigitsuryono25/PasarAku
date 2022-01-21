package com.surelabsid.lti.pasaraku.ui.chat

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.RelativeLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.pixplicity.easyprefs.library.Prefs
import com.surelabsid.lti.pasaraku.R
import com.surelabsid.lti.pasaraku.model.firebase.messaging.ChatActivity
import com.surelabsid.lti.pasaraku.model.firebase.model.ChatHeader
import com.surelabsid.lti.pasaraku.ui.login.LoginBottomSheet
import com.surelabsid.lti.pasaraku.ui.messages.MessageActivity
import com.surelabsid.lti.pasaraku.utils.Constant

private const val chatHeader = "chatHeader"

class BottomSheetMessage : BottomSheetDialogFragment() {
    private var behavior: BottomSheetBehavior<View>? = null
    private var param1: ChatHeader? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getParcelable(chatHeader)
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val bottomSheets = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        val view = View.inflate(context, R.layout.message_bottom_sheets, null)
        val root = view.findViewById<RelativeLayout>(R.id.root)
        val params = RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.MATCH_PARENT,
            RelativeLayout.LayoutParams.MATCH_PARENT
        )
        root.layoutParams = params

        bottomSheets.setContentView(view)
        behavior = BottomSheetBehavior.from(view.parent as View)

        val startChat = view.findViewById<Button>(R.id.startChat)


        Log.d("CHATHEADER", param1.toString())
        startChat.setOnClickListener {
            if(!Prefs.contains(Constant.EMAIL)){
                val loginSheets = LoginBottomSheet()
                loginSheets.show(requireActivity().supportFragmentManager, "loginSheets")
                return@setOnClickListener
            }
            Intent(activity, MessageActivity::class.java).apply {
                putExtra("email", param1?.user_id)
                putExtra("nama", param1?.nama)
                startActivity(this)
            }
            bottomSheets.dismiss()
        }

        return bottomSheets
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: ChatHeader?) =
            BottomSheetMessage().apply {
                arguments = Bundle().apply {
                    putParcelable(chatHeader, param1)
                }
            }
    }
}