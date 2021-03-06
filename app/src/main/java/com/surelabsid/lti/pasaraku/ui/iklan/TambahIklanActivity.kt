package com.surelabsid.lti.pasaraku.ui.iklan

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kroegerama.imgpicker.BottomSheetImagePicker
import com.kroegerama.imgpicker.ButtonType
import com.pixplicity.easyprefs.library.Prefs
import com.surelabsid.currencyformatedittext.CurrencyEdittext
import com.surelabsid.lti.base.Baseapp
import com.surelabsid.lti.pasaraku.R
import com.surelabsid.lti.pasaraku.databinding.ActivityTambahIklanBinding
import com.surelabsid.lti.pasaraku.model.FotoIklan
import com.surelabsid.lti.pasaraku.network.NetworkModule
import com.surelabsid.lti.pasaraku.response.DataIklanItem
import com.surelabsid.lti.pasaraku.response.DataKategoriItem
import com.surelabsid.lti.pasaraku.response.GeneralResponse
import com.surelabsid.lti.pasaraku.ui.WebBrowserActivity
import com.surelabsid.lti.pasaraku.ui.iklan.adapter.AdapterSelectedImage
import com.surelabsid.lti.pasaraku.ui.kategori.KategoriActivity
import com.surelabsid.lti.pasaraku.ui.wilayah.WilayahActivity
import com.surelabsid.lti.pasaraku.utils.Constant
import com.surelabsid.lti.pasaraku.utils.FileUtils
import com.surelabsid.lti.pasaraku.utils.GPSTracker
import kotlinx.coroutines.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.ByteArrayOutputStream
import java.io.File


class TambahIklanActivity : Baseapp(), BottomSheetImagePicker.OnImagesSelectedListener {
    private lateinit var adapterSelectedImage: AdapterSelectedImage
    private var isChanged = false
    private var isAdded = true
    private lateinit var binding: ActivityTambahIklanBinding
    private var multipartTypedOutput: MutableList<MultipartBody.Part?>? = null
    private var previousFotoDelete: MutableList<RequestBody> = mutableListOf()
    private lateinit var vm: IklanViewModel
    private var isBaru = "Y"
    private var datakategori: DataKategoriItem? = null
    private var PROVID: String? = null
    private var KABID: String? = null
    private var KECID: String? = null
    private var isEdit: Boolean = false
    private var kategori: String? = null
    private var mode: RequestBody? = null
    private var iklanId: RequestBody? = null
    private var isPickPlace = false
    private var mTitleEdit: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTambahIklanBinding.inflate(layoutInflater)

        setContentView(binding.root)

        vm = ViewModelProvider(this)[IklanViewModel::class.java]
//        Toast.makeText(this, "$provIdTemp, $kabIdTemp, $kecIdTemp", Toast.LENGTH_SHORT).show()

        datakategori = intent.getParcelableExtra(KATEGORI_DATA)
        kategori = datakategori?.idKategori
        mode = "add".toRequestBody("text/plain".toMediaTypeOrNull())

//        binding.harga.setCurrency(CurrencySymbols.INDONESIA)
//        binding.harga.setDecimals(false)
//        binding.harga.setDelimiter(false)
        binding.harga.setSeparator(".")
        binding.harga.setCurrencySymbol(CurrencyEdittext.INDONESIA)

        mTitleEdit = intent.getStringExtra("title")

        supportActionBar?.apply {
            if (mTitleEdit?.isEmpty() == true) {
                title = getString(R.string.add_ads)
            } else {
                title = mTitleEdit
            }
            setDisplayHomeAsUpEnabled(true)
        }
        binding.lokasi.setOnClickListener {
            isPickPlace = true
            Intent(this@TambahIklanActivity, WilayahActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                putExtra(WilayahActivity.PROVINSI_REQ, true)
                if (isEdit) {
                    putExtra(WilayahActivity.ISEDIT, true)
                }
                startActivity(this)
            }
        }

