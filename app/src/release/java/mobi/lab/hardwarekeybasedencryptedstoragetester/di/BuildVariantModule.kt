package mobi.lab.hardwarekeybasedencryptedstoragetester.di

import dagger.Module
import dagger.Provides
import mobi.lab.hardwarekeybasedencryptedstoragetester.common.debug.DebugActions
import mobi.lab.hardwarekeybasedencryptedstoragetester.debug.ReleaseDebugActions
import javax.inject.Singleton

@Module
object BuildVariantModule {

    @Provides
    @Singleton
    fun provideDebugActions(): DebugActions {
        return ReleaseDebugActions()
    }
}
