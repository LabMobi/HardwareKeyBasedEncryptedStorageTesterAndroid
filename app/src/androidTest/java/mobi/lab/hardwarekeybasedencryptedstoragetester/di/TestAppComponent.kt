package mobi.lab.hardwarekeybasedencryptedstoragetester.di

import dagger.Component
import mobi.lab.hardwarekeybasedencryptedstoragetester.domain.di.UseCaseModule
import mobi.lab.hardwarekeybasedencryptedstoragetester.infrastructure.di.GatewayModule
import mobi.lab.hardwarekeybasedencryptedstoragetester.infrastructure.di.MapperModule
import mobi.lab.hardwarekeybasedencryptedstoragetester.infrastructure.di.PlatformModule
import mobi.lab.hardwarekeybasedencryptedstoragetester.infrastructure.di.ResourceModule
import mobi.lab.hardwarekeybasedencryptedstoragetester.infrastructure.di.StorageModule
import javax.inject.Singleton

/**
 * A different implementation of BaseAppCompnent that can be passed into Injector.
 * This component allows us to switch out modules for testing purposes.
 * For example, we provide a different SchedulerProvider via TestSchedulerModule
 */
@Singleton
@Component(
    modules = [
        ResourceModule::class,
        UseCaseModule::class,
        GatewayModule::class,
        MapperModule::class,
        StorageModule::class,
        AppModule::class,
        PlatformModule::class,
        TestSchedulerModule::class
    ]
)
interface TestAppComponent : BaseAppComponent {
    fun inject(item: SchedulerModuleTest)
}
