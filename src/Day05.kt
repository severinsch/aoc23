fun main() {
    fun part1(input: List<String>): Long {
        val seeds = input.first().removePrefix("seeds: ").split(" ").map { it.toLong() }

        fun parseMap(mapString: String): Map<Long, Long> {
            val map: MutableMap<Long, Long> = mutableMapOf<Long, Long>().withDefault { k -> k }
            mapString.split("\n").drop(1).map {line ->
                val (destStart, sourceStart, len) = line.split(" ").map { it.toLong() }
                (destStart..<destStart+len).zip(sourceStart..<sourceStart+len).forEach {
                    (dest, source) -> map[source] = dest
                }
            }
            return map
        }

        val maps = input.drop(2).joinToString("\n").split("\n\n")
        val parsedMaps = maps.map { parseMap(it) }
        println(parsedMaps)
        val locations = parsedMaps.fold(seeds) {
            acc, map -> acc.map { map.getValue(it) }
        }
        return locations.min()
    }

    fun part2(input: List<String>): Int {
        TODO()
    }

    val input = readInput("day05")

    println("Part 1: ${part1(input)}")
    println("Part 2: ${part2(input)}")
}
