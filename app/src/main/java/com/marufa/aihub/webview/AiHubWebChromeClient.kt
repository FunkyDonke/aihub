package com.marufa.aihub.webview

import android.webkit.ConsoleMessage
import android.webkit.JsResult
import android.webkit.PermissionRequest
import android.webkit.WebChromeClient
import android.webkit.WebView

class AiHubWebChromeClient : WebChromeClient() {

    var onProgressChanged: ((Int) -> Unit)? = null
    var onTitleReceived: ((String) -> Unit)? = null

    override fun onProgressChanged(view: WebView?, newProgress: Int) {
        super.onProgressChanged(view, newProgress)
        onProgressChanged?.invoke(newProgress)
    }

    override fun onReceivedTitle(view: WebView?, title: String?) {
        super.onReceivedTitle(view, title)
        title?.let { onTitleReceived?.invoke(it) }
    }

    override fun onPermissionRequest(request: PermissionRequest?) {
        // Grant microphone/camera if AI needs it (e.g. voice input)
        request?.grant(request.resources)
    }

    override fun onJsAlert(view: WebView?, url: String?, message: String?, result: JsResult?): Boolean {
        result?.confirm()
        return true
    }
}
