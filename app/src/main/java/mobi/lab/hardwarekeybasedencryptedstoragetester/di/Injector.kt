package mobi.lab.hardwarekeybasedencryptedstoragetester.di

import android.app.Application
import mobi.lab.hardwarekeybasedencryptedstoragetester.App
import mobi.lab.hardwarekeybasedencryptedstoragetester.domain.di.UseCaseModule
import mobi.lab.hardwarekeybasedencryptedstoragetester.infrastructure.di.GatewayModule
import mobi.lab.hardwarekeybasedencryptedstoragetester.infrastructure.di.MapperModule
import mobi.lab.hardwarekeybasedencryptedstoragetester.infrastructure.di.PlatformModule
import mobi.lab.hardwarekeybasedencryptedstoragetester.infrastructure.di.ResourceModule
import mobi.lab.hardwarekeybasedencryptedstoragetester.infrastructure.di.StorageModule
import mobi.lab.hardwarekeybasedencryptedstoragetester.login.LoginFragment
import mobi.lab.hardwarekeybasedencryptedstoragetester.main.MainFragment
import mobi.lab.hardwarekeybasedencryptedstoragetester.prototype.PrototypeActivity
import mobi.lab.hardwarekeybasedencryptedstoragetester.splash.SplashActivity

object Injector : BaseAppComponent {

    private lateinit var appComponent: BaseAppComponent

    /**
     * UseCaseModule uses constructor injection for providing UseCases. Gateway and Storage modules are only used by UseCaseModule.
     * Since Dagger can resolve UseCaseModule on its own, specifically providing said modules is deprecated (a no-op in reality).
     *
     * We'll leave these module definitions here to have a better overview of what's provided and used.
     */
    @Suppress("DEPRECATION")
    fun buildGraph(application: Application) {
        buildGraph(
            DaggerAppComponent
                .builder()
                .resourceModule(ResourceModule)
                .mapperModule(MapperModule)
                .appModule(AppModule(application))
                .platformModule(PlatformModule)
                .useCaseModule(UseCaseModule)
                .gatewayModule(GatewayModule)
                .storageModule(StorageModule)
                .build()
        )
    }

    fun buildGraph(component: BaseAppComponent) {
        appComponent = component
    }

    override fun inject(target: App) {
        appComponent.inject(target)
    }

    override fun inject(target: SplashActivity) {
        appComponent.inject(target)
    }

    override fun inject(target: MainFragment) {
        appComponent.inject(target)
    }

    override fun inject(target: LoginFragment) {
        appComponent.inject(target)
    }

    override fun inject(target: PrototypeActivity) {
        appComponent.inject(target)
    }
}
