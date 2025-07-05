package ru.metaclone.theme

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.statusBars
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp

@Composable
fun statusBarHeightDp(): Dp {
    val density = LocalDensity.current
    val heightPx = WindowInsets.statusBars.getTop(density)
    return with(density) { heightPx.toDp() }
}