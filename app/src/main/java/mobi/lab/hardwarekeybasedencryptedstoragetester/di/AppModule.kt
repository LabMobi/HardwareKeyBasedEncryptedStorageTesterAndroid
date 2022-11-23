package mobi.lab.hardwarekeybasedencryptedstoragetester.di

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides
import mobi.lab.hardwarekeybasedencryptedstoragetester.Env
import mobi.lab.hardwarekeybasedencryptedstoragetester.common.util.isDebugBuild
import mobi.lab.hardwarekeybasedencryptedstoragetester.infrastructure.common.platform.AppEnvironment
import javax.inject.Singleton

@Module(includes = [ViewModelModule::class, BuildVariantModule::class])
class AppModule(private val application: Application) {

    @Provides
    @Singleton
    fun provideContext(): Context = application.applicationContext

    @Provides
    @Singleton
    fun provideAppEnvironment(): AppEnvironment = AppEnvironment(Env.URL_BASE, isDebugBuild())
}
