package mobi.lab.hardwarekeybasedencryptedstoragetester.di

import dagger.Module
import dagger.Provides
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Scheduler
import mobi.lab.hardwarekeybasedencryptedstoragetester.common.rx.SchedulerProvider
import javax.inject.Singleton

@Module
object TestSchedulerModule {

    @Singleton
    @Provides
    fun provideSchedulerProvider(): SchedulerProvider {
        return object : SchedulerProvider {
            override val main: Scheduler = AndroidSchedulers.mainThread()
            override val computation: Scheduler = AndroidSchedulers.mainThread()
            override val io: Scheduler = AndroidSchedulers.mainThread()
        }
    }
}
