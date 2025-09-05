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
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.burnoutcrew.reorderable.ItemPosition
import javax.inject.Inject

const val THIKER_LIST_SORT_SAVED_STATE_KEY = "THIKER_LIST_SORT_SAVED_STATE_KEY"

data class ThikerUiState(
    val thiker: Thiker = Thiker(),
    val isLoading: Boolean = false,
)

data class ThikerListUiState(
    val thikerList: List<Thiker> = emptyList(),
    val filteredThikerList: List<Thiker> = emptyList(),
    var query: String = "",
    var savedSortType: ThikerListSortType = ThikerListSortType.Custom,
    var isLoading: Boolean = false,
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
    private val _uiState = MutableStateFlow(ThikerListUiState())
    private val _thikerUiState = MutableStateFlow(ThikerUiState())

    val uiState = _uiState.asStateFlow()
    val thikerUiState = _thikerUiState.asStateFlow()

    init {
        combine(getThikerListUseCase(), _savedSortType) { thikerList, type ->
            _uiState.update {
                it.copy(
                    thikerList = sortThikerList(thikerList = thikerList, sortingType = type),
                    savedSortType = type,
                    isLoading = false
                )
            }
        }
            .launchIn(viewModelScope)
    }

    private fun sortThikerList(thikerList: List<Thiker>, sortingType: ThikerListSortType): List<Thiker> {
        return when (sortingType) {
            ThikerListSortType.Custom -> thikerList.sortedBy { it.orderIndex }
            ThikerListSortType.CreatedDate_ASC -> thikerList.sortedBy { it.createdAt }
            ThikerListSortType.CreatedDate_DESC -> thikerList.sortedByDescending { it.createdAt }
            ThikerListSortType.ModifiedDate_ASC -> thikerList.sortedBy { it.updatedAt }
            ThikerListSortType.ModifiedDate_DESC -> thikerList.sortedByDescending { it.updatedAt }
        }
    }

    fun setSortType(sortType: ThikerListSortType) {
        savedStateHandle.set(key = THIKER_LIST_SORT_SAVED_STATE_KEY, value = sortType)
    }

    fun onQueryChange(query: String) {
        _uiState.update {
            it.copy(
                query = query,
                filteredThikerList = if (query.isNotBlank()) {
                    it.thikerList
                        .filter { thiker ->
                            thiker.title.contains(query.trim())
                        }
                } else emptyList()
            )
        }
    }

    fun onReorderThikerList(from: ItemPosition, to: ItemPosition) {
        _uiState.update { uiState ->
            uiState.copy(
                thikerList = uiState.thikerList
                    .toMutableList()
                    .apply {
                        add(index = to.index, element = removeAt(from.index))

                        val startIndex = minOf(from.index, to.index)
                        val endIndex = maxOf(from.index, to.index)

                        for (index in startIndex..endIndex) {
                            set(
                                index = index,
                                element = get(index).copy(orderIndex = index)
                            )
                        }
                    }
            )
        }
    }

    fun resetCreateUpdateThikerUiState() {
        _thikerUiState.update {
            ThikerUiState()
        }
    }

    fun onUpdateOrderIndex() {
        viewModelScope.launch {
            updateThikerOrderIndexUseCase(thikerList = uiState.value.thikerList)
        }
    }

    fun loadCreateUpdateThikerUiState(thikerId: Long) {
        val thiker = uiState.value.thikerList.find { thiker -> thiker.id == thikerId } !!
        _thikerUiState.update { it.copy(thiker = thiker) }
    }

    fun onUpdateThikerUiState(newThiker: Thiker) {
        _thikerUiState.update {
            it.copy(
                thiker = newThiker
            )
        }
    }

    fun onCreateThiker() {
        viewModelScope.launch {
            createThikerUseCase(
                thiker = thikerUiState.value.thiker.copy(
                    createdAt = System.currentTimeMillis(),
                    updatedAt = System.currentTimeMillis(),
                    orderIndex = uiState.value.thikerList.count()
                )
            )
            clearCreateUpdateThikerUiState()
        }
    }

    fun onUpdateThiker() {
        viewModelScope.launch {
            updateThikerUseCase(
                thiker = thikerUiState.value.thiker.copy(
                    updatedAt = System.currentTimeMillis(),
                )
            )
            clearCreateUpdateThikerUiState()
        }
    }

    fun clearCreateUpdateThikerUiState() {
        _thikerUiState.update {
            ThikerUiState()
        }
    }

    fun onDeleteThiker(thiker: Thiker) {
        viewModelScope.launch {
            deleteThikerUseCase(thiker = thiker)
        }
    }
}
