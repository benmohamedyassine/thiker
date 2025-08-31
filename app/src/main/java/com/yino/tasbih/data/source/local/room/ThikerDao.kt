package com.yino.tasbih.data.source.local.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.yino.tasbih.data.model.ThikerEntity
import com.yino.tasbih.domain.model.Thiker
import kotlinx.coroutines.flow.Flow

@Dao
interface ThikerDao {

    @Query("SELECT * FROM thiker ORDER BY order_index ASC")
    fun selectAll(): Flow<List<ThikerEntity>>

    @Query("SELECT * FROM thiker WHERE id = :id")
    fun selectById(id: Long): ThikerEntity?

    @Insert
    fun create(thikerEntity: ThikerEntity): Long?

    @Update
    fun update(thikerEntity: ThikerEntity)

    @Query("UPDATE thiker SET order_index = :newOrderIndex WHERE id = :id")
    fun updateOrderIndex(id: Long, newOrderIndex: Int)

    @Transaction
    @Update
    fun updateAll(thikerEntityList: List<ThikerEntity>)

    @Transaction
    @Update
    fun updateAllOrderIndex(thikerEntityList: List<ThikerEntity>) {
        thikerEntityList.forEach { thikerEntity ->
            updateOrderIndex(
                id = thikerEntity.id,
                newOrderIndex = thikerEntity.orderIndex
            )
        }
    }

    @Delete
    fun delete(thikerEntity: ThikerEntity)
}