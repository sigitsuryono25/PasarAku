package com.surelabsid.lti.pasaraku

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.print.PrintAttributes
import android.print.PrintDocumentAdapter
import android.print.PrintManager
import android.view.Menu
import android.view.MenuItem
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.surelabsid.lti.base.Baseapp
import com.surelabsid.lti.pasaraku.databinding.ActivityFaqBinding

class FaqActivity : Baseapp() {
    private lateinit var binding: ActivityFaqBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityFaqBinding.inflate(layoutInflater)

        setContentView(binding.root)

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
        }

        showLoading()

        binding.webView.settings.javaScriptEnabled = true
        binding.webView.settings.javaScriptCanOpenWindowsAutomatically = true

        binding.webView.webChromeClient =
            object : WebChromeClient() {
                override fun onProgressChanged(view: WebView?, newProgress: Int) {
                    super.onProgressChanged(view, newProgress)

                }
            }

        binding.webView.webViewClient =
            object : WebViewClient() {
                override fun onPageFinished(view: WebView?, url: String?) {
                    super.onPageFinished(view, url)
                    dismissLoading()
                    supportActionBar?.apply {
                        title = view?.title
                    }
                }
            }


        binding.webView.loadUrl("https://pasaraku.com/mobile/faq")

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        /**
         * @var menu.add(param1, param2, param3, param4)
         * @param1 item group
         * @param2 itemId
         * @param3 order
         * @param4 title
         */
        menu?.add(0, 1, 0, "Maju")
        menu?.add(0, 2, 0, "Kembali")
        menu?.add(0, 3, 0, "Buka dengan Chrome")

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
            1 -> {
                if (binding.webView.canGoForward()) {
                    binding.webView.goForward()
                }
            }
            2 -> {
                if (binding.webView.canGoBack()) {
                    binding.webView.goBack()
                }
            }
            3 -> {
                try {
                    val i = Intent(Intent.ACTION_VIEW)
                    i.setPackage("com.android.chrome")
                    i.action = Intent.ACTION_VIEW
                    i.data = Uri.parse(binding.webView.url)
                    startActivity(i)
                } catch (e: ActivityNotFoundException) {
                    e.printStackTrace()
                    Toast.makeText(
                        this@FaqActivity,
                        "Google Chrome not installed",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            4 -> {
                createWebPrintJob(this, binding.webView)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        if (binding.webView.canGoBack()) {
            binding.webView.goBack()
        } else
            super.onBackPressed()
    }

    @Suppress("DEPRECATION")
    private fun createWebPrintJob(context: Context, webView: WebView) {
        // Get a PrintManager instance
        val printManager =
            context.getSystemService(Context.PRINT_SERVICE) as PrintManager
        val jobName: String = webView.title + " Document"

        // Get a print adapter instance
        val printAdapter: PrintDocumentAdapter
        printAdapter =
            webView.createPrintDocumentAdapter(jobName)

        // Create a print job with name and adapter instance
        printManager.print(
            jobName, printAdapter,
            PrintAttributes.Builder().build()
        )

        // Save the job object for later status checking
//        mPrintJobs.add(printJob)
    }

}