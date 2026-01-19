package com.system.design.di

import com.system.design.data.local.InMemoryUserDao
import com.system.design.data.mapper.UserMapper
import com.system.design.data.remote.UserRemoteSourceStub
import com.system.design.data.repository.UserRepositoryImpl
import com.system.design.domain.usecase.GetUserUseCase

object AppModule {
    fun provideRemoteSource() = UserRemoteSourceStub()
    fun provideUserDao() = InMemoryUserDao()
    fun provideMapper() = UserMapper()
    fun provideRepository() = UserRepositoryImpl(provideRemoteSource(), provideUserDao(), provideMapper())
    fun provideGetUserUseCase() = GetUserUseCase(provideRepository())
}
