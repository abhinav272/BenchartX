package charting.line

import androidx.compose.ui.graphics.Color

private val COLOR_PURPLE = Color(0xFF9100FF)
private val COLOR_ORANGE = Color(0xFFFF8A00)
private val colors = arrayOf(Color.Red, Color.Cyan, Color.Green, COLOR_PURPLE, COLOR_ORANGE, Color.Blue, Color.Yellow)

fun getColor(index: Int): Color {
    return with(colors) {
        get(index % size)
    }
}

fun getAxisLineColor(): Color {
    return Color.Black
}

fun getTextColor(): Color {
    return Color.Black
}

fun getChartBackgroundColor(): Color {
    return Color(0xFFE1E1E1)
}

