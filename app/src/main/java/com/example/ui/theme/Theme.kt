package com.example.ui.theme

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
    primary = ValetGold,
    onPrimary = Navy900,
    primaryContainer = Navy700,
    onPrimaryContainer = ValetGoldLight,
    secondary = AeroBlue,
    onSecondary = Color.White,
    secondaryContainer = Navy800,
    onSecondaryContainer = SkyBlueLight,
    tertiary = ValetGoldDark,
    background = Navy900,
    surface = Navy800,
    onBackground = Color.White,
    onSurface = Color.White,
    surfaceVariant = Navy700,
    onSurfaceVariant = Slate300,
    outline = Slate700
)

private val LightColorScheme = lightColorScheme(
    primary = Navy800,
    onPrimary = Color.White,
    primaryContainer = Navy900,
    onPrimaryContainer = ValetGold,
    secondary = AeroBlue,
    onSecondary = Color.White,
    secondaryContainer = SkyBlueLight,
    onSecondaryContainer = Navy800,
    tertiary = ValetGoldDark,
    onTertiary = Color.White,
    background = Slate50,
    surface = Color.White,
    onBackground = Slate900,
    onSurface = Slate900,
    surfaceVariant = Slate100,
    onSurfaceVariant = Slate700,
    outline = Slate300
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Use our intentional custom palette for brand identity
    content: @Composable () -> Unit,
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
