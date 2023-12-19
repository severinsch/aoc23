fun main() {
    fun <T> Sequence<T>.repeat() = sequence { while (true) yieldAll(this@repeat) }

    fun getStepsForStart(start: String, directions: Map<String, Pair<String, String>>, instructions: Sequence<Char>, isGoal: (String) -> Boolean): Long {
        val (_, steps) = instructions.fold(
            start to 0L
        ) {
                (current, steps), instruction ->
            if (isGoal(current)) return steps
            val (left, right) = directions.getValue(current)
            val next = if (instruction == 'L') left else right
            next to steps + 1
        }

        return steps
    }

    val getDirectionMap = { input: List<String> ->
        input.drop(2).associate { line ->
            val k = line.take(3)
            val vs = line.drop(7).removeSuffix(")").split(", ")
            k to (vs[0] to vs[1])
        }
    }

    fun part1(input: List<String>): Long {
        val instructions = input.first().asSequence().repeat()
        val directionMap = getDirectionMap(input)
        return getStepsForStart("AAA", directionMap, instructions) { it == "ZZZ" }
    }

    fun String.isStart() = this[2] == 'A'
    fun String.isGoal() = this[2] == 'Z'

    fun part2bruteforce(input: List<String>): Long {
        val instructions = input.first().asSequence().repeat()
        val directionMap = getDirectionMap(input)

        val startPoints = input.drop(2).map { it.take(3) }.filter { it.isStart() }


        val (_, steps) = instructions.fold(
            startPoints to 0L
        ) {
                (current, steps), instruction ->
            if (current.all { it.isGoal() }) return steps
            val nexts = current.map { point ->
                val (left, right) = directionMap.getValue(point)
                if (instruction == 'L') left else right
            }
            nexts to (steps + 1)
        }

        return steps
    }

    fun findLCM(a: Long, b: Long): Long {
        val larger = if (a > b) a else b
        val maxLcm = a * b
        var lcm = larger
        while (lcm <= maxLcm) {
            if (lcm % a == 0L && lcm % b == 0L) {
                return lcm
            }
            lcm += larger
        }
        return maxLcm
    }

    fun findLCMOfListOfNumbers(numbers: List<Long>): Long {
        var result = numbers[0]
        for (i in 1 until numbers.size) {
            result = findLCM(result, numbers[i])
        }
        return result
    }

    fun part2(input: List<String>): Long {
        val instructions = input.first().asSequence().repeat()
        val directionMap = getDirectionMap(input)
        val startPoints = input.drop(2).map { it.take(3) }.filter { it.isStart() }
        val steps = startPoints.map {
            getStepsForStart(it, directionMap, instructions, String::isGoal)
        }
        return findLCMOfListOfNumbers(steps)
    }

    val input = readInput("day08")
    println("Part 1: ${part1(input)}")
    println("Part 2: ${part2(input)}")
}
