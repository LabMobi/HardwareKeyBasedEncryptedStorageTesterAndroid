package mobi.lab.hardwarekeybasedencryptedstoragetester.infrastructure.di

import dagger.Module
import dagger.Provides
import mobi.lab.hardwarekeybasedencryptedstoragetester.infrastructure.common.remote.error.ApiErrorResponseMapper
import javax.inject.Singleton

@Module
object MapperModule {

    // Uses HttpException not available outside the package
    @Provides
    @Singleton
    internal fun provideErrorMapper(): ApiErrorResponseMapper = ApiErrorResponseMapper()

    /**
     * Use constructor injection for other mappers to avoid unnecessary boilerplate
     */
}
