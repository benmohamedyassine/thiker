package com.yino.tasbih.presentation.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yino.tasbih.data.model.ThikerEntity
import com.yino.tasbih.data.source.local.room.ThikerDao
import com.yino.tasbih.domain.model.Thiker
import com.yino.tasbih.domain.use_case.CreateThikerUseCase
import com.yino.tasbih.domain.use_case.DeleteThikerUseCase
import com.yino.tasbih.domain.use_case.GetThikerByIdUseCase
import com.yino.tasbih.domain.use_case.GetThikerListUseCase
import com.yino.tasbih.domain.use_case.UpdateThikerUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

data class CreateThikerDialogUiState(
    var title: String = "",
    var maxCount: String = "",
    var currentCount: String = "",
    var isCreateThikerDialogOpen: Boolean = false
)

data class UpdateThikerDialogUiState(
    var thiker: Thiker = Thiker(),
    var title: String = "",
    var maxCount: String = "",
    var currentCount: String = "",
    var isUpdateThikerDialogOpen: Boolean = false
)

data class HomeScreenUiState(
    val createThikerDialogUiState: CreateThikerDialogUiState = CreateThikerDialogUiState(),
    val updateThikerDialogUiState: UpdateThikerDialogUiState = UpdateThikerDialogUiState(),
    val thikerList: List<Thiker> = emptyList(),
)


@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val dao: ThikerDao,
    private val getThikerListUseCase: GetThikerListUseCase,
    private val getThikerByIdUseCase: GetThikerByIdUseCase,
    private val createThikerUseCase: CreateThikerUseCase,
    private val updateThikerUseCase: UpdateThikerUseCase,
    private val deleteThikerUseCase: DeleteThikerUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeScreenUiState())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            getThikerListUseCase()
                .collect { thikerList ->
                    _uiState.update {
                        it.copy(
                            thikerList = thikerList.map { newThiker ->
                                val oldThiker = it.thikerList.find { oldThiker -> oldThiker.id == newThiker.id }

                                if (oldThiker != null) {
                                    newThiker.copy(
                                        isSelected = oldThiker.isSelected
                                    )
                                } else {
                                    newThiker
                                }
                            }
                        )
                    }
                }
        }
    }

    fun onUpdateUiStateThikerList(newThikerList: List<Thiker>) {
        _uiState.update {
            it.copy(
                thikerList = newThikerList
            )
        }
    }

    fun initUpdateThikerDialogUiState(thiker: Thiker) {
        _uiState.update {
            it.copy(
                updateThikerDialogUiState = UpdateThikerDialogUiState(
                    thiker = thiker,
                    title = thiker.title,
                    maxCount = thiker.maxCount.toString(),
                    currentCount = thiker.currentCount.toString(),
                    isUpdateThikerDialogOpen = true,
                )
            )
        }
    }

    fun onCreateThikerDialogUiStateChange(newCreateThikerDialogUiState: CreateThikerDialogUiState) {
        _uiState.update {
            it.copy(
                createThikerDialogUiState = newCreateThikerDialogUiState
            )
        }
    }

    fun onUpdateThikerDialogUiStateChange(newUpdateThikerDialogUiState: UpdateThikerDialogUiState) {
        _uiState.update {
            it.copy(
                updateThikerDialogUiState = newUpdateThikerDialogUiState
            )
        }
    }

    fun onCreateThiker(thiker: Thiker) {
        viewModelScope.launch {
            createThikerUseCase(thiker)
        }
    }

    fun onUpdateThiker(thiker: Thiker) {
        viewModelScope.launch {
            updateThikerUseCase(thiker = thiker)
        }
    }

    fun onUpdateThikerList(newThikerList: List<Thiker>) {
//        val newThikerEntityList = newThikerList.mapIndexed { index, thiker ->
//            ThikerEntity(
//                id = thiker.id,
//                title = thiker.title,
//                maxCount = thiker.maxCount,
//                currentCount = thiker.currentCount,
//                orderIndex = index
//            )
//        }
//        viewModelScope.launch {
//            withContext(Dispatchers.IO) {
//                dao.updateAll(thikerEntityList = newThikerEntityList)
//            }
//        }
    }

    fun onDeleteThiker(thiker: Thiker) {
        viewModelScope.launch {
            deleteThikerUseCase(thiker = thiker)
        }
    }

}