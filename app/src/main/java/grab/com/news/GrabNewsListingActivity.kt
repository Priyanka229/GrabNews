package grab.com.news

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import grab.com.news.database.ArticleDatabase

class GrabNewsListingActivity : GrabNewsBaseActivity() {
    private val rootLayout by lazy { findViewById<View>(R.id.root_view) }
    private val recyclerView by lazy { findViewById<RecyclerView>(R.id.recyclerview) }
    private val progressBar by lazy { findViewById<ProgressBar>(R.id.progress_bar) }
    private val noDataTv by lazy { findViewById<TextView>(R.id.no_data_tv) }
    private val viewModel by lazy {
        ViewModelProviders.of(this,
            GrabNewsVMFactory(GrabNewsApplication.instance,
                GrabNewsNavigator(this))).get(GrabNewsVM::class.java) }
    private val linearLayoutManager = LinearLayoutManager(this@GrabNewsListingActivity)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val articleAdapter = ArticleAdapter().apply {
            mCallback = object : ArticleAdapter.ArticleAdapterCallback {
                override fun onItemTap(url: String?) {
                    viewModel.openNewsDetailsScreen(url)
                }
            }
        }

        recyclerView.apply { /** provide the local context on calling field */
            layoutManager = linearLayoutManager
            adapter = articleAdapter

            addOnScrollListener(object : RecyclerView.OnScrollListener(){
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)

                    val visibleItemCount = linearLayoutManager.childCount
                    val totalItemCount = linearLayoutManager.itemCount
                    val firstVisibleItemPosition = linearLayoutManager.findFirstVisibleItemPosition()

                    viewModel.notifyScroll(firstVisibleItemPosition, visibleItemCount, totalItemCount)
                }
            })
        }

        /** room database observer */
        ArticleDatabase.getAppDatabase(this).articleDao().getArticles().observe(this, Observer {
            it?.apply {
                if (it.isEmpty()) {
                    recyclerView.visibility = View.GONE
                } else {
                    recyclerView.visibility = View.VISIBLE
                    articleAdapter.articleList = this
                    articleAdapter.notifyDataSetChanged()
                }
            }
        })

        /** loader observer */
        viewModel.loaderLiveData.observe(this, Observer {
            it?.let {showLoader ->
                progressBar.visibility = if (showLoader) View.VISIBLE else  View.GONE
            } ?: run {
                progressBar.visibility = View.GONE
            }
        })

        /** no-data observer */
        viewModel.noNewsLiveData.observe(this, Observer {
            it?.let {isNoNewsAvailable ->
                noDataTv.visibility = if (isNoNewsAvailable) View.VISIBLE else  View.GONE
            } ?: run {
                noDataTv.visibility = View.GONE
            }
        })

        /** no-internet observer */
        viewModel.noInternetLiveData.observe(this, Observer {
            it?.let {isNoInternetAvailable ->
                if (isNoInternetAvailable) {
                    Snackbar.make(
                        rootLayout, "No Internet", Snackbar.LENGTH_SHORT
                    ).show()
                }
            } ?: run {
                noDataTv.visibility = View.GONE
            }
        })


        viewModel.fetchAdvancedSearchBasedNews(
            intent?.getStringExtra(GrabNewsVM.PARAM_QUERY),
            intent?.getStringExtra(GrabNewsVM.PARAM_COUNTRY),
            intent?.getStringExtra(GrabNewsVM.PARAM_CATEGORY))
    }
}