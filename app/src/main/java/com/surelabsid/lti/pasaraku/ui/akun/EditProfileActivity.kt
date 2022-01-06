package com.surelabsid.lti.pasaraku.ui.akun

import android.app.ProgressDialog
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Base64
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.kroegerama.imgpicker.BottomSheetImagePicker
import com.kroegerama.imgpicker.ButtonType
import com.pixplicity.easyprefs.library.Prefs
import com.surelabsid.lti.pasaraku.R
import com.surelabsid.lti.pasaraku.databinding.ActivityEditProfileBinding
import com.surelabsid.lti.pasaraku.model.UserRequest
import com.surelabsid.lti.pasaraku.network.NetworkModule
import com.surelabsid.lti.pasaraku.response.DataUser
import com.surelabsid.lti.pasaraku.utils.Constant
import com.surelabsid.lti.pasaraku.utils.FileUtils
import com.vanillaplacepicker.utils.ToastUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.io.File

class EditProfileActivity : AppCompatActivity(),
    BottomSheetImagePicker.OnImagesSelectedListener {
    private lateinit var binding: ActivityEditProfileBinding
    private lateinit var pd: ProgressDialog
    private var isPickingUpPict = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.apply {
            title = "Edit Profile"
            setDisplayHomeAsUpEnabled(true)
        }

        val detailProfile = intent.getParcelableExtra<DataUser?>(USERDATA)

        setUI(detailProfile)

        binding.editFoto.setOnClickListener {
            BottomSheetImagePicker
                .Builder(getString(R.string.file_provider))
                .cameraButton(ButtonType.Button)
                .galleryButton(ButtonType.Button)
                .requestTag("single")                //tag can be used if multiple pickers are used
                .show(supportFragmentManager)
        }
        binding.save.setOnClickListener {
            val userRequest = UserRequest()
            userRequest.email = binding.email.text.toString()
            userRequest.nomor_telepon = binding.nomorTelepon.text.toString()
            userRequest.password = Prefs.getString(Constant.PASSWORD)
            if (isPickingUpPict)
                userRequest.foto = Prefs.getString(Constant.PHOTO)
            userRequest.nama_lengkap = binding.namaLengkap.text.toString()
            userRequest.facebook_token = Prefs.getString(Constant.FACEBOOK_TOKEN)
            userRequest.google_token = Prefs.getString(Constant.GOOGLE_TOKEN)
            userRequest.tentang_anda = binding.tentang.text.toString()
            userRequest.mode = "edit"
            userRequest.old_email = Prefs.getString(Constant.EMAIL)
            sendData(userRequest)
        }
    }

    private fun sendData(userRequest: UserRequest) {
        pd = ProgressDialog.show(
            this,
            "Silahkan tunggu",
            "mengirimkan data...",
            true,
            false
        )
        CoroutineScope(Dispatchers.IO).launch {
            withContext(Dispatchers.IO) {
                try {
                    val data = NetworkModule.getService().registerUser(userRequest)
                    withContext(Dispatchers.Main) {
                        pd.dismiss()
                        ToastUtils.showToast(this@EditProfileActivity, data.message)
                        Prefs.putString(Constant.PHONE, binding.nomorTelepon.text.toString())
                        Prefs.putString(Constant.EMAIL, binding.email.text.toString())
                        if (binding.password.text.toString().isNotEmpty()) {
                            Prefs.putString(Constant.PASSWORD, binding.password.text.toString())
                        }

                    }
                } catch (t: Throwable) {
                    t.printStackTrace()
                    withContext(Dispatchers.Main) {
                        pd.dismiss()
                        ToastUtils.showToast(this@EditProfileActivity, t.message)
                    }
                }
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setUI(detailProfile: DataUser?) {
        binding.namaLengkap.setText(detailProfile?.namaLengkap)
        binding.email.setText(detailProfile?.email)
        binding.nomorTelepon.setText(detailProfile?.nomorTelepon)
        binding.tentang.setText(detailProfile?.tentangAnda)

        Glide.with(this)
            .asBitmap()
            .load(NetworkModule.BASE_URL + Constant.URL_IMAGE_USER + detailProfile?.foto)
            .into(binding.imageUser)
    }

    companion object {
        const val USERDATA = "userdata"
    }

    override fun onImagesSelected(uris: List<Uri>, tag: String?) {
        pd = ProgressDialog.show(this, "", "mengkonversi gambar", true, false)
        uris.forEachIndexed { i, it ->
            val file2 = File(FileUtils.getPath(this@EditProfileActivity, it)).path

            CoroutineScope(Dispatchers.IO).launch {
                withContext(Dispatchers.IO) {
                    val bmp = BitmapFactory.decodeFile(file2)
                    val byteArrayOutputStream = ByteArrayOutputStream()
                    bmp.compress(Bitmap.CompressFormat.WEBP, 70, byteArrayOutputStream)

                    val photo = byteArrayOutputStream.toByteArray()
                    Prefs.putString(Constant.PHOTO, Base64.encodeToString(photo, Base64.DEFAULT))
                    withContext(Dispatchers.Main) {
                        Glide.with(this@EditProfileActivity)
                            .load(file2)
                            .circleCrop()
                            .into(binding.imageUser)
                    }

                    pd.dismiss()
                }

            }


        }
    }
}