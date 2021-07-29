package com.example.photoupload.di

import com.example.photoupload.data.network.NetworkConnectionInterceptor
import com.example.photoupload.data.network.UploadApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    fun provideUploadApi(networkConnectionInterceptor: NetworkConnectionInterceptor): UploadApi{
        return UploadApi(networkConnectionInterceptor)
    }
}