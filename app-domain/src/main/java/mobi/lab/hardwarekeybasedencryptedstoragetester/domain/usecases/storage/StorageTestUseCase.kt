package mobi.lab.hardwarekeybasedencryptedstoragetester.domain.usecases.storage

import com.google.gson.reflect.TypeToken
import io.reactivex.rxjava3.core.Single
import mobi.lab.hardwarekeybasedencryptedstoragetester.domain.entities.StorageSpeedMeasurement
import mobi.lab.hardwarekeybasedencryptedstoragetester.domain.entities.StorageSpeedMeasurementResults
import mobi.lab.hardwarekeybasedencryptedstoragetester.domain.entities.StorageTestResult
import mobi.lab.hardwarekeybasedencryptedstoragetester.domain.gateway.ClearTextStorageGateway
import mobi.lab.hardwarekeybasedencryptedstoragetester.domain.gateway.EncryptedStorageGateway
import mobi.lab.hardwarekeybasedencryptedstoragetester.domain.gateway.LoggerGateway
import mobi.lab.hardwarekeybasedencryptedstoragetester.domain.gateway.StorageGateway
import mobi.lab.hardwarekeybasedencryptedstoragetester.domain.usecases.UseCase
import java.util.Random
import javax.inject.Inject
import kotlin.math.pow
import kotlin.math.sqrt

class StorageTestUseCase @Inject constructor(
    private val encryptedStorageGateway: EncryptedStorageGateway,
    private val clearTextStorageGateway: ClearTextStorageGateway,
    private val logger: LoggerGateway
) : UseCase() {
    fun execute(): Single<StorageTestResult> {
        return Single.fromCallable { runTest() }
    }

    @Suppress("MagicNumber")
    private fun runTest(): StorageTestResult {
        return try {
            runUsageTests()
            val measurementResults = measureUsageSpeed()
            logger.d("Done\n")
            StorageTestResult.Success(measurementResults)
        } catch (t: Throwable) {
            StorageTestResult.Failed(t)
        }
    }

    private fun measureUsageSpeed(): StorageSpeedMeasurementResults {
        val data = createRandomBytes(MEASUREMENT_BYTE_SIZE)

        val writeClearText = measureStorageOp("write", clearTextStorageGateway.getTypeName(), MEASUREMENT_LOOP_COUNT, data.size) {
            clearTextStorageGateway.storeData(it, data)
        }
        val writeEncrypted = measureStorageOp("write", encryptedStorageGateway.getTypeName(), MEASUREMENT_LOOP_COUNT, data.size) {
            encryptedStorageGateway.storeData(it, data)
        }

        val readClearText = measureStorageOp("read", clearTextStorageGateway.getTypeName(), MEASUREMENT_LOOP_COUNT, data.size) {
            clearTextStorageGateway.retrieveData<ByteArray>(it, object : TypeToken<ByteArray>() {}.type)
        }
        val readEncrypted = measureStorageOp("read", encryptedStorageGateway.getTypeName(), MEASUREMENT_LOOP_COUNT, data.size) {
            encryptedStorageGateway.retrieveData<ByteArray>(it, object : TypeToken<ByteArray>() {}.type)
        }

        val deleteClearText = measureStorageOp("delete", clearTextStorageGateway.getTypeName(), MEASUREMENT_LOOP_COUNT, data.size) {
            clearTextStorageGateway.removeData(it)
        }
        val deleteEncrypted = measureStorageOp("delete", encryptedStorageGateway.getTypeName(), MEASUREMENT_LOOP_COUNT, data.size) {
            encryptedStorageGateway.removeData(it)
        }

        return StorageSpeedMeasurementResults(
            writeClearText = writeClearText,
            writeEncrypted = writeEncrypted,
            readClearText = readClearText,
            readEncrypted = readEncrypted,
            deleteClearText = deleteClearText,
            deleteEncrypted = deleteEncrypted
        )
    }

    @Suppress("MagicNumber")
    private fun measureStorageOp(
        operationName: String,
        storageTypeName: String,
        @Suppress("SameParameterValue") loops: Int,
        dataSize: Int,
        operation: (tag: String) -> Unit
    ): StorageSpeedMeasurement {
        var totalDuration = 0.0
        val durations = DoubleArray(loops)
        logger.d("Testing $operationName speed for $storageTypeName ..")
        for (i in 0 until loops) {
            val startMillis = System.currentTimeMillis().toDouble()
            operation.invoke("tag_$i")
            durations[i] = System.currentTimeMillis() - startMillis
            totalDuration += durations[i]
        }
        val averageMillis = totalDuration / loops.toDouble()
        val standardDeviationMillis = calculateStandardDeviation(loops.toDouble(), durations, averageMillis)
        return StorageSpeedMeasurement(
            dataSizeBytes = dataSize,
            averageSec = averageMillis / 1000,
            standardDeviationSec = standardDeviationMillis / 1000
        )
    }

    @Suppress("MagicNumber")
    private fun runUsageTests() {
        for (gateway: StorageGateway in arrayOf(encryptedStorageGateway, clearTextStorageGateway)) {
            for (i in 1..3) {
                logger.d("\nRunning loop $i")
                runTestLoop(gateway)
            }
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
        assert(dataX3Deleted == null) { "The x3 stored value was still available: $dataX3Deleted" }
    }

    private fun createRandomBytes(byteCount: Int): ByteArray {
        val bytes = ByteArray(byteCount)
        Random().nextBytes(bytes)
        return bytes
    }

    private fun calculateStandardDeviation(sampleCount: Double, samplesValues: DoubleArray, average: Double): Double {
        var variance = 0.0
        for (duration in samplesValues) {
            variance += (duration - average).pow(2.0)
        }
        variance /= sampleCount
        return sqrt(variance)
    }

    companion object {
        const val MEASUREMENT_BYTE_SIZE = 32
        const val MEASUREMENT_LOOP_COUNT = 15
    }
}
