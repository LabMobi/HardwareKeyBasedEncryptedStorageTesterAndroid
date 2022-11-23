package mobi.lab.hardwarekeybasedencryptedstoragetester.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.disposables.Disposable
import mobi.lab.hardwarekeybasedencryptedstoragetester.common.rx.SchedulerProvider
import mobi.lab.hardwarekeybasedencryptedstoragetester.common.rx.dispose
import mobi.lab.hardwarekeybasedencryptedstoragetester.domain.usecases.auth.LogoutUseCase
import mobi.lab.hardwarekeybasedencryptedstoragetester.mvvm.SingleEvent
import mobi.lab.hardwarekeybasedencryptedstoragetester.mvvm.asLiveData
import timber.log.Timber
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val logoutUseCase: LogoutUseCase,
    private val schedulers: SchedulerProvider,
) : ViewModel() {

    private val _action = MutableLiveData<SingleEvent<Action>>()
    val action = _action.asLiveData()

    private var disposable: Disposable? = null

    override fun onCleared() {
        super.onCleared()
        dispose(disposable)
    }

    fun onOpenPrototypeClicked() {
        // TODO add proper url here
        _action.value = SingleEvent(Action.OpenWebLink("https://www.google.com"))
    }

    fun onDebugClicked() {
        _action.value = SingleEvent(Action.OpenDebug())
    }

    fun onLogoutClicked() {
        dispose(disposable)
        disposable = logoutUseCase.execute()
            .compose(schedulers.completable())
            .subscribe(
                { _action.value = SingleEvent(Action.RestartApplication) }, // Success
                { // Error
                    Timber.e(it, "logout error")
                    _action.value = SingleEvent(Action.RestartApplication)
                }
            )
    }

    sealed class Action {
        data class OpenWebLink(val url: String) : Action()
        object RestartApplication : Action()
        class OpenDebug : Action()
    }
}
