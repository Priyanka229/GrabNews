package grab.com.news.network

import grab.com.news.NewsModel
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface RetroFitApiServices {
    @GET("top-headlines")
    fun getNews(@QueryMap options: Map<String, String>): Observable<NewsModel>
}