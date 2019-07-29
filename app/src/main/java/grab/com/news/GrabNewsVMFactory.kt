package grab.com.news

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider

class GrabNewsVMFactory constructor(private val application: GrabNewsApplication,
                                    private val navigator: GrabNewsNavigator): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(GrabNewsVM::class.java!!)) {
            GrabNewsVM(application, navigator) as T
        } else {
            throw IllegalArgumentException("ViewModel Not Found")
        }
    }
}