package com.yino.tasbih.di

import android.content.Context
import androidx.room.Room
import com.yino.tasbih.data.source.local.room.ThikerDao
import com.yino.tasbih.data.source.local.room.ThikerRoomDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RoomDatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): ThikerRoomDatabase {
        return Room.databaseBuilder(
            context = context.applicationContext,
            klass = ThikerRoomDatabase::class.java,
            name = "thiker.db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideThikerDao(appDatabase: ThikerRoomDatabase) : ThikerDao {
        return appDatabase.thikerDao()
    }
}