package com.kyodude.hike.di

import com.kyodude.hike.model.api.ApiService
import com.kyodude.hike.model.api.ApiConfig
import com.kyodude.hike.repository.MainRepository
import com.kyodude.hike.repository.Repository
import com.kyodude.weatherapp.util.DispatcherProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SingletonModule {

    @Singleton
    @Provides
    fun provideApiService(): ApiService {

        val oktHttpClient = OkHttpClient.Builder()
        oktHttpClient.addInterceptor(object : Interceptor {
            @Throws(IOException::class)
            override fun intercept(chain: Interceptor.Chain): Response {
                val request = chain.request().newBuilder()
                val originalHttpUrl = chain.request().url
                val url = originalHttpUrl.newBuilder()
                    .addQueryParameter("api_key", ApiConfig.apiKey)
                    .addQueryParameter("method", ApiConfig.method)
                    .addQueryParameter("format", ApiConfig.format)
                    .addQueryParameter("nojsoncallback", ApiConfig.noJsonCallback)
                    .build()
                request.url(url)
                return chain.proceed(request.build())
            }
        })

       return Retrofit.Builder()
        .baseUrl(ApiConfig.BASE_URL)
        .client(oktHttpClient.build())
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(ApiService::class.java)
    }

    @Singleton
    @Provides
    fun provideMainRepository(api: ApiService) : Repository = MainRepository(api)

    @Singleton
    @Provides
    fun provideDispatcher(): DispatcherProvider = object: DispatcherProvider{
        override val main: CoroutineDispatcher
            get() = Dispatchers.Main
        override val io: CoroutineDispatcher
            get() = Dispatchers.IO
        override val default: CoroutineDispatcher
            get() = Dispatchers.Default
        override val unconfined: CoroutineDispatcher
            get() = Dispatchers.Unconfined
    }
}