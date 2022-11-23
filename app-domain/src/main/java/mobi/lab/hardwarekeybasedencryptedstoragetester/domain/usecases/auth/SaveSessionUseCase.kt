package mobi.lab.hardwarekeybasedencryptedstoragetester.domain.usecases.auth

import dagger.Reusable
import mobi.lab.hardwarekeybasedencryptedstoragetester.domain.entities.Session
import mobi.lab.hardwarekeybasedencryptedstoragetester.domain.storage.SessionStorage
import mobi.lab.hardwarekeybasedencryptedstoragetester.domain.usecases.UseCase
import javax.inject.Inject

@Reusable
class SaveSessionUseCase @Inject constructor(private val sessionStorage: SessionStorage) : UseCase() {

    fun execute(session: Session) {
        sessionStorage.save(session)
    }
}
