package com.konaet.cover.core.designsystem.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import com.konaet.cover.core.designsystem.tokens.KonaetColorTokens

private val DarkColorScheme = darkColorScheme(
    primary = KonaetColorTokens.Verified,
    secondary = KonaetColorTokens.Ember,
    tertiary = KonaetColorTokens.Verified,
    background = KonaetColorTokens.Obsidian,
    surface = KonaetColorTokens.Surface,
    error = KonaetColorTokens.Danger,
    onPrimary = KonaetColorTokens.Obsidian,
    onSecondary = KonaetColorTokens.Obsidian,
    onBackground = KonaetColorTokens.Text,
    onSurface = KonaetColorTokens.Text,
    onError = KonaetColorTokens.Obsidian,
)

@Composable
fun KonaetCoverTheme(
    darkTheme: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = DarkColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = KonaetTypography,
        content = content
    )
}
