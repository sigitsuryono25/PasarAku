package com.surelabsid.lti.pasaraku.ui.favorite

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.pixplicity.easyprefs.library.Prefs
import com.surelabsid.lti.pasaraku.databinding.ActivityFavoriteBinding
import com.surelabsid.lti.pasaraku.network.NetworkModule
import com.surelabsid.lti.pasaraku.response.ResponseListIklan
import com.surelabsid.lti.pasaraku.ui.explore.adapter.AdapterIklan
import com.surelabsid.lti.pasaraku.ui.iklan.DetailIklanActivity
import com.surelabsid.lti.pasaraku.utils.Constant
import kotlinx.coroutines.*

class FavoriteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFavoriteBinding
    private lateinit var adapterIklan: AdapterIklan

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.customToolbar)
        supportActionBar?.apply {
            title = "Your Favorite Stuff"
            setDisplayHomeAsUpEnabled(true)
        }

        adapterIklan = AdapterIklan(
            isGrid = false, onClick = {
                Intent(this@FavoriteActivity, DetailIklanActivity::class.java).apply {
                    putExtra(DetailIklanActivity.DATA_IKLAN, it)
                    startActivity(this)
                }
            }, onFavClick = { data, img ->

            }
        )
        binding.rvIklan.apply {
            adapter = adapterIklan
            layoutManager = LinearLayoutManager(this@FavoriteActivity)
        }

        getFavList(Prefs.getString(Constant.EMAIL))
    }

    private fun getFavList(userid: String?) {
        CoroutineScope(Dispatchers.IO).launch {
            withContext(Dispatchers.IO) {
                try {
                    val data = NetworkModule.getService().getFavIklan(userid)
                    MainScope().launch {
                        updateUI(data)
                    }
                } catch (e: Throwable) {
                    e.printStackTrace()
                }
            }
        }
    }

    private fun updateUI(data: ResponseListIklan) {
        data.dataIklan?.let { adapterIklan.addIklan(it) }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }

}