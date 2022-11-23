package mobi.lab.hardwarekeybasedencryptedstoragetester.di

import dagger.Component
import mobi.lab.hardwarekeybasedencryptedstoragetester.domain.di.UseCaseModule
import mobi.lab.hardwarekeybasedencryptedstoragetester.infrastructure.di.GatewayModule
import mobi.lab.hardwarekeybasedencryptedstoragetester.infrastructure.di.MapperModule
import mobi.lab.hardwarekeybasedencryptedstoragetester.infrastructure.di.PlatformModule
import mobi.lab.hardwarekeybasedencryptedstoragetester.infrastructure.di.ResourceModule
import mobi.lab.hardwarekeybasedencryptedstoragetester.infrastructure.di.StorageModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        ResourceModule::class,
        UseCaseModule::class,
        GatewayModule::class,
        MapperModule::class,
        StorageModule::class,
        AppModule::class,
        SchedulerModule::class,
        PlatformModule::class
    ]
)
interface AppComponent : BaseAppComponent
/**
 * DO NOT ADD METHODS HERE. Add methods to [BaseAppComponent].
 */
