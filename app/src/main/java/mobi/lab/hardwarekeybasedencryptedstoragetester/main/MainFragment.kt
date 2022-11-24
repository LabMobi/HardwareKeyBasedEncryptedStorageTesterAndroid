package mobi.lab.hardwarekeybasedencryptedstoragetester.main

import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import mobi.lab.hardwarekeybasedencryptedstoragetester.BuildConfig
import mobi.lab.hardwarekeybasedencryptedstoragetester.R
import mobi.lab.hardwarekeybasedencryptedstoragetester.app.common.exhaustive
import mobi.lab.hardwarekeybasedencryptedstoragetester.common.BaseFragment
import mobi.lab.hardwarekeybasedencryptedstoragetester.common.FragmentBindingHolder
import mobi.lab.hardwarekeybasedencryptedstoragetester.common.ViewBindingHolder
import mobi.lab.hardwarekeybasedencryptedstoragetester.common.ViewModelFactory
import mobi.lab.hardwarekeybasedencryptedstoragetester.common.debug.DebugActions
import mobi.lab.hardwarekeybasedencryptedstoragetester.common.dialog.ProgressDialogFragment
import mobi.lab.hardwarekeybasedencryptedstoragetester.common.platform.LogoutMonitor
import mobi.lab.hardwarekeybasedencryptedstoragetester.common.util.DialogUtil
import mobi.lab.hardwarekeybasedencryptedstoragetester.databinding.FragmentMainBinding
import mobi.lab.hardwarekeybasedencryptedstoragetester.di.Injector
import mobi.lab.hardwarekeybasedencryptedstoragetester.login.LoginFragment
import timber.log.Timber
import javax.inject.Inject

class MainFragment : BaseFragment(), ViewBindingHolder<FragmentMainBinding> by FragmentBindingHolder() {

    @Inject
    lateinit var debugActions: DebugActions

    @Inject
    lateinit var factory: ViewModelFactory

    private val viewModel: MainViewModel by viewModels { factory }

    init {
        Injector.inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        LogoutMonitor.reset() // Reset logout monitor. If we can see this screen, then we have a valid session
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return createBinding(FragmentMainBinding.inflate(inflater), this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireBinding {
            initToolbar(this)
            buttonStartEncryptedTest.setOnClickListener { viewModel.onRunTestEncryptedClicked() }
            buttonStartClearTextTest.setOnClickListener { viewModel.onRunTestClearTextClicked() }
        }

        /**
         * Init ViewModel in onViewCreated as they are connected to View's lifecycle.
         */
        initViewModelActions()
        initViewModelState()
    }

    private fun initViewModelActions() {
        viewModel.action.onEachEvent { event ->
            when (event) {
                is MainViewModel.Action.OpenDebug -> openDebug()
                is MainViewModel.Action.ShareResultAndLog -> TODO()
                is MainViewModel.Action.ShareResultOnly -> TODO()
                is MainViewModel.Action.ShareResults -> shareResults(event.state)
            }.exhaustive
        }
    }

    private fun initViewModelState() {
        viewModel.state.onEachNotNull { state ->
            when (state.status) {
                UiTestStatus.NotStated -> onTestEnded()
                UiTestStatus.InProgress -> onTestStarted()
                is UiTestStatus.FailedGeneric -> onTestFailed(state.status)
                UiTestStatus.Success -> onTestSuccess(UiTestStatus.Success)
            }.exhaustive
            onLogLinesUpdated(state.logLines)
        }
    }

    private fun onTestStarted() {
        requireBinding {
            textTitle.text = getString(R.string.text_status_running)
            textTitle.background = null
            showProgress()
        }
    }

    private fun onTestEnded() {
        requireBinding {
            hideProgress()
        }
    }

    private fun onTestSuccess(status: UiTestStatus.Success) {
        onTestEnded()
        requireBinding {
            textTitle.text = getResultAsText(status)
            textTitle.setBackgroundColor(resources.getColor(R.color.green, requireContext().theme))
        }
    }

    private fun onTestFailed(status: UiTestStatus.FailedGeneric) {
        onTestEnded()
        requireBinding {
            textTitle.text = getResultAsText(status)
            textTitle.setBackgroundColor(resources.getColor(R.color.red, requireContext().theme))
        }
    }

    private fun showProgress() {
        if (isProgressShown()) {
            // Already there
            return
        }
        DialogUtil.show(this, ProgressDialogFragment.newInstance(), LoginFragment.TAG_DIALOG_PROGRESS)
    }

    private fun hideProgress() {
        if (!isProgressShown()) {
            // Already there
            return
        }
        DialogUtil.dismiss(this, LoginFragment.TAG_DIALOG_PROGRESS)
    }

    private fun isProgressShown(): Boolean {
        return DialogUtil.isShowing(activity, LoginFragment.TAG_DIALOG_PROGRESS)
    }

    private fun onLogLinesUpdated(logLines: List<CharSequence>) {
        requireBinding {
            textLog.text = getLogAsText(logLines)
            scrollview.post {
                scrollview.smoothScrollTo(0, textLog.bottom)
            }
        }
    }

    private fun getLogAsText(logLines: List<CharSequence>): String {
        return logLines.joinToString(separator = "\n")
    }

    private fun getResultAsText(status: UiTestStatus): String {
        return when (status) {
            is UiTestStatus.FailedGeneric -> getString(R.string.text_status_test_failed_see_log)
            UiTestStatus.InProgress -> getString(R.string.text_status_test_in_progress)
            UiTestStatus.NotStated -> getString(R.string.text_status_test_not_started)
            UiTestStatus.Success -> getString(R.string.text_status_test_success)
        }.exhaustive
    }

    private fun shareResults(state: MainViewModel.State) {
        val text = getResultAsText(state.status) + "\n\n" + getLogAsText(state.logLines)
        shareResultsAsText(text)
    }

    private fun shareResultsAsText(text: String) {
        // Log it also so it is in logcat
        Timber.d("shareResultsAsText:\n$text")
        val intent = Intent()
        intent.action = Intent.ACTION_SEND
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.text_title_share_results))
        intent.putExtra(Intent.EXTRA_TEXT, text)
        try {
            requireActivity().startActivity(Intent.createChooser(intent, getString(R.string.text_title_share_results)))
        } catch (e: ActivityNotFoundException) {
            Timber.e("Share activity not found", e)
        }
    }

    private fun initToolbar(binding: FragmentMainBinding) {
        if (BuildConfig.DEBUG) {
            binding.toolbar.inflateMenu(R.menu.debug_toolbar)
            binding.toolbar.setOnMenuItemClickListener { item ->
                if (item.itemId == R.id.button_debug) {
                    viewModel.onDebugClicked()
                    return@setOnMenuItemClickListener true
                } else if (item.itemId == R.id.button_share) {
                    viewModel.onShareClicked()
                    return@setOnMenuItemClickListener true
                }

                return@setOnMenuItemClickListener false
            }
        } else {
            binding.toolbar.inflateMenu(R.menu.main_toolbar)
            binding.toolbar.setOnMenuItemClickListener { item ->
                if (item.itemId == R.id.button_share) {
                    viewModel.onShareClicked()
                    return@setOnMenuItemClickListener true
                }
                return@setOnMenuItemClickListener false
            }
        }
    }

    private fun openDebug() {
        // Open DebugActivity
        context?.let { debugActions.launchDebugActivity(it) }
    }

    companion object {
        fun newInstance(): MainFragment {
            return MainFragment()
        }
    }
}
