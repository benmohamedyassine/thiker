package com.yino.tasbih.presentation.tasbih_screen

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.yino.tasbih.presentation.ui.theme.TASBIHTheme

@Composable
fun TasbihScreen(
    uiState: TasbihUiState = TasbihUiState(),
    onIncrementCount: () -> Unit = { },
    onResetCount: () -> Unit = { },
) {

    var currentProgress by remember {
        var progress = 0F

        if (uiState.thiker.maxCount > 0) {
            progress = (uiState.count * 100 / uiState.thiker.maxCount).toFloat()
        }

        mutableFloatStateOf(progress)
    }
    

    Scaffold(
    ) { innerPadding ->

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
//            verticalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .weight(1f)
            ) {
                Text(
                    text = uiState.thiker.title,
                    fontSize = 70.sp,
                )
            }

            Box(
                modifier = Modifier
                    .weight(1f)
            ) {
                var isSwipeUp by remember { mutableStateOf(false) }

                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxSize(0.8f)
                        .aspectRatio(1.0f)
                        .clip(CircleShape)
                        .clickable(onClick = onIncrementCount)
                        .pointerInput(Unit) {
                            detectVerticalDragGestures(
                                onVerticalDrag = { change, dragAmount ->
                                    change.consume()
                                    isSwipeUp = if (dragAmount < 0) true else false
                                },
                                onDragEnd = {
                                    if (isSwipeUp) onResetCount()
                                }
                            )
                        }
                ) {
                    CircularProgressIndicator(
                        progress = { currentProgress },
                        strokeWidth = 20.dp,
                        modifier = Modifier.fillMaxSize()
                    )
                    Text(
                        text = uiState.count.toString(),
                        fontSize = 70.sp,
                        fontWeight = FontWeight.Bold
                    )

                }
            }
        }
    }
}

@Preview
@Composable
private fun TasbihScreenPreview() {
    TASBIHTheme {
        TasbihScreen()
    }
}