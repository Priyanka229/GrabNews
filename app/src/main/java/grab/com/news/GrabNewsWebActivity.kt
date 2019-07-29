package grab.com.news

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import grab.com.news.databinding.ActivityWebBinding


class GrabNewsWebActivity: AppCompatActivity() {
    companion object {
        const val URL_KEY = "url_key"
    }

    private val binding by lazy {
        ActivityWebBinding.inflate(LayoutInflater.from(this))
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val url = intent?.getStringExtra(URL_KEY)

        binding.apply {
            progressBar.visibility = View.VISIBLE
            webView.apply {
                /** start loading url */
                loadUrl(url)

                /** set page finish listener */
                webViewClient = object : WebViewClient() {
                    override fun onPageFinished(view: WebView, url: String) {
                        progressBar.visibility = View.GONE
                    }
                }

                /** enable java script */
                settings.javaScriptEnabled = true //fixme
            }
        }
    }
}