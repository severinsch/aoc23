fun main() {

    fun Char.isSymbol() = !this.isDigit() and (this != '.')
    val SYMBOLS = "*#+$/=%@&-"

    fun part1(input: List<String>): Int {
        val symbolCoords = input.flatMapIndexed { y, line ->
            line.mapIndexed { x, c ->
                if (c.isSymbol()) Pair(x, y) else null
            }.filterNotNull()
        }.toSet()

        fun checkIfRelevant(xs: List<Int>, y: Int): Boolean {
            val surroundingCoords = xs.flatMap { x -> listOf(x to y+1, x to y, x to y-1) }
            val res = surroundingCoords.any { p -> symbolCoords.contains(p)}
            return res
        }


        fun getRelevantNumbers(y: Int, line: String): List<Int> {
            var currentIndex = 0
            val numbers = line.split(
                delimiters = (".$SYMBOLS").map { it.toString() }.toTypedArray()
            ).map { it.trim { c -> c.isSymbol()} }.filter { it.isNotEmpty() }
            val relevantNumbers = numbers.filter { no ->
                val startIndex = line.indexOf(no, startIndex = currentIndex)
                val endIndex = startIndex + no.length - 1
                currentIndex = endIndex + 1
                // check one column left and right of the number
                return@filter checkIfRelevant((startIndex - 1.. endIndex + 1).toList(), y)
            }.map { it.toInt() }
            return relevantNumbers
        }


        return input.flatMapIndexed { i, line -> getRelevantNumbers(i, line) }.sum()

    }

    fun part2(input: List<String>): Int {
        fun getAdjacentNumbers(x: Int, y: Int): Pair<Int, Int>? {
            fun getAdjacentNumbersForLine(line: String): List<Int> {
                val numbers = line.split(
                    delimiters = (".$SYMBOLS").map { it.toString() }.toTypedArray()
                ).map { it.trim { c -> c.isSymbol()} }.filter { it.isNotEmpty() }
                var currentIndex = 0
                val relevantNumbers = numbers.filter { no ->
                    val startIndex = line.indexOf(no, startIndex = currentIndex)
                    val endIndex = startIndex + no.length - 1
                    currentIndex = endIndex + 1

                    return@filter x in (startIndex - 1  .. endIndex + 1)
                }.map { it.toInt() }
                return relevantNumbers
            }
            (-1..1).mapNotNull { input.getOrNull(y + it) }.flatMap {
                val ns = getAdjacentNumbersForLine(it)
                return@flatMap ns
            }.let {
                return if (it.size == 2) it[0] to it[1] else null
            }
        }


        fun getGearRatios(y: Int, line: String): List<Int> {
            return line.mapIndexed { x, c ->
                if (c != '*') null else {
                    getAdjacentNumbers(x, y)?.let { (a, b) -> a * b }
                }
            }.filterNotNull()
        }


        return input.flatMapIndexed { i, line -> getGearRatios(i, line) }.sum()
    }

    val input = readInput("day03")
    println("Part 1: ${part1(input)}")
    println("Part 2: ${part2(input)}")
}
