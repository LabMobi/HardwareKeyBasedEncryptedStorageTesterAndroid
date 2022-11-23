package mobi.lab.hardwarekeybasedencryptedstoragetester.login

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.activityScenarioRule
import androidx.test.filters.LargeTest
import mobi.lab.hardwarekeybasedencryptedstoragetester.R
import mobi.lab.hardwarekeybasedencryptedstoragetester.main.MainActivity
import mobi.lab.hardwarekeybasedencryptedstoragetester.util.BaseInstrumentationTest
import mobi.lab.hardwarekeybasedencryptedstoragetester.util.ConditionIdlingResource
import mobi.lab.hardwarekeybasedencryptedstoragetester.util.conditions.FragmentNotResumedCondition
import mobi.lab.hardwarekeybasedencryptedstoragetester.util.conditions.FragmentResumedCondition
import mobi.lab.hardwarekeybasedencryptedstoragetester.util.hasNoTextInputLayoutError
import mobi.lab.hardwarekeybasedencryptedstoragetester.util.hasTextInputLayoutError
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@LargeTest
class LoginActivityTest : BaseInstrumentationTest() {

    /**
     * Use [androidx.test.ext.junit.rules.ActivityScenarioRule] to create and launch the activity under test before each test,
     * and close it after each test. This is a replacement for
     * [androidx.test.rule.ActivityTestRule].
     */
    @get:Rule val activityScenarioRule = activityScenarioRule<LoginActivity>()

    private lateinit var activity: LoginActivity
    private val idlingResource = ConditionIdlingResource()

    @Before
    fun setUp() {
        Intents.init()
        activityScenarioRule.scenario.onActivity {
            activity = it
        }
        registerIdlingResource(idlingResource)
    }

    @After
    fun tearDown() {
        Intents.release()
        idlingResource.destroy()
        unregisterIdlingResource(idlingResource)
    }

    @Test
    fun show_input_error_when_fields_are_empty() {
        onView(withId(R.id.button_login)).perform(click())

        waitForProgressDialogToShowAndHide()

        onView(withId(R.id.input_layout_email)).check(matches(hasTextInputLayoutError(TEXT_ID_REQUIRED)))
        onView(withId(R.id.input_layout_password)).check(matches(hasTextInputLayoutError(TEXT_ID_REQUIRED)))

        Intents.assertNoUnverifiedIntents()
    }

    @Test
    fun show_input_error_when_only_username_is_filled() {
        onView(withId(R.id.edit_text_email)).perform(typeText("asd"), closeSoftKeyboard())
        onView(withId(R.id.button_login)).perform(click())

        waitForProgressDialogToShowAndHide()

        onView(withId(R.id.input_layout_email)).check(matches(hasNoTextInputLayoutError()))
        onView(withId(R.id.input_layout_password)).check(matches(hasTextInputLayoutError(TEXT_ID_REQUIRED)))

        Intents.assertNoUnverifiedIntents()
    }

    @Test
    fun show_input_error_when_only_password_is_filled() {
        onView(withId(R.id.edit_text_password)).perform(typeText("asd"), closeSoftKeyboard())
        onView(withId(R.id.button_login)).perform(click())

        waitForProgressDialogToShowAndHide()

        onView(withId(R.id.input_layout_email)).check(matches(hasTextInputLayoutError(TEXT_ID_REQUIRED)))
        onView(withId(R.id.input_layout_password)).check(matches(hasNoTextInputLayoutError()))

        Intents.assertNoUnverifiedIntents()
    }

    @Test
    fun login_success_when_fields_are_filled() {
        onView(withId(R.id.edit_text_email)).perform(typeText("asd"), closeSoftKeyboard())
        onView(withId(R.id.edit_text_password)).perform(typeText("asd"), closeSoftKeyboard())
        onView(withId(R.id.button_login)).perform(click())

        waitForProgressDialogToShowAndHide()

        Intents.intended(IntentMatchers.hasComponent(MainActivity::class.java.name))
    }

    @Test
    fun show_error_dialog_when_login_fails() {
        // "test" is a keyword to trigger an error response. See LoginUseCase implementation
        onView(withId(R.id.edit_text_email)).perform(typeText("test"), closeSoftKeyboard())
        onView(withId(R.id.edit_text_password)).perform(typeText("asd"), closeSoftKeyboard())
        onView(withId(R.id.button_login)).perform(click())

        waitForProgressDialogToShowAndHide()
        idlingResource.addCondition(FragmentResumedCondition(activity, LoginFragment.TAG_DIALOG_ERROR))

        // Validate the dialog and close it
        onView(withText(R.string.error_generic)).check(matches(isDisplayed()))
        onView(withText(android.R.string.ok)).perform(click())

        onView(withId(R.id.input_layout_email)).check(matches(hasNoTextInputLayoutError()))
        onView(withId(R.id.input_layout_password)).check(matches(hasNoTextInputLayoutError()))

        Intents.assertNoUnverifiedIntents()
    }

    private fun waitForProgressDialogToShowAndHide() {
        // Wait for the dialog to show and then hide itself
        idlingResource.addCondition(FragmentResumedCondition(activity, LoginFragment.TAG_DIALOG_PROGRESS))
        idlingResource.addCondition(FragmentNotResumedCondition(activity, LoginFragment.TAG_DIALOG_PROGRESS))
    }

    companion object {
        private const val TEXT_ID_REQUIRED = R.string.text_required
    }
}
