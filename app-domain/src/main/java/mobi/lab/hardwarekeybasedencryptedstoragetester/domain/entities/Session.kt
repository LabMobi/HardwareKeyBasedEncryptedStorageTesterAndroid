package mobi.lab.hardwarekeybasedencryptedstoragetester.domain.entities

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import mobi.lab.hardwarekeybasedencryptedstoragetester.app.common.isStringEmpty

@Parcelize
data class Session(val token: String?) : Parcelable {

    fun isValid(): Boolean {
        return !isStringEmpty(token)
    }
}
