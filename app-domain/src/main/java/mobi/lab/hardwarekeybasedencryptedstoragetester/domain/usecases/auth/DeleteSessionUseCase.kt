package mobi.lab.hardwarekeybasedencryptedstoragetester.domain.usecases.auth

import dagger.Reusable
import mobi.lab.hardwarekeybasedencryptedstoragetester.domain.storage.SessionStorage
import mobi.lab.hardwarekeybasedencryptedstoragetester.domain.usecases.UseCase
import javax.inject.Inject

@Reusable
class DeleteSessionUseCase @Inject constructor(private val sessionStorage: SessionStorage) : UseCase() {

    fun execute() {
        sessionStorage.clear()
    }
}
