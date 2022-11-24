package mobi.lab.hardwarekeybasedencryptedstoragetester.infrastructure.di

import dagger.Binds
import dagger.Module
import mobi.lab.hardwarekeybasedencryptedstoragetester.domain.gateway.AuthGateway
import mobi.lab.hardwarekeybasedencryptedstoragetester.domain.gateway.LoggerGateway
import mobi.lab.hardwarekeybasedencryptedstoragetester.domain.gateway.StorageGateway
import mobi.lab.hardwarekeybasedencryptedstoragetester.infrastructure.auth.AuthProvider
import mobi.lab.hardwarekeybasedencryptedstoragetester.infrastructure.common.logger.Logger
import mobi.lab.hardwarekeybasedencryptedstoragetester.infrastructure.storage.StorageImpl

@Module(includes = [GatewayModule.Definitions::class])
object GatewayModule {

    @Module
    internal interface Definitions {
        @Binds fun bindAuthGateway(impl: AuthProvider): AuthGateway
        @Binds fun bindLoggerGateway(impl: Logger): LoggerGateway
        @Binds fun bindStorageGateway(impl: StorageImpl): StorageGateway
    }
}
