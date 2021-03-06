package com.surelabsid.lti.pasaraku.ui.register

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.mukesh.countrypicker.CountryPicker
import com.pixplicity.easyprefs.library.Prefs
import com.surelabsid.lti.base.Baseapp
import com.surelabsid.lti.pasaraku.databinding.ActivityRegisterPhoneBinding
import com.surelabsid.lti.pasaraku.network.NetworkModule
import com.surelabsid.lti.pasaraku.utils.Constant
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit

class RegisterPhoneActivity : Baseapp() {

    private lateinit var binding: ActivityRegisterPhoneBinding
    private lateinit var countryPicker: CountryPicker
    private var storedVerificationId = ""
    private lateinit var resendToken: PhoneAuthProvider.ForceResendingToken
    private lateinit var phoneAuthCredential: PhoneAuthCredential
    private var cleanNumber = ""

    private val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        override fun onVerificationCompleted(p0: PhoneAuthCredential) {
            signInWithPhoneCredential(p0)
        }

        override fun onVerificationFailed(p0: FirebaseException) {
            if (p0 is FirebaseAuthInvalidCredentialsException) {
                Toast.makeText(
                    this@RegisterPhoneActivity,
                    "Invalid Credentials",
                    Toast.LENGTH_SHORT
                ).show()
            } else if (p0 is FirebaseTooManyRequestsException) {
                Toast.makeText(
                    this@RegisterPhoneActivity,
                    "Too Many Request. Try again later",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        override fun onCodeSent(p0: String, p1: PhoneAuthProvider.ForceResendingToken) {
            super.onCodeSent(p0, p1)
            storedVerificationId = p0
            resendToken = p1
            binding.flipper.displayedChild = 1
            binding.verifikasiLayout.nomorTelepon.text = cleanNumber
        }

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterPhoneBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getCountryCodeBySIm()

        binding.countryCode.setOnClickListener {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(binding.countryCode.windowToken, 0)
            countryPicker.showBottomSheet(this)
        }

        binding.next.setOnClickListener {
            if (binding.phoneNumber.text.toString().isEmpty()) {
                Toast.makeText(
                    this@RegisterPhoneActivity,
                    "Isi nomor telepon terlebih dahulu",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                val countryCode = binding.countryCode.text.toString()
                var number = binding.phoneNumber.text.toString()
                if (number.first() == '0') {
                    number = number.substring(0, 1).replace("0", "")
                    number += binding.phoneNumber.text.toString()
                        .substring(1, binding.phoneNumber.text.toString().length)
                }

                cleanNumber = countryCode + number
                checkNumber(cleanNumber)
            }
        }
        binding.verifikasiLayout.verifikasiBtn.setOnClickListener {
            val credentials = PhoneAuthProvider.getCredential(
                storedVerificationId,
                binding.verifikasiLayout.verifikasiKode.text.toString()
            )
            Firebase.auth.signInWithCredential(credentials).addOnSuccessListener {
                Prefs.putString(Constant.UID, it.user?.uid)
                Prefs.putString(Constant.PHONE, cleanNumber)
                Intent(this@RegisterPhoneActivity, BasicInfoActivity::class.java).apply {
                    startActivity(this)
                }
            }
        }
    }

    private fun getCountryCodeBySIm() {
        countryPicker = CountryPicker.Builder()
            .sortBy(CountryPicker.SORT_BY_NAME)
            .canSearch(true)
            .listener {
                binding.countryCode.setText(it.dialCode)
            }
            .build()

        binding.countryCode.setText("+62")
    }

    private fun checkNumber(cleanNumber: String) {
        showLoading()
        CoroutineScope(Dispatchers.IO).launch {
            withContext(Dispatchers.IO) {
                try {
                    val checkPhoneNumber = NetworkModule.getService().checkPhone("$cleanNumber")
                    if (checkPhoneNumber.code == 200) {
                        withContext(Dispatchers.Main) {
                            dismissLoading()
                            Intent(this@RegisterPhoneActivity, PasswordActivity::class.java).apply {
                                putExtra(PasswordActivity.DATAUSER, checkPhoneNumber.dataUser)
                                startActivity(this)
                            }
                        }
                    } else {
//                        withContext(Dispatchers.Main) {
//                            initializePhoneAuth(cleanNumber)
//                        }
                        withContext(Dispatchers.Main) {
                            dismissLoading()
                            Prefs.putString(Constant.PHONE, cleanNumber)
                            Intent(
                                this@RegisterPhoneActivity,
                                BasicInfoActivity::class.java
                            ).apply {
                                startActivity(this)
                            }
                        }
                    }
                } catch (e: Throwable) {
                    e.printStackTrace()
//                    withContext(Dispatchers.Main) {
//                        initializePhoneAuth(cleanNumber)
//                    }

                    withContext(Dispatchers.Main) {
                        dismissLoading()
                        Prefs.putString(Constant.PHONE, cleanNumber)
                        Intent(
                            this@RegisterPhoneActivity,
                            BasicInfoActivity::class.java
                        ).apply {
                            startActivity(this)
                        }
                    }
                }
            }
        }
    }


    private fun initializePhoneAuth(cleanNumber: String) {
        val options = PhoneAuthOptions.newBuilder(Firebase.auth)
            .setPhoneNumber(cleanNumber)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(this)
            .setCallbacks(callbacks)
            .build()

        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    private fun signInWithPhoneCredential(credential: PhoneAuthCredential) {
        Firebase.auth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = task.result?.user
                    Toast.makeText(
                        this@RegisterPhoneActivity,
                        user?.displayName.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {

                    }
                }
            }
    }

}