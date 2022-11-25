package mobi.lab.hardwarekeybasedencryptedstoragetester.main

import mobi.lab.hardwarekeybasedencryptedstoragetester.domain.entities.StorageSpeedMeasurementResults

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

    class Success(val measurementResults: StorageSpeedMeasurementResults) : UiTestStatus() {
        override fun toString(): String {
            return "Success(measurementResults=$measurementResults)"
        }
    }
}
