fun main() {
    fun part1(input: List<String>): Long {
        val listOfSeeds = readListOfSeeds(input)
        val allMaps = mutableListOf<MutableList<MapRanges>>()

        var mapLevel = -1
        for (i in 2..input.lastIndex) {
            val currentLine = input[i]
            if (currentLine.contains("map:")) {
                mapLevel += 1
                allMaps.add(mutableListOf())
            } else if (currentLine.isNotEmpty() && currentLine.first().isDigit()) {
                val currentList = allMaps[mapLevel]
                populateListWithCurrentLine(currentList, currentLine)
            }
        }
        var lowestLocation = Long.MAX_VALUE
        listOfSeeds.forEach { seed ->
            val location = mapSeedToLocation(seed, allMaps)
            println("seed: $seed, location: $location")
            if (location < lowestLocation) {
                lowestLocation = location
            }
        }
        return lowestLocation
    }

    fun part2(input: List<String>): Long {
        val seedLineParsed = readListOfSeeds(input)
        val allMaps = mutableListOf<MutableList<MapRanges>>()

        var mapLevel = -1
        for (i in 2..input.lastIndex) {
            val currentLine = input[i]
            if (currentLine.contains("map:")) {
                mapLevel += 1
                allMaps.add(mutableListOf())
            } else if (currentLine.isNotEmpty() && currentLine.first().isDigit()) {
                val currentList = allMaps[mapLevel]
                populateListWithCurrentLine(currentList, currentLine)
            }
        }
        var lowestLocation = Long.MAX_VALUE

        seedLineParsed.chunked(2).forEach { seedInfo ->
            val start = seedInfo.first()
            val length = seedInfo.last()
            for (seed in start..<start + length) {
                val location = mapSeedToLocation(seed, allMaps)
                //println("seed: $seed, location: $location")
                if (location < lowestLocation) {
                    lowestLocation = location
                }
            }
        }
        return lowestLocation
    }

    val input = readInput("Day05")
    part1(input).println()
    part2(input).println()
}

private fun readListOfSeeds(input: List<String>) = input[0]
        .removePrefix("seeds: ")
        .split(" ")
        .filterNot { it == "" }
        .map { it.toLong() }


private fun populateListWithCurrentLine(mapRangeList: MutableList<MapRanges>, line: String) {
    val parsedLine = line.split(' ').map { it.toLong() }
    val destinationStart = parsedLine.first()
    val sourceStart = parsedLine[1]
    val length = parsedLine.last()
    mapRangeList.add(MapRanges(sourceStart, destinationStart, length))
}

private fun mapSeedToLocation(seed: Long, allMaps: List<List<MapRanges>>): Long {
    var result = seed
    allMaps.forEach { currentSet ->
        for (i in currentSet.indices) {
            val mapRangeInfo = currentSet[i]
            if (result >= mapRangeInfo.sourceStart && result < mapRangeInfo.sourceStart + mapRangeInfo.length) {
                val delta = mapRangeInfo.destinationStart - mapRangeInfo.sourceStart
                result += delta
                break
            }
        }
    }
    return result
}

data class MapRanges(
        val sourceStart: Long,
        val destinationStart: Long,
        val length: Long,
)