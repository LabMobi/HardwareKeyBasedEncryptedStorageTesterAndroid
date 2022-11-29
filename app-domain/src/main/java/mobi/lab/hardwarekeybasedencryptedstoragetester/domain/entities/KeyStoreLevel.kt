package mobi.lab.hardwarekeybasedencryptedstoragetester.domain.entities

sealed class KeyStoreLevel {
    object None : KeyStoreLevel() {
        override fun toString(): String {
            return "None"
        }
    }

    object TEE : KeyStoreLevel() {
        override fun toString(): String {
            return "TEE hardware"
        }
    }

    object Strongbox : KeyStoreLevel() {
        override fun toString(): String {
            return "Strongbox Keymaster hardware"
        }
    }
}
