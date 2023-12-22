fun main() {
    fun part1(input: List<String>): Long {
        val times = input
                .first()
                .removePrefix("Time:")
                .split(' ')
                .filterNot { it == "" }
                .map { it.toLong() }
        val recordDistance = input
                .last()
                .removePrefix("Distance:")
                .split(' ')
                .filterNot { it == "" }
                .map { it.toLong() }
        var possibilities = 1L
        for (i in times.indices) {
            val currentTime = times[i]
            val distanceRecord = recordDistance[i]
            val possibilitiesToBeatGame = calculatePossibilities(currentTime, distanceRecord)
            possibilities *= possibilitiesToBeatGame
        }
        return possibilities
    }

    fun part2(input: List<String>): Long {
        val time = input
                .first()
                .removePrefix("Time:")
                .filter { it.isDigit() }
                .toLong()
        val recordDistance = input
                .last()
                .removePrefix("Distance:")
                .filter { it.isDigit() }
                .toLong()
        val possibilitiesToBeatGame = calculatePossibilities(time, recordDistance)
        return possibilitiesToBeatGame
    }

    val input = readInput("Day06")
    part1(input).println()
    part2(input).println()
}

private fun calculatePossibilities(time: Long, distanceRecord: Long): Long {
    var possibilities = 0L
    for (timeSpentOnButton in 0..time) {
        val distanceTravelled = timeSpentOnButton * (time - timeSpentOnButton)
        if (distanceTravelled > distanceRecord) {
            possibilities += 1
        }
    }
    return possibilities
}