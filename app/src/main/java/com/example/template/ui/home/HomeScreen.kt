package com.example.template.ui.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.window.core.layout.WindowSizeClass

@Composable
fun HomeScreen(
    windowSizeClass: WindowSizeClass,
    message: String,
    onNavigateToDetail: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Text(
            text = "$message from Home",
            modifier = Modifier
                .align(Alignment.Center)
                .clickable { onNavigateToDetail() }
        )
    }
}

private val CompactWindowSizeClass = WindowSizeClass.compute(360f, 780f)

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    HomeScreen(
        windowSizeClass = CompactWindowSizeClass,
        message = "demo string",
        onNavigateToDetail = {}
    )
}