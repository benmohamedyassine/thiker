package com.yino.tasbih.domain.use_case

import com.yino.tasbih.domain.model.Thiker
import com.yino.tasbih.domain.repository.ThikerRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CreateThikerUseCase @Inject constructor(
    val thikerRepository: ThikerRepository
) {
    suspend operator fun invoke(thiker: Thiker): Long? {
        return withContext(Dispatchers.IO) {
            return@withContext thikerRepository.createThiker(thiker = thiker)
        }
    }
}