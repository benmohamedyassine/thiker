package com.yino.tasbih.di

import com.yino.tasbih.data.repository.ThikerRepositoryImpl
import com.yino.tasbih.domain.repository.ThikerRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {

    @Binds
    fun bindThikerRepository(thikerRepositoryImpl: ThikerRepositoryImpl): ThikerRepository
}