package grab.com.news.database

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query

@Dao
interface ArticleDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertArticle(articleEntity: ArticleEntity)

    @Query("SELECT * FROM ArticleEntity")
    fun getArticles(): LiveData<List<ArticleEntity>>

    @Query("DELETE FROM ArticleEntity")
    fun flushArticles()
}