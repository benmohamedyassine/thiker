package com.yino.tasbih.presentation.tasbih_screen

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.yino.tasbih.domain.model.Thiker
import com.yino.tasbih.presentation.ui.theme.TASBIHTheme



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TasbihScreen(
    uiState: TasbihUiState = TasbihUiState(),
    onIncrementCount: () -> Unit = { },
    onResetCount: () -> Unit = { },
    onNavBack: () -> Unit = { },
) {

    val scrollState = rememberScrollState()
    var currentProgress by remember { mutableFloatStateOf(0F) }
    val animatedProgress by animateFloatAsState(
        targetValue = currentProgress,
        animationSpec = tween(durationMillis = 1000)
    )

    LaunchedEffect(uiState.count) {
        val maxCount = uiState.thiker.maxCount

        if (maxCount > 0) {
            currentProgress = (uiState.count - 0f) * (1f - 0f) / (maxCount - 0f) + 0f
        }
    }
    

    Scaffold(
        topBar = {
            TopAppBar(
                title = { },
                navigationIcon = {
                    IconButton(onClick = onNavBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                            contentDescription = null
                        )
                    }
                }
            )
        }
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
                    .verticalScroll(scrollState)
                    .weight(1f)
            ) {
                Text(
                    text = uiState.thiker.title,
                    fontSize = 40.sp,
                    lineHeight = 40.sp,
                    modifier = Modifier
                        .padding(horizontal = 20.dp)
                )
            }

            Spacer(Modifier.height(50.dp))

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
                ) {
                    CircularProgressIndicator(
                        progress = { animatedProgress },
//                        strokeWidth = 20.dp,
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(CircleShape)
                            .clickable(onClick = onIncrementCount)
                            .pointerInput(Unit) {
                                detectVerticalDragGestures(
                                    onVerticalDrag = { change, dragAmount ->
                                        change.consume()
                                        isSwipeUp = dragAmount < 0
                                    },
                                    onDragEnd = {
                                        if (isSwipeUp) onResetCount()
                                    }
                                )
                            }
                    )
                    Text(
                        text = uiState.count.toString(),
                        fontSize = 50.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Button(
                        onClick = onResetCount,
                        modifier = Modifier
                            .size(30.dp)
                            .aspectRatio(1f)
                            .align(Alignment.BottomStart)
                    ) { }
                }
            }
        }
    }
}

//fun Float.map(
//    inMin: Float, inMax: Float,
//    outMin: Float, outMax: Float
//): Float {
//    return (this - inMin) * (outMax - outMin) / (inMax - inMin) + outMin
//}

@Preview
@Composable
private fun TasbihScreenPreview() {
    val longText1 = "سبحان الله و الحمد لله و الله أكبر"
    val longText2 = "هذا نص طويل جدًا يهدف إلى توضيح كيفية التعامل مع النصوص بأناقة داخل صف دون دفع العناصر الأخرى خارج الشاشة." +
            "هذا نص طويل جدًا يهدف إلى توضيح كيفية التعامل مع النصوص بأناقة داخل صف دون دفع العناصر الأخرى خارج الشاشة." +
            "هذا نص طويل جدًا يهدف إلى توضيح كيفية التعامل مع النصوص بأناقة داخل صف دون دفع العناصر الأخرى خارج الشاشة."

    TASBIHTheme {
        TasbihScreen(
            uiState = TasbihUiState(
                thiker = Thiker(
                    title = longText2
                )
            )
        )
    }
}