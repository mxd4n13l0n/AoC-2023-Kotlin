fun main() {

    fun part1(input: List<String>): Long {
        val universe = input
        val horizontalExpansionIndices = getHorizontalExpansionIndices(universe)
        val verticalExpansionIndices = getVerticalExpansionIndices(universe)
        val galaxies = readGalaxies(universe)
        val galaxiesDistances = mutableListOf<Long>()
        for (i in 0..<galaxies.lastIndex) {
            for (j in i + 1 .. galaxies.lastIndex) {
                val path = calculateShortestPathWithExpansionIndices(
                        galaxies[i],
                        galaxies[j],
                        horizontalExpansionIndices,
                        verticalExpansionIndices,
                        2,
                )
                galaxiesDistances.add(path)
            }
        }
        val sum = galaxiesDistances.sum()
        return sum
    }

    fun part2(input: List<String>): Long {
        val universe = input
        val horizontalExpansionIndices = getHorizontalExpansionIndices(universe)
        val verticalExpansionIndices = getVerticalExpansionIndices(universe)
        val galaxies = readGalaxies(universe)
        val galaxiesDistances = mutableListOf<Long>()
        for (i in 0..<galaxies.lastIndex) {
            for (j in i + 1 .. galaxies.lastIndex) {
                val path = calculateShortestPathWithExpansionIndices(
                        galaxies[i],
                        galaxies[j],
                        horizontalExpansionIndices,
                        verticalExpansionIndices,
                        1000000,
                )
                galaxiesDistances.add(path)
            }
        }
        val sum = galaxiesDistances.sum()
        return sum
    }

    val input = readInput("Day11")
    part1(input).println()
    part2(input).println()
}


private fun getHorizontalExpansionIndices(map: List<String>): List<Int> {
    val horizontalExpansion = mutableListOf<Int>()
    for (i in 0..map.first().lastIndex) {
        val allFromColumn = map.map { it[i] }
        if (allFromColumn.count { it == '#' } == 0 ) {
            horizontalExpansion.add(i)
        }
    }
    return horizontalExpansion
}

private fun getVerticalExpansionIndices(map: List<String>): List<Int> {
    val verticalExpansion = mutableListOf<Int>()
    for (i in 0..map.lastIndex) {
        val allFromRow = map[i]
        if (allFromRow.count { it == '#' } == 0 ) {
            verticalExpansion.add(i)
        }
    }
    return verticalExpansion
}

private fun readGalaxies(map: List<String>): List<Pair<Int, Int>> {
    val galaxies = mutableListOf<Pair<Int, Int>>()
    for (y in map.indices) {
        for (x in map[y].indices) {
            if (map[y][x] == '#') {
                galaxies.add(Pair(x, y))
            }
        }
    }
    return galaxies
}

private fun calculateShortestPathWithExpansionIndices(
        pair1: Pair<Int, Int>,
        pair2: Pair<Int, Int>,
        horizontalExpansion: List<Int>,
        verticalExpansion: List<Int>,
        multiplier: Int,
): Long {
    var amount = 0L
    val x1 = pair1.first.coerceAtMost(pair2.first)
    val x2 = pair1.first.coerceAtLeast(pair2.first)
    for (x in x1 + 1..x2) {
        amount += if (x in horizontalExpansion) {
            multiplier
        } else {
            1
        }
    }
    val y1 = pair1.second.coerceAtMost(pair2.second)
    val y2 = pair1.second.coerceAtLeast(pair2.second)
    for (y in y1 + 1..y2) {
        amount += if (y in verticalExpansion) {
            multiplier
        } else {
            1
        }
    }
    return amount
}