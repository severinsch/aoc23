fun main() {

    fun getNextForHistory(history: List<Int>): Int {
        var current = history
        val lasts = mutableListOf<Int>()
        while(current.any { it != 0 }) {
            lasts.add(current.last())
            val next = current.zipWithNext().map {
                (a, b) -> b - a
            }
            current = next
        }
        return lasts.sum()
    }



    fun part1(input: List<String>): Int {
       return input.map { line -> line.split(" ").map { it.toInt() } }.sumOf {
           getNextForHistory(it)
       }
    }

    fun getPreviousForHistory(history: List<Int>): Int {
        var current = history
        val firsts = mutableListOf<Int>()
        while(current.any { it != 0 }) {
            firsts.add(current.first())
            val next = current.zipWithNext().map {
                    (a, b) -> b - a
            }
            current = next
        }

        return firsts.reduceRight { a, b ->
            a - b
        }

    }

    fun part2(input: List<String>): Int {
        return input.map { line -> line.split(" ").map { it.toInt() } }.sumOf {
            getPreviousForHistory(it)
        }
    }

    val input = readInput("day09")
    println("Part 1: ${part1(input)}")
    println("Part 2: ${part2(input)}")
}
