package mobi.lab.hardwarekeybasedencryptedstoragetester.main

sealed class UiTestStatus {
    object NotStated : UiTestStatus() {
        override fun toString(): String {
            return "NotStated"
        }
    }

    object InProgress : UiTestStatus() {
        override fun toString(): String {
            return "InProgress"
        }
    }

    data class FailedGeneric(val error: Throwable) : UiTestStatus()
    object Success : UiTestStatus() {
        override fun toString(): String {
            return "Success"
        }
    }
}
