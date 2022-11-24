package mobi.lab.hardwarekeybasedencryptedstoragetester.infrastructure.di

import dagger.Binds
import dagger.Module
import mobi.lab.hardwarekeybasedencryptedstoragetester.domain.gateway.AuthGateway
import mobi.lab.hardwarekeybasedencryptedstoragetester.domain.gateway.ClearTextStorageGateway
import mobi.lab.hardwarekeybasedencryptedstoragetester.domain.gateway.LoggerGateway
import mobi.lab.hardwarekeybasedencryptedstoragetester.domain.gateway.EncryptedStorageGateway
import mobi.lab.hardwarekeybasedencryptedstoragetester.infrastructure.auth.AuthProvider
import mobi.lab.hardwarekeybasedencryptedstoragetester.infrastructure.common.logger.Logger
import mobi.lab.hardwarekeybasedencryptedstoragetester.infrastructure.storage.StorageClearTextImpl
import mobi.lab.hardwarekeybasedencryptedstoragetester.infrastructure.storage.StorageEncryptedImpl

@Module(includes = [GatewayModule.Definitions::class])
object GatewayModule {

    @Module
    internal interface Definitions {
        @Binds fun bindAuthGateway(impl: AuthProvider): AuthGateway
        @Binds fun bindLoggerGateway(impl: Logger): LoggerGateway
        @Binds fun bindEncryptedStorageGateway(impl: StorageEncryptedImpl): EncryptedStorageGateway
        @Binds fun bindClearTextStorageGateway(impl: StorageClearTextImpl): ClearTextStorageGateway
    }
}
