package com.yino.tasbih.data.source.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.yino.tasbih.data.model.ThikerEntity
import javax.inject.Singleton

@Singleton
@Database(
    entities = [ThikerEntity::class],
    version = 1,
    exportSchema = false
)
abstract class ThikerRoomDatabase : RoomDatabase() {
    abstract fun thikerDao(): ThikerDao
}