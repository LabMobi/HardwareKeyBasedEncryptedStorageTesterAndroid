package mobi.lab.hardwarekeybasedencryptedstoragetester.infrastructure.common.http

interface ErrorTransformer {
    fun transform(error: Throwable): Throwable
}
