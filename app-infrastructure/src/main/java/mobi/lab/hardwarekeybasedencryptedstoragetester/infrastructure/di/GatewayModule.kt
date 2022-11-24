package mobi.lab.hardwarekeybasedencryptedstoragetester.infrastructure.di

import dagger.Binds
import dagger.Module
import mobi.lab.hardwarekeybasedencryptedstoragetester.domain.gateway.AuthGateway
import mobi.lab.hardwarekeybasedencryptedstoragetester.domain.gateway.LoggerGateway
import mobi.lab.hardwarekeybasedencryptedstoragetester.infrastructure.auth.AuthProvider
import mobi.lab.hardwarekeybasedencryptedstoragetester.infrastructure.common.logger.Logger

@Module(includes = [GatewayModule.Definitions::class])
object GatewayModule {

    @Module
    internal interface Definitions {
        @Binds fun bindAuthGateway(impl: AuthProvider): AuthGateway
        @Binds fun bindLoggerGateway(impl: Logger): LoggerGateway
    }
}
