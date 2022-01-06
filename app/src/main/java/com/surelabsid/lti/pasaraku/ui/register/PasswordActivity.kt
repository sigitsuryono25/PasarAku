package com.surelabsid.lti.pasaraku.ui.register

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.pixplicity.easyprefs.library.Prefs
import com.surelabsid.lti.pasaraku.MainActivity
import com.surelabsid.lti.pasaraku.databinding.ActivityPasswordBinding
import com.surelabsid.lti.pasaraku.network.NetworkModule
import com.surelabsid.lti.pasaraku.response.DataUser
import com.surelabsid.lti.pasaraku.ui.forgotpass.ForgotPasswordBottomSheet
import com.surelabsid.lti.pasaraku.utils.Constant
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PasswordActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPasswordBinding
    private lateinit var pd: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbarPassword)

        val dataUser = intent.getParcelableExtra<DataUser?>(DATAUSER)


        binding.nomorTelepon.text = "Welcome back, ${dataUser?.nomorTelepon}"
        Glide.with(this)
            .load(NetworkModule.BASE_URL + Constant.URL_IMAGE_USER + dataUser?.foto)
            .into(binding.profilePic)

        binding.login.setOnClickListener {
            if (binding.password.text.toString().isNotEmpty()) {
                pd = ProgressDialog.show(
                    this@PasswordActivity,
                    "silahkan tunggu",
                    "memverifikasi data...",
                    true,
                    false
                )
                doLogin(dataUser)
            } else {
                Toast.makeText(
                    this@PasswordActivity,
                    "Please fill password first",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        supportActionBar?.apply {
            setDisplayShowHomeEnabled(true)
            title = "Login"
        }

        binding.forgotPassword.setOnClickListener {
            val forgot = ForgotPasswordBottomSheet()
            forgot.show(supportFragmentManager, "forgot")
        }
    }

    private fun doLogin(dataUser: DataUser?) {
        CoroutineScope(Dispatchers.IO).launch {
            withContext(Dispatchers.IO) {
                try {
                    val login = NetworkModule.getService()
                        .login(dataUser?.nomorTelepon, binding.password.text.toString())
                    Prefs.putString(Constant.EMAIL, login.dataUser?.email)
                    Prefs.putString(Constant.PHONE, login.dataUser?.nomorTelepon)
                    Prefs.putString(Constant.PASSWORD, binding.password.text.toString())
                    Prefs.putString(Constant.PHOTO, login.dataUser?.foto)
                    Prefs.putString(Constant.NAME, login.dataUser?.namaLengkap)
                    Prefs.putString(Constant.FACEBOOK_TOKEN, login.dataUser?.facebookToken)
                    Prefs.putString(Constant.GOOGLE_TOKEN, login.dataUser?.googleToken)
                    Prefs.putBoolean(Constant.FROM_REGISTER, false)
                    withContext(Dispatchers.Main) {
                        pd.dismiss()
                        Intent(this@PasswordActivity, MainActivity::class.java).apply {
                            startActivity(this)
                        }
                        finishAffinity()
                    }

                } catch (e: Throwable) {
                    e.printStackTrace()
                    pd.dismiss()
                    withContext(Dispatchers.Main) {
                        Toast.makeText(
                            this@PasswordActivity,
                            e.message.toString(),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }

    }

    companion object {
        const val DATAUSER = "datauser"
    }
}