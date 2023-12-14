
class RangeMap() {
    val ranges: MutableList<Pair<LongRange, Long>> = mutableListOf()

    fun addRangeWithLen(start: Long, len: Long, offset: Long) {
        ranges.add((start..<start+len) to offset)
    }

    fun addRange(range: LongRange, offset: Long) {
        ranges.add(range to offset)
    }

    operator fun contains(value: Long): Boolean {
        return ranges.any { it.first.contains(value) }
    }

    operator fun get(value: Long): Long {
        return value + (ranges.firstOrNull { it.first.contains(value) }?.second ?: 0)
    }

    override fun toString(): String {
        return "RangeMap{${ranges.joinToString("; ") { "${it.first} with offset ${it.second}" }}}"
    }

}

fun LongRange.fastIntersect(other: LongRange): LongRange? {
    val start = maxOf(this.first, other.first)
    val end = minOf(this.last, other.last)
    return if (start <= end) start..end else null
}

fun LongRange.addOffset(offset: Long): LongRange {
    return (this.first+offset)..(this.last+offset)
}

// returns (overlap, non overlapping b4, non overlapping after) or null if no overlap
fun LongRange.transformOnOverlap(other: LongRange, offset: Long): LongRange? {
    return this.fastIntersect(other)?.addOffset(offset)
}

fun validateRangeMap(rangeMap: RangeMap) {
    rangeMap.ranges.forEach { (range, _) ->
        rangeMap.ranges.forEach { (otherRange, _) ->
            if(range != otherRange) {
                val overlap = range.fastIntersect(otherRange)
                if(overlap != null) {
                    println("overlap: $overlap in $range and $otherRange")
                    throw Exception("overlap")
                }
            }
        }
    }
    rangeMap.ranges.sortedBy { it.first.first }.zipWithNext().forEach { (a, b) ->
        if(a.first.last != b.first.first-1) {
            println("gap: $a and $b")
            throw Exception("gap")
        }
    }
}

fun insertMissingRanges(rangeMap: RangeMap): RangeMap {
    val sorted = rangeMap.ranges.sortedBy { it.first.first }
    val rest = sorted.zipWithNext().mapNotNull { (a, b) ->
        if(b.first.first - a.first.last > 1) {
            a.first.last+1..<b.first.first
        } else {
            null
        }
    }
    rest.forEach { rangeMap.addRange(it, 0) }
    rangeMap.addRange(sorted.last().first.last+1..Long.MAX_VALUE, 0)
    return rangeMap
}

fun main() {
    fun parseMap(mapString: String): RangeMap {
        val map = RangeMap()
        mapString.split("\n").drop(1).map {line ->
            val (destStart, sourceStart, len) = line.split(" ").map { it.toLong() }
            map.addRangeWithLen(sourceStart, len, destStart - sourceStart)
        }
        return map
    }

    fun getMaps(input: List<String>): List<RangeMap> = input.drop(2).joinToString("\n").split("\n\n").map { parseMap(it) }

    fun part1(input: List<String>): Long {
        val seeds = input.first().removePrefix("seeds: ").split(" ").map { it.toLong() }

        val maps = getMaps(input)
        val locations = maps.fold(seeds) {
                acc, map -> acc.map { map[it] }
        }
        return locations.min()
    }

    fun part2(input: List<String>): Long {
        val seedRanges = input.first().removePrefix("seeds: ").split(" ").map { it.toLong() }.chunked(2
        ) {
            (it[0]..<(it[0]+it[1]))
        }
        val fixedMaps = getMaps(input).map { insertMissingRanges(it) }
        fixedMaps.forEach { validateRangeMap(it) }

        val locations = fixedMaps.fold(seedRanges) {
                seedRangeSet, rangeMap ->  seedRangeSet.flatMap { seedRange ->
                    rangeMap.ranges.mapNotNull { range ->
                        seedRange.transformOnOverlap(range.first, range.second)
                    }
                }
        }
        return locations.minOf { it.first }
    }

    val input = readInput("day05")

    println("Part 1: ${part1(input)}")
    println("Part 2 sev: ${part2(input)} (correct: 31161857)")
    println("Part 2 max: ${part2(readInput("day05_max"))} (correct: 52210644)")
    println("Part 2 mal: ${part2(readInput("day05_malte"))} (correct: 9622622)")
}
