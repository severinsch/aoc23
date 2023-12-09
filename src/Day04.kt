import kotlin.math.pow

fun main() {
    fun part1(input: List<String>): Int {
        val points = input.map { line ->
            val (winning, losing) = line.dropWhile { it != ':' }.drop(2).split(" | ").map { numbers ->
                numbers.split(" ").filter { it.isNotEmpty() }.map { it.toInt() }
            }
            losing.count { it in winning }.let { when(it) {
                0 -> 0
                1 -> 1
                else -> 2.0.pow(it - 1).toInt()
            } }
        }
        return points.sum()
    }

    fun part2(input: List<String>): Int {
        val copies: MutableMap<Int, Int> = mutableMapOf()
        input.forEachIndexed { index, line ->
            val (winning, losing) = line.dropWhile { it != ':' }.drop(2).split(" | ").map { numbers ->
                numbers.split(" ").filter { it.isNotEmpty() }.map { it.toInt() }
            }
            val matchingNumbers = (winning intersect losing.toSet()).count()

            val copiesOfCurrent = copies.getOrPut(index) { 1 }
            (index+1..index+matchingNumbers).forEach {
                copies[it] = copies.getOrDefault(it, 1) + copiesOfCurrent
            }
        }
        println(copies)
        return copies.values.sum()
    }

    val input = readInput("day04")
    println("Part 1: ${part1(input)}")
    println("Part 2: ${part2(input)}")
}
