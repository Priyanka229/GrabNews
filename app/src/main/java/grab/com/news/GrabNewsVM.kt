package grab.com.news

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MutableLiveData
import grab.com.news.database.ArticleDatabase
import grab.com.news.database.ArticleEntity
import grab.com.news.network.RetroFitClient
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class GrabNewsVM(application: Application, val navigator: GrabNewsNavigator): AndroidViewModel(application) {
    private val grabNewsInteractor: GrabNewsInteractor = GrabNewsInteractor()
    val noInternetLiveData: MutableLiveData<Boolean> = MutableLiveData()
    val noNewsLiveData: MutableLiveData<Boolean> = MutableLiveData()
    val loaderLiveData: MutableLiveData<Boolean> = MutableLiveData()
    val countriesLiveData: MutableLiveData<List<Country>> = MutableLiveData()
    val categoriesLiveData: MutableLiveData<List<String>> = MutableLiveData()
    private val compositeDisposable: CompositeDisposable = CompositeDisposable()

    /** pagination state starts */
    private var pageNo = 1
    private var isLoading = false
    private var isLastPage = false
    private val newsQueryMap = HashMap<String, String>()
    companion object {
        private const val THRESHOLD_SIZE = 3
        private const val PARAM_API_KEY = "apiKey"
        const val PARAM_QUERY = "q"
        const val PARAM_COUNTRY = "country"
        const val PARAM_CATEGORY = "category"
        private const val PARAM_SOURCE = "sources"
    }
    /** pagination state ends */

    /** on query submit */
    fun onQuerySubmit(query: String?) {
        query?.let {
            fetchQueryBasedNews(it)
        }
    }

    /** fetch default news */
    fun fetchDefaultNews() {
        if (isInternetAvailable()) {
            /** update query map */
            newsQueryMap[PARAM_COUNTRY] = "IN"
            newsQueryMap[PARAM_API_KEY] = RetroFitClient.API_KEY

            /** flush articles */
            flushArticleInDatabase()

            /** reset page number */
            pageNo = 1

            /** fetch news */
            fetchNews()
        } else {
            loaderLiveData.value = false
            noNewsLiveData.value = false
            noInternetLiveData.value = true
        }
    }

    /** fetch news on basis of query */
    private fun fetchQueryBasedNews(query: String) {
        if (isInternetAvailable()) {
            if (query.isBlank().not()) {
                /** update query map */
                newsQueryMap[PARAM_QUERY] = query
                newsQueryMap[PARAM_API_KEY] = RetroFitClient.API_KEY

                /** no-data message visibility */
                noNewsLiveData.value = false

                /** flush articles */
                flushArticleInDatabase()

                /** reset page number */
                pageNo = 1

                /** fetch news */
                fetchNews()
            } else {
                /** fetch default news */
                fetchDefaultNews()
            }
        } else {
            loaderLiveData.value = false
            noNewsLiveData.value = false
            noInternetLiveData.value = true
        }
    }

    /** fetch news on basis of query */
    fun fetchAdvancedSearchBasedNews(query: String? = null, country: String? = null, category: String? = null) {
        if (isInternetAvailable()) {
            /** clear query map */
            newsQueryMap.clear()

            /** update query map */
            if (query.isNullOrBlank().not()) newsQueryMap[PARAM_QUERY] = query!!
            if (country.isNullOrBlank().not()) newsQueryMap[PARAM_COUNTRY] = country!!
            if (category.isNullOrBlank().not()) newsQueryMap[PARAM_CATEGORY] = category!!

            /** flush articles */
            flushArticleInDatabase()

            /** reset page number */
            pageNo = 1

            if (newsQueryMap.isNullOrEmpty().not()) {
                /** update query map */
                newsQueryMap[PARAM_API_KEY] = RetroFitClient.API_KEY

                /** no-data message visibility */
                noNewsLiveData.value = false

                /** fetch news */
                fetchNews()
            } else {
                loaderLiveData.value = false
                noNewsLiveData.value = true
                noInternetLiveData.value = false
            }
        } else {
            loaderLiveData.value = false
            noNewsLiveData.value = false
            noInternetLiveData.value = true
        }
    }

    /** fetch news via interactor */
    private fun fetchNews() {
        if (newsQueryMap.isNullOrEmpty().not()) {
            isLoading = true
            noNewsLiveData.value = false
            loaderLiveData.value = true

            compositeDisposable.add(
                grabNewsInteractor.getNews(getApplication(), newsQueryMap)
                    .subscribeOn(Schedulers.io())
                    .map {
                        if (it.articles.isNullOrEmpty().not()) {
                            for (item in it.articles.orEmpty()) {
                                val article = ArticleEntity(
                                    item.author.orEmpty(),
                                    item.title.orEmpty(),
                                    item.description.orEmpty(),
                                    item.url.orEmpty(),
                                    item.urlToImage.orEmpty(),
                                    item.publishedAt.orEmpty(),
                                    item.content.orEmpty()
                                )

                                ArticleDatabase.getAppDatabase(getApplication()).articleDao().insertArticle(article)
                            }
                        }
                        it
                    }
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                        {
                            isLoading = false
                            loaderLiveData.value = false
                            pageNo++

                            it.totalResults?.let {totalPage ->
                                isLastPage = pageNo > totalPage
                            }

                            noNewsLiveData.value = !it.articles.isNullOrEmpty().not()

                        },
                        {
                            loaderLiveData.value = false
                            isLoading = false
                        }
                    )
            )
        }
    }

    /** decomposition of view model */
    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

    /** handling for recycler view scroll events */
    fun notifyScroll(firstVisibleItemPosition: Int, visibleItemCount: Int, totalItemCount: Int) {
        if (!isLoading && !isLastPage) {
            if (visibleItemCount + firstVisibleItemPosition + THRESHOLD_SIZE >= totalItemCount && firstVisibleItemPosition >= 0) {
                fetchNews()
            }
        }
    }

    /** flush articles */
    private fun flushArticleInDatabase() {
        compositeDisposable.add(
            grabNewsInteractor.flushArticle(getApplication())
                .subscribeOn(Schedulers.io())
                .subscribe()
        )
    }

    /** advanced search */
    fun advancedSearch() {
        navigator.openAdvancedSearchScreen()
    }

    /** open grab news listing screen */
    fun openNewsListingActivity(query: String? = null, country: String? = null, category: String? = null) {
        navigator.openNewsListingScreen(query, country, category)
    }

    /** open grab news deatil screen */
    fun openNewsDetailsScreen(url: String?) {
        url?.let {
            navigator.openNewsDetailsScreen(it)
        }
    }

    /** fetch countries */
    fun fetchCountries() {
        compositeDisposable.add(
            grabNewsInteractor.getCountries(getApplication())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe{
                    countriesLiveData.value = it
                }
        )
    }

    /** fetch categories */
    fun fetchCategories() {
        compositeDisposable.add(
            grabNewsInteractor.getCategories(getApplication())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe{
                    categoriesLiveData.value = it
                }
        )
    }
}