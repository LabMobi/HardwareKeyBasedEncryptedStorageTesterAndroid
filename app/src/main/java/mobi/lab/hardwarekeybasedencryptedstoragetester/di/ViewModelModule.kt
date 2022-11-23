package mobi.lab.hardwarekeybasedencryptedstoragetester.di

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import mobi.lab.hardwarekeybasedencryptedstoragetester.di.annotations.ViewModelKey
import mobi.lab.hardwarekeybasedencryptedstoragetester.main.MainViewModel
import mobi.lab.hardwarekeybasedencryptedstoragetester.splash.SplashViewModel

@Module(includes = [ViewModelModule.Definitions::class])
object ViewModelModule {

    @Module
    internal interface Definitions {
        @Binds
        @IntoMap
        @ViewModelKey(SplashViewModel::class)
        fun bindSplashViewModel(viewModel: SplashViewModel): ViewModel

        @Binds
        @IntoMap
        @ViewModelKey(MainViewModel::class)
        fun bindMainViewModel(viewModel: MainViewModel): ViewModel
    }
}
