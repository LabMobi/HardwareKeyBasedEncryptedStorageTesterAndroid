package mobi.lab.hardwarekeybasedencryptedstoragetester.infrastructure.auth

import io.reactivex.rxjava3.core.Single
import mobi.lab.hardwarekeybasedencryptedstoragetester.domain.entities.Session
import mobi.lab.hardwarekeybasedencryptedstoragetester.domain.gateway.AuthGateway
import mobi.lab.hardwarekeybasedencryptedstoragetester.infrastructure.auth.remote.ApiLoginRequest
import mobi.lab.hardwarekeybasedencryptedstoragetester.infrastructure.auth.remote.ApiSessionMapper
import mobi.lab.hardwarekeybasedencryptedstoragetester.infrastructure.auth.remote.AuthResource
import javax.inject.Inject

internal class AuthProvider @Inject constructor(
    private val res: AuthResource,
    private val sessionMapper: ApiSessionMapper
) : AuthGateway {

    override fun login(username: String, password: String): Single<Session> {
        return res.login(ApiLoginRequest(email = username, password = password))
            .map(sessionMapper::toEntity)
    }
}
