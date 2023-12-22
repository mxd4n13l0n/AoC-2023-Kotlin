fun main() {
    fun part1(input: List<String>): Long {
        var sum = 0L
        input.forEach { line ->
            val gameLine = line.split(':')
            if (isGamePossible(gameLine.last())) {
                val gameID = extractGameID(gameLine.first())
                sum += gameID
            }
        }
        return sum
    }

    fun part2(input: List<String>): Long {
        var sum = 0L
        input.forEach { line ->
            val gameLine = line.split(':')
            val minimumCubes = calculateMinimumCubeFromGame(gameLine.last())
            var multiplier = 1L
            minimumCubes.values.forEach { multiplier *= it }
            sum += multiplier
        }
        return sum
    }

    val input = readInput("Day02")
    part1(input).println()
    part2(input).println()
}

private fun extractGameID(line: String): Long {
    return line.removePrefix("Game ").toLong()
}

private fun isGamePossible(line: String): Boolean {
    val subGames = line.trim().split(';').map { it.trim() }
    subGames.forEach { subGame ->
        val colors = subGame.split(',').map { it.trim() }
        colors.forEach { color ->
            MAX_COLORS.keys.forEach { colorMatch ->
                if (color.endsWith(colorMatch)) {
                    val colorCount = color.removeSuffix(colorMatch).toLong()
                    if (colorCount > MAX_COLORS.getOrDefault(colorMatch, 0) ) {
                        return false
                    }
                }
            }
        }
    }
    return true
}

private fun calculateMinimumCubeFromGame(line: String): Map<String, Int> {
    val subGames = line.trim().split(';').map { it.trim() }
    val minimumColorCounts = mutableMapOf(
            " red" to 0,
            " green" to 0,
            " blue" to 0,
    )
    subGames.forEach { subGame ->
        val colors = subGame.split(',').map { it.trim() }
        colors.forEach { color ->
            minimumColorCounts.keys.forEach { colorMatch ->
                if (color.endsWith(colorMatch)) {
                    val colorCount = color.removeSuffix(colorMatch).toInt()
                    val actualMinCountForColor = minimumColorCounts.getOrDefault(colorMatch, 0)
                    if (colorCount > actualMinCountForColor) {
                        minimumColorCounts[colorMatch] = colorCount
                    }
                }
            }
        }
    }
    return minimumColorCounts
}

val MAX_COLORS = mapOf(
        " red" to 12,
        " green" to 13,
        " blue" to 14,
)