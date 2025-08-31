package com.yino.tasbih.data.source.local

import com.yino.tasbih.data.model.ThikerEntity
import com.yino.tasbih.data.source.DataSource
import com.yino.tasbih.data.source.local.room.ThikerDao
import com.yino.tasbih.domain.model.Thiker
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class LocalThikerDataSource @Inject constructor(
    private val thikerDao: ThikerDao
) : DataSource.LocalThiker {
    override fun getThikerList(): Flow<List<Thiker>> {
        return thikerDao.selectAll().map {
            it.map { (id, title, maxCount, currentCount, orderIndex, createdAt, updatedAt) ->
                Thiker(
                    id = id,
                    title = title,
                    maxCount = maxCount,
                    currentCount = currentCount,
                    createdAt = createdAt,
                    updatedAt = updatedAt,
                    orderIndex = orderIndex,
                )
            }
        }
    }

    override suspend fun getThikerById(thikerId: Long): Thiker? {
        return thikerDao.selectById(
            id = thikerId
        )?.run {
            Thiker(
                id = id,
                title = title,
                maxCount = maxCount,
                currentCount = currentCount,
                createdAt = createdAt,
                updatedAt = updatedAt,
                orderIndex = orderIndex,
            )
        }
    }

    override suspend fun createThiker(thiker: Thiker): Long? {
        return thikerDao.create(
            ThikerEntity(
                title = thiker.title,
                maxCount = thiker.maxCount,
                currentCount = thiker.currentCount,
                createdAt = thiker.createdAt,
                updatedAt = thiker.updatedAt,
                orderIndex = thiker.orderIndex,
            )
        )
    }

    override suspend fun updateThiker(thiker: Thiker) {
        thikerDao.update(
            thikerEntity = ThikerEntity(
                id = thiker.id,
                title = thiker.title,
                maxCount = thiker.maxCount,
                currentCount = thiker.currentCount,
                createdAt = thiker.createdAt,
                updatedAt = thiker.updatedAt,
                orderIndex = thiker.orderIndex,
            )
        )
    }

    override suspend fun updateAllOrderIndex(thikerList: List<Thiker>) {
        thikerDao.updateAllOrderIndex(
            thikerList.map { thiker ->
                ThikerEntity(
                    id = thiker.id,
                    title = thiker.title,
                    maxCount = thiker.maxCount,
                    currentCount = thiker.currentCount,
                    createdAt = thiker.createdAt,
                    updatedAt = thiker.updatedAt,
                    orderIndex = thiker.orderIndex,
                )
            }
        )
    }

    override suspend fun deleteThiker(thiker: Thiker) {
        thikerDao.delete(
            ThikerEntity(
                id = thiker.id,
                title = thiker.title,
                maxCount = thiker.maxCount,
                currentCount = thiker.currentCount,
                createdAt = thiker.createdAt,
                updatedAt = thiker.updatedAt,
                orderIndex = thiker.orderIndex,
            )
        )
    }

}