fun main() {
    fun part1(input: List<String>) =
        input.sumOf { line ->
            line.first { it.isDigit() }.digitToInt() * 10 + line.last { it.isDigit() }.digitToInt()
        }

    fun part2(input: List<String>): Int {
        val digits =
            (0..9).associateBy { it.toString() } + mapOf (
            "zero" to 0,
            "one" to 1,
            "two" to 2,
            "three" to 3,
            "four" to 4,
            "five" to 5,
            "six" to 6,
            "seven" to 7,
            "eight" to 8,
            "nine" to 9
        )

        return input.sumOf {line ->
            line.findAnyOf(digits.keys)?.let { res -> digits[res.second] }!! * 10 +
                    line.findLastAnyOf(digits.keys)?.let { res -> digits[res.second] }!!
        }
    }

    val input = readInput("day01")
    println("Part 1: ${part1(input)}")
    println("Part 2: ${part2(input)}")
}
