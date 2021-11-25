package com.surelabsid.lti.pasaraku.ui.iklan

import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kroegerama.imgpicker.BottomSheetImagePicker
import com.pixplicity.easyprefs.library.Prefs
import com.surelabsid.lti.pasaraku.R
import com.surelabsid.lti.pasaraku.databinding.ActivityTambahIklanBinding
import com.surelabsid.lti.pasaraku.response.DataKategoriItem
import com.surelabsid.lti.pasaraku.response.GeneralResponse
import com.surelabsid.lti.pasaraku.ui.iklan.adapter.AdapterSelectedImage
import com.surelabsid.lti.pasaraku.utils.Constant
import com.surelabsid.lti.pasaraku.utils.FileUtils
import com.surelabsid.lti.pasaraku.utils.GPSTracker
import me.abhinay.input.CurrencySymbols
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.ByteArrayOutputStream
import java.io.File


class TambahIklanActivity : AppCompatActivity(), BottomSheetImagePicker.OnImagesSelectedListener {
    private lateinit var adapterSelectedImage: AdapterSelectedImage
    private var isChanged = false
    private lateinit var binding: ActivityTambahIklanBinding
    private lateinit var multipartTypedOutput: Array<MultipartBody.Part?>
    private lateinit var vm: IklanViewModel
    private var isBaru = "Y"
    private var datakategori: DataKategoriItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTambahIklanBinding.inflate(layoutInflater)

        setContentView(binding.root)

        vm = ViewModelProvider(this).get(IklanViewModel::class.java)

        datakategori = intent.getParcelableExtra(KATEGORI_DATA)

        binding.harga.setCurrency(CurrencySymbols.INDONESIA)
        binding.harga.setDecimals(false)
        binding.harga.setDelimiter(false)

        supportActionBar?.apply {
            title = getString(R.string.add_ads)
            setDisplayHomeAsUpEnabled(true)
        }

        binding.pilihFoto.setOnClickListener {
            checkPermission()
        }

        binding.pratinjau.setOnClickListener {
            populateData()
        }

        binding.rgKondisi.setOnCheckedChangeListener { group, checkedId ->
            if (checkedId == R.id.baru) {
                isBaru = "Y"
            } else {
                isBaru = "N"
            }
        }

        val lokasi =
            if (Prefs.getString(Constant.KEC).isNotEmpty() || Prefs.contains(Constant.KEC)) {
                "${Prefs.getString(Constant.KEC)}, ${Prefs.getString(Constant.KAB)}"
            } else {
                Prefs.getString(Constant.KAB)
            }
        binding.lokasi.setText(lokasi)

        adapterSelectedImage = AdapterSelectedImage()
        binding.listFoto.apply {
            adapter = adapterSelectedImage
            layoutManager =
                LinearLayoutManager(this@TambahIklanActivity, RecyclerView.HORIZONTAL, false)
        }
    }

    private fun populateData() {
        val judul =
            binding.judulIklan.text.toString().toRequestBody("text/plain".toMediaTypeOrNull())
        val lokasi = binding.lokasi.text.toString().toRequestBody("text/plain".toMediaTypeOrNull())
        val deskripsi =
            binding.deskripsiIklan.text.toString().toRequestBody("text/plain".toMediaTypeOrNull())
        val harga =
            binding.harga.cleanIntValue.toString().toRequestBody("text/plain".toMediaTypeOrNull())

        val gps = GPSTracker(this)
        val lat = gps.latitude.toString().toRequestBody("text/plain".toMediaTypeOrNull())
        val lon = gps.longitude.toString().toRequestBody("text/plain".toMediaTypeOrNull())
        val kondisi = isBaru.toRequestBody("text/plain".toMediaTypeOrNull())

        val addedBy = "sigitsuryono25".toRequestBody("text/plain".toMediaTypeOrNull())
        val detail = "detail".toRequestBody("text/plain".toMediaTypeOrNull())
        val idKab = Prefs.getString(Constant.KAB_ID).toRequestBody("text/plain".toMediaTypeOrNull())
        val idKec =
            Prefs.getString(Constant.LOKASI_ID).toRequestBody("text/plain".toMediaTypeOrNull())
        val idProv =
            Prefs.getString(Constant.PROV_ID).toRequestBody("text/plain".toMediaTypeOrNull())
        val kategori = datakategori?.idKategori?.toRequestBody("text/plain".toMediaTypeOrNull())


        vm.sendIklan(
            judulIklan = judul,
            lokasi = lokasi,
            deskripsiIklan = deskripsi,
            harga = harga,
            lat = lat,
            lon = lon,
            foto = null,
            added_by = addedBy,
            detail = detail,
            id_kab = idKab,
            id_kec = idKec,
            id_prov = idProv,
            kategori = kategori!!,
            kondisi = kondisi
        )

        vm.data.observe(this) {
            showMessage(it)
        }
    }

    private fun showMessage(generalResponse: GeneralResponse) {
        if (generalResponse.code == 200) {
            AlertDialog.Builder(this)
                .setMessage(generalResponse.message)
                .setTitle("Konfirmasi")
                .setPositiveButton("Tutup") { _, _ ->
                    finish()
                }
                .create().show()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> confirmQuit()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun confirmQuit() {
        if (isChanged) {
            AlertDialog.Builder(this)
                .setTitle("Konfirmasi")
                .setMessage("Yakin ingin keluar? Semua tidak akan tersimpan")
                .setPositiveButton("Ya") { d, i ->
                }
                .setNegativeButton("Batal") { d, i ->
                }
                .create().show()
        } else {
            finish()
        }
    }

    override fun onBackPressed() = confirmQuit()

    private fun checkPermission() {
        if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_DENIED
        ) {
            requestPermissions(arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), 1000)
        } else {
            BottomSheetImagePicker
                .Builder(getString(R.string.file_provider))
                .multiSelect(
                    1,
                    10
                )                  //size of the columns (will be changed a little to fit)
                .requestTag("multi")                //tag can be used if multiple pickers are used
                .show(supportFragmentManager)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1000) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                binding.pilihFoto.performClick()
            }
        }
    }

    override fun onImagesSelected(uris: List<Uri>, tag: String?) {
        multipartTypedOutput = arrayOfNulls(uris.size)

        uris.forEachIndexed { i, it ->
            val file2 = File(FileUtils.getPath(this, it)).path

            val bmp = BitmapFactory.decodeFile(file2)
            val byteArrayOutputStream = ByteArrayOutputStream()
            bmp.compress(Bitmap.CompressFormat.WEBP, 70, byteArrayOutputStream)
            //createbody
            val photo = byteArrayOutputStream.toByteArray()
                .toRequestBody("image/*".toMediaTypeOrNull(), 0, byteArrayOutputStream.size())

            //create image input
            val body = MultipartBody.Part.createFormData(
                "imageFiles[]",
                File(FileUtils.getPath(this, it)).name,
                photo
            )
            multipartTypedOutput[i] = body
        }
        adapterSelectedImage.addItem(uris, true)
    }

    companion object {
        const val KATEGORI_DATA = "kategoriData"
    }
}