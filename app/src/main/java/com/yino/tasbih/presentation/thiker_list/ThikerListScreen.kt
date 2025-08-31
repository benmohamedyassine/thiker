package com.yino.tasbih.presentation.thiker_list

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
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
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Sort
import androidx.compose.material.icons.automirrored.rounded.ViewList
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.ArrowDownward
import androidx.compose.material.icons.rounded.ArrowUpward
import androidx.compose.material.icons.rounded.Cancel
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.DragIndicator
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.GridView
import androidx.compose.material.icons.rounded.SwitchRight
import androidx.compose.material.icons.rounded.ViewList
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetState
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.yino.tasbih.domain.model.Thiker
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.burnoutcrew.reorderable.ReorderableItem
import org.burnoutcrew.reorderable.detectReorder
import org.burnoutcrew.reorderable.rememberReorderableLazyGridState
import org.burnoutcrew.reorderable.rememberReorderableLazyListState
import org.burnoutcrew.reorderable.reorderable
import kotlin.math.abs

@ExperimentalMaterial3Api
@Composable
fun ThikerListScreen(
    viewModel: ThikerListScreenViewModel = hiltViewModel<ThikerListScreenViewModel>(),
    modifier: Modifier = Modifier
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var items by remember { mutableStateOf(uiState.items) }

    val scope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState()
    var currentSortType by remember { mutableStateOf(ThikerListSortType.Custom) }
    var isBottomSheetVisible by remember { mutableStateOf(false) }

    var isViewList by remember { mutableStateOf(true) }
    var isCreateUpdateThikerDailogOpen by remember { mutableStateOf(false) }

    val reorderableState = rememberReorderableLazyListState(
        onMove = { from, to ->
            items = items
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
        },
    )

    val reorderableLazyGridState = rememberReorderableLazyGridState(
        onMove = { from, to ->
            items = items
                .toMutableList()
                .apply {
                    add(
                        index = to.index,
                        element = removeAt(from.index)
                            .apply {
                                orderIndex = to.index
                            }
                    )

                    val startIndex = if (from.index <= to.index) from.index else to.index
                    val endIndex = if (to.index >= from.index) to.index else from.index

                    for (index in startIndex..endIndex) {
                        set(
                            index = index,
                            element = get(index).copy(orderIndex = index)
                        )
                    }
                }
        },
    )

    LaunchedEffect(uiState.items) {
//        Log.d("ThikerList", items.toString())
        items = uiState.items
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { },
                actions = {
                    IconButton(
                        onClick = { isViewList = !isViewList }
                    ) {
                        Icon(
                            imageVector = if (isViewList) {
                                Icons.AutoMirrored.Rounded.ViewList
                            } else {
                                Icons.Rounded.GridView
                            },
                            contentDescription = null
                        )
                    }
                    IconButton(
                        onClick = { isBottomSheetVisible = true }
                    ) {
                        Icon(
                            imageVector = when(currentSortType) {
                                ThikerListSortType.Custom -> Icons.AutoMirrored.Rounded.Sort
                                ThikerListSortType.CreatedDate_ASC  -> Icons.Rounded.SwitchRight
                                ThikerListSortType.CreatedDate_DESC -> Icons.Rounded.SwitchRight
                                ThikerListSortType.ModifiedDate_ASC -> Icons.Rounded.SwitchRight
                                ThikerListSortType.ModifiedDate_DESC -> Icons.Rounded.SwitchRight
                            },
                            contentDescription = null,
                            modifier = Modifier.let {
                                when (currentSortType) {
                                    ThikerListSortType.Custom -> it
                                    ThikerListSortType.CreatedDate_ASC -> it.rotate(270f)
                                    ThikerListSortType.CreatedDate_DESC -> it.rotate(90f)
                                    ThikerListSortType.ModifiedDate_ASC -> it.rotate(270f)
                                    ThikerListSortType.ModifiedDate_DESC -> it.rotate(90f)
                                }
                            }
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { isCreateUpdateThikerDailogOpen = true }
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

        if (items.isEmpty()) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                Text(
                    text = "لا يوجد أذكار"
                )
            }
        } else if (isViewList) {
            LazyColumn(
                state = reorderableState.listState,
                verticalArrangement = Arrangement.spacedBy(5.dp),
                modifier = Modifier
                    .padding(innerPadding)
                    .padding(horizontal = 15.dp)
                    .reorderable(reorderableState)
//                        .detectReorderAfterLongPress(reorderableState)
                    .fillMaxSize()
            ) {
                items(items = items, key = { it.id }) { thiker ->
                    ReorderableItem(reorderableState, key = thiker.id) { isDragging ->
                        ThikerItem(
                            thiker = thiker,
                            onUpdate = {
                                viewModel.initUpdateCreateUpdateThikerUiState(
                                    thiker = items.find { it.id == thiker.id } !!
                                )
                                isCreateUpdateThikerDailogOpen = true
                            },
                            onDelete = { viewModel.onDeleteThiker(thiker) },
                            onSelect = { },
                            onClick = { },
                            modifier = Modifier
                                .let {
                                    if (currentSortType == ThikerListSortType.Custom) {
                                        it.detectReorder(reorderableState)
                                    } else it
                                }
                        )

                        LaunchedEffect(isDragging) {
                            if (!isDragging) {
                                viewModel.onUpdateOrderIndex(thikerList = items)
                            }
                        }
                    }
                }
            }
        } else {

            LazyVerticalGrid(
                state = reorderableLazyGridState.gridState,
                columns = GridCells.Fixed(2),
                verticalArrangement = Arrangement.spacedBy(5.dp),
                horizontalArrangement = Arrangement.spacedBy(5.dp),
                modifier = Modifier
                    .padding(innerPadding)
                    .padding(horizontal = 15.dp)
                    .reorderable(reorderableLazyGridState)
                    .fillMaxSize()

            ) {
                items(items = items, key = { it.id }) { thiker ->
                    ReorderableItem(
                        reorderableState = reorderableLazyGridState,
                        key = thiker.id,
                    ) { isDragging ->
                        ThikerItem(
                            thiker = thiker,
                            onUpdate = {
                                viewModel.initUpdateCreateUpdateThikerUiState(
                                    thiker = items.find { it.id == thiker.id } !!
                                )
                                isCreateUpdateThikerDailogOpen = true
                            },
                            onDelete = { viewModel.onDeleteThiker(thiker) },
                            onSelect = { },
                            onClick = { },
                            modifier = Modifier
                                .let {
                                    if (currentSortType == ThikerListSortType.Custom) {
                                        it.detectReorder(reorderableLazyGridState)
                                    } else it
                                }
                        )

                        LaunchedEffect(isDragging) {
                            if (!isDragging) {
                                viewModel.onUpdateOrderIndex(thikerList = items)
                            }
                        }
                    }
                }
            }
        }


        val createUpdateThikerUiState by viewModel.createUpdateThikerUiState.collectAsStateWithLifecycle()

        if (isCreateUpdateThikerDailogOpen) {
            CreateUpdateThikerDailog(
                uiState = createUpdateThikerUiState,
                onUpdateCreateUpdateThikerUiState = viewModel::initUpdateCreateUpdateThikerUiState,
                onSave = {
                    if (createUpdateThikerUiState.thiker.id == 0L) {
                        viewModel.onCreateThiker()
                    } else {
                        viewModel.onUpdateThiker()
                    }
                    isCreateUpdateThikerDailogOpen = false
                },
                onDismissRequest = { isCreateUpdateThikerDailogOpen = false }
            )
        }

        ThikerListSortBottomSheet(
            sheetState = sheetState,
            isBottomSheetVisible = isBottomSheetVisible,
            currentSortType = currentSortType,
            onDismissRequest = { isBottomSheetVisible = false },
            onSort = {
                currentSortType = it
                viewModel.setSorting(it)
            }
        )
    }
}

@Composable
fun ThikerItem(
    thiker: Thiker,
    onClick: () -> Unit = {},
    onUpdate: () -> Unit = {},
    onDelete: () -> Unit = {},
    onSelect: () -> Unit = {},
    modifier: Modifier = Modifier
) {
//    Log.d("ThikerList", thiker.toString())

    var isRemoved by remember { mutableStateOf(false) }

    val swipeToDismissBoxState = rememberSwipeToDismissBoxState(
        confirmValueChange = {
            if (it == SwipeToDismissBoxValue.StartToEnd) onUpdate()
            else if (it == SwipeToDismissBoxValue.EndToStart) { isRemoved = true }
            it != SwipeToDismissBoxValue.StartToEnd
        }
    )

//    val isContextMenuVisible = remember { mutableStateOf(false) }

//    val density = LocalDensity.current
//    var pressOffset by remember { mutableStateOf(DpOffset.Zero) }
//    var itemHeight by remember { mutableStateOf(0.dp) }

    val interactionSource = remember { MutableInteractionSource() }

    LaunchedEffect(key1 = isRemoved) {
        if(isRemoved) {
            delay(300L)
            onDelete()
        }
    }

    AnimatedVisibility(
        visible = !isRemoved,
        exit = shrinkVertically(
            animationSpec = tween(durationMillis = 300),
            shrinkTowards = Alignment.Top
        ) + fadeOut()
    ) {
        SwipeToDismissBox(
            state = swipeToDismissBoxState,
            backgroundContent = {
                when (swipeToDismissBoxState.dismissDirection) {
                    SwipeToDismissBoxValue.StartToEnd -> {
                        Icon(
                            Icons.Rounded.Edit,
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxSize()
                                .background(MaterialTheme.colorScheme.tertiaryContainer)
                                .wrapContentSize(Alignment.CenterStart)
                                .padding(12.dp),
                            tint = Color.White
                        )
                    }

                    SwipeToDismissBoxValue.EndToStart -> {
                        Icon(
                            imageVector = Icons.Rounded.Delete,
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxSize()
                                .background(MaterialTheme.colorScheme.errorContainer)
                                .wrapContentSize(Alignment.CenterEnd)
                                .padding(12.dp),
                            tint = Color.White
                        )
                    }

                    SwipeToDismissBoxValue.Settled -> {}
                }
            },
            modifier = Modifier.clip(shape = CardDefaults.elevatedShape)
        ) {
            Box {
                ElevatedCard(
                    modifier = Modifier
//                .padding(bottom = 15.dp)
                        .height(IntrinsicSize.Max)
//                .onSizeChanged {
//                    itemHeight = with(density) {
//                        it.height.toDp()
//                    }
//                }
                        .let {
                            if (thiker.isSelected) {
                                it
                                    .border(
                                        width = 3.dp,
                                        color = MaterialTheme.colorScheme.primaryContainer,
                                        shape = CardDefaults.elevatedShape
                                    )
                            } else {
                                it
                            }
                        }
                ) {

                    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
                        Box(
                            modifier = Modifier
                                .indication(interactionSource, LocalIndication.current)
                                .pointerInput(true) {
                                    detectTapGestures(
                                        onPress = {
                                            val press = PressInteraction.Press(it)
                                            interactionSource.emit(press)
                                            tryAwaitRelease()
                                            interactionSource.emit(PressInteraction.Release(press))
                                        },
                                        onTap = {
//                                    pressOffset = DpOffset(x = it.x.toDp(), y = it.y.toDp())
//                                    isContextMenuVisible.value = true
                                            onClick()
                                        },
                                        onLongPress = {
                                            onSelect()
                                        }
                                    )
                                }
                                .fillMaxWidth()
                        ) {
                            Row(
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .fillMaxWidth()
                            ) {
                                Column(
                                    modifier = Modifier
                                        .padding(vertical = 15.dp, horizontal = 20.dp)
                                ) {
                                    Text(
                                        text = "${String.format("%02d", thiker.orderIndex)} - ${thiker.title}",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 20.sp,
                                    )
                                    Text(
                                        text = "عدد التسبيح: ${String.format("%02d", thiker.currentCount)}",
                                        fontSize = 12.sp,
                                    )
                                }

                                Column (
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center,
                                    modifier = modifier
                                        .width(50.dp)
                                        .fillMaxHeight()
                                ) {
                                    Icon(
                                        imageVector = Icons.Rounded.DragIndicator,
                                        contentDescription = null,
                                    )
                                }
                            }
                        }
                    }
                }

//        ThikerItemDropdownMenu(
//            isContextMenuVisible = isContextMenuVisible,
//            offset = pressOffset.copy(y = pressOffset.y - itemHeight),
//            onUpdate = onUpdate,
//            onDelete = onDelete,
//        )
            }
        }
    }
}

