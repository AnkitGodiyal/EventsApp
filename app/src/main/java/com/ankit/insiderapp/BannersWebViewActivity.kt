package com.ankit.insiderapp

import android.os.Bundle
import android.view.View
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.ankit.insiderapp.databinding.ActivityBannersWebViewBinding
import com.ankit.insiderapp.helpers.iConnectedToNetwork

class BannersWebViewActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_banners_web_view)
        val intent = intent

        val mBinding: ActivityBannersWebViewBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_banners_web_view)

        mBinding.apply {
            webView.apply {
                settings.domStorageEnabled = true
                settings.javaScriptEnabled = true
                settings.loadsImagesAutomatically = true
                scrollBarStyle = View.SCROLLBARS_INSIDE_OVERLAY
            }

            var webViewClient = WebViewClient()
            if (iConnectedToNetwork()) {
                intent.getStringExtra("url")?.let { webView.loadUrl(it) }
                if (webView.isShown) {
                    webViewLoader.visibility = View.INVISIBLE
                }
            } else {
                cvNoConnectionLayout.visibility = View.VISIBLE
                webViewLoader.visibility = View.INVISIBLE
            }
        }
    }
}