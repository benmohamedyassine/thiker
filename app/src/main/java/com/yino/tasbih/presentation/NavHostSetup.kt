package com.yino.tasbih.presentation

import android.annotation.SuppressLint
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.yino.tasbih.presentation.home.HomeScreen
import com.yino.tasbih.presentation.home.HomeScreenViewModel
import com.yino.tasbih.presentation.tasbih_screen.TasbihScreen
import com.yino.tasbih.presentation.tasbih_screen.TasbihScreenViewModel
import com.yino.tasbih.presentation.thiker_list.ThikerListScreen
import kotlinx.serialization.Serializable

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("SuspiciousIndentation")
@Composable
fun NavHostSetup(modifier: Modifier = Modifier) {
    val navController = rememberNavController()

    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        NavHost(
            navController = navController,
            startDestination = Destination.ThikerList,
            modifier = modifier
        ) {
            composable<Destination.ThikerList> {
                ThikerListScreen(
                    onNavToTasbih = {
                        navController.navigate(
                            Destination.Tasbih(thikerId = it)
                        )
                    }
                )
            }

            composable<Destination.Tasbih> {
                val tasbihRoute = it.toRoute<Destination.Tasbih>()

                val viewModel = hiltViewModel<TasbihScreenViewModel>()
                val uiState by viewModel.tasbihUiState.collectAsStateWithLifecycle()

                LaunchedEffect(tasbihRoute.thikerId) {
                    viewModel.loadThiker(tasbihRoute.thikerId)
                }

                TasbihScreen(
                    uiState = uiState,
                    onIncrementCount = viewModel::onIncrementCount,
                    onResetCount = viewModel::onResetCount,
                    onNavBack = { navController.popBackStack() }
                )
            }
        }
    }
}

sealed interface Destination {

    @Serializable
    data object Home : Destination

    @Serializable
    data object ThikerList : Destination

    @Serializable
    data class Tasbih(val thikerId: Long = 0) : Destination
}
