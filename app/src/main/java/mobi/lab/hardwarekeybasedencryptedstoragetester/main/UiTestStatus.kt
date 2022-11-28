package mobi.lab.hardwarekeybasedencryptedstoragetester.main

import android.content.Context
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

    class Success(private val measurementResults: List<StorageSpeedMeasurementResults>) : UiTestStatus() {

        fun getFormattedMeasurementResults(context: Context): String {
            var results = ""
            for (measurementResult in measurementResults) {
                results += measurementResult.getFormattedResults(context)
            }
            return results
        }
    }
}
