package com.example.festa.services

import com.example.festa.BuildConfig
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


@InstallIn(SingletonComponent::class)
@Module
object AppModule {
    @Provides
    fun providesBaseUrl(): String {
        return BuildConfig.API_KEY
    }


    @Provides
    fun providesHttpLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    }

    @Provides
    fun providesOkhttpClient(interceptor: HttpLoggingInterceptor): OkHttpClient {
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .callTimeout(60, TimeUnit.SECONDS)
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
        return okHttpClient.build()
    }
    @Provides
    fun providesConverterFactory(): Converter.Factory {
        return GsonConverterFactory.create()
    }

    var gson: Gson = GsonBuilder()
        .setLenient()
        .create()


    @Provides
    fun providesRetrofit(
        client: OkHttpClient
    ): Retrofit {
        return Retrofit.Builder()
            //.baseUrl(baseUrl)
            .baseUrl(BuildConfig.API_KEY)
            //.addConverterFactory(converterFactory)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(client)
            .build()
    }

    @Provides
    fun providesRetrofitService(retrofit: Retrofit): ApiServices {
        return retrofit.create(ApiServices::class.java)
    }
}