package com.vinh.wondermusic.di

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.vinh.wondermusic.common.Config
import com.vinh.wondermusic.common.DataLocal
import com.vinh.wondermusic.data.apis.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideAuthInterceptor(): Interceptor {
        return Interceptor { chain ->
            val request = chain.request().newBuilder()
//                .header("Accept", "application/json")
                .header("Authorization", "Bearer ${DataLocal.ACCESS_TOKEN}")
                .build()
            chain.proceed(request)
        }
    }

    @Provides
    @Singleton
    fun provideOKHttpClient(
        httpLoggingInterceptor: HttpLoggingInterceptor,
        authInterceptor: Interceptor,
    ): OkHttpClient {
        val builder = OkHttpClient.Builder()

        builder.interceptors().add(httpLoggingInterceptor)
        builder.addInterceptor(authInterceptor)
        builder.callTimeout(2, TimeUnit.MINUTES).readTimeout(2, TimeUnit.MINUTES)
        return builder.build()
    }

    @Provides
    @Singleton
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        return httpLoggingInterceptor
    }

    @Provides
    @Singleton
    fun provideMoshiConverterFactory(): MoshiConverterFactory {
        val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
        return MoshiConverterFactory.create(moshi)
    }

    @Provides
    @Singleton
    @Named("MainSite")
    fun provideRetrofitMainSite(
        okHttpClient: OkHttpClient,
        moshiConverterFactory: MoshiConverterFactory
    ): Retrofit {
        return Retrofit.Builder().addConverterFactory(moshiConverterFactory)
//            .baseUrl("http://192.168.1.8:8000/")
            .baseUrl(Config.MainSite)
            .client(okHttpClient)
            .build()
    }

    @Provides
    fun provideSongAPI(@Named("MainSite") retrofit: Retrofit): SongAPI {
        return retrofit.create(SongAPI::class.java)
    }

    @Provides
    fun provideAccountAPI(@Named("MainSite") retrofit: Retrofit): AccountAPI {
        return retrofit.create(AccountAPI::class.java)
    }

    @Provides
    fun provideCommentAPI(@Named("MainSite") retrofit: Retrofit): CommentAPI {
        return retrofit.create(CommentAPI::class.java)
    }

    @Provides
    fun provideNotificationAPI(@Named("MainSite") retrofit: Retrofit): NotificationAPI {
        return retrofit.create(NotificationAPI::class.java)
    }

    @Provides
    fun providePlaylistAPI(@Named("MainSite") retrofit: Retrofit): PlaylistAPI {
        return retrofit.create(PlaylistAPI::class.java)
    }

    @Provides
    fun provideAlbumAPI(@Named("MainSite") retrofit: Retrofit): AlbumAPI {
        return retrofit.create(AlbumAPI::class.java)
    }

    @Provides
    fun provideTypeAPI(@Named("MainSite") retrofit: Retrofit): TypeAPI {
        return retrofit.create(TypeAPI::class.java)
    }
}