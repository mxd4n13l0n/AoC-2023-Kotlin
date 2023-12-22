fun main() {

    fun part1(input: List<String>): Int {
        val result = runSimulation(Beam(Direction.Right, Pair(-1,0)), input)
        return result
    }

    fun part2(input: List<String>): Int {
        val resultSet = mutableSetOf<Int>()
        for (verticalIndex in input.indices) {
            val rightBeam = Beam(Direction.Right, Pair(-1, verticalIndex))
            resultSet.add(runSimulation(rightBeam, input))
            val leftBeam = Beam(Direction.Left, Pair(input.first().length, verticalIndex))
            resultSet.add(runSimulation(leftBeam, input))
        }
        for (horizontalIndex in input.first().indices) {
            val upBeam = Beam(Direction.Up, Pair(horizontalIndex, input.size))
            resultSet.add(runSimulation(upBeam, input))
            val downBeam = Beam(Direction.Down, Pair(horizontalIndex, -1))
            resultSet.add(runSimulation(downBeam, input))
        }
        return resultSet.max()
    }

    val input = readInput("Day16")
    part1(input).println()
    part2(input).println()
}


private fun runSimulation(startingBeam: Beam, map: List<String>): Int {
    val beams = mutableSetOf(startingBeam)
    val energized = mutableSetOf<String>()
    val rememberCollision = mutableSetOf<String>()
    val horizontalBound = IntRange(0, map.first().lastIndex)
    val verticalBound = IntRange(0, map.lastIndex)
    while (beams.isNotEmpty()) {
        simulateBeams(map, beams, energized, rememberCollision, horizontalBound, verticalBound)
    }
    return energized.size
}

private fun simulateBeams(map: List<String>, beams: MutableSet<Beam>, energized: MutableSet<String>, rememberCollision: MutableSet<String>, horizontalBound: IntRange, verticalBound: IntRange) {
    val beamsToBeRemoved = mutableSetOf<Beam>()
    val beamsToBeAdded = mutableSetOf<Beam>()
    beams.forEach { beam ->
        beam.move()
        if (beam.isOutOfBounds(horizontalBound, verticalBound)) {
            beamsToBeRemoved.add(beam)
        } else {
            val hashCodeEnergized = "${beam.coordinates.first}-${beam.coordinates.second}"
            energized.add(hashCodeEnergized)
            val character = map[beam.coordinates.second][beam.coordinates.first]
            colliders[character]?.collide(beam)?.let { resultingBeams ->
                resultingBeams.forEach { newBeam ->
                    val hashCodeBeamCollided = "${newBeam.coordinates.first}-${newBeam.coordinates.second}-${newBeam.direction}"
                    if (rememberCollision.contains(hashCodeBeamCollided)) {
                        beamsToBeRemoved.add(newBeam)
                    } else {
                        rememberCollision.add(hashCodeBeamCollided)
                        beamsToBeAdded.add(newBeam)
                    }
                }
                beamsToBeRemoved.add(beam)
            }
        }
    }
    beams.removeAll(beamsToBeRemoved)
    beams.addAll(beamsToBeAdded)
}

data class Beam(
        var direction: Direction,
        var coordinates: Pair<Int, Int>,
) {
    fun move() {
        coordinates = when(direction) {
            Direction.Right -> Pair(coordinates.first + 1, coordinates.second)
            Direction.Left -> Pair(coordinates.first - 1, coordinates.second)
            Direction.Up -> Pair(coordinates.first, coordinates.second - 1)
            Direction.Down -> Pair(coordinates.first, coordinates.second + 1)
        }
    }

    fun isOutOfBounds(horizontalBound: IntRange, verticalBound: IntRange): Boolean {
        if (horizontalBound.contains(coordinates.first) && verticalBound.contains(coordinates.second)) {
            return false
        }
        return true
    }
}

interface Collider {
    fun collide(beam: Beam): List<Beam>
}

class ForwardSlash: Collider {
    override fun collide(beam: Beam): List<Beam> {
        return when(beam.direction) {
            Direction.Right -> {
                beam.direction = Direction.Up
                listOf(beam)
            }
            Direction.Left -> {
                beam.direction = Direction.Down
                listOf(beam)
            }
            Direction.Up -> {
                beam.direction = Direction.Right
                listOf(beam)
            }
            Direction.Down -> {
                beam.direction = Direction.Left
                listOf(beam)
            }
        }
    }
}

class BackwardSlash: Collider {
    override fun collide(beam: Beam): List<Beam> {
        return when(beam.direction) {
            Direction.Right -> {
                beam.direction = Direction.Down
                listOf(beam)
            }
            Direction.Left -> {
                beam.direction = Direction.Up
                listOf(beam)
            }
            Direction.Up -> {
                beam.direction = Direction.Left
                listOf(beam)
            }
            Direction.Down -> {
                beam.direction = Direction.Right
                listOf(beam)
            }
        }
    }
}

class HorizontalLine: Collider {
    override fun collide(beam: Beam): List<Beam> {
        return when(beam.direction) {
            Direction.Left, Direction.Right -> {
                listOf(beam)
            }
            Direction.Up, Direction.Down -> {
                listOf(
                        Beam(Direction.Left, beam.coordinates),
                        Beam(Direction.Right, beam.coordinates),
                )
            }
        }
    }
}

class VerticalLine: Collider {
    override fun collide(beam: Beam): List<Beam> {
        return when(beam.direction) {
            Direction.Left, Direction.Right -> {
                listOf(
                        Beam(Direction.Up, beam.coordinates),
                        Beam(Direction.Down, beam.coordinates),
                )
            }
            Direction.Up, Direction.Down -> {
                listOf(beam)
            }
        }
    }
}

class NoCollider: Collider {
    override fun collide(beam: Beam): List<Beam> = listOf(beam)
}

private val colliders = mapOf(
        '.' to NoCollider(),
        '/' to ForwardSlash(),
        '\\' to BackwardSlash(),
        '|' to VerticalLine(),
        '-' to HorizontalLine(),
)


enum class Direction {
    Up,
    Down,
    Left,
    Right
}