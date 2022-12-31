package charting.line

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import charting.line.render.*
import charting.line.render.point.FilledCircularPointDrawer
import charting.line.render.point.IPointDrawer
import charting.line.render.xaxis.IXAxisDrawer
import charting.line.render.xaxis.SimpleXAxisDrawer
import charting.line.render.yaxis.IYAxisDrawer
import charting.line.render.yaxis.SimpleYAxisDrawer
import charting.simpleChartAnimation

/**
 * @Author bytebeats
 * @Email <happychinapc@gmail.com>
 * @Github https://github.com/bytebeats
 * @Created at 2022/3/10 20:55
 * @Version 1.0
 * @Description TO-DO
 */

@Composable
fun LineChart(
    lineChartData: LineChartData,
    modifier: Modifier = Modifier,
    animation: AnimationSpec<Float> = simpleChartAnimation(),
    pointDrawer: IPointDrawer = FilledCircularPointDrawer(),
    xAxisDrawer: IXAxisDrawer = SimpleXAxisDrawer(),
    yAxisDrawer: IYAxisDrawer = SimpleYAxisDrawer(),
    horizontalOffset: Float = 5F,
) {
    check(horizontalOffset in 0F..25F) {
        "Horizontal Offset is the percentage offset from side, and must be between 0 and 25, included."
    }
    val transitionAnimation = remember(lineChartData.variants) { Animatable(initialValue = 0F) }

    LaunchedEffect(lineChartData.variants) {
        transitionAnimation.snapTo(0F)
        transitionAnimation.animateTo(1F, animationSpec = animation)
    }

    Canvas(modifier = modifier.fillMaxSize()) {
        drawIntoCanvas { canvas ->
            val yAxisDrawableArea = computeYAxisDrawableArea(
                xAxisLabelSize = xAxisDrawer.requireHeight(this),
                size = size
            )
            val xAxisDrawableArea = computeXAxisDrawableArea(
                yAxisWidth = yAxisDrawableArea.width,
                labelHeight = xAxisDrawer.requireHeight(this),
                size = size
            )
            val xAxisLabelsDrawableArea = computeXAxisLabelsDrawableArea(
                xAxisDrawableArea = xAxisDrawableArea,
                offset = horizontalOffset
            )

            val chartDrawableArea = computeDrawableArea(
                xAxisDrawableArea = xAxisDrawableArea,
                yAxisDrawableArea = yAxisDrawableArea,
                size = size,
                offset = horizontalOffset
            )

            lineChartData.variants.forEachIndexed { index, variant ->
                SolidLineDrawer(color = getColor(index)).drawLine(
                    drawScope = this,
                    canvas = canvas,
                    linePath = computeLinePath(
                        drawableArea = chartDrawableArea,
                        lineChartData = lineChartData,
                        points = variant.points,
                        transitionProgress = transitionAnimation.value
                    )
                )

                variant.points.forEachIndexed { index, point ->
                    withProgress(
                        index = index,
                        points = variant.points,
                        transitionProgress = transitionAnimation.value
                    ) {
                        pointDrawer.drawPoint(
                            drawScope = this,
                            canvas = canvas,
                            center = computePointLocation(
                                drawableArea = chartDrawableArea,
                                lineChartData = lineChartData,
                                points = variant.points,
                                point = point,
                                index = index
                            )
                        )
                    }
                }
            }

            xAxisDrawer.drawXAxisLine(
                drawScope = this,
                drawableArea = xAxisDrawableArea,
                canvas = canvas
            )
            xAxisDrawer.drawXAxisLabels(
                drawScope = this,
                canvas = canvas,
                drawableArea = xAxisLabelsDrawableArea,
                labels = lineChartData.variants[0].points.map { it.label })
            yAxisDrawer.drawAxisLine(
                drawScope = this,
                drawableArea = yAxisDrawableArea,
                canvas = canvas
            )
            yAxisDrawer.drawAxisLabels(
                drawScope = this,
                canvas = canvas,
                drawableArea = yAxisDrawableArea,
                minValue = lineChartData.minY,
                maxValue = lineChartData.maxY,
            )

            yAxisDrawer.drawChartLegends(
                drawScope = this,
                canvas = canvas,
                drawableArea = yAxisDrawableArea,
                minValue = lineChartData.minY,
                maxValue = lineChartData.maxY,
                lineChartData = lineChartData
            )
        }
    }
}