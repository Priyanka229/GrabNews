package grab.com.news.database

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context

@Database(entities = [ArticleEntity::class], version = 1)
abstract class ArticleDatabase : RoomDatabase() {

    abstract fun articleDao(): ArticleDao

    companion object {

        private var INSTANCE: ArticleDatabase? = null

        fun getAppDatabase(context: Context): ArticleDatabase {
            if (INSTANCE == null) {
                INSTANCE =
                    Room.databaseBuilder(context.applicationContext, ArticleDatabase::class.java, "article-database")
                        .build()
            }
            return INSTANCE as ArticleDatabase
        }

        fun destroyInstance() {
            INSTANCE = null
        }
    }
}