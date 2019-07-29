package grab.com.news

import com.google.gson.annotations.SerializedName

data class NewsModel(
    @SerializedName("articles") val articles: List<Article>?,
    @SerializedName("totalResults") val totalResults: Int?,
    @SerializedName("status") val status: String?
)

data class Article(
    @SerializedName("author") val author: String?,
    @SerializedName("title") val title: String?,
    @SerializedName("description") val description: String?,
    @SerializedName("url") val url: String?,
    @SerializedName("urlToImage") val urlToImage: String?,
    @SerializedName("publishedAt") val publishedAt: String?,
    @SerializedName("content") val content: String?
)

data class CountryModel(
    @SerializedName("countries") val countryList: List<Country>
)

data class Country(
    @SerializedName("name") val countryName: String,
    @SerializedName("code") val countryCode: String
)

data class CategoriesModel(
    @SerializedName("categories") val countryList: List<String>
)