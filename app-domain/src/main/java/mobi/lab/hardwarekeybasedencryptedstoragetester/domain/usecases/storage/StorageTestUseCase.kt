package mobi.lab.hardwarekeybasedencryptedstoragetester.domain.usecases.storage

import com.google.gson.reflect.TypeToken
import io.reactivex.rxjava3.core.Single
import mobi.lab.hardwarekeybasedencryptedstoragetester.domain.entities.StorageTestResult
import mobi.lab.hardwarekeybasedencryptedstoragetester.domain.gateway.ClearTextStorageGateway
import mobi.lab.hardwarekeybasedencryptedstoragetester.domain.gateway.EncryptedStorageGateway
import mobi.lab.hardwarekeybasedencryptedstoragetester.domain.gateway.LoggerGateway
import mobi.lab.hardwarekeybasedencryptedstoragetester.domain.gateway.StorageGateway
import mobi.lab.hardwarekeybasedencryptedstoragetester.domain.usecases.UseCase
import java.util.Random
import javax.inject.Inject

class StorageTestUseCase @Inject constructor(
    private val encryptedStorageGateway: EncryptedStorageGateway,
    private val clearTextStorageGateway: ClearTextStorageGateway,
    private val logger: LoggerGateway
) : UseCase() {
    fun execute(useEncryptedStorage: Boolean = true): Single<StorageTestResult> {
        return Single.fromCallable { runTest(useEncryptedStorage) }
    }

    @Suppress("MagicNumber")
    private fun runTest(useEncryptedStorage: Boolean): StorageTestResult {
        val storageImpl: StorageGateway = if (useEncryptedStorage) {
            encryptedStorageGateway
        } else {
            clearTextStorageGateway
        }
        return try {
            for (i in 1..3) {
                logger.d("\nRunning loop $i")
                runTestLoop(storageImpl)
            }
            logger.d("Done\n")
            StorageTestResult.Success
        } catch (t: Throwable) {
            StorageTestResult.Failed(t)
        }
    }

    @Suppress("MagicNumber")
    private fun runTestLoop(storageImpl: StorageGateway) {
        val x1Tag = "x1"
        val x1Data = createRandomBytes(16)
        logger.d("Storing x1 ..")
        storageImpl.storeData(x1Tag, x1Data)

        val x2Tag = "x2"
        val x2Data = createRandomBytes(32)
        logger.d("Storing x2 ..")
        storageImpl.storeData(x2Tag, x2Data)

        val x3Tag = "x3"
        val x3Data = createRandomBytes(256)
        logger.d("Storing x3 ..")
        storageImpl.storeData(x3Tag, x3Data)

        logger.d("Comparing the stored x1 to the original x1 ..")
        val data1XStored = storageImpl.retrieveData<ByteArray>(x1Tag, object : TypeToken<ByteArray>() {}.type)!!
        assert(x1Data.contentEquals(data1XStored)) { "Stored x1 does not match the original!" }

        logger.d("Comparing the stored x3 to the original x3 ..")
        val data3XStored = storageImpl.retrieveData<ByteArray>(x3Tag, object : TypeToken<ByteArray>() {}.type)!!
        assert(x3Data.contentEquals(data3XStored)) { "Stored x3 does not match the original!" }

        logger.d("Comparing the stored x2 to the original x2 ..")
        val data2XStored = storageImpl.retrieveData<ByteArray>(x2Tag, object : TypeToken<ByteArray>() {}.type)!!
        assert(x2Data.contentEquals(data2XStored)) { "Stored x2 does not match the original!" }

        logger.d("Deleting x1 from storage ..")
        storageImpl.removeData(x1Tag)

        logger.d("Deleting x2 from storage ..")
        storageImpl.removeData(x2Tag)

        logger.d("Deleting x3 from storage ..")
        storageImpl.removeData(x3Tag)

        logger.d("Checking that x1 can't be retrieved anymore ..")
        val dataX1Deleted = storageImpl.retrieveData<ByteArray>(x1Tag, object : TypeToken<ByteArray>() {}.type)
        assert(dataX1Deleted == null) { "The x1 stored value was still available: $dataX1Deleted" }

        logger.d("Checking that x2 can't be retrieved anymore ..")
        val dataX2Deleted = storageImpl.retrieveData<ByteArray>(x2Tag, object : TypeToken<ByteArray>() {}.type)
        assert(dataX2Deleted == null) { "The x2 stored value was still available: $dataX2Deleted" }

        logger.d("Checking that x3 can't be retrieved anymore ..")
        val dataX3Deleted = storageImpl.retrieveData<ByteArray>(x3Tag, object : TypeToken<ByteArray>() {}.type)
        assert(dataX3Deleted == null) { "The xx stored value was still available: $dataX3Deleted" }
    }

    private fun createRandomBytes(byteCount: Int): ByteArray {
        val bytes = ByteArray(byteCount)
        Random().nextBytes(bytes)
        return bytes
    }
}
