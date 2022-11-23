package mobi.lab.hardwarekeybasedencryptedstoragetester.domain.usecases.auth

import dagger.Reusable
import io.reactivex.rxjava3.core.Single
import mobi.lab.hardwarekeybasedencryptedstoragetester.app.common.isStringEmpty
import mobi.lab.hardwarekeybasedencryptedstoragetester.domain.entities.DomainException
import mobi.lab.hardwarekeybasedencryptedstoragetester.domain.entities.ErrorCode
import mobi.lab.hardwarekeybasedencryptedstoragetester.domain.entities.Session
import mobi.lab.hardwarekeybasedencryptedstoragetester.domain.gateway.AuthGateway
import mobi.lab.hardwarekeybasedencryptedstoragetester.domain.usecases.UseCase
import javax.inject.Inject

@Reusable
class LoginUseCase @Inject constructor(
    private val gw: AuthGateway,
    private val saveSessionUseCase: SaveSessionUseCase
) : UseCase() {
    fun execute(email: String, password: String): Single<Session> {
        if (isStringEmpty(email) || isStringEmpty(password)) {
            return Single.error(DomainException(ErrorCode.LOCAL_INVALID_CREDENTIALS))
        }
        // Just a local trick to allow error handling tests
        val magicWord = "test"
        val requestEmail: String
        val requestPassword: String
        if (email.contains(magicWord) || password.contains(magicWord)) {
            requestEmail = ""
            requestPassword = ""
        } else {
            requestEmail = "eve.holt@reqres.in"
            requestPassword = "cityslicka"
        }
        return gw.login(requestEmail, requestPassword).doOnSuccess { saveSessionUseCase.execute(it) }
    }
}
