package com.example.template.ui.detail

import androidx.compose.foundation.clickable
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun DetailScreen(
    idProduct: String,
    onNavigateToDetail: () -> Unit
) {
    Text(
        text = "Detail screen: $idProduct",
        modifier = Modifier.clickable { onNavigateToDetail() }
    )
}