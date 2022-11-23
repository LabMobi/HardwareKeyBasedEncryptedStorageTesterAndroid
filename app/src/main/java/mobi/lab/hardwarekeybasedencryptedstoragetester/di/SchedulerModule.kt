package mobi.lab.hardwarekeybasedencryptedstoragetester.di

import dagger.Module
import dagger.Provides
import mobi.lab.hardwarekeybasedencryptedstoragetester.common.rx.AndroidSchedulerProvider
import mobi.lab.hardwarekeybasedencryptedstoragetester.common.rx.SchedulerProvider
import javax.inject.Singleton

@Module
object SchedulerModule {
    @Provides
    @Singleton
    fun provideSchedulerProvider(): SchedulerProvider = AndroidSchedulerProvider()
}
