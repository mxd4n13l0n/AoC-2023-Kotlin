fun main() {
    fun part1(input: List<String>): Long {
        val mapOfHandsAndBids = mutableMapOf<String, Long>()
        val mapOfHandsAndTypes = mutableMapOf<String, CamelCardType>()
        input.forEach { fileLine ->
            val splitLine = fileLine.split(' ')
            val hand = splitLine.first()
            val bid = splitLine.last().toLong()
            mapOfHandsAndBids[hand] = bid
            mapOfHandsAndTypes[hand] = evaluateHandType(hand)
        }
        val orderedHand = mutableListOf<String>()
        with(mapOfHandsAndTypes.filterValues { it == CamelCardType.FiveOfAKind }.keys) {
            orderedHand.addAll( this.sortedWith(secondLevelComparator()))
        }
        with(mapOfHandsAndTypes.filterValues { it == CamelCardType.FourOfAKind }.keys) {
            orderedHand.addAll( this.sortedWith(secondLevelComparator()))
        }
        with(mapOfHandsAndTypes.filterValues { it == CamelCardType.FullHouse }.keys) {
            orderedHand.addAll( this.sortedWith(secondLevelComparator()))
        }
        with(mapOfHandsAndTypes.filterValues { it == CamelCardType.ThreeOfAKind }.keys) {
            orderedHand.addAll( this.sortedWith(secondLevelComparator()))
        }
        with(mapOfHandsAndTypes.filterValues { it == CamelCardType.TwoPair }.keys) {
            orderedHand.addAll( this.sortedWith(secondLevelComparator()))
        }
        with(mapOfHandsAndTypes.filterValues { it == CamelCardType.OnePair }.keys) {
            orderedHand.addAll( this.sortedWith(secondLevelComparator()))
        }
        with(mapOfHandsAndTypes.filterValues { it == CamelCardType.HighCard }.keys) {
            orderedHand.addAll( this.sortedWith(secondLevelComparator()))
        }
        var totalWinnings = 0L
        var multiply = orderedHand.size
        orderedHand.forEach { hand ->
            val bid = mapOfHandsAndBids.getOrDefault(hand, 0L)
            totalWinnings += bid * multiply
            multiply -= 1
        }
        return totalWinnings
    }

    fun part2(input: List<String>): Long {
        val mapOfHandsAndBids = mutableMapOf<String, Long>()
        val mapOfHandsAndTypes = mutableMapOf<String, CamelCardType>()
        input.forEach { fileLine ->
            val splitLine = fileLine.split(' ')
            val hand = splitLine.first()
            val bid = splitLine.last().toLong()
            mapOfHandsAndBids[hand] = bid
            mapOfHandsAndTypes[hand] = evaluateHandType(hand)
        }
        val orderedHand = mutableListOf<String>()
        with(mapOfHandsAndTypes.filterValues { it == CamelCardType.FiveOfAKind }.keys) {
            orderedHand.addAll( this.sortedWith(secondLevelComparator(useJokers = true)))
        }
        with(mapOfHandsAndTypes.filterValues { it == CamelCardType.FourOfAKind }.keys) {
            orderedHand.addAll( this.sortedWith(secondLevelComparator(useJokers = true)))
        }
        with(mapOfHandsAndTypes.filterValues { it == CamelCardType.FullHouse }.keys) {
            orderedHand.addAll( this.sortedWith(secondLevelComparator(useJokers = true)))
        }
        with(mapOfHandsAndTypes.filterValues { it == CamelCardType.ThreeOfAKind }.keys) {
            orderedHand.addAll( this.sortedWith(secondLevelComparator(useJokers = true)))
        }
        with(mapOfHandsAndTypes.filterValues { it == CamelCardType.TwoPair }.keys) {
            orderedHand.addAll( this.sortedWith(secondLevelComparator(useJokers = true)))
        }
        with(mapOfHandsAndTypes.filterValues { it == CamelCardType.OnePair }.keys) {
            orderedHand.addAll( this.sortedWith(secondLevelComparator(useJokers = true)))
        }
        with(mapOfHandsAndTypes.filterValues { it == CamelCardType.HighCard }.keys) {
            orderedHand.addAll( this.sortedWith(secondLevelComparator(useJokers = true)))
        }
        var totalWinnings = 0L
        var multiply = orderedHand.size
        orderedHand.forEach { hand ->
            val bid = mapOfHandsAndBids.getOrDefault(hand, 0L)
            val type = mapOfHandsAndTypes.getOrDefault(hand, CamelCardType.HighCard)
            println("hand: $hand - type: $type - bid: $bid")
            totalWinnings += bid * multiply
            multiply -= 1
        }
        return totalWinnings
    }

    val input = readInput("Day07")
    part1(input).println()
    part2(input).println()
}

