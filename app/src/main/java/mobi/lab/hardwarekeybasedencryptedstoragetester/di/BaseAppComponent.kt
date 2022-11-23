package mobi.lab.hardwarekeybasedencryptedstoragetester.di

import mobi.lab.hardwarekeybasedencryptedstoragetester.App
import mobi.lab.hardwarekeybasedencryptedstoragetester.login.LoginFragment
import mobi.lab.hardwarekeybasedencryptedstoragetester.main.MainFragment
import mobi.lab.hardwarekeybasedencryptedstoragetester.prototype.PrototypeActivity
import mobi.lab.hardwarekeybasedencryptedstoragetester.splash.SplashActivity

interface BaseAppComponent {
    fun inject(target: App)
    fun inject(target: SplashActivity)
    fun inject(target: PrototypeActivity)
    fun inject(target: MainFragment)
    fun inject(target: LoginFragment)
}
