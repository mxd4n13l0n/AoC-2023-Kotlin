fun main() {

    fun part1(input: List<String>): Long {
        val tilted = tiltToNorth(input)
        val sum = calculateRockSum(tilted)
        return sum
    }

    fun part2(input: List<String>): Long {
        var cycleTotal = input
        val crc = cycleUntilCRCIsFound(input)
        val diff = crc.second - crc.first
        val target = 1000000000 - crc.second
        val remainingLoops = target % diff
        for (i in 1.. remainingLoops+crc.first) {
            cycleTotal = cycle(cycleTotal)
        }
        val sum = calculateRockSum(cycleTotal)
        return sum
    }

    val input = readInput("Day14")
    part1(input).println()
    part2(input).println()
}


private fun tiltToNorth(lines: List<String>): List<String> {
    val result = lines.map{ it.toMutableList() }.toMutableList()
    for(x in result.first().indices) {
        var spacesRemaining = 0
        var firstIndexWithSpace = -1
        for (y in result.indices) {
            when(result[y][x]) {
                '#' -> {
                    spacesRemaining = 0
                    firstIndexWithSpace = -1
                }
                '.' -> {
                    if (spacesRemaining == 0) {
                        firstIndexWithSpace = y
                    }
                    spacesRemaining += 1
                }
                'O' -> {
                    if (spacesRemaining > 0) {
                        result[firstIndexWithSpace][x] = 'O'
                        result[y][x] = '.'
                        firstIndexWithSpace += 1
                    }
                }
            }
        }
    }
    return result.map { it.joinToString("") }
}

private fun tiltToWest(lines: List<String>): List<String> {
    val result = lines.map{ it.toMutableList() }.toMutableList()
    for(y in result.indices) {
        var spacesRemaining = 0
        var firstIndexWithSpace = -1
        for (x in result.first().indices) {
            when(result[y][x]) {
                '#' -> {
                    spacesRemaining = 0
                    firstIndexWithSpace = -1
                }
                '.' -> {
                    if (spacesRemaining == 0) {
                        firstIndexWithSpace = x
                    }
                    spacesRemaining += 1
                }
                'O' -> {
                    if (spacesRemaining > 0) {
                        result[y][firstIndexWithSpace] = 'O'
                        result[y][x] = '.'
                        firstIndexWithSpace += 1
                    }
                }
            }
        }
    }
    return result.map { it.joinToString("") }
}

private fun tiltToSouth(lines: List<String>): List<String> {
    val result = lines.map{ it.toMutableList() }.toMutableList()
    for(x in result.first().lastIndex downTo 0) {
        var spacesRemaining = 0
        var firstIndexWithSpace = -1
        for (y in result.lastIndex downTo 0) {
            when(result[y][x]) {
                '#' -> {
                    spacesRemaining = 0
                    firstIndexWithSpace = -1
                }
                '.' -> {
                    if (spacesRemaining == 0) {
                        firstIndexWithSpace = y
                    }
                    spacesRemaining += 1
                }
                'O' -> {
                    if (spacesRemaining > 0) {
                        result[firstIndexWithSpace][x] = 'O'
                        result[y][x] = '.'
                        firstIndexWithSpace -= 1
                    }
                }
            }
        }
    }
    return result.map { it.joinToString("") }
}

private fun tiltToEast(lines: List<String>): List<String> {
    val result = lines.map{ it.toMutableList() }.toMutableList()
    for(y in result.lastIndex downTo 0) {
        var spacesRemaining = 0
        var firstIndexWithSpace = -1
        for (x in result.first().lastIndex downTo 0) {
            when(result[y][x]) {
                '#' -> {
                    spacesRemaining = 0
                    firstIndexWithSpace = -1
                }
                '.' -> {
                    if (spacesRemaining == 0) {
                        firstIndexWithSpace = x
                    }
                    spacesRemaining += 1
                }
                'O' -> {
                    if (spacesRemaining > 0) {
                        result[y][firstIndexWithSpace] = 'O'
                        result[y][x] = '.'
                        firstIndexWithSpace -= 1
                    }
                }
            }
        }
    }
    return result.map { it.joinToString("") }
}

private fun cycle(lines: List<String>): List<String> {
    var result = tiltToNorth(lines)
    result = tiltToWest(result)
    result = tiltToSouth(result)
    result = tiltToEast(result)
    return result
}

private fun cycleUntilCRCIsFound(lines: List<String>): Pair<Int, Int> {
    var cycleCount = 0
    val originalCycledOnce = cycle(lines)
    cycleCount += 1
    val hashCodeMap = mutableMapOf<Int, Int>()
    hashCodeMap[originalCycledOnce.hashCode()] = cycleCount
    var repeatingCycled = mutableListOf<String>().also { it.addAll(originalCycledOnce) }.toList()
    while(true) {
        repeatingCycled = cycle(repeatingCycled)
        cycleCount += 1
        val hash = repeatingCycled.hashCode()
        if (hashCodeMap.containsKey(hash)) {
            return Pair(hashCodeMap[hash]!!, cycleCount)
        }
        hashCodeMap[repeatingCycled.hashCode()] = cycleCount
    }
}

private fun calculateRockSum(lines: List<String>): Long {
    var multiplier = lines.size
    var sum = 0L
    for (i in lines.indices) {
        val line = lines[i]
        val amount = line.count { it == 'O' }
        sum += amount * multiplier
        multiplier -= 1
    }
    return sum
}