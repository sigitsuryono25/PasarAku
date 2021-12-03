package com.surelabsid.lti.pasaraku.ui.register.ui

import android.app.ProgressDialog
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Base64
import android.view.View
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.kroegerama.imgpicker.BottomSheetImagePicker
import com.kroegerama.imgpicker.ButtonType
import com.pixplicity.easyprefs.library.Prefs
import com.surelabsid.lti.pasaraku.R
import com.surelabsid.lti.pasaraku.databinding.FragmentAddPictureProfileBinding
import com.surelabsid.lti.pasaraku.utils.Constant
import com.surelabsid.lti.pasaraku.utils.FileUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.io.File

class AddPictureProfileFragment : Fragment(R.layout.fragment_add_picture_profile),
    BottomSheetImagePicker.OnImagesSelectedListener {
    private lateinit var binding: FragmentAddPictureProfileBinding
    private var isPickPicture = false
    private lateinit var pd: ProgressDialog

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentAddPictureProfileBinding.bind(view)

        binding.next.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.containerBasicInfo, AddEmailFragment()).commit()
        }

        binding.imagePicker.setOnClickListener {
            isPickPicture = true
            BottomSheetImagePicker
                .Builder(getString(R.string.file_provider))
                .cameraButton(ButtonType.Button)
                .galleryButton(ButtonType.Button)
                .requestTag("single")                //tag can be used if multiple pickers are used
                .show(childFragmentManager)

        }
    }

    override fun onImagesSelected(uris: List<Uri>, tag: String?) {
        pd = ProgressDialog.show(requireActivity(), "", "mengkonversi gambar", true, false)
        uris.forEachIndexed { i, it ->
            val file2 = File(FileUtils.getPath(requireActivity(), it)).path

            CoroutineScope(Dispatchers.IO).launch {
                withContext(Dispatchers.IO) {
                    val bmp = BitmapFactory.decodeFile(file2)
                    val byteArrayOutputStream = ByteArrayOutputStream()
                    bmp.compress(Bitmap.CompressFormat.WEBP, 70, byteArrayOutputStream)

                    val photo = byteArrayOutputStream.toByteArray()
                    Prefs.putString(Constant.PHOTO, Base64.encodeToString(photo, Base64.DEFAULT))
                    withContext(Dispatchers.Main) {
                        Glide.with(requireActivity())
                            .load(file2)
                            .circleCrop()
                            .into(binding.imagePicker)
                    }

                    pd.dismiss()
                }

            }


        }
    }


}