package com.marufa.aihub.webview

import android.graphics.Bitmap
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient

class AiHubWebViewClient : WebViewClient() {

    var onPageStarted: ((String) -> Unit)? = null
    var onPageFinished: ((String) -> Unit)? = null
    var onLoadingChanged: ((Boolean) -> Unit)? = null

    override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
        super.onPageStarted(view, url, favicon)
        onLoadingChanged?.invoke(true)
        url?.let { onPageStarted?.invoke(it) }
    }

    override fun onPageFinished(view: WebView?, url: String?) {
        super.onPageFinished(view, url)
        onLoadingChanged?.invoke(false)
        url?.let { onPageFinished?.invoke(it) }
    }

    override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
        // Let the WebView handle all navigation within the same domain
        return false
    }
}