        binding.pilihFoto.setOnClickListener {
            checkPermission()
        }

        binding.pratinjau.setOnClickListener {
            if (binding.judulIklan.text.toString()
                    .isEmpty() || binding.deskripsiIklan.text.toString()
                    .isEmpty() || binding.harga.text.toString().isEmpty() ||
                binding.lokasi.text.toString().isEmpty()
            ) {
                Toast.makeText(this, "Isi semua informasi yang disediakan", Toast.LENGTH_SHORT)
                    .show()
            } else
                populateData()
        }

        binding.rgKondisi.setOnCheckedChangeListener { group, checkedId ->
            isBaru = if (checkedId == R.id.baru) {
                "Y"
            } else {
                "N"
            }
        }

        getLokasi()

        adapterSelectedImage = AdapterSelectedImage()
        binding.listFoto.apply {
            adapter = adapterSelectedImage
            layoutManager =
                LinearLayoutManager(this@TambahIklanActivity, RecyclerView.HORIZONTAL, false)
        }

        val adsItem = intent.getParcelableExtra<DataIklanItem?>(ADS_DATA)
        if (adsItem != null) {
            isEdit = true
            mode = "edit".toRequestBody("text/plain".toMediaTypeOrNull())
            updateUIForEdit(adsItem)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        if (mTitleEdit?.isNotEmpty() == true)
            if (isAdded)
                menu?.add(0, 0, 0, "Help")?.setIcon(R.drawable.ic_help_)
                    ?.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS)
        isAdded = false
        return super.onCreateOptionsMenu(menu)
    }


    private fun updateUIForEdit(adsItem: DataIklanItem) {
        binding.fotoSebelumnya.visibility = View.VISIBLE
        binding.kategori.visibility = View.VISIBLE
        binding.deskripsiIklan.setText(adsItem.deskripsiIklan)
        binding.harga.setText(adsItem.harga?.replace("Rp", "")?.replace(".", ""))
        binding.judulIklan.setText(adsItem.judulIklan)
        binding.lokasi.text = adsItem.lokasi
        kategori = adsItem.kategori
        binding.kategori.text = adsItem.namaKategori
        iklanId = adsItem.iklanId?.toRequestBody("text/plain".toMediaTypeOrNull())
        PROVID = adsItem.idProv
        KECID = adsItem.idKec
        KABID = adsItem.idKab

        binding.kategori.setOnClickListener {
            Intent(this@TambahIklanActivity, KategoriActivity::class.java).apply {
                putExtra("edit", "edit")
                startActivityForResult(this, 1258)
            }
        }

        if (adsItem.kondisi?.equals("bekas", true) == true) {
            binding.bekas.isChecked = true
        } else {
            binding.baru.isChecked = true
        }
        if (adsItem.foto?.isNotEmpty() == true) {
            adsItem.foto.map { file ->
                var toDelete = false
                val img = ImageView(this)
                val lParams = LinearLayout.LayoutParams(750, 700)
                lParams.setMargins(0, 0, 20, 0)
                img.layoutParams = lParams
                val urlGambar = Constant.ADS_PIC_URL + adsItem.iklanId + "/" + file
                Glide.with(this)
                    .load(urlGambar)
                    .centerCrop()
                    .into(img)
                var index = 0
                img.setOnClickListener {
                    if (!toDelete) {
                        val photo = file!!.toRequestBody("text/plain".toMediaTypeOrNull())
                        img.alpha = 0.4F
                        previousFotoDelete.add(index, photo)
                        toDelete = true
                        index++
                        Toast.makeText(this@TambahIklanActivity, "Selected", Toast.LENGTH_SHORT)
                            .show()
                    } else if (toDelete) {
                        val fotoIklan = FotoIklan()
                        fotoIklan.file = file
                        img.alpha = 1F
                        previousFotoDelete.removeAt(--index)
                        toDelete = false
                        Toast.makeText(this@TambahIklanActivity, "Unselected", Toast.LENGTH_SHORT)
                            .show()
                    }
                }


                Log.d("updateUIForEdit", "updateUIForEdit: ${previousFotoDelete.size}")

                binding.containerPrevious.addView(img, -1)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 1258 && resultCode == Activity.RESULT_OK) {
            Log.d(
                "onActivityResult",
                "onActivityResult: ${data?.getParcelableExtra<DataKategoriItem?>("kategoriItem")}"
            )
            data?.let {
                val dataKategoriIntent = it.getParcelableExtra<DataKategoriItem?>("kategoriItem")
                kategori = dataKategoriIntent?.idKategori
                binding.kategori.text = dataKategoriIntent?.namaKategori
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun populateData() {
        showLoading()
        val judul =
            binding.judulIklan.text.toString().toRequestBody("text/plain".toMediaTypeOrNull())
        val lokasi = binding.lokasi.text.toString().toRequestBody("text/plain".toMediaTypeOrNull())
        val deskripsi =
            binding.deskripsiIklan.text.toString().toRequestBody("text/plain".toMediaTypeOrNull())
        val harga =
            binding.harga.getCleanInt().toString().toRequestBody("text/plain".toMediaTypeOrNull())

        val gps = GPSTracker(this)
        val lat = gps.latitude.toString().toRequestBody("text/plain".toMediaTypeOrNull())
        val lon = gps.longitude.toString().toRequestBody("text/plain".toMediaTypeOrNull())
        val kondisi = isBaru.toRequestBody("text/plain".toMediaTypeOrNull())

        val addedBy =
            Prefs.getString(Constant.EMAIL).toRequestBody("text/plain".toMediaTypeOrNull())
        val detail = "detail".toRequestBody("text/plain".toMediaTypeOrNull())
        if (!isEdit) {
            PROVID = Prefs.getString(Constant.PROV_ID)
            KABID = Prefs.getString(Constant.KAB_ID)
            KECID = Prefs.getString(Constant.LOKASI_ID)
        } else {
            PROVID = Prefs.getString(Constant.PROV_ID_EDIT)
            KABID = Prefs.getString(Constant.KAB_ID_EDIT)
            KECID = Prefs.getString(Constant.LOKASI_ID_EDIT)
        }
        val idKab = KABID?.toRequestBody("text/plain".toMediaTypeOrNull())
        val idKec =
            KECID?.toRequestBody("text/plain".toMediaTypeOrNull())
        val idProv =
            PROVID?.toRequestBody("text/plain".toMediaTypeOrNull())
        val kategoriS = kategori?.toRequestBody("text/plain".toMediaTypeOrNull())


        vm.sendIklan(
            judulIklan = judul,
            lokasi = lokasi,
            deskripsiIklan = deskripsi,
            harga = harga,
            lat = lat,
            lon = lon,
            foto = multipartTypedOutput,
            added_by = addedBy,
            detail = detail,
            id_kab = idKab,
            id_kec = idKec,
            id_prov = idProv,
            kategori = kategoriS!!,
            kondisi = kondisi,
            previousDelete = previousFotoDelete,
            mode = mode!!,
            iklanId = iklanId
        )

        vm.data.observe(this) {
            showMessage(it)
        }
        vm.error.observe(this) {
            dismissLoading()
            Toast.makeText(this@TambahIklanActivity, it.message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun showMessage(generalResponse: GeneralResponse) {
        dismissLoading()
        if (generalResponse.code == 200) {
            AlertDialog.Builder(this)
                .setMessage(generalResponse.message)
                .setTitle("Konfirmasi")
                .setPositiveButton("Tutup") { _, _ ->
                    resetPreferences()
                    finish()
                }.setCancelable(false)
                .create().show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        resetPreferences()
    }

    private fun resetPreferences() {
        Prefs.remove(Constant.IS_EDIT)
        Prefs.remove(Constant.PROV_ID_EDIT)
        Prefs.remove(Constant.PROV_EDIT)
        Prefs.remove(Constant.KAB_EDIT)
        Prefs.remove(Constant.KAB_ID_EDIT)
        Prefs.remove(Constant.KEC_EDIT)
        Prefs.remove(Constant.LOKASI_ID_EDIT)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> confirmQuit()
            0 -> {
                Intent(this@TambahIklanActivity, WebBrowserActivity::class.java).apply {
                    putExtra(WebBrowserActivity.URL, NetworkModule.BASE_URL + "help/index.html")
                    startActivity(this)
                }
            }
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
                .cameraButton(ButtonType.Button)
                .galleryButton(ButtonType.Button)
                .multiSelect(
                    1,
                    10
                )              //size of the columns (will be changed a little to fit)
                .requestTag("multi")                //tag can be used if multiple pickers are used
                .show(supportFragmentManager)
        }
    }

    override fun onResume() {
        super.onResume()
        if (Prefs.getBoolean(Constant.IS_EDIT) && Prefs.contains(Constant.IS_EDIT)) {
            if (isPickPlace) {
                val lokasi =
                    if (Prefs.getString(Constant.KEC_EDIT)
                            .isNotEmpty() || Prefs.contains(Constant.KEC_EDIT)
                    ) {
                        "${Prefs.getString(Constant.KEC_EDIT)}, ${Prefs.getString(Constant.KAB_EDIT)}"
                    } else if (Prefs.getString(Constant.KAB_EDIT)
                            .isNotEmpty() || Prefs.contains(Constant.KAB_EDIT)
                    ) {
                        Prefs.getString(Constant.KAB_EDIT)
                    } else {
                        Prefs.getString(Constant.PROV_EDIT)
                    }
                if (lokasi.isNotEmpty())
                    binding.lokasi.text = lokasi
                else
                    binding.lokasi.text = "Indonesia"
            }
        } else {
            getLokasi()
        }
    }

    private fun getLokasi() {
        val lokasi =
            if (Prefs.getString(Constant.KEC).isNotEmpty() || Prefs.contains(Constant.KEC)) {
                "${Prefs.getString(Constant.KEC)}, ${Prefs.getString(Constant.KAB)}"
            } else if (Prefs.getString(Constant.KAB)
                    .isNotEmpty() || Prefs.contains(Constant.KAB)
            ) {
                Prefs.getString(Constant.KAB)
            } else {
                Prefs.getString(Constant.PROV)
            }
        if (lokasi.isNotEmpty())
            binding.lokasi.text = lokasi
        else
            binding.lokasi.text = "Indonesia"

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
        multipartTypedOutput = mutableListOf()
//        pd = ProgressDialog.show(
//            this@TambahIklanActivity,
//            "",
//            "Mengkonversi gambar...",
//            false,
//            false
//        )
        showLoading()
        CoroutineScope(Dispatchers.IO).launch {
            withContext(Dispatchers.IO) {
                uris.forEachIndexed { i, it ->
                    val file2 = File(FileUtils.getPath(this@TambahIklanActivity, it)).path

                    val bmp = BitmapFactory.decodeFile(file2)
                    val byteArrayOutputStream = ByteArrayOutputStream()
                    bmp.compress(Bitmap.CompressFormat.WEBP, 70, byteArrayOutputStream)
                    //createbody
                    val photo = byteArrayOutputStream.toByteArray()
                        .toRequestBody(
                            "image/*".toMediaTypeOrNull(),
                            0,
                            byteArrayOutputStream.size()
                        )

                    //create image input
                    val body = MultipartBody.Part.createFormData(
                        "imageFiles[]",
                        File(FileUtils.getPath(this@TambahIklanActivity, it)).name,
                        photo
                    )
//                    multipartTypedOutput!![i] = body
                    multipartTypedOutput?.add(i, body)
                    MainScope().launch {
                        dismissLoading()
                        adapterSelectedImage.addItem(uris, true)
                    }
                }
            }

        }
    }

    companion object {
        const val KATEGORI_DATA = "kategoriData"
        const val ADS_DATA = "adsData"
    }
}