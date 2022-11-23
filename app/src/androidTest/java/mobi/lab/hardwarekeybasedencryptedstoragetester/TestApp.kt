package mobi.lab.hardwarekeybasedencryptedstoragetester

import mobi.lab.hardwarekeybasedencryptedstoragetester.di.AppModule
import mobi.lab.hardwarekeybasedencryptedstoragetester.di.DaggerTestAppComponent
import mobi.lab.hardwarekeybasedencryptedstoragetester.di.Injector
import mobi.lab.hardwarekeybasedencryptedstoragetester.di.TestAppComponent

/**
 * Custom Application class for tests that has its own TestAppComponent.
 * This component can be used to inject dependencies into test classes directly
 * and it is provided to Injector class instead of the application code's AppComponent.
 */
class TestApp : App() {

    val appComponent: TestAppComponent = DaggerTestAppComponent
        .builder()
        .appModule(AppModule(this))
        .build()

    override fun initDependencyInjection() {
        // Init Injector with a component with custom test modules
        Injector.buildGraph(appComponent)
    }
}
