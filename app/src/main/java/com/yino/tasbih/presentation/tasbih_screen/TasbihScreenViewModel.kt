package com.yino.tasbih.presentation.tasbih_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yino.tasbih.domain.model.Thiker
import com.yino.tasbih.domain.use_case.GetThikerByIdUseCase
import com.yino.tasbih.domain.use_case.UpdateThikerUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class TasbihUiState(
    val thiker: Thiker = Thiker(),
    var count: Long = 0,
    var isLoading: Boolean = false,
)

@HiltViewModel
class TasbihScreenViewModel @Inject constructor(
    private val getThikerByIdUseCase: GetThikerByIdUseCase,
    private val updateThikerUseCase: UpdateThikerUseCase,
) : ViewModel() {

    private val _tasbihUiState = MutableStateFlow(TasbihUiState())
    val tasbihUiState = _tasbihUiState.asStateFlow()

    fun loadThiker(thikerId: Long) {
        _tasbihUiState.update { tasbihUiState ->
            tasbihUiState.copy(isLoading = true)
        }

        viewModelScope.launch {
            val thiker = getThikerByIdUseCase(thikerId) !!

            _tasbihUiState.update { tasbihUiState ->
                tasbihUiState.copy(
                    thiker = thiker,
                    count = thiker.currentCount,
                    isLoading = false
                )
            }
        }
    }

    fun onResetCount() {
        onUpdateCount(0)
    }

    fun onIncrementCount() {
        val maxCount = _tasbihUiState.value.thiker.maxCount
        val currentCount = _tasbihUiState.value.count

        onUpdateCount(
            if (currentCount < maxCount) {
                currentCount + 1
            } else {
                currentCount
            }
        )
    }

    private fun onUpdateCount(count: Long) {
        _tasbihUiState.update { tasbihUiState ->
            tasbihUiState.copy(isLoading = true)
        }

        viewModelScope.launch {
            _tasbihUiState.update { tasbihUiState ->
                updateThikerUseCase(
                    thiker = tasbihUiState.thiker
                        .copy(currentCount = count)
                )

                tasbihUiState.copy(
                    count = count,
                    isLoading = false
                )
            }
        }
    }

}