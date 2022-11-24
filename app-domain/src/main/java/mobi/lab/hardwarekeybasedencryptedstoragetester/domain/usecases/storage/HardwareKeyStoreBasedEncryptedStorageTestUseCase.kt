package mobi.lab.hardwarekeybasedencryptedstoragetester.domain.usecases.storage

import io.reactivex.rxjava3.core.Single
import mobi.lab.hardwarekeybasedencryptedstoragetester.domain.entities.StorageTestResult
import mobi.lab.hardwarekeybasedencryptedstoragetester.domain.usecases.UseCase
import javax.inject.Inject

class HardwareKeyStoreBasedEncryptedStorageTestUseCase @Inject constructor() : UseCase() {
    fun execute(): Single<StorageTestResult> {
        return Single.fromCallable { runTest() }
    }

    private fun runTest(): StorageTestResult {
        return StorageTestResult.Success
    }
}
