fun main() {
    fun part1(input: List<String>): Int {
        val parseNumbers = { line: String -> line.split(" ").filterNot { it.isBlank() }.map { it.toInt() } }
        val pairs = parseNumbers(input.first().removePrefix("Time: ")).zip(parseNumbers(input.last().removePrefix("Distance: ")))
        return pairs.fold(1) { acc, (time, record) -> acc * (1..time).map { holdDuration ->
                // speed (equals holdDoration) * remaining time after holding
                holdDuration * (time - holdDuration)
            }.count { it > record }
        }
    }

    fun part2(input: List<String>): Int {
        val getNumber = {line: String -> line.replace(" ", "").toLong()}
        val time = getNumber(input.first().removePrefix("Time: "))
        val record = getNumber(input.last().removePrefix("Distance: "))
        return  (1..time).map { holdDuration ->
            // speed (equals holdDoration) * remaining time after holding
            holdDuration * (time - holdDuration)
        }.count { it > record }
    }

    val input = readInput("day06")
    println("Part 1: ${part1(input)}")
    println("Part 2: ${part2(input)}")
}
