package grab.com.news

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo

fun isInternetAvailable(): Boolean {
    val connectivityManager = GrabNewsApplication.instance.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val activeNetwork: NetworkInfo? = connectivityManager.activeNetworkInfo

    return activeNetwork?.isConnectedOrConnecting == true
}