enum class CamelCardType {
    FiveOfAKind, FourOfAKind, FullHouse, ThreeOfAKind, TwoPair, OnePair, HighCard
}

private fun secondLevelComparator(useJokers: Boolean = false) = Comparator { hand1: String, hand2: String ->
    for (i in 0..hand1.lastIndex) {
        val cardHand1 = evaluateCard(hand1[i], useJokers)
        val cardHand2 = evaluateCard(hand2[i], useJokers)
        if (cardHand1 > cardHand2) {
            return@Comparator -1
        }
        if (cardHand2 > cardHand1) {
            return@Comparator 1
        }
    }
    0
}

private fun evaluateHandType(hand: String, useJoker: Boolean = false): CamelCardType {
    return if (useJoker) {
        evaluateHandTypeWithJokers(hand)
    } else {
        return evaluateHandTypeNoJokers(hand)
    }
}

private fun evaluateHandTypeNoJokers(hand: String): CamelCardType {
    val handMapCounts = hand.groupBy { it }.values.map { it.size }
    return when {
        handMapCounts.contains(5) -> CamelCardType.FiveOfAKind
        handMapCounts.contains(4) -> CamelCardType.FourOfAKind
        handMapCounts.contains(3) && handMapCounts.contains(2) -> CamelCardType.FullHouse
        handMapCounts.contains(3) -> CamelCardType.ThreeOfAKind
        handMapCounts.filter { it == 2 }.size == 2 -> CamelCardType.TwoPair
        handMapCounts.contains(2) -> CamelCardType.OnePair
        else -> CamelCardType.HighCard
    }
}

private fun evaluateHandTypeWithJokers(hand: String): CamelCardType {
    val handMapCounts = hand.groupBy { it }.values.map { it.size }
    val howManyJokers = hand.count { it == 'J' }
    return when {
        handMapCounts.contains(5) -> CamelCardType.FiveOfAKind
        handMapCounts.contains(4) -> {
            when (howManyJokers) {
                1, 4 -> CamelCardType.FiveOfAKind
                else -> CamelCardType.FourOfAKind
            }
        }
        handMapCounts.contains(3) && handMapCounts.contains(2) -> {
            when (howManyJokers) {
                2, 3 -> CamelCardType.FiveOfAKind
                else -> CamelCardType.FullHouse
            }
        }
        handMapCounts.contains(3) -> {
            when (howManyJokers) {
                1, 3 -> CamelCardType.FourOfAKind
                else -> CamelCardType.ThreeOfAKind
            }
        }
        handMapCounts.filter { it == 2 }.size == 2 -> {
            when(howManyJokers) {
                1 -> CamelCardType.FullHouse
                2 -> CamelCardType.FourOfAKind
                else -> CamelCardType.TwoPair
            }
        }
        handMapCounts.contains(2) -> {
            when (howManyJokers) {
                1, 2 -> CamelCardType.ThreeOfAKind
                else -> CamelCardType.OnePair
            }
        }
        else -> when (howManyJokers) {
            1 -> CamelCardType.OnePair
            else -> CamelCardType.HighCard
        }
    }
}

private fun evaluateCard(card: Char, useJoker: Boolean = false): Int {
    when (useJoker) {
        false -> {
            return when(card) {
                '2' -> 0
                '3' -> 1
                '4' -> 2
                '5' -> 3
                '6' -> 4
                '7' -> 5
                '8' -> 6
                '9' -> 7
                'T' -> 8
                'J' -> 9
                'Q' -> 10
                'K' -> 11
                'A' -> 12
                else -> 0
            }
        } else -> {
        return when(card) {
            'J' -> -1
            '2' -> 0
            '3' -> 1
            '4' -> 2
            '5' -> 3
            '6' -> 4
            '7' -> 5
            '8' -> 6
            '9' -> 7
            'T' -> 8
            'Q' -> 10
            'K' -> 11
            'A' -> 12
            else -> 0
        }
        }
    }
}