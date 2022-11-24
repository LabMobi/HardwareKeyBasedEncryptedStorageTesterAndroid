package mobi.lab.hardwarekeybasedencryptedstoragetester.domain.entities

sealed class StorageTestResult {
    object Success : StorageTestResult() {
        override fun toString(): String {
            return "StorageTestResult.Success"
        }
    }
    data class Failed(val throwable: Throwable) : StorageTestResult()
}
