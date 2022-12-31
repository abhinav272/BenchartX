import charting.line.LineChartData

/** Sample Input

A1 Device - Dev branch - 20Dec
HomeScrollBenchmark_performUpDownScrollTest
frameDurationCpuMs   P50   17.5,   P90   32.1,   P95   47.9,   P99   79.2
frameOverrunMs   P50   -2.4,   P90   43.1,   P95   60.3,   P99  118.2
Traces: Iteration 0 1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 16 17 18 19


A1 Device - Dev Branch - 20 Dec - Masthead Stripped
HomeScrollBenchmark_performUpDownScrollTest
frameDurationCpuMs   P50   17.0,   P90   28.8,   P95   39.9,   P99   67.3
frameOverrunMs   P50   -3.7,   P90   39.6,   P95   56.4,   P99   91.0
Traces: Iteration 0 1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 16 17 18 19
*/

fun parseData(input: String): Pair<LineChartData?, LineChartData?> {
    if (input.isEmpty())
        return Pair(null, null)
    var lineChartData1: LineChartData? = null
    var lineChartData2: LineChartData? = null
    val splitInput = input.split("\n").removeAdjacentDuplicate().plus("")
    val blocksMap = createBlocks(splitInput)
    blocksMap.map {
        val variant = it.value[0]
        val title1 = it.value[2].parseName() ?: ""
        val title2 = it.value[3].parseName() ?: ""
        val points1 = it.value[2].parsePoints()
        val points2 = it.value[3].parsePoints()

        lineChartData1 = if (lineChartData1 == null){
            LineChartData(title = title1, variants = listOf(LineChartData.Variant(title = variant, points = points1)))
        } else {
            val variants = lineChartData1!!.variants
            lineChartData1!!.copy(
                variants = variants.plus(LineChartData.Variant(title = variant, points1))
            )
        }

        lineChartData2 = if (lineChartData2 == null){
            LineChartData(title = title2, variants = listOf(LineChartData.Variant(title = variant, points = points2)))
        }else {
            val variants = lineChartData2!!.variants
            lineChartData2!!.copy(
                variants = variants.plus(LineChartData.Variant(title = variant, points2))
            )
        }
    }
    return Pair(lineChartData1, lineChartData2)
}

private fun String.parsePoints(): List<LineChartData.Point> {
    val points = mutableListOf<LineChartData.Point>()
    this.split(",   ").forEach { section ->
        val splitSection = section.split("   ")
        if (splitSection.size > 2){
            points.add(LineChartData.Point(value = splitSection[2].toFloat(), label = splitSection[1]))
        } else {
            points.add(LineChartData.Point(value = splitSection[1].toFloat(), label = splitSection[0]))
        }
    }
    return points
}

private fun String.parseName(): String? {
    return this.split(" ").firstOrNull()
}

private fun createBlocks(splitInput: List<String>): MutableMap<Int, List<String>> {
    var blockCount = 0
    var list = mutableListOf<String>()
    val map = mutableMapOf<Int, List<String>>()
    splitInput.map {
        if (it.isNotEmpty()) {
            list.add(it)
        } else {
            map[blockCount] = list
            blockCount++
            list = mutableListOf()
        }
    }
    return map
}

fun <T : Any> Iterable<T>.removeAdjacentDuplicate(): List<T> {
    var last: T? = null
    return mapNotNull {
        if (it == last) {
            null
        } else {
            last = it
            it
        }
    }
}
