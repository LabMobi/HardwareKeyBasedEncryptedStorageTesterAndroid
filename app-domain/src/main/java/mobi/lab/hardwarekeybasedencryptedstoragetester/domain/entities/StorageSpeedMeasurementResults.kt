package mobi.lab.hardwarekeybasedencryptedstoragetester.domain.entities

import android.content.Context
import mobi.lab.hardwarekeybasedencryptedstoragetester.domain.R
import java.math.RoundingMode

@Suppress("LongParameterList")
class StorageSpeedMeasurementResults(
    val dataSizeBytes: Int,
    val writeClearText: StorageSpeedMeasurement,
    val writeEncrypted: StorageSpeedMeasurement,
    val readClearText: StorageSpeedMeasurement,
    val readEncrypted: StorageSpeedMeasurement,
    val deleteClearText: StorageSpeedMeasurement,
    val deleteEncrypted: StorageSpeedMeasurement
) {

    fun getFormattedResults(context: Context): String {
        return context.getString(
            R.string.text_measurement_log_line,
            roundAndFormatToSec(writeClearText.averageSec),
            roundAndFormatToSec(writeClearText.standardDeviationSec),
            roundAndFormatToSec(writeEncrypted.averageSec),
            roundAndFormatToSec(writeEncrypted.standardDeviationSec),
            roundAndFormatToSec(readClearText.averageSec),
            roundAndFormatToSec(readClearText.standardDeviationSec),
            roundAndFormatToSec(readEncrypted.averageSec),
            roundAndFormatToSec(readEncrypted.standardDeviationSec),
            roundAndFormatToSec(deleteClearText.averageSec),
            roundAndFormatToSec(deleteClearText.standardDeviationSec),
            roundAndFormatToSec(deleteEncrypted.averageSec),
            roundAndFormatToSec(deleteEncrypted.standardDeviationSec),
            dataSizeBytes.toShort()
        )
    }

    @Suppress("MagicNumber")
    private fun roundAndFormatToSec(value: Double): String {
        return value.toBigDecimal().setScale(3, RoundingMode.HALF_UP).toPlainString()
    }
}
