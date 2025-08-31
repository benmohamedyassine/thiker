package com.yino.tasbih.presentation

import android.annotation.SuppressLint
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.yino.tasbih.presentation.home.HomeScreen
import com.yino.tasbih.presentation.home.HomeScreenViewModel
import com.yino.tasbih.presentation.thiker_list.ThikerListScreen
import kotlinx.serialization.Serializable

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("SuspiciousIndentation")
@Composable
fun NavHostSetup(modifier: Modifier = Modifier) {
    val navController = rememberNavController()

//    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        NavHost(
            navController = navController,
            startDestination = Destination.Home,
            modifier = modifier
        ) {
            composable<Destination.Home> {

                ThikerListScreen()

//                val homeScreenViewModel = hiltViewModel<HomeScreenViewModel>()
//                val uiState by homeScreenViewModel.uiState.collectAsStateWithLifecycle()

//                HomeScreen(
//                    uiState = uiState,
//                    initUpdateThikerDialogUiState = homeScreenViewModel::initUpdateThikerDialogUiState,
//                    onUpdateUiStateThikerList = homeScreenViewModel::onUpdateUiStateThikerList,
//                    onCreateThikerDialogUiStateChange = homeScreenViewModel::onCreateThikerDialogUiStateChange,
//                    onUpdateThikerDialogUiStateChange = homeScreenViewModel::onUpdateThikerDialogUiStateChange,
//                    onUpdateThikerList = homeScreenViewModel::onUpdateThikerList,
//                    onCreateThiker = homeScreenViewModel::onCreateThiker,
//                    onUpdateThiker = homeScreenViewModel::onUpdateThiker,
//                    onDeleteThiker = homeScreenViewModel::onDeleteThiker,
//                )
            }
        }
//    }
}

sealed interface Destination {

    @Serializable
    data object Home : Destination
}
