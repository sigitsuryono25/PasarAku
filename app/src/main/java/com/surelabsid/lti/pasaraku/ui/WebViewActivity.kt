package com.surelabsid.lti.pasaraku.ui

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.print.PrintAttributes
import android.print.PrintDocumentAdapter
import android.print.PrintManager
import android.webkit.WebView
import androidx.appcompat.widget.PopupMenu
import com.surelabsid.lti.pasaraku.R
import com.surelabsid.lti.pasaraku.databinding.ActivityWebViewBinding
import com.surelabsid.lti.pasaraku.model.PremiumModel
import com.surelabsid.lti.pasaraku.network.NetworkModule

class WebViewActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWebViewBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWebViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val rek = intent.getParcelableExtra<PremiumModel>(REKG)

        setData(rek)
    }

    override fun onBackPressed() {
        finish()
    }

    private fun setData(rek: PremiumModel?) {
        val url = NetworkModule.BASE_URL + "index.php/Invoice/index?inv=" + rek?.id
        binding.webview.settings.javaScriptEnabled = true
        binding.webview.loadUrl(url)

        binding.close.setOnClickListener {
            finish()
        }

        val popup = PopupMenu(this, binding.titik3)
        popup.inflate(R.menu.options_invoice)

        binding.titik3.setOnClickListener {
            popup.show()
        }
        popup.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.print -> {
                    createWebPrintJob(this, binding.webview)
                }

                R.id.openWIthChrome -> {
                    val i = Intent(Intent.ACTION_VIEW)
                    i.setPackage("com.android.chrome")
                    i.action = Intent.ACTION_VIEW
                    i.data = Uri.parse(url)
                    startActivity(i)
                }
            }
            return@setOnMenuItemClickListener false
        }
    }

    @Suppress("DEPRECATION")
    private fun createWebPrintJob(context: Context, webView: WebView) {
        // Get a PrintManager instance
        val printManager =
            context.getSystemService(Context.PRINT_SERVICE) as PrintManager
        val jobName: String = webView.title + " Document"

        // Get a print adapter instance
        val printAdapter: PrintDocumentAdapter = webView.createPrintDocumentAdapter(jobName)

        // Create a print job with name and adapter instance
        printManager.print(
            jobName, printAdapter,
            PrintAttributes.Builder().build()
        )
    }

    companion object{
        const val REKG = "rekening"
    }
}