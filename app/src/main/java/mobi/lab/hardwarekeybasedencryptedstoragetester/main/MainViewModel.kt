package mobi.lab.hardwarekeybasedencryptedstoragetester.main

import android.os.Build
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import mobi.lab.hardwarekeybasedencryptedstoragetester.app.common.exhaustive
import mobi.lab.hardwarekeybasedencryptedstoragetester.common.rx.SchedulerProvider
import mobi.lab.hardwarekeybasedencryptedstoragetester.common.rx.dispose
import mobi.lab.hardwarekeybasedencryptedstoragetester.domain.entities.StorageTestResult
import mobi.lab.hardwarekeybasedencryptedstoragetester.domain.gateway.LoggerGateway
import mobi.lab.hardwarekeybasedencryptedstoragetester.domain.usecases.storage.StorageTestUseCase
import mobi.lab.hardwarekeybasedencryptedstoragetester.mvvm.SingleEvent
import mobi.lab.hardwarekeybasedencryptedstoragetester.mvvm.asLiveData
import java.util.Locale
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val schedulers: SchedulerProvider,
    private val logger: LoggerGateway,
    private val hardwareKeyStoreBasedEncryptedStorageTestUseCase: StorageTestUseCase
) : ViewModel(), LoggerGateway.LoggerGatewayListener {

    private val _action = MutableLiveData<SingleEvent<Action>>()
    val action = _action.asLiveData()

    private val _state = MutableLiveData(defaultState())
    val state = _state.asLiveData()

    private var disposable: Disposable? = null

    init {
        logger.setLogLinesListener(this)
    }

    override fun onCleared() {
        super.onCleared()
        dispose(disposable)
        logger.setLogLinesListener(null)
    }

    fun onRunTestEncryptedClicked() {
        logger.clearLog()
        updateState { it.copy(status = UiTestStatus.InProgress) }
        dispose(disposable)
        logger.d(getDeviceInfoAsText())
        disposable = hardwareKeyStoreBasedEncryptedStorageTestUseCase
            .execute(useEncryptedStorage = true)
            .compose(schedulers.single(Schedulers.computation(), AndroidSchedulers.mainThread()))
            .subscribe(::onTestSuccess, ::onTestFailed)
    }

    fun onRunTestClearTextClicked() {
        logger.clearLog()
        updateState { it.copy(status = UiTestStatus.InProgress) }
        dispose(disposable)
        logger.d(getDeviceInfoAsText())
        disposable = hardwareKeyStoreBasedEncryptedStorageTestUseCase
            .execute(useEncryptedStorage = false)
            .compose(schedulers.single(Schedulers.computation(), AndroidSchedulers.mainThread()))
            .subscribe(::onTestSuccess, ::onTestFailed)
    }

    private fun onTestFailed(error: Throwable) {
        logger.d(Log.getStackTraceString(error))
        updateState { it.copy(status = UiTestStatus.FailedGeneric(error)) }
    }

    private fun onTestSuccess(result: StorageTestResult) {
        logger.d(result.toString())
        updateState { it.copy(status = mapResult(result)) }
    }

    private fun mapResult(result: StorageTestResult): UiTestStatus {
        return when (result) {
            StorageTestResult.Success -> UiTestStatus.Success
            is StorageTestResult.Failed -> UiTestStatus.FailedGeneric(result.throwable)
        }.exhaustive
    }

    private fun getDeviceInfoAsText(): String {
        return "${Build.BRAND.replaceFirstChar { it.uppercase(Locale.ENGLISH) }} ${Build.MODEL} API level ${Build.VERSION.SDK_INT}"
    }

    fun onDebugClicked() {
        _action.value = SingleEvent(Action.OpenDebug)
    }

    fun onShareClicked() {
        startShare()
    }

    private fun startShare() {
        val status = state.value?.status ?: UiTestStatus.NotStated
        when (status) {
            is UiTestStatus.FailedGeneric,
            UiTestStatus.InProgress,
            UiTestStatus.NotStated,
            UiTestStatus.Success -> shareResults(state.value ?: defaultState())
        }.exhaustive
    }

    private fun shareResults(state: State) {
        _action.value = SingleEvent(Action.ShareResults(state))
    }

    private fun currentStateOrDefault(): State {
        return _state.value ?: defaultState()
    }

    private fun updateState(function: (State) -> State) {
        _state.value = function.invoke(currentStateOrDefault())
    }

    private fun defaultState(): State {
        return State(UiTestStatus.NotStated, ArrayList())
    }

    override fun onLogLinesUpdated(logLines: List<CharSequence>) {
        updateState { it.copy(logLines = ArrayList(logLines)) }
    }

    sealed class Action {
        object OpenDebug : Action()
        data class ShareResultOnly(val state: State) : Action()
        data class ShareResultAndLog(val state: State) : Action()
        data class ShareResults(val state: State) : Action()
    }

    data class State(val status: UiTestStatus, val logLines: List<CharSequence>)
}
