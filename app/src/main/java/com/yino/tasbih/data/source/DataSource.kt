package com.yino.tasbih.data.source

import com.yino.tasbih.domain.model.Thiker
import kotlinx.coroutines.flow.Flow

interface DataSource {
    interface LocalThiker {
        fun getThikerList(): Flow<List<Thiker>>
        suspend fun getThikerById(thikerId: Long): Thiker?
        suspend fun createThiker(thiker: Thiker): Long?
        suspend fun updateThiker(thiker: Thiker)
        suspend fun updateAllOrderIndex(thikerList: List<Thiker>)
        suspend fun deleteThiker(thiker: Thiker)
    }
}