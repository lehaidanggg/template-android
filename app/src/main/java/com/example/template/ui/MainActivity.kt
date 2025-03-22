package com.example.template.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.template.navigation.AppNavigation
import com.example.template.ui.splash.SplashScreen
import com.example.template.ui.theme.Template_AndroidTheme
import com.google.accompanist.adaptive.calculateDisplayFeatures

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val displayFeatures = calculateDisplayFeatures(this)
            Template_AndroidTheme {
                AppNavigation(
                    displayFeatures = displayFeatures
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    Template_AndroidTheme(
        darkTheme = true
    ) {
        SplashScreen(
            titleSplash = "This is splash screen!",
            onNavigateToDetail = {}
        )
    }
}