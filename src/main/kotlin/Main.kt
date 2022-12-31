// Copyright 2000-2021 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import charting.line.LineChartData
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged

@Composable
@Preview
fun App() {
    val lineChartData1State = remember { mutableStateOf<LineChartData?>(null) }
    val lineChartData2State = remember { mutableStateOf<LineChartData?>(null) }
    LazyColumn(modifier = Modifier.fillMaxSize().padding(5.dp)) {
        items(
            count = 4,
            key = { index -> index },
        ) { index ->
            when (index) {
                0 -> {
                    HeaderWidgetUi()
                }

                1 -> {
                    DataInputWidgetUi(lineChartData1State,lineChartData2State)
                }

                2 -> {
                    BenchartXWidgetUi(
                        lineChartData1 = lineChartData1State.value,
                        lineChartData2 = lineChartData2State.value
                    )
                }

                3 -> {
                    BenchartXSummaryUi(
                        lineChartData1 = lineChartData1State.value,
                        lineChartData2 = lineChartData2State.value
                    )
                }

                else -> {}
            }
        }
    }
}

@OptIn(FlowPreview::class)
@Composable
fun DataInputWidgetUi(
    lineChartData1State: MutableState<LineChartData?>,
    lineChartData2State: MutableState<LineChartData?>
) {
    val inputString = remember { mutableStateOf(TextFieldValue(text = "")) }
    LaunchedEffect(Unit) {
        snapshotFlow { inputString.value }.debounce(500)
            .distinctUntilChanged { old, new -> old.text == new.text }
            .collectLatest {
                val parseData = parseData(it.text)
                lineChartData1State.value = parseData.first
                lineChartData2State.value = parseData.second
            }
    }
    TextField(
        value = inputString.value,
        onValueChange = {
            inputString.value = it
        },
        modifier = Modifier.fillMaxWidth(0.8f).height(200.dp)
            .border(width = 2.dp, color = Color.DarkGray, shape = RoundedCornerShape(10.dp)),
        label = {
            Text(text = "Input", fontSize = 20.sp)
        },
        placeholder = {
            Text(text = "Enter your Benchart values here ....", fontSize = 12.sp)
        },
    )
}

@Composable
fun HeaderWidgetUi(modifier: Modifier = Modifier) {
    Text(
        text = "BenchartX",
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        fontSize = 48.sp
    )
}

fun main() = application {
    Window(onCloseRequest = ::exitApplication) {
        App()
    }
}
