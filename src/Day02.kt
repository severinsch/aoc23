fun main() {
    val BLUE = Pair("blue", 14)
    val GREEN = Pair("green", 13)
    val RED = Pair("red", 12)
    val colors = listOf(BLUE, GREEN, RED)
    val colorNames = colors.map { it.first }

    fun part1(input: List<String>): Int {
        fun isImpossible(line: String) =
            line.split(";").any { set ->
                colors.any { color ->
                    ("""(?<number>\d+) ${color.first}""".toRegex().find(set)?.groups?.get("number")?.value?.toInt()
                        ?: 0) > color.second
                }
            }


        return input.map { game ->
            game.removePrefix("Game ").takeWhile { it.isDigit() }.toInt() to game
        }.filterNot { (_, game) -> isImpossible(game) }.sumOf { (id, _) -> id }

    }
    fun part2(input: List<String>): Int {
        val gameSets = input.map { game ->
            val sets = game.split(";").map { set ->
                val colorVals = colorNames.map {c ->
                    """(?<number>\d+) $c""".toRegex().find(set)?.groups?.get("number")?.value?.toInt() ?: 0
                }
                Triple(colorVals[0], colorVals[1], colorVals[2])
            }
            Triple(sets.maxOf { it.first }, sets.maxOf { it.second }, sets.maxOf { it.third })
        }
        return gameSets.sumOf { (a, b, c) -> a * b * c }
    }

    val input = readInput("day02")
    println("Part 1: ${part1(input)}")
    println("Part 2: ${part2(input)}")
}
