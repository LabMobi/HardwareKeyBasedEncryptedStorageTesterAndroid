package mobi.lab.hardwarekeybasedencryptedstoragetester.domain.gateway

import io.reactivex.rxjava3.core.Single
import mobi.lab.hardwarekeybasedencryptedstoragetester.domain.entities.Session

interface AuthGateway {
    fun login(username: String, password: String): Single<Session>
}
