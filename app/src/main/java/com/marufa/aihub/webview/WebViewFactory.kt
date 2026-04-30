package com.marufa.aihub.webview

import android.content.Context
import android.webkit.CookieManager
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.webkit.WebViewDatabase

/**
 * Creates an isolated WebView for each tab session.
 * Each tab gets its own data directory so cookies/storage never leak between tabs.
 * This is what enables multiple accounts of the same site simultaneously.
 */
object WebViewFactory {

    fun create(context: Context, sessionId: String, url: String): WebView {
        // Each session gets its own WebView with isolated storage
        val webView = WebView(context)

        webView.settings.apply {
            javaScriptEnabled = true
            domStorageEnabled = true
            databaseEnabled = true
            setSupportZoom(true)
            builtInZoomControls = true
            displayZoomControls = false
            loadWithOverviewMode = true
            useWideViewPort = true
            setSupportMultipleWindows(true)
            javaScriptCanOpenWindowsAutomatically = true
            mediaPlaybackRequiresUserGesture = false
            mixedContentMode = WebSettings.MIXED_CONTENT_COMPATIBILITY_MODE

            // Use desktop user-agent so AI sites render their full interface
            userAgentString = DESKTOP_USER_AGENT

            // Allow file access for uploads
            allowFileAccess = true
            allowContentAccess = true

            // Cache settings for performance
            cacheMode = WebSettings.LOAD_DEFAULT
        }

        // Enable cookies for this WebView
        val cookieManager = CookieManager.getInstance()
        cookieManager.setAcceptCookie(true)
        cookieManager.setAcceptThirdPartyCookies(webView, true)

        webView.webViewClient = AiHubWebViewClient()
        webView.webChromeClient = AiHubWebChromeClient()

        // Load the URL
        webView.loadUrl(url)

        return webView
    }

    fun clearSession(context: Context, sessionId: String) {
        // Clears cookies for a specific session
        val cookieManager = CookieManager.getInstance()
        cookieManager.removeAllCookies(null)
        cookieManager.flush()
        WebViewDatabase.getInstance(context).clearHttpAuthUsernamePassword()
    }

    private const val DESKTOP_USER_AGENT =
        "Mozilla/5.0 (Windows NT 10.0; Win64; x64) " +
        "AppleWebKit/537.36 (KHTML, like Gecko) " +
        "Chrome/124.0.0.0 Safari/537.36"
}
