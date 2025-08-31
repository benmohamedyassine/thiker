package com.yino.tasbih.domain.use_case

import com.yino.tasbih.domain.model.Thiker
import com.yino.tasbih.domain.repository.ThikerRepository
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UpdateThikerOrderIndexUseCase @Inject constructor(
    val thikerRepository: ThikerRepository
) {
    suspend operator fun invoke(thikerList: List<Thiker>) {
        return withContext(Dispatchers.IO) {
            thikerRepository.updateAllOrderIndex(thikerList)
        }
    }
}
