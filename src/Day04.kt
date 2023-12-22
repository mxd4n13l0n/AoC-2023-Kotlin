fun main() {
    fun part1(input: List<String>): Long {
        var sum = 0L
        input.forEach { line ->
            var pointsForCard = 0
            val numbers = extractNumbers(line)
            numbers.first().forEach { winningNumber ->
                if (numbers.last().contains(winningNumber)) {
                    pointsForCard = if (pointsForCard == 0) 1 else pointsForCard + pointsForCard
                }
            }
            sum += pointsForCard
        }
        return sum
    }

    fun part2(input: List<String>): Long {
        val scratchCardCount = mutableMapOf<Int, Long>()
        for(i in input.indices) {
            scratchCardCount[i] = 1
        }

        for(i in input.indices) {
            val currentCardAmount = scratchCardCount.getOrDefault(i, 0)
            if (currentCardAmount > 0) {
                val currentLine = input[i]
                val numbers = extractNumbers(currentLine)
                val wins = calculateWins(numbers.first(), numbers.last())
                // Add cards multiplied for the current amount
                if (i < input.lastIndex) {
                    for (j in i+1 ..input.lastIndex.coerceAtMost(i + wins)) {
                        scratchCardCount[j] = scratchCardCount.getOrDefault(j, 0) + currentCardAmount
                    }
                }
            }
        }
        val sum = scratchCardCount.values.sum()
        return sum
    }

    val input = readInput("Day04")
    part1(input).println()
    part2(input).println()
}

private fun extractNumbers(line: String): List<Set<String>> {
    val allNumbers = line.split(':').last().split('|')
    val winningNumbers = allNumbers.first().trim().split(' ').filterNot { it == "" }.toSet()
    val myNumbers = allNumbers.last().trim().split(' ').filterNot { it == "" }.toSet()
    return listOf(winningNumbers, myNumbers)
}

private fun calculateWins(winningNumbers: Set<String>, numbers: Set<String>): Int {
    var pointsForCard = 0
    winningNumbers.forEach { winningNumber ->
        if (numbers.contains(winningNumber)) {
            pointsForCard += 1
        }
    }
    return pointsForCard
}