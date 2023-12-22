fun main() {

    fun part1(input: List<String>): Long {
        var total = 0L
        input.forEach { line ->
            val splittedLine = line.split(' ')
            val springs = splittedLine.first()
            val arrangements = splittedLine.last().split(',').map{ it.toInt() }
            val cache = mutableMapOf<String, Long>()
            val waysPossibles = calculateNumberOfSolutions(springs, arrangements, cache)
            total += waysPossibles
        }
        return total
    }

    fun part2(input: List<String>): Long {
        var total = 0L
        input.forEach { line ->
            val splitLine = line.split(' ')
            val springs = with(splitLine.first()) {
                List(5) { this }.joinToString(separator = "?")
            }
            val arrangements = with(splitLine.last()) {
                List(5) { this }.joinToString(separator = ",")
            }.split(',').map{ it.toInt() }
            val cache = mutableMapOf<String, Long>()
            val waysPossibles = calculateNumberOfSolutions(springs, arrangements, cache)
            total += waysPossibles
        }
        return total
    }

    val input = readInput("Day12")
    part1(input).println()
    part2(input).println()
}

private fun calculateNumberOfSolutions(springs: String, groups: List<Int>, cache: MutableMap<String, Long>): Long {
    val hashOperation = "_$springs-${groups.joinToString("-")}"
    cache[hashOperation]?.let {
        return it
    }
    if (springs.isEmpty()) {
        val result = if (groups.isEmpty()) 1L else 0L
        cache[hashOperation] = result
        return result
    }
    if (groups.isEmpty()) {
        val result = if (springs.contains('#').not()) 1L else 0L
        cache[hashOperation] = result
        return result
    }

    val currentSpring = springs.first()
    val restOfSprings = springs.substring(1)

    return when (currentSpring) {
        '.' -> calculateNumberOfSolutions(restOfSprings, groups, cache)
        '#' -> {
            val group = groups.first()
            if (
                    springs.length >= group &&
                    springs.substring(0, group).contains('.').not() &&
                    (springs.length == group || springs[group] != '#')
            ) {
                val nextSubstring = if (group + 1 > springs.length) {
                    springs.substring(group, springs.length)
                } else {
                    springs.substring(group + 1, springs.length)
                }
                val nextGroups = groups.subList(1, groups.size)
                val possibleSolutions = calculateNumberOfSolutions(nextSubstring, nextGroups, cache)
                cache[hashOperation] = possibleSolutions
                possibleSolutions
            } else {
                0
            }
        }
        else -> {
            val possibleSolutions =
                    calculateNumberOfSolutions(".${restOfSprings}", groups, cache) +
                            calculateNumberOfSolutions("#${restOfSprings}", groups, cache)
            cache[hashOperation] = possibleSolutions
            possibleSolutions
        }
    }
}