@Composable
fun CreateUpdateThikerDailog(
    uiState: CreateUpdateThikerUiState,
    onUpdateCreateUpdateThikerUiState: (Thiker) -> Unit = { },
    onSave: () -> Unit = { },
    onDismissRequest: () -> Unit = { },
    modifier: Modifier = Modifier
) {
    Dialog(
        onDismissRequest = onDismissRequest,
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
                        value = uiState.thiker.title,
                        onValueChange = {
                            onUpdateCreateUpdateThikerUiState(
                                uiState.thiker.copy(title = it)
                            )
                        },
                        label = { Text(text = "الذكر") },
                        maxLines = 3,
                    )
                    Spacer(Modifier.height(5.dp))
                    OutlinedTextField(
                        value = uiState.thiker.maxCount.toString(),
                        onValueChange = {
                            onUpdateCreateUpdateThikerUiState(
                                uiState.thiker.copy(maxCount = it.toLongOrNull() ?: 0L)
                            )
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        maxLines = 1,
                        label = { Text(text = "العدد الإجمالي للدورة") },
                    )
                    Spacer(Modifier.height(5.dp))
                    OutlinedTextField(
                        value = uiState.thiker.currentCount.toString(),
                        onValueChange = {
                            val currentCountValue = it.toLongOrNull() ?: 0L

                            if (currentCountValue <= uiState.thiker.maxCount) {
                                onUpdateCreateUpdateThikerUiState(
                                    uiState.thiker.copy(currentCount = currentCountValue)
                                )
                            }
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        maxLines = 1,
                        label = { Text(text = "العدد الحالي") },
                    )
                    Spacer(Modifier.height(15.dp))
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Button(
                            onClick = onSave,
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(text = "حفظ")
                            Spacer(Modifier.width(8.dp))
                            Icon(
                                imageVector = Icons.Rounded.Check,
                                contentDescription = null
                            )
                        }
                        Spacer(Modifier.width(16.dp))
                        Button(
                            onClick = onDismissRequest,
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ThikerListSortBottomSheet(
    sheetState: SheetState,
    currentSortType: ThikerListSortType,
    isBottomSheetVisible: Boolean,
    onDismissRequest: () -> Unit = { },
    onSort: (ThikerListSortType) -> Unit = { },
    modifier: Modifier = Modifier
) {
    var isCreatedDateASC by remember { mutableStateOf(true) }
    var isModifiedDateASC by remember { mutableStateOf(true) }

    if (isBottomSheetVisible) {
        ModalBottomSheet(
            sheetState = sheetState,
            onDismissRequest = onDismissRequest,
            modifier = modifier
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

                ThikerListSortBotton(
                    label = ThikerListSortType.Custom.displayName,
                    icon = {
                        Icon(
                            imageVector = Icons.Rounded.Check,
                            contentDescription = null
                        )
                    },
                    isCurrentThikerListSortType = currentSortType == ThikerListSortType.Custom,
                    onClick = {
                        onSort(ThikerListSortType.Custom)
                        onDismissRequest()
                    }
                )

                if (isCreatedDateASC) {
                    ThikerListSortBotton(
                        label = ThikerListSortType.CreatedDate_ASC.displayName,
                        icon = {
                            Icon(
                                imageVector = Icons.Rounded.ArrowDownward,
                                contentDescription = null
                            )
                        },
                        isCurrentThikerListSortType = currentSortType == ThikerListSortType.CreatedDate_ASC,
                        onClick = {
                            isCreatedDateASC = false
                            onSort(ThikerListSortType.CreatedDate_DESC)
                            onDismissRequest()
                        }
                    )
                } else {
                    ThikerListSortBotton(
                        label = ThikerListSortType.CreatedDate_DESC.displayName,
                        icon = {
                            Icon(
                                imageVector = Icons.Rounded.ArrowUpward,
                                contentDescription = null
                            )
                        },
                        isCurrentThikerListSortType = currentSortType == ThikerListSortType.CreatedDate_DESC,
                        onClick = {
                            isCreatedDateASC = true
                            onSort(ThikerListSortType.CreatedDate_ASC)
                            onDismissRequest()
                        }
                    )
                }

                if (isModifiedDateASC) {
                    ThikerListSortBotton(
                        label = ThikerListSortType.ModifiedDate_ASC.displayName,
                        icon = {
                            Icon(
                                imageVector = Icons.Rounded.ArrowDownward,
                                contentDescription = null
                            )
                        },
                        isCurrentThikerListSortType = currentSortType == ThikerListSortType.ModifiedDate_ASC,
                        onClick = {
                            isModifiedDateASC = false
                            onSort(ThikerListSortType.ModifiedDate_DESC)
                            onDismissRequest()
                        }
                    )
                } else {
                    ThikerListSortBotton(
                        label = ThikerListSortType.ModifiedDate_DESC.displayName,
                        icon = {
                            Icon(
                                imageVector = Icons.Rounded.ArrowUpward,
                                contentDescription = null
                            )
                        },
                        isCurrentThikerListSortType = currentSortType == ThikerListSortType.ModifiedDate_DESC,
                        onClick = {
                            isModifiedDateASC = true
                            onSort(ThikerListSortType.ModifiedDate_ASC)
                            onDismissRequest()
                        }
                    )
                }

            }
        }

    }
}

@Composable
fun ThikerListSortBotton(
    label: String,
    icon: @Composable () -> Unit = {},
    isCurrentThikerListSortType: Boolean = false,
    onClick: () -> Unit = { },
    modifier: Modifier = Modifier
) {
    FilledTonalButton(
        colors = ButtonDefaults.filledTonalButtonColors(
            containerColor = if (isCurrentThikerListSortType) {
                Color.Unspecified
            } else {
                Color.Transparent
            }
        ),
        contentPadding = PaddingValues(0.dp),
        onClick = onClick,
        modifier = modifier
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
                if (isCurrentThikerListSortType) {
                    icon()
                }
            }
            Text(text = label)
        }
    }
}