package com.example.template.ui.splash

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.coroutines.delay


@Composable
fun SplashScreen(
    titleSplash: String,
    onNavigateToDetail: @Composable () -> Unit
) {
    LaunchedEffect(Unit) {
        delay(3000)
        onNavigateToDetail.invoke()
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Text(
            text = titleSplash,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}


@Preview(showBackground = true)
@Composable
fun SplashScreenPreview() {
    SplashScreen(
        titleSplash = "This is splash screen!",
        onNavigateToDetail = {}
    )
}