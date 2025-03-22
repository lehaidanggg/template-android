package com.example.template.navigation


import androidx.compose.animation.EnterTransition
import androidx.compose.animation.scaleOut
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.window.layout.DisplayFeature
import com.example.template.MainAppState
import com.example.template.R
import com.example.template.Screen
import com.example.template.rememberMainAppState
import com.example.template.ui.home.HomeScreen
import com.example.template.ui.splash.SplashScreen
import org.koin.compose.koinInject

@Composable
fun AppNavigation(
    displayFeatures: List<DisplayFeature>,
    appState: MainAppState = rememberMainAppState()
) {
    val adaptiveInfo = currentWindowAdaptiveInfo()
    if (appState.isOnline) {
        NavHost(
            navController = appState.navController,
            startDestination = Screen.Splash.route,
            popExitTransition = { scaleOut(targetScale = 0.9f) },
            popEnterTransition = { EnterTransition.None }
        ) {

            composable(Screen.Splash.route) { backStackEntry ->
                val txt: String = koinInject()
                SplashScreen(titleSplash = "Splash screen") {
                    appState.navigateToHome(txt, backStackEntry)
                }
            }

            composable(Screen.Home.route) { backStackEntry ->
                HomeScreen(
                    message = txt,
                    windowSizeClass = adaptiveInfo.windowSizeClass,
                    onNavigateToDetail = {
//                        appState.navigateToPlayer(episode.uri, backStackEntry)
//                        app
                    }
                )
            }
//            composable(Screen.Player.route) {
//                PlayerScreen(
//                    windowSizeClass = adaptiveInfo.windowSizeClass,
//                    displayFeatures = displayFeatures,
//                    onBackPress = appState::navigateBack
//                )
//            }
        }
    } else {
        OfflineDialog { appState.refreshOnline() }
    }
}

@Composable
fun OfflineDialog(onRetry: () -> Unit) {
    AlertDialog(
        onDismissRequest = {},
        title = { Text(text = stringResource(R.string.connection_error_title)) },
        text = { Text(text = stringResource(R.string.connection_error_message)) },
        confirmButton = {
            TextButton(onClick = onRetry) {
                Text(stringResource(R.string.retry_label))
            }
        }
    )
}