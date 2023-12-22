import kotlin.math.abs

@OptIn(ExperimentalStdlibApi::class)
fun main() {

    fun part1(input: List<String>): Long {
        val instructions = input.map { line ->
            val split = line.split(" ")
            val direction = when(split.first()) {
                "U" -> Day18.Direction.Up
                "D" -> Day18.Direction.Down
                "L" -> Day18.Direction.Left
                else -> Day18.Direction.Right
            }
            val amount = split[1].toInt()
            val color = ""
            Day18.Instruction(direction, amount, color)
        }
        val polygon = readPolygonVertices(instructions)
        return shoelaceFormula(polygon) + perimeter(polygon) + 1
    }

    fun part2(input: List<String>): Long {
        val instructions = input.map { line ->
            val split = line.split(" ")
            val realInstruction = split.last().removeSurrounding("(#", ")")
            val amount = realInstruction.substring(0, 5).hexToInt()
            val direction = when(realInstruction.last()) {
                '0' -> Day18.Direction.Right
                '1' -> Day18.Direction.Down
                '2' -> Day18.Direction.Left
                else -> Day18.Direction.Up
            }
            val color = ""
            Day18.Instruction(direction, amount, color)
        }
        val polygon = readPolygonVertices(instructions)
        return shoelaceFormula(polygon) + perimeter(polygon) + 1
    }

    val input = readInput("Day18")
    part1(input).println()
    part2(input).println()
}

private fun readPolygonVertices(data: List<Day18.Instruction>): List<Pair<Long, Long>> {
    val resultList = mutableListOf<Pair<Long, Long>>()
    var x = 0L
    var y = 0L
    data.forEach { instruction ->
        when(instruction.direction) {
            Day18.Direction.Up -> y -= instruction.amount
            Day18.Direction.Down -> y += instruction.amount
            Day18.Direction.Left -> x -= instruction.amount
            Day18.Direction.Right -> x += instruction.amount
        }
        resultList.add(Pair(x, y))
    }
    return resultList
}

private fun shoelaceFormula(points: List<Pair<Long, Long>>): Long {
    var sum = 0L
    var lastPoint = points.last()
    for (i in points.indices) {
        val currentPoint =  points[i]
        sum = sum + (lastPoint.first * currentPoint.second) - (lastPoint.second * currentPoint.first)
        lastPoint = currentPoint
    }
    return abs(sum) / 2
}

private fun perimeter(points: List<Pair<Long, Long>>): Long {
    var sum = 0L
    var lastPoint = points.last()
    for (i in points.indices) {
        val currentPoint =  points[i]
        sum += abs(lastPoint.first - currentPoint.first) + abs(lastPoint.second - currentPoint.second)
        lastPoint = currentPoint
    }
    return sum / 2
}

sealed class Day18 {
    data class Instruction(
            val direction: Direction,
            val amount: Int,
            val hexCode: String,
    )


    enum class Direction {
        Up, Down, Left, Right
    }
}
