package charting.line.render.yaxis

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import charting.LabelFormatter
import charting.line.LineChartData
import charting.line.getAxisLineColor
import charting.line.getColor
import charting.line.getTextColor
import org.jetbrains.skia.Font
import org.jetbrains.skia.FontStyle
import org.jetbrains.skia.TextLine
import kotlin.math.roundToInt

/**
 * @Author bytebeats
 * @Email <happychinapc@gmail.com>
 * @Github https://github.com/bytebeats
 * @Created at 2022/3/10 20:50
 * @Version 1.0
 * @Description TO-DO
 */

class SimpleYAxisDrawer(
    val labelTextSize: TextUnit = 12.sp,
    val labelTextColor: Color = getTextColor(),
    val drawLabelEvery: Int = 10,
    val labelValueFormatter: LabelFormatter = { value -> "%.1f".format(value) },
    val axisLineThickness: Dp = 1.dp,
    val axisLineColor: Color = getAxisLineColor(),
) : IYAxisDrawer {
    private val mAxisLinePaint by lazy {
        Paint().apply {
            isAntiAlias = true
            color = axisLineColor
            style = PaintingStyle.Stroke
        }
    }

    private val mTextPaint by lazy {
        org.jetbrains.skia.Paint().apply {
            isAntiAlias = true
            color = labelTextColor.toArgb()
        }
    }

    private val mTextFont by lazy {
        Font()
    }

    override fun drawAxisLine(drawScope: DrawScope, canvas: Canvas, drawableArea: Rect) =
        with(drawScope) {
            val lineThickness = axisLineThickness.toPx()
            val x = drawableArea.right - lineThickness / 2F
            canvas.drawLine(
                p1 = Offset(x = x, y = drawableArea.top),
                p2 = Offset(x = x, y = drawableArea.bottom),
                paint = mAxisLinePaint.apply { strokeWidth = lineThickness })
        }

    override fun drawAxisLabels(
        drawScope: DrawScope,
        canvas: Canvas,
        drawableArea: Rect,
        minValue: Float,
        maxValue: Float,
    ) {
        with(drawScope) {
            val minVal = (minValue).roundToInt()
            val maxVal = (maxValue).roundToInt()
            val labelPaint = mTextPaint

            val labelFont = mTextFont.apply {
                size = labelTextSize.toPx()
            }

            val minLabelHeight = labelTextSize.toPx() * drawLabelEvery.toFloat()
            val totalHeight = drawableArea.height
            val labelCount = (drawableArea.height / minLabelHeight).roundToInt().coerceAtLeast(2)

            for (i in 0..labelCount) {
                val value = (minVal + i * (maxVal - minVal) / labelCount).toFloat()
                val label = labelValueFormatter(value)
                val textLine = TextLine.make(label, labelFont)
                val x = drawableArea.right - axisLineThickness.toPx() - textLine.width - labelTextSize.toPx() / 2
                val y = drawableArea.bottom - i * (totalHeight / labelCount) - textLine.height / 2F
                canvas.nativeCanvas.drawTextLine(textLine, x, y, labelPaint)
            }
        }
    }

    override fun drawChartLegends(
        drawScope: DrawScope,
        canvas: Canvas,
        drawableArea: Rect,
        minValue: Float,
        maxValue: Float,
        lineChartData: LineChartData
    ) {
        val labelPaint = mTextPaint
        val nameX = (drawableArea.right + 20F)
        val nameY = (drawableArea.top + 50F)
        canvas.nativeCanvas.drawString(
            lineChartData.title, nameX, nameY, Font(
                typeface = org.jetbrains.skia.Typeface.makeFromName(name = null, style = FontStyle.BOLD_ITALIC),
                size = 34f
            ), labelPaint
        )

        lineChartData.variants.forEachIndexed { index, variant ->
            val variantColor = getColor(index)
            val label = variant.title
            val labelX = (drawableArea.right + 30f)
            val labelY = (drawableArea.top + 90F) + (index * 40f)

            canvas.nativeCanvas.drawCircle(
                x = labelX,
                y = labelY,
                radius = 14f,
                paint = org.jetbrains.skia.Paint().apply {
                    isAntiAlias = true
                    color = variantColor.toArgb()
                }
            )

            canvas.nativeCanvas.drawString(
                label, labelX + 18f, labelY + 7f, Font(
                    typeface = org.jetbrains.skia.Typeface.makeFromName(name = null, style = FontStyle.NORMAL),
                    size = 24f
                ), labelPaint
            )
//            canvas.nativeCanvas.drawRect(
//                r = org.jetbrains.skia.Rect(
//                    left = (drawableArea.right + 20f),
//                    top = (drawableArea.right) + (index * 30f),
//                    right = (drawableArea.right + 20f) + 70f,
//                    bottom = (drawableArea.right) + (index * 30f) + 20f,
//                ), paint = org.jetbrains.skia.Paint().apply {
//                    isAntiAlias = true
//                    color = variantColor.toArgb()
//                }
//            )
        }
    }
}