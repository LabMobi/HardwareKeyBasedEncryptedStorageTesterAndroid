package mobi.lab.hardwarekeybasedencryptedstoragetester.domain.usecases.storage

import io.reactivex.rxjava3.core.Single
import mobi.lab.hardwarekeybasedencryptedstoragetester.domain.entities.StorageTestResult
import mobi.lab.hardwarekeybasedencryptedstoragetester.domain.gateway.LoggerGateway
import mobi.lab.hardwarekeybasedencryptedstoragetester.domain.gateway.StorageGateway
import mobi.lab.hardwarekeybasedencryptedstoragetester.domain.usecases.UseCase
import java.util.Random
import javax.inject.Inject

class HardwareKeyStoreBasedEncryptedStorageTestUseCase @Inject constructor(
    private val storageGateway: StorageGateway,
    private val logger: LoggerGateway
) : UseCase() {
    fun execute(): Single<StorageTestResult> {
        return Single.fromCallable { runTest() }
    }

    @Suppress("MagicNumber")
    private fun runTest(): StorageTestResult {
        try {
            val x1Tag = "x1"
            val x1Data = createRandomBytes(16)
            logger.d("Storing x1 ..")
            storageGateway.storeData(x1Tag, x1Data)

            val x2Tag = "x2"
            val x2Data = createRandomBytes(32)
            logger.d("Storing x2 ..")
            storageGateway.storeData(x2Tag, x2Data)

            val x3Tag = "x3"
            val x3Data = createRandomBytes(256)
            logger.d("Storing x3 ..")
            storageGateway.storeData(x3Tag, x3Data)

            return StorageTestResult.Success
        } catch (t: Throwable) {
            return StorageTestResult.Failed(t)
        }
    }

    private fun createRandomBytes(byteCount: Int): ByteArray {
        val bytes = ByteArray(byteCount)
        Random().nextBytes(bytes)
        return bytes
    }
}
