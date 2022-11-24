package mobi.lab.hardwarekeybasedencryptedstoragetester.domain.gateway

import mobi.lab.hardwarekeybasedencryptedstoragetester.domain.entities.StorageException
import java.lang.reflect.Type

interface StorageGateway {

    @Throws(StorageException::class)
    fun storeData(tag: String, data: Any?)

    @Throws(StorageException::class)
    fun <T> retrieveData(tag: String, type: Type): T?

    @Throws(StorageException::class)
    fun removeData(tag: String)
}
