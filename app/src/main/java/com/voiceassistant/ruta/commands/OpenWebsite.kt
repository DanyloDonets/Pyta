package com.voiceassistant.ruta.commands

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.webkit.URLUtil

class OpenWebsite(private val context: Context) {

    fun openWebsite(url: String) {
        val websiteUrl = getWebsiteUrl(url)
        if (websiteUrl != null) {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(websiteUrl))
            if (context.packageManager.resolveActivity(browserIntent, PackageManager.MATCH_DEFAULT_ONLY) != null) {
                context.startActivity(browserIntent)
            }
        }
    }

    private fun getWebsiteUrl(input: String): String? {
        val pattern = "((https?|ftp)://)([\\w_-]+(?:(?:\\.[\\w_-]+)+))([\\w.,@?^=%&:/~+#-]*[\\w@?^=%&/~+#-])?"
        val regex = Regex(pattern)
        val matchResult = regex.find(input)
        return if (matchResult != null && URLUtil.isValidUrl(matchResult.value)) {
            matchResult.value
        } else {
            val query = input.substringAfter("відкрий сайт").trim()
            val searchUrl = "https://www.google.com/search?q=${Uri.encode(query)}"
            "$searchUrl&btnI"
        }
    }
}