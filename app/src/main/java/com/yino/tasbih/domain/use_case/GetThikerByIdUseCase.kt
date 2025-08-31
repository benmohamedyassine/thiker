package com.yino.tasbih.domain.use_case

import com.yino.tasbih.domain.model.Thiker
import com.yino.tasbih.domain.repository.ThikerRepository
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GetThikerByIdUseCase @Inject constructor(
    val thikerRepository: ThikerRepository
) {
    suspend operator fun invoke(id: Long): Thiker? {
        return withContext(Dispatchers.IO) {
            return@withContext thikerRepository.getThikerById(id = id)
        }
    }
}