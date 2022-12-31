package charting.app.screen.line

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import charting.line.LineChartData
import charting.line.LineChartData.Point
import charting.line.render.point.EmptyPointDrawer
import charting.line.render.point.FilledCircularPointDrawer
import charting.line.render.point.HollowCircularPointDrawer
import charting.line.render.point.IPointDrawer
import kotlin.concurrent.timerTask
import kotlin.random.Random

/**
 * @Author bytebeats
 * @Email <happychinapc@gmail.com>
 * @Github https://github.com/bytebeats
 * @Created at 2022/3/10 21:16
 * @Version 1.0
 * @Description TO-DO
 */

class LineChartDataModel {
    var lineChartData by mutableStateOf(
        LineChartData(
            title = "Sample",
            variants = listOf(
                LineChartData.Variant(
                    title = "Before",
                    listOf(
                        LineChartData.Point(13f, "Line 1"),
                        LineChartData.Point(15f, "Line 2"),
                        LineChartData.Point(17f, "Line 3"),
                    )
                ),
                LineChartData.Variant(
                    title = "After",
                    listOf(
                        LineChartData.Point(10f, "Line 1"),
                        LineChartData.Point(30f, "Line 2"),
                        LineChartData.Point(12f, "Line 3"),
                    )
                )
            )
        )
    )

    var horizontalOffset by mutableStateOf(5F)
    var pointDrawerType by mutableStateOf(PointDrawerType.Hollow)
    val pointDrawer: IPointDrawer
        get() {
            return when (pointDrawerType) {
                PointDrawerType.None -> EmptyPointDrawer
                PointDrawerType.Filled -> FilledCircularPointDrawer()
                PointDrawerType.Hollow -> HollowCircularPointDrawer()
            }
        }


    private fun randomYValue(): Float = Random.Default.nextInt(45, 145).toFloat()
}