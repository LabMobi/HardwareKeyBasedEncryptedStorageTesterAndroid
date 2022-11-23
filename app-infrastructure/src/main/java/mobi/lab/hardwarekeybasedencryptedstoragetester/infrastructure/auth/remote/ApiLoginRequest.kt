package mobi.lab.hardwarekeybasedencryptedstoragetester.infrastructure.auth.remote

import androidx.annotation.Keep
import se.ansman.kotshi.JsonSerializable

@Keep
@JsonSerializable
data class ApiLoginRequest(
    val email: String,
    val password: String
)
