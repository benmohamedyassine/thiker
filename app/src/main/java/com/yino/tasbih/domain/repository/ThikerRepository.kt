package com.yino.tasbih.domain.repository

import com.yino.tasbih.domain.model.Thiker
import kotlinx.coroutines.flow.Flow

interface ThikerRepository {
    val thikerList: Flow<List<Thiker>>
    suspend fun getThikerById(id: Long): Thiker?
    suspend fun createThiker(thiker: Thiker): Long?
    suspend fun updateThiker(thiker: Thiker)
    suspend fun updateAllOrderIndex(thikerList: List<Thiker>)
    suspend fun deleteThiker(thiker: Thiker)
}