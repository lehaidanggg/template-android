package com.example.template.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext


private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFFBB86FC),       // Tím sáng
    onPrimary = Color(0xFF000000),     // Đen
    secondary = Color(0xFF03DAC6),     // Xanh ngọc
    onSecondary = Color(0xFF000000),   // Đen
    background = Color(0xFF121212),    // Đen đậm
    onBackground = Color(0xFFFFFFFF),  // Trắng (cho chữ trên background)
    surface = Color(0xFF121212),       // Đen đậm
    onSurface = Color(0xFFFFFFFF)      // Trắng (cho chữ trên surface)
)

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF6200EE),       // Tím đậm
    onPrimary = Color(0xFFFFFFFF),     // Trắng (cho chữ trên primary)
    secondary = Color(0xFF03DAC6),     // Xanh ngọc
    onSecondary = Color(0xFF000000),   // Đen
    background = Color(0xFFFFFFFF),    // Trắng
    onBackground = Color(0xFF000000),  // Đen (cho chữ trên background)
    surface = Color(0xFFFFFFFF),       // Trắng
    onSurface = Color(0xFF000000)      // Đen (cho chữ trên surface)
)


@Composable
fun Template_AndroidTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}