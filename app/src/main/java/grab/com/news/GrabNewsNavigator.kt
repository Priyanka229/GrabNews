package grab.com.news

import android.content.Intent
import grab.com.news.GrabNewsVM.Companion.PARAM_CATEGORY
import grab.com.news.GrabNewsVM.Companion.PARAM_COUNTRY
import grab.com.news.GrabNewsVM.Companion.PARAM_QUERY
import grab.com.news.GrabNewsWebActivity.Companion.URL_KEY
import grab.com.news.advancedsearch.AdvancedSearchActivity

class GrabNewsNavigator (private val baseActivity: GrabNewsBaseActivity) {
    /** open news listing activity */
    fun openNewsListingScreen(query: String? = null, country: String? = null, category: String? = null) {
        val intent = Intent(baseActivity, GrabNewsListingActivity::class.java)
        intent.apply {
            putExtra(PARAM_QUERY, query)
            putExtra(PARAM_COUNTRY, country)
            putExtra(PARAM_CATEGORY, category)
        }
        baseActivity.startActivity(intent)
    }

    /** open advanced search activity */
    fun openNewsDetailsScreen(url: String) {
        val intent = Intent(baseActivity, GrabNewsWebActivity::class.java)
        intent.apply {
            putExtra(URL_KEY, url)
        }
        baseActivity.startActivity(intent)
    }

    /** open advanced search activity */
    fun openAdvancedSearchScreen() {
        val intent = Intent(baseActivity, AdvancedSearchActivity::class.java)
        baseActivity.startActivity(intent)
    }
}