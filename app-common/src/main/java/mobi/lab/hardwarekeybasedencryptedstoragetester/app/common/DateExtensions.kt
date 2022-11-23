package mobi.lab.hardwarekeybasedencryptedstoragetester.app.common

import java.time.OffsetDateTime

fun OffsetDateTime.toEpochMilli() = this.toInstant().toEpochMilli()
