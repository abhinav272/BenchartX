package charting.line

/**
 * @Author bytebeats
 * @Email <happychinapc@gmail.com>
 * @Github https://github.com/bytebeats
 * @Created at 2022/3/10 20:27
 * @Version 1.0
 * @Description TO-DO
 */

data class LineChartData(
    val title: String,
    val variants: List<Variant>,
    val padBy: Float = 20F,//percentage we pad yValue by
    val startAtZero: Boolean = false
) {
    init {
        require(padBy in 0F..100F) {
            "padBy must be between 0F and 100F, included"
        }
    }

    private val yMinMaxValues: Pair<Float, Float>
        get() {
            val minVal = variants.minOf { it.points.minOf { point -> point.value } }
            val maxVal = variants.maxOf { it.points.maxOf { point -> point.value } }
            return minVal to maxVal
        }

    internal val maxY: Float
        get() = yMinMaxValues.second + (yMinMaxValues.second - yMinMaxValues.first) * padBy / 100F
    internal val minY: Float
        get() = if (startAtZero) 0F else yMinMaxValues.first - (yMinMaxValues.second - yMinMaxValues.first) * padBy / 100F

    internal val yRange: Float
        get() = maxY - minY

    data class Point(val value: Float, val label: String)
    data class Variant(val title: String, val points: List<Point>)
}