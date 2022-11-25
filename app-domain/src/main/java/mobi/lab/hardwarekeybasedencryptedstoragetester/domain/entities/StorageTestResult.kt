package mobi.lab.hardwarekeybasedencryptedstoragetester.domain.entities

sealed class StorageTestResult {
    class Success(val measurementResults: StorageSpeedMeasurementResults) : StorageTestResult() {
        override fun toString(): String {
            return "Success(measurements=$measurementResults)"
        }
    }

    data class Failed(val throwable: Throwable) : StorageTestResult()
}
