import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import charting.line.LineChart
import charting.line.LineChartData
import charting.line.getChartBackgroundColor
import charting.line.render.point.FilledCircularPointDrawer
import charting.line.render.xaxis.SimpleXAxisDrawer
import charting.line.render.yaxis.SimpleYAxisDrawer
import charting.simpleChartAnimation

@Composable
fun LineChartUi(modifier: Modifier = Modifier, lineChartData: LineChartData) {
    LineChart(
        lineChartData = lineChartData,
        modifier = modifier,
        animation = simpleChartAnimation(),
        pointDrawer = FilledCircularPointDrawer(),
        xAxisDrawer = SimpleXAxisDrawer(),
        yAxisDrawer = SimpleYAxisDrawer(),
        horizontalOffset = 5f
    )
}

@Composable
fun BenchartXSummaryUi(modifier: Modifier = Modifier, lineChartData1: LineChartData?, lineChartData2: LineChartData?) {
    Row(
        modifier = modifier
            .fillMaxSize()
            .padding(10.dp)
            .clipToBounds()
    ) {

    }
}

@Composable
fun BenchartXWidgetUi(modifier: Modifier = Modifier, lineChartData1: LineChartData?, lineChartData2: LineChartData?) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(600.dp)
            .padding(10.dp)
            .clipToBounds()
    ) {
        lineChartData1?.let { data ->
            LineChartUi(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(0.6f)
                    .background(getChartBackgroundColor())
                    .border(width = 2.dp, color = Color.Black)
                    .padding(bottom = 10.dp, end = 10.dp),
                lineChartData = data
            )
        }
        Spacer(modifier = Modifier.width(5.dp))
        lineChartData2?.let { data ->
            LineChartUi(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(0.6f)
                    .background(getChartBackgroundColor())
                    .border(width = 2.dp, color = Color.Black)
                    .padding(bottom = 10.dp, end = 10.dp),
                lineChartData = data
            )
        }
    }
}