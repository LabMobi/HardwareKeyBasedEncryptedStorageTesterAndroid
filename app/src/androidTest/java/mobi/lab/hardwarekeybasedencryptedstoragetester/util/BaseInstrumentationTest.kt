package mobi.lab.hardwarekeybasedencryptedstoragetester.util

import android.content.Context
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.IdlingResource
import androidx.test.platform.app.InstrumentationRegistry
import mobi.lab.hardwarekeybasedencryptedstoragetester.TestApp
import mobi.lab.hardwarekeybasedencryptedstoragetester.di.TestAppComponent

open class BaseInstrumentationTest {

    fun getContext(): Context {
        return InstrumentationRegistry.getInstrumentation().targetContext
    }

    fun getAppComponent(): TestAppComponent {
        return (getContext().applicationContext as TestApp).appComponent
    }

    fun registerIdlingResource(resource: IdlingResource) {
        IdlingRegistry.getInstance().register(resource)
    }

    fun unregisterIdlingResource(resource: IdlingResource) {
        IdlingRegistry.getInstance().unregister(resource)
    }
}
