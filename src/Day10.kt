fun main() {
    fun part1(input: List<String>): Int {
        val startingPosition = findStartingPosition(input)
        val resultToNorth = evaluatePath(input, startingPosition, Directions.North).size
        if (resultToNorth > 0) {
            return resultToNorth/2
        }

        val resultToSouth = evaluatePath(input, startingPosition, Directions.South).size
        if (resultToSouth > 0) {
            return resultToSouth/2
        }

        val resultToWest = evaluatePath(input, startingPosition, Directions.West).size
        if (resultToWest > 0) {
            return resultToWest/2
        }

        val resultToEast = evaluatePath(input, startingPosition, Directions.East).size
        if (resultToEast > 0) {
            return resultToEast/2
        }
        return -1
    }

    fun part2(input: List<String>): Int {
        val startingPosition = findStartingPosition(input)
        val pipePoints = evaluatePath(input, startingPosition, Directions.North)
        val pointsInside = getPointsInside(input, pipePoints)
        return pointsInside
    }

    val input = readInput("Day10")
    part1(input).println()
    part2(input).println()
}


private fun findStartingPosition(map: List<String>): Pair<Int, Int> {
    for (y in map.indices) {
        val line = map[y]
        for (x in line.indices) {
            val char = line[x]
            if (char == 'S') {
                return Pair(x, y)
            }
        }
    }
    println("starting position of the animal not found")
    return Pair(0, 0)
}

private fun readPosition(map: List<String>, position: Pair<Int, Int>): Char {
    return map[position.second][position.first]
}

private fun evaluatePath(map: List<String>, startingPosition: Pair<Int, Int>, direction: Directions): List<Pair<Int, Int>> {
    val points = mutableListOf<Pair<Int, Int>>()
    var currentPosition = startingPosition.copy()
    var currentDirection = direction
    while(true) {
        when(currentDirection) {
            Directions.North -> {
                if (currentPosition.second == 0) {
                    return emptyList()
                }
                currentPosition = currentPosition.copy(second = currentPosition.second - 1)
                when(readPosition(map, currentPosition)) {
                    'S' -> {
                        points.add(currentPosition)
                        return points
                    }
                    '|' -> {
                        points.add(currentPosition)
                    }
                    '7' -> {
                        points.add(currentPosition)
                        currentDirection = Directions.West
                    }
                    'F' -> {
                        points.add(currentPosition)
                        currentDirection = Directions.East
                    }
                    else -> return emptyList()
                }
            }
            Directions.South -> {
                if (currentPosition.second == map.lastIndex) {
                    return emptyList()
                }
                currentPosition = currentPosition.copy(second = currentPosition.second + 1)
                when(readPosition(map, currentPosition)) {
                    'S' -> {
                        points.add(currentPosition)
                        return points
                    }
                    '|' -> {
                        points.add(currentPosition)
                    }
                    'L' -> {
                        points.add(currentPosition)
                        currentDirection = Directions.East
                    }
                    'J' -> {
                        points.add(currentPosition)
                        currentDirection = Directions.West
                    }
                    else -> return emptyList()
                }
            }
            Directions.West -> {
                if (currentPosition.first == 0) {
                    return emptyList()
                }
                currentPosition = currentPosition.copy(first = currentPosition.first - 1)
                when(readPosition(map, currentPosition)) {
                    'S' -> {
                        points.add(currentPosition)
                        return points
                    }
                    '-' -> {
                        points.add(currentPosition)
                    }
                    'L' -> {
                        points.add(currentPosition)
                        currentDirection = Directions.North
                    }
                    'F' -> {
                        points.add(currentPosition)
                        currentDirection = Directions.South
                    }
                    else -> return emptyList()
                }
            }
            Directions.East -> {
                if (currentPosition.first == map.first().lastIndex) {
                    return emptyList()
                }
                currentPosition = currentPosition.copy(first = currentPosition.first + 1)
                when(readPosition(map, currentPosition)) {
                    'S' -> {
                        points.add(currentPosition)
                        return points
                    }
                    '-' -> {
                        points.add(currentPosition)
                    }
                    'J' -> {
                        points.add(currentPosition)
                        currentDirection = Directions.North
                    }
                    '7' -> {
                        points.add(currentPosition)
                        currentDirection = Directions.South
                    }
                    else -> return emptyList()
                }
            }
        }
    }
}

private fun getPointsInside(map: List<String>, polygon: List<Pair<Int, Int>>): Int {
    /**
     * valid pipes are the ones that connects through north to either south, east or west
     */
    val validPipes = "S|LJ"
    var points = 0
    for(y in map.indices) {
        var isInside = false
        val line = map[y]
        for(x in line.indices) {
            val char = line[x]
            val currentPosition = Pair(x,y)
            if (currentPosition in polygon && char in validPipes) {
                isInside = isInside.not()
            }
            if (currentPosition !in polygon && isInside) {
                points += 1
            }
        }
    }
    return points
}

enum class Directions {
    North, South, West, East
}