package mobi.lab.hardwarekeybasedencryptedstoragetester.infrastructure.storage

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.text.TextUtils
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonSyntaxException
import mobi.lab.hardwarekeybasedencryptedstoragetester.domain.entities.KeyStoreLevel
import mobi.lab.hardwarekeybasedencryptedstoragetester.domain.entities.StorageException
import mobi.lab.hardwarekeybasedencryptedstoragetester.domain.gateway.ClearTextStorageGateway
import java.lang.reflect.Type
import javax.inject.Inject

class StorageClearTextImpl @Inject constructor(private val appContext: Context) : ClearTextStorageGateway {

    @SuppressLint("ApplySharedPref")
    @Suppress("SwallowedException")
    override fun storeData(tag: String, data: Any?) {
        val pref = getSharedPrefsFor(tag)

        // Convert the result to JSON and store
        val dataJson = if (data != null) createGson().toJson(data) else null
        // Strategy:
        // We cache the old value temporarily
        // We try to store and write the new value to the disk
        // If success, then we are done
        // If fail then we try to restore the old value and throw an Exception
        try {
            // Get a copy of the old data
            val oldDataJson = pref.getString(getPrimaryDataKey(tag), null)
            val success: Boolean = try {
                // Write the new one to storage it to storage
                pref.edit().putString(getPrimaryDataKey(tag), dataJson).commit()
            } catch (e: Throwable) {
                false
            }
            if (!success) {
                // Restore the memory cache value to the old one
                try {
                    pref.edit().putString(getPrimaryDataKey(tag), oldDataJson).commit()
                } catch (e: Throwable) {
                    // Ignore any and all errors from there
                }
                throw StorageException.forStore("Unable to store a value to device storage. Check if there is space on the disk.")
            }
        } catch (e: Throwable) {
            throw StorageException.forStore("Unable to store a value to device storage. Check if there is space on the disk.")
        }
    }

    @Suppress("SwallowedException")
    override fun <T> retrieveData(tag: String, type: Type): T? {
        val pref = getSharedPrefsFor(tag)

        // Get the value if we have any
        val dataJson = pref.getString(getPrimaryDataKey(tag), null)
        if (TextUtils.isEmpty(dataJson)) {
            return null // We have nothing stored here
        }

        return try {
            createGson().fromJson(dataJson, type)
        } catch (e: JsonSyntaxException) {
            return null // We have nothing
        }
    }

    @SuppressLint("CommitPrefEdits")
    @Suppress("SwallowedException")
    override fun removeData(tag: String) {
        // If we do not do this then the read after remove will not return a null value
        storeData(tag, null)
        try {
            val pref = getSharedPrefsFor(tag)
            pref.edit().remove(tag)
        } catch (e: Throwable) {
            throw StorageException.forStore("Unable to remove a value from device storage. Check if there is space on the disk.")
        }
    }

    override fun getTypeName() = "Clear text storage"

    override fun getKeyStoreLevel(): KeyStoreLevel {
        return KeyStoreLevel.None
    }

    private fun getSharedPrefsFor(tag: String): SharedPreferences {
        return appContext.getSharedPreferences(getStoragePrefix(tag), Context.MODE_PRIVATE)
    }

    private fun getStoragePrefix(tag: String): String {
        return "$STORAGE_ID.STORAGE_$tag"
    }

    private fun getPrimaryDataKey(tag: String): String {
        return "$STORAGE_ID.KEY_PRIMARY_DATA_$tag"
    }

    private fun createGson(): Gson {
        return GsonBuilder().create()
    }

    companion object {
        const val STORAGE_ID = "mobi.lab.hardwarekeybasedencryptedstoragetester_clear"
    }
}
