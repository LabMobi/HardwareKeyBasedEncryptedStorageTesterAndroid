package mobi.lab.hardwarekeybasedencryptedstoragetester.infrastructure.auth.local

import mobi.lab.hardwarekeybasedencryptedstoragetester.domain.entities.Session
import javax.inject.Inject

internal class DbSessionMapper @Inject constructor() {

    fun toEntity(item: DbSession): Session {
        return Session(item.token)
    }

    fun toDb(item: Session): DbSession {
        return DbSession(item.token)
    }
}
