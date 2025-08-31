package com.yino.tasbih.presentation.thiker_list

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yino.tasbih.domain.model.Thiker
import com.yino.tasbih.domain.use_case.CreateThikerUseCase
import com.yino.tasbih.domain.use_case.DeleteThikerUseCase
import com.yino.tasbih.domain.use_case.GetThikerListUseCase
import com.yino.tasbih.domain.use_case.UpdateThikerOrderIndexUseCase
import com.yino.tasbih.domain.use_case.UpdateThikerUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

const val THIKER_LIST_SORT_SAVED_STATE_KEY = "THIKER_LIST_SORT_SAVED_STATE_KEY"

data class CreateUpdateThikerUiState(
    val thiker: Thiker = Thiker(),
    val isLoading: Boolean = false,
)

data class ThikerListUiState(
    val items: List<Thiker> = emptyList(),
    val isLoading: Boolean = false,
)

@HiltViewModel
class ThikerListScreenViewModel @Inject constructor(
    private val getThikerListUseCase: GetThikerListUseCase,
    private val createThikerUseCase: CreateThikerUseCase,
    private val updateThikerUseCase: UpdateThikerUseCase,
    private val updateThikerOrderIndexUseCase: UpdateThikerOrderIndexUseCase,
    private val deleteThikerUseCase: DeleteThikerUseCase,
    val savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val _savedSortType = savedStateHandle
        .getStateFlow(THIKER_LIST_SORT_SAVED_STATE_KEY, ThikerListSortType.Custom)

    private val _createUpdateThikerUiState = MutableStateFlow(CreateUpdateThikerUiState())
    val createUpdateThikerUiState = _createUpdateThikerUiState.asStateFlow()

    val uiState: StateFlow<ThikerListUiState> = combine(
        getThikerListUseCase(), _savedSortType
    ) { items, type ->
        ThikerListUiState(
            items = sortItems(items = items, sortingType = type),
            isLoading = false
        )
    }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = ThikerListUiState(isLoading = true)
        )

    fun setSorting(requestType: ThikerListSortType) {
        savedStateHandle.set(key = THIKER_LIST_SORT_SAVED_STATE_KEY, value = requestType)
    }

    private fun sortItems(items: List<Thiker>, sortingType: ThikerListSortType): List<Thiker> {
        return when (sortingType) {
            ThikerListSortType.Custom -> items.sortedBy { it.orderIndex }
            ThikerListSortType.CreatedDate_ASC -> items.sortedBy { it.createdAt }
            ThikerListSortType.CreatedDate_DESC -> items.sortedByDescending { it.createdAt }
            ThikerListSortType.ModifiedDate_ASC -> items.sortedBy { it.updatedAt }
            ThikerListSortType.ModifiedDate_DESC -> items.sortedByDescending { it.updatedAt }
        }
    }

    fun resetCreateUpdateThikerUiState() {
        _createUpdateThikerUiState.update {
            CreateUpdateThikerUiState()
        }
    }

    fun onUpdateOrderIndex(thikerList: List<Thiker>) {
        viewModelScope.launch {
            updateThikerOrderIndexUseCase(thikerList = thikerList)
        }
    }

    fun initUpdateCreateUpdateThikerUiState(thiker: Thiker) {
        _createUpdateThikerUiState.update {
            it.copy(
                thiker = thiker
            )
        }
    }

    fun onCreateThiker() {
        viewModelScope.launch {
            createThikerUseCase(
                thiker = createUpdateThikerUiState.value.thiker.copy(
                    createdAt = System.currentTimeMillis(),
                    updatedAt = System.currentTimeMillis(),
                    orderIndex = uiState.value.items.count()
                )
            )
        }
        _createUpdateThikerUiState.update {
            CreateUpdateThikerUiState()
        }
    }

    fun onUpdateThiker() {
        viewModelScope.launch {
            updateThikerUseCase(
                thiker = createUpdateThikerUiState.value.thiker.copy(
                    updatedAt = System.currentTimeMillis(),
                )
            )
        }
        _createUpdateThikerUiState.update {
            CreateUpdateThikerUiState()
        }
    }

    fun onDeleteThiker(thiker: Thiker) {
        viewModelScope.launch {
            deleteThikerUseCase(thiker = thiker)
        }
    }
}
