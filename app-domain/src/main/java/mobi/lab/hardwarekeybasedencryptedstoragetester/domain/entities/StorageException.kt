package mobi.lab.hardwarekeybasedencryptedstoragetester.domain.entities

import java.io.IOException

class StorageException(val errorCode: Int, message: String) : IOException(message) {

    companion object {
        const val ERROR_CODE_UNABLE_TO_STORE = 1
        const val ERROR_CODE_UNABLE_TO_RETRIEVE = 2

        public fun forStore(message: String): StorageException {
            return StorageException(ERROR_CODE_UNABLE_TO_STORE, message)
        }

        public fun forRetrieve(message: String): StorageException {
            return StorageException(ERROR_CODE_UNABLE_TO_RETRIEVE, message)
        }
    }
}
