package grab.com.news

import android.app.Application

class GrabNewsApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    companion object {
        lateinit var instance: GrabNewsApplication
            private set
    }
}