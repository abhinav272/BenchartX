data class BenchartXData(
    val title: String,
    val points: List<BenchartXPoint>,
)

data class BenchartXPoint(
    val label: String,
    val value: Float,
)
