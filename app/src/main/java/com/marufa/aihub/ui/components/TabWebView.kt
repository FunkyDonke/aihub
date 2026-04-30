package com.marufa.aihub.ui.components

import android.view.ViewGroup
import android.webkit.WebView
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.marufa.aihub.webview.AiHubWebChromeClient
import com.marufa.aihub.webview.AiHubWebViewClient
import com.marufa.aihub.webview.WebViewFactory

// Cache of WebViews — each tab keeps its WebView alive so sessions persist
private val webViewCache = mutableMapOf<String, WebView>()

@Composable
fun TabWebView(
    tabId: String,
    sessionId: String,
    url: String,
    isActive: Boolean,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var loadingProgress by remember { mutableIntStateOf(0) }
    var isLoading by remember { mutableStateOf(true) }

    // Get or create WebView for this tab
    val webView = remember(tabId) {
        webViewCache.getOrPut(tabId) {
            WebViewFactory.create(context, sessionId, url).also { wv ->
                val chromeClient = wv.webChromeClient as? AiHubWebChromeClient
                chromeClient?.onProgressChanged = { progress ->
                    loadingProgress = progress
                }
                val viewClient = wv.webViewClient as? AiHubWebViewClient
                viewClient?.onLoadingChanged = { loading ->
                    isLoading = loading
                }
            }
        }
    }

    // Pause/resume WebView based on active state (saves CPU/memory for background tabs)
    LaunchedEffect(isActive) {
        if (isActive) {
            webView.onResume()
        } else {
            webView.onPause()
        }
    }

    // Clean up WebView from parent when removed from composition
    DisposableEffect(tabId) {
        onDispose {
            // Don't destroy — keep in cache for fast tab switching
            webView.onPause()
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        AndroidView(
            factory = {
                // Remove from previous parent if needed
                (webView.parent as? ViewGroup)?.removeView(webView)
                webView
            },
            modifier = Modifier.fillMaxSize()
        )

        // Loading progress bar at top
        if (isLoading && loadingProgress < 100) {
            LinearProgressIndicator(
                progress = { loadingProgress / 100f },
                modifier = Modifier.align(Alignment.TopCenter)
            )
        }
    }
}

// Call when a tab is permanently deleted to free memory
fun destroyWebView(tabId: String) {
    webViewCache[tabId]?.destroy()
    webViewCache.remove(tabId)
}

// Handle back press for the active WebView
fun canGoBack(tabId: String): Boolean = webViewCache[tabId]?.canGoBack() ?: false
fun goBack(tabId: String) { webViewCache[tabId]?.goBack() }
fun reload(tabId: String) { webViewCache[tabId]?.reload() }
