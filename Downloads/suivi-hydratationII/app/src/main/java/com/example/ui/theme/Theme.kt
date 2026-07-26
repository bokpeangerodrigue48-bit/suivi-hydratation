package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
  primary = TurquoisePrimary,
  onPrimary = OnTurquoisePrimary,
  secondary = TurquoiseSecondary,
  onSecondary = OnTurquoiseSecondary,
  tertiary = TurquoiseTertiary,
  background = DarkBackground,
  surface = DarkSurface,
  onBackground = OnDarkBackground,
  onSurface = OnDarkSurface,
  surfaceVariant = DarkSurfaceVariant,
  onSurfaceVariant = OnDarkBackground
)

private val LightColorScheme = DarkColorScheme // Always use dark turquoise theme

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = true, // Default to dark theme as requested
  dynamicColor: Boolean = false, // Disable dynamic colors to keep turquoise accents
  content: @Composable () -> Unit,
) {
  val colorScheme = DarkColorScheme


  MaterialTheme(colorScheme = colorScheme, typography = Typography, content = content)
}
