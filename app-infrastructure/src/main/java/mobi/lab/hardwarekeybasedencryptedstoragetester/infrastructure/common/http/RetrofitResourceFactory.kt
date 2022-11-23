package mobi.lab.hardwarekeybasedencryptedstoragetester.infrastructure.common.http

import mobi.lab.hardwarekeybasedencryptedstoragetester.domain.storage.SessionStorage
import mobi.lab.hardwarekeybasedencryptedstoragetester.infrastructure.common.platform.AppEnvironment
import retrofit2.Retrofit
import kotlin.reflect.KClass

internal class RetrofitResourceFactory(
    env: AppEnvironment,
    httpFactory: HttpClientFactory,
    retrofitFactory: RetrofitFactory,
    sessionStorage: SessionStorage,
    converterFactoryProvider: RetrofitConverterFactoryProvider,
) {

    private val retrofit: Retrofit

    init {
        val httpClient = if (env.debug) {
            httpFactory.newInstance(
                interceptors = arrayOf(
                    httpFactory.createContentTypeInterceptor(),
                    httpFactory.createAuthInterceptor(env, sessionStorage),
                    httpFactory.createLoggingInterceptor()
                )
            )
        } else {
            httpFactory.newInstance(
                interceptors = arrayOf(
                    httpFactory.createAuthInterceptor(env, sessionStorage),
                    httpFactory.createContentTypeInterceptor()
                )
            )
        }
        retrofit = retrofitFactory.newInstance(env.baseUrl, httpClient, converterFactoryProvider)
    }

    fun <T : Any> create(cls: KClass<T>): T {
        return retrofit.create(cls.java)
    }
}
