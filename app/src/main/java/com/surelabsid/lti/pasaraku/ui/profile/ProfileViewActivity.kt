package com.surelabsid.lti.pasaraku.ui.profile

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.surelabsid.lti.base.Baseapp
import com.surelabsid.lti.pasaraku.databinding.ActivityProfileViewBinding
import com.surelabsid.lti.pasaraku.network.NetworkModule
import com.surelabsid.lti.pasaraku.response.ResponseProfileView
import com.surelabsid.lti.pasaraku.ui.explore.adapter.AdapterIklan
import com.surelabsid.lti.pasaraku.utils.Constant
import kotlinx.coroutines.*

class ProfileViewActivity : Baseapp() {
    private lateinit var binding: ActivityProfileViewBinding
    private lateinit var adapterIklan: AdapterIklan
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.apply {
            title = "Profile Seller"
            setDisplayHomeAsUpEnabled(true)
        }

        val userid = intent.getStringExtra(USERID)

        adapterIklan = AdapterIklan(isGrid = false, onFavClick = { dataIklanItem, imageView ->

        }, onClick = {

        })

        binding.postIklan.apply {
            adapter = adapterIklan
            layoutManager = LinearLayoutManager(this@ProfileViewActivity)
        }

        getDetailProfile(userid)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun getDetailProfile(userid: String?) {
        showLoading()
        CoroutineScope(Dispatchers.IO).launch {
            withContext(Dispatchers.IO) {
                try {
                    val res = NetworkModule.getService().getProfile(userid)
                    MainScope().launch {
                        dismissLoading()
                        updateUI(res)
                    }
                } catch (e: Throwable) {
                    e.printStackTrace()
                    MainScope().launch {
                        dismissLoading()
                    }
                }
            }
        }
    }

    private fun updateUI(res: ResponseProfileView) {
        Glide.with(this)
            .asBitmap()
            .load(NetworkModule.BASE_URL + Constant.URL_IMAGE_USER + res.detailProfile?.user?.profilePic)
            .into(binding.imageUser)

        binding.bergabung.text = "Bergabung sejak\n ${res.detailProfile?.user?.bergabung}"
        binding.tentang.text = res.detailProfile?.user?.tentangAnda
        binding.namaLengkap.text = res.detailProfile?.user?.namaLengkap
        binding.totalPost.text = "Total Iklan\n ${res.detailProfile?.iklan?.size.toString()}"

        res.detailProfile?.iklan?.let { adapterIklan.addIklan(it) }
    }


    companion object {
        const val USERID = "userid"
    }
}