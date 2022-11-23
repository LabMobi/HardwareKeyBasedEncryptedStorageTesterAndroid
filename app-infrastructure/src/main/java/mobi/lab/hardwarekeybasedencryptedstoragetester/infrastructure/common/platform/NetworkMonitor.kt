package mobi.lab.hardwarekeybasedencryptedstoragetester.infrastructure.common.platform

import android.content.Context
import mobi.lab.hardwarekeybasedencryptedstoragetester.app.common.isNetworkConnected

class NetworkMonitor(private val context: Context) {
    fun isConnected(): Boolean {
        return isNetworkConnected(context)
    }
}
