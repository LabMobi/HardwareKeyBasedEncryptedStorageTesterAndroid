package mobi.lab.hardwarekeybasedencryptedstoragetester.debug

import android.content.Context
import mobi.lab.hardwarekeybasedencryptedstoragetester.common.debug.DebugActions

class DevDebugActions : DebugActions {
    override fun launchDebugActivity(context: Context) {
        return context.startActivity(DebugActivity.getIntent(context))
    }
}
