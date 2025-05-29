package com.postopia.di

import com.postopia.data.remote.AuthInterceptor
import com.postopia.data.remote.AuthRemoteDataSource
import com.postopia.data.remote.CommentRemoteDataSource
import com.postopia.data.remote.PostRemoteDataSource
import com.postopia.data.remote.SpaceRemoteDataSource
import com.postopia.data.remote.UserRemoteDataSource
import com.postopia.data.remote.VoteRemoteDataSource
import com.postopia.data.remote.util.UnitJsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideMoshi(): Moshi {
        return Moshi.Builder()
            .add(UnitJsonAdapter.FACTORY)
            .add(KotlinJsonAdapterFactory())
            .build()
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(authInterceptor: AuthInterceptor): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(authInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient, moshi: Moshi): Retrofit {
        return Retrofit.Builder()
            .baseUrl("http://47.97.225.160:8080/")
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
    }

    @Provides
    @Singleton
    fun provideAuthRemoteDataSource(retrofit: Retrofit): AuthRemoteDataSource {
        return retrofit.create(AuthRemoteDataSource::class.java)
    }

    @Provides
    @Singleton
    fun provideSpaceRemoteDataSource(retrofit: Retrofit) : SpaceRemoteDataSource {
        return retrofit.create(SpaceRemoteDataSource::class.java)
    }

    @Provides
    @Singleton
    fun provideUserRemoteDataSource(retrofit: Retrofit): UserRemoteDataSource {
        return retrofit.create(UserRemoteDataSource::class.java)
    }

    @Provides
    @Singleton
    fun providePostRemoteDataSource(retrofit: Retrofit): PostRemoteDataSource {
        return retrofit.create(PostRemoteDataSource::class.java)
    }

    @Provides
    @Singleton
    fun provideCommentRemoteDataSource(retrofit: Retrofit): CommentRemoteDataSource {
        return retrofit.create(CommentRemoteDataSource::class.java)
    }

    @Provides
    @Singleton
    fun provideVoteRemoteDataSource(retrofit: Retrofit): VoteRemoteDataSource {
        return retrofit.create(VoteRemoteDataSource::class.java)
    }
}

