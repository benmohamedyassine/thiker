package com.yino.tasbih.di

import com.yino.tasbih.data.source.DataSource
import com.yino.tasbih.data.source.local.LocalThikerDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface DataSourceModule {

    @Binds
    fun bindDataSourceLocalThiker(localThikerDataSource: LocalThikerDataSource) : DataSource.LocalThiker
}