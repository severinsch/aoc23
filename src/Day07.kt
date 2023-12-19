fun main() {

    val cards = listOf('2', '3', '4', '5', '6', '7', '8', '9', 'T', 'J', 'Q', 'K', 'A')

    fun compareCards(a: Char, b: Char): Int {
        return cards.indexOf(a).compareTo(cards.indexOf(b))
    }

    fun type(hand: String): Int {
        return when(hand.groupingBy { it }.eachCount().values.sorted()) {
            listOf(5) -> 6
            listOf(1, 4) -> 5
            listOf(2, 3) -> 4
            listOf(1, 1, 3) -> 3
            listOf(1, 2, 2) -> 2
            listOf(1, 1, 1, 2) -> 1
            else -> 0
        }
    }

    val comp = Comparator<Pair<String, Int>> { pa, pb -> type(pa.first).compareTo(type(pb.first))}.thenComparator {
        pa, pb -> pa.first.zip(pb.first).map { compareCards(it.first, it.second) }.firstOrNull { it != 0 } ?: 0
    }


    fun part1(input: List<String>): Int {
        val hands = input.map {line -> line.split(" ").let{Pair(it[0], it[1].toInt())}}
        return hands.sortedWith(comp).zip((1..hands.size)).sumOf { it.first.second * it.second }
    }

    val cards2 = listOf('J', '2', '3', '4', '5', '6', '7', '8', '9', 'T', 'Q', 'K', 'A')

    fun compareCards2(a: Char, b: Char): Int {
        return cards2.indexOf(a).compareTo(cards2.indexOf(b))
    }

    fun type2(hand: String): Int {
        return when(hand.replace("J", "").groupingBy { it }.eachCount().values.sorted()) {
            // 5 of a kind with jokers
            listOf(5), listOf(4), listOf(3), listOf(2), listOf(1), emptyList<Int>() -> 6
            // 4 of a kind with jokers
            listOf(1,4), listOf(1, 3), listOf(1,2), listOf(1,1) -> 5
            // full house with jokers
            listOf(2, 3), listOf(2,2) -> 4
            // 3 of a kind with jokers
            listOf(1, 1, 3), listOf(1, 1, 2), listOf(1, 1, 1) -> 3
            // two pairs
            listOf(1, 2, 2) -> 2
            // one pair with jokers
            listOf(1, 1, 1, 2), listOf(1, 1, 1, 1) -> 1
            listOf(1, 1, 1, 1, 1) -> 0
            else -> throw Error("Unknown hand: $hand")
        }
    }

    val comp2 = Comparator<Pair<String, Int>> { pa, pb ->
        val res = type2(pa.first).compareTo(type2(pb.first))
        println("$pa (${type2(pa.first)}) vs $pb (${type2(pb.first)}): ${if (res == 0) "tie" else if (res < 0) "pb" else "pa"}")
        res
    }.thenComparator {
            pa, pb -> pa.first.zip(pb.first).map { compareCards2(it.first, it.second) }.firstOrNull { it != 0 } ?: 0
    }


    fun part2(input: List<String>): Int {
        val hands = input.map {line -> line.split(" ").let{Pair(it[0], it[1].toInt())}}
        return hands.sortedWith(comp2).zip((1..hands.size)).sumOf { it.first.second * it.second }
    }

    val input = readInput("day07")
    println("Part 1: ${part1(input)}")
    println("Part 2: ${part2(input)}")
}
