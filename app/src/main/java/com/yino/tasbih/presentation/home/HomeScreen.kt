package com.yino.tasbih.presentation.home

import android.annotation.SuppressLint
import android.util.Log
import android.widget.GridLayout
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Sort
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Cancel
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.DragIndicator
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.Sort
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxState
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.yino.tasbih.domain.model.Thiker
import com.yino.tasbih.presentation.thiker_list.ThikerItem
import com.yino.tasbih.presentation.ui.theme.TASBIHTheme
import kotlinx.coroutines.delay
import org.burnoutcrew.reorderable.ReorderableItem
import org.burnoutcrew.reorderable.detectReorder
import org.burnoutcrew.reorderable.rememberReorderableLazyGridState
import org.burnoutcrew.reorderable.rememberReorderableLazyListState
import org.burnoutcrew.reorderable.reorderable
import java.time.LocalDateTime

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    uiState: HomeScreenUiState,
    initUpdateThikerDialogUiState: (Long) -> Unit = {},
    onUpdateUiStateThikerList: (List<Thiker>) -> Unit = {},
    onCreateThikerDialogUiStateChange: (CreateThikerDialogUiState) -> Unit = {},
    onUpdateThikerDialogUiStateChange: (UpdateThikerDialogUiState) -> Unit = {},
    onUpdateThikerList: (List<Thiker>) -> Unit = {},
    onCreateThiker: (Thiker) -> Unit = {},
    onUpdateThiker: (Thiker) -> Unit = {},
    onDeleteThiker: (Thiker) -> Unit = {},
) {
    val scope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState()
    var isBottomSheetVisible by remember { mutableStateOf(false) }

    val thikerList = uiState.thikerList
    val createThikerDialogUiState = uiState.createThikerDialogUiState
    val updateThikerDialogUiState = uiState.updateThikerDialogUiState

    var data by remember { mutableStateOf(thikerList) }

    val reorderableState = rememberReorderableLazyListState(
        onMove = { from, to ->


            data = data
                .toMutableList()
                .apply {
                    get(to.index).orderIndex = from.index

                    add(
                        index = to.index,
                        element = removeAt(from.index)
                            .apply {
                                orderIndex = to.index
                            }
                    )
                }
//                .mapIndexed { index, thiker ->
//                    thiker.copy(
//                        orderIndex = index
//                    )
//                }
            Log.d("ThikerList", data.toString())
        },
    )

    val reorderableLazyGridState = rememberReorderableLazyGridState(
        onMove = { from, to ->
            data = data
                .toMutableList()
                .apply {

                    val tmp =

                    add(
                        index = to.index,
                        element = removeAt(from.index)
                    )
                }
                .mapIndexed { index, thiker ->
                    thiker.copy(
                        orderIndex = index
                    )
                }

            Log.d("ThikerList", "from: ${from}, to: ${to}")
            Log.d("ThikerList", data.toString())
        },
    )


    LaunchedEffect(thikerList) {
        data = thikerList
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { },
                actions = {
                    IconButton(
                        onClick = { isBottomSheetVisible = true }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.Sort,
                            contentDescription = null
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    onCreateThikerDialogUiStateChange(
                        createThikerDialogUiState.copy(isCreateThikerDialogOpen = true)
                    )
                }
            ) {
                Icon(
                    imageVector = Icons.Rounded.Add,
                    contentDescription = null
                )
            }
        },
//        floatingActionButtonPosition = FabPosition.Start,
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->

        if (isBottomSheetVisible) {
            ModalBottomSheet(
                sheetState = sheetState,
                onDismissRequest = { isBottomSheetVisible = false },
            ) {
                Text(
                    text = "Sort by",
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier
                        .padding(15.dp)
                )
                HorizontalDivider(Modifier.height(2.dp))
               Column(
                   modifier = Modifier
                       .padding(10.dp)
                       .fillMaxWidth()
               ) {
                   FilledTonalButton(
                       onClick = { },
//                       colors = ButtonDefaults
//                           .let {
//                               it.filledTonalButtonColors(
//                                   containerColor = Color.Transparent
//                               )
//                           }
                       contentPadding = PaddingValues(0.dp),
                   ) {
                       Row(
                           verticalAlignment = Alignment.CenterVertically,
                           modifier = Modifier
                               .padding(15.dp)
                               .fillMaxWidth()
                       ) {
                           Box(
                               modifier = Modifier.width(50.dp)
                           ) {
                               Icon(imageVector = Icons.Rounded.Check, contentDescription = null)
                           }
                           Text(
                               text = "Custom",
                           )
                       }
                   }
                   FilledTonalButton(
                       onClick = { },
                       colors = ButtonDefaults.filledTonalButtonColors(
                           containerColor = Color.Transparent
                       ),
                       contentPadding = PaddingValues(0.dp),
                   ) {
                       Row(
                           verticalAlignment = Alignment.CenterVertically,
                           modifier = Modifier
                               .padding(15.dp)
                               .fillMaxWidth()
                       ) {
                           Box(
                               modifier = Modifier.width(50.dp)
                           ) {
//                               Icon(imageVector = Icons.Rounded.Check, contentDescription = null)
                           }
                           Text("Date created")
                       }
                   }
                   FilledTonalButton(
                       onClick = { },
                       colors = ButtonDefaults.filledTonalButtonColors(
                           containerColor = Color.Transparent
                       ),
                       contentPadding = PaddingValues(0.dp),
                   ) {
                       Row(
                           verticalAlignment = Alignment.CenterVertically,
                           modifier = Modifier
                               .padding(15.dp)
                               .fillMaxWidth()
                       ) {
                           Box(
                               modifier = Modifier.width(50.dp)
                           ) {
//                               Icon(imageVector = Icons.Rounded.Check, contentDescription = null)
                           }
                           Text("Date modified")
                       }
                   }
               }
            }
        }



        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(all = 15.dp)
                .fillMaxSize()
        ) {
            if (thikerList.isEmpty()) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Text(
                        text = "لا يوجد أذكار"
                    )
                }
            } else {
                LazyColumn(
                    state = reorderableState.listState,
                    verticalArrangement = Arrangement.spacedBy(5.dp),
                    modifier = Modifier
                        .reorderable(reorderableState)
//                        .detectReorderAfterLongPress(reorderableState)
                        .fillMaxSize()
                ) {
                    items(items = data, key = { it.id }) { thiker ->
                        ReorderableItem(reorderableState, key = thiker.id) { isDragging ->
                            ThikerItem(
                                thiker = thiker,
                                onSelect = {
                                    onUpdateUiStateThikerList(
                                        data.map { mapThiker ->
                                            if (mapThiker.id == thiker.id) {
                                                mapThiker.copy(isSelected = !mapThiker.isSelected)
                                            } else {
                                                mapThiker
                                            }
                                        }
                                    )
                                },
                                onUpdate = { initUpdateThikerDialogUiState(thiker.id) },
                                onDelete = { onDeleteThiker(thiker) },
                                onClick = { },
                                modifier = Modifier
                                    .detectReorder(reorderableState)
                            )

                            LaunchedEffect(isDragging) {
                                if (!isDragging) {
                                    onUpdateThikerList(data)
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    if (createThikerDialogUiState.isCreateThikerDialogOpen) {
        CreateThikerDialog(
            thikerListCount = thikerList.count(),
            createThikerDialogUiState = createThikerDialogUiState,
            onCreateThikerDialogUiStateChange = onCreateThikerDialogUiStateChange,
            onCreateThiker = onCreateThiker,
        )
    }

    if (updateThikerDialogUiState.isUpdateThikerDialogOpen) {
        UpdateThikerDialog(
            updateThikerDialogUiState = updateThikerDialogUiState,
            onUpdateThikerDialogUiStateChange = onUpdateThikerDialogUiStateChange,
            onUpdateThiker = onUpdateThiker,
        )
    }
}


@Composable
fun ThikerItemDropdownMenu(
    isContextMenuVisible: MutableState<Boolean>,
    offset: DpOffset,
    onUpdate: () -> Unit = {},
    onDelete: () -> Unit = {}
) {

    DropdownMenu(
        expanded = isContextMenuVisible.value,
        onDismissRequest = { isContextMenuVisible.value = false },
        offset = offset
    ) {
        DropdownMenuItem(
            text = { Text("تعديل") },
            onClick = {
                isContextMenuVisible.value = false
                onUpdate()
            }
        )
        DropdownMenuItem(
            text = { Text("حذف") },
            onClick = {
                isContextMenuVisible.value = false
                onDelete()
            }
        )
    }
}

@Composable
fun CreateThikerDialog(
    thikerListCount: Int = 0,
    createThikerDialogUiState: CreateThikerDialogUiState,
    onCreateThikerDialogUiStateChange: (CreateThikerDialogUiState) -> Unit = { },
    onCreateThiker: (Thiker) -> Unit = {},
    modifier: Modifier = Modifier
) {
    Dialog(
        onDismissRequest = {
            onCreateThikerDialogUiStateChange(
                createThikerDialogUiState.copy(isCreateThikerDialogOpen = false)
            )
        },
    ) {
        ElevatedCard(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
        ) {
            Column(
                modifier = Modifier
                    .padding(horizontal = 15.dp, vertical = 20.dp)
            ) {
                CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
                    Text(
                        text = "إضافة ذكر",
                        style = MaterialTheme.typography.headlineMedium,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(Modifier.height(5.dp))
                    OutlinedTextField(
                        value = createThikerDialogUiState.title,
                        onValueChange = {
                            onCreateThikerDialogUiStateChange(createThikerDialogUiState.copy(title = it))
                        },
                        label = {
                            Text(text = "الذكر")
                        },
                        maxLines = 3,
                    )
                    Spacer(Modifier.height(5.dp))
                    OutlinedTextField(
                        value = createThikerDialogUiState.maxCount,
                        onValueChange = {
                            onCreateThikerDialogUiStateChange(createThikerDialogUiState.copy(maxCount = it))
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        maxLines = 1,
                        label = {
                            Text(text = "العدد الإجمالي للدورة")
                        },
                    )
                    Spacer(Modifier.height(5.dp))
                    OutlinedTextField(
                        value = createThikerDialogUiState.currentCount,
                        onValueChange = {
                            val maxCountValue = createThikerDialogUiState.maxCount.toLongOrNull() ?: 0L
                            val currentCountValue = it.toLongOrNull() ?: 0L

                            if (currentCountValue <= maxCountValue) {
                                onCreateThikerDialogUiStateChange(createThikerDialogUiState.copy(currentCount = it))
                            }
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        maxLines = 1,
                        label = {
                            Text(text = "العدد الحالي")
                        },
                    )
                    Spacer(Modifier.height(15.dp))
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Button(
                            onClick = {
                                val thiker = Thiker(
                                    title = createThikerDialogUiState.title,
                                    maxCount =  createThikerDialogUiState.maxCount.toLongOrNull() ?: 0L,
                                    currentCount = createThikerDialogUiState.currentCount.toLongOrNull() ?: 0L,
                                    createdAt = System.currentTimeMillis(),
                                    updatedAt = System.currentTimeMillis(),
                                    orderIndex = thikerListCount
                                )
                                onCreateThikerDialogUiStateChange(CreateThikerDialogUiState())
                                onCreateThiker(thiker)
                            },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(text = "تم")
                            Spacer(Modifier.width(8.dp))
                            Icon(
                                imageVector = Icons.Rounded.Check,
                                contentDescription = null
                            )
                        }
                        Spacer(Modifier.width(16.dp))
                        Button(
                            onClick = {
                                onCreateThikerDialogUiStateChange(CreateThikerDialogUiState())
                            },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(text = "الغاء")
                            Spacer(Modifier.width(8.dp))
                            Icon(
                                imageVector = Icons.Rounded.Cancel,
                                contentDescription = null
                            )
                        }
                    }
                }
            }
        }

    }
}

@Composable
fun UpdateThikerDialog(
    updateThikerDialogUiState: UpdateThikerDialogUiState,
    onUpdateThikerDialogUiStateChange: (UpdateThikerDialogUiState) -> Unit = { },
    onUpdateThiker: (Thiker) -> Unit = { },
    modifier: Modifier = Modifier
) {
    Dialog(
        onDismissRequest = {
            onUpdateThikerDialogUiStateChange(
                updateThikerDialogUiState.copy(
                    isUpdateThikerDialogOpen = false
                )
            )
        },
    ) {
        ElevatedCard(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
        ) {
            Column(
                modifier = Modifier
                    .padding(horizontal = 15.dp, vertical = 20.dp)
            ) {
                CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
                    Text(
                        text = "تعديل على الذكر",
                        style = MaterialTheme.typography.headlineMedium,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(Modifier.height(5.dp))
                    OutlinedTextField(
                        value = updateThikerDialogUiState.title,
                        onValueChange = {
                            onUpdateThikerDialogUiStateChange(updateThikerDialogUiState.copy(title = it))
                        },
                        label = {
                            Text(text = "الذكر")
                        },
                        maxLines = 3,
                    )
                    Spacer(Modifier.height(5.dp))
                    OutlinedTextField(
                        value = updateThikerDialogUiState.maxCount,
                        onValueChange = {
                            onUpdateThikerDialogUiStateChange(updateThikerDialogUiState.copy(maxCount = it))
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        maxLines = 1,
                        label = {
                            Text(text = "العدد الإجمالي للدورة")
                        },
                    )
                    Spacer(Modifier.height(5.dp))
                    OutlinedTextField(
                        value = updateThikerDialogUiState.currentCount,
                        onValueChange = {
                            val maxCountValue = updateThikerDialogUiState.maxCount.toLongOrNull() ?: 0L
                            val currentCountValue = it.toLongOrNull() ?: 0L

                            if (currentCountValue <= maxCountValue) {
                                onUpdateThikerDialogUiStateChange(updateThikerDialogUiState.copy(currentCount = it))
                            }
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        maxLines = 1,
                        label = {
                            Text(text = "العدد الحالي")
                        },
                    )
                    Spacer(Modifier.height(15.dp))
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Button(
                            onClick = {
//                                val thiker = Thiker(
//                                    id = updateThikerDialogUiState.id,
//                                    title = updateThikerDialogUiState.title,
//                                    maxCount =  updateThikerDialogUiState.maxCount.toLongOrNull() ?: 0L,
//                                    currentCount = updateThikerDialogUiState.currentCount.toLongOrNull() ?: 0L,
//                                    updatedAt = System.currentTimeMillis(),
//                                    orderIndex = updateThikerDialogUiState.orderIndex
//                                )
//                                onUpdateThikerDialogUiStateChange(UpdateThikerDialogUiState())
//                                onUpdateThiker(thiker)
                            },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(text = "تم")
                            Spacer(Modifier.width(8.dp))
                            Icon(
                                imageVector = Icons.Rounded.Check,
                                contentDescription = null
                            )
                        }
                        Spacer(Modifier.width(16.dp))
                        Button(
                            onClick = {
//                                onUpdateThikerDialogUiStateChange(UpdateThikerDialogUiState(thiker = thiker))
                            },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(text = "الغاء")
                            Spacer(Modifier.width(8.dp))
                            Icon(
                                imageVector = Icons.Rounded.Cancel,
                                contentDescription = null
                            )
                        }
                    }
                }
            }
        }

    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Preview
@Composable
private fun HomeScreenPreview() {
    TASBIHTheme {
        CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
//            HomeScreen(
//                thikerList = listOf(
//                    Thiker(
//                        title = "اللهم صلي على محمد",
//                        maxCount = 100,
//                    ),
//                    Thiker(
//                        title = "سبحان الله",
//                        maxCount = 100,
//                    ),
//                    Thiker(
//                        title = "الله أكبر",
//                        maxCount = 100,
//                    ),
//                ),
//            )
        }
    }
}