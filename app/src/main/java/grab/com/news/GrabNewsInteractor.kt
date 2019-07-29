package grab.com.news

import android.content.Context
import com.google.gson.Gson
import grab.com.news.database.ArticleDatabase
import grab.com.news.network.RetroFitClient
import io.reactivex.Observable

class GrabNewsInteractor {

    /** retrieve query based news list from server */
    fun getNews(context: Context, queryMap: Map<String, String>): Observable<NewsModel> {
        return RetroFitClient.getRetroFitService(context).getNews(queryMap)
            .map { it }
    }

    /** flush database */
    fun flushArticle(context: Context): Observable<Boolean> {
        return Observable.create {
            ArticleDatabase.getAppDatabase(context).articleDao().flushArticles()
        }
    }

    /** get country list */
    fun getCountries(context: Context): Observable<List<Country>> {
        return Observable.create {emitter ->
            val countryModel = Gson().fromJson(context.assets.open("countries.json").bufferedReader().use{it.readText()}, CountryModel::class.java)
            emitter.onNext(countryModel.countryList)
        }
    }

    /** get category list */
    fun getCategories(context: Context): Observable<List<String>> {
        return Observable.create {emitter ->
            val categoryModel = Gson().fromJson(context.assets.open("categories.json").bufferedReader().use{it.readText()}, CategoriesModel::class.java)
            emitter.onNext(categoryModel.countryList)
        }
    }
}