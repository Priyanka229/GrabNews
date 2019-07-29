package grab.com.news.database

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.arch.persistence.room.ColumnInfo

@Entity
class ArticleEntity {
    constructor()
    constructor(author: String, title: String, description: String, url: String, urlToImage: String, publishedAt: String, content: String) {
        this.author = author
        this.title = title
        this.description = description
        this.url = url
        this.urlToImage = urlToImage
        this.publishedAt = publishedAt
        this.content = content
    }

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int = 0

    @ColumnInfo(name = "author")
    var author: String? = null

    @ColumnInfo(name = "title")
    var title: String? = null

    @ColumnInfo(name = "description")
    var description: String? = null

    @ColumnInfo(name = "url")
    var url: String? = null

    @ColumnInfo(name = "urlToImage")
    var urlToImage: String? = null

    @ColumnInfo(name = "publishedAt")
    var publishedAt: String? = null

    @ColumnInfo(name = "content")
    var content: String? = null
}