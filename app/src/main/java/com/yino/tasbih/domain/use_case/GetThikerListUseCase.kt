package com.yino.tasbih.domain.use_case

import com.yino.tasbih.domain.model.Thiker
import com.yino.tasbih.domain.repository.ThikerRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetThikerListUseCase @Inject constructor(
    private val thikerRepository: ThikerRepository
) {
    operator fun invoke(): Flow<List<Thiker>> =
        thikerRepository.thikerList
}