package mobi.lab.hardwarekeybasedencryptedstoragetester.splash

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.disposables.Disposable
import mobi.lab.hardwarekeybasedencryptedstoragetester.common.rx.dispose
import mobi.lab.hardwarekeybasedencryptedstoragetester.mvvm.SingleEvent
import mobi.lab.hardwarekeybasedencryptedstoragetester.mvvm.asLiveData
import javax.inject.Inject

class SplashViewModel @Inject constructor() : ViewModel() {

    private val _action = MutableLiveData<SingleEvent<Action>>()
    val action = _action.asLiveData()

    private var disposable: Disposable? = null

    init {
        startMain()
    }

    private fun startMain() {
        _action.value = SingleEvent(Action.LaunchApplication)
    }

    override fun onCleared() {
        super.onCleared()
        dispose(disposable)
    }

    sealed class Action {
        object LaunchApplication : Action()
    }
}

