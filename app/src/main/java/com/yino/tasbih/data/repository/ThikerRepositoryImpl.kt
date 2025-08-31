package com.yino.tasbih.data.repository

import com.yino.tasbih.data.source.DataSource
import com.yino.tasbih.domain.model.Thiker
import com.yino.tasbih.domain.repository.ThikerRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ThikerRepositoryImpl @Inject constructor(
    private val local: DataSource.LocalThiker,
) : ThikerRepository {
    override val thikerList: Flow<List<Thiker>>
        get() = local.getThikerList()

    override suspend fun getThikerById(id: Long): Thiker? {
        return local.getThikerById(thikerId = id)
    }

    override suspend fun createThiker(thiker: Thiker): Long? {
        return local.createThiker(thiker)
    }

    override suspend fun updateThiker(thiker: Thiker) {
        local.updateThiker(thiker = thiker)
    }

    override suspend fun updateAllOrderIndex(thikerList: List<Thiker>) {
        local.updateAllOrderIndex(thikerList)
    }

    override suspend fun deleteThiker(thiker: Thiker) {
        local.deleteThiker(thiker = thiker)
    }
}