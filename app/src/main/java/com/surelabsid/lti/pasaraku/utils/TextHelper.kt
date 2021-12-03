package com.surelabsid.lti.pasaraku.utils

import android.net.Uri
import java.util.regex.Pattern

object TextHelper {

    fun pullLinks(text: CharSequence?): ArrayList<String> {
        val links = ArrayList<String>()

        val regex =
            "\\(?\\b(https?://|www[.]|ftp://)[-A-Za-z0-9+&@#/%?=~_()|!:,.;]*[-A-Za-z0-9+&@#/%=~_()|]"
        val p = Pattern.compile(regex)
        val m = p.matcher(text!!)
        while (m.find()) {
            var urlStr = m.group()
            if (urlStr.startsWith("(") && urlStr.endsWith(")")) {
                urlStr = urlStr.substring(1, urlStr.length - 1)
            }
            links.add(urlStr)
        }
        return links
    }

    fun getLastPath(url: String): String? {
        val uri = Uri.parse(url)
        return uri.lastPathSegment
    }
}