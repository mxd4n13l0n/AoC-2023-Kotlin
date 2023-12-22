fun main() {
    fun part1(input: List<String>): Long {
        var sum = 0L
        input.forEach { line  ->
            sum += extrapolateAndCalculateNext(line.split(' ').map { it.toInt()})
        }
        return sum
    }

    fun part2(input: List<String>): Long {
        var sum = 0L
        input.forEach { line  ->
            sum += extrapolateAndCalculatePrevious(line.split(' ').map { it.toInt()})
        }
        return sum
    }

    val input = readInput("Day09")
    part1(input).println()
    part2(input).println()
}

private fun extrapolateAndCalculateNext(numbers: List<Int>): Int {
    var currentList = numbers
    val matrix = mutableListOf<List<Int>>()
    matrix.add(currentList)
    while (currentList.all { it == 0 }.not()) {
        currentList = calculateDifferences(currentList)
        matrix.add(currentList)
    }
    var newNumber = 0
    for (i in matrix.lastIndex -1 downTo 0)  {
        newNumber += matrix[i].last()
    }
    return newNumber
}

private fun extrapolateAndCalculatePrevious(numbers: List<Int>): Int {
    var currentList = numbers
    val matrix = mutableListOf<List<Int>>()
    matrix.add(currentList)
    while (currentList.all { it == 0 }.not()) {
        currentList = calculateDifferences(currentList)
        matrix.add(currentList)
    }
    var newNumber = 0
    for (i in matrix.lastIndex -1 downTo 0)  {
        newNumber = matrix[i].first() - newNumber
    }
    return newNumber
}

private fun calculateDifferences(number: List<Int>): List<Int> {
    val result = mutableListOf<Int>()
    for (i in 0..<number.lastIndex) {
        result.add(number[i + 1] - number[i])
    }
    return result
}