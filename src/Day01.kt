import java.lang.StringBuilder

fun main() {
    fun part1(input: List<String>): Long {
        var sum = 0L
        input.forEach { line ->
            val filteredNumbers = line.filter { it.isDigit() }
            val firstAndLast ="${filteredNumbers.firstOrNull() ?: '0'}${filteredNumbers.lastOrNull() ?: '0'}"
            sum += firstAndLast.toLong()
        }
        return sum
    }

    fun part2(input: List<String>): Long {
        var sum = 0L
        input.forEach { line ->
            val onlyNumbers = translateToOnlyNumbers(line)
            val firstNumber = onlyNumbers.firstOrNull() ?: '0'
            val lastNumber = onlyNumbers.lastOrNull() ?: '0'
            val number = "${firstNumber}${lastNumber}".toLong()
            sum += number
        }
        return sum
    }

    val input = readInput("Day01")
    part1(input).println()
    part2(input).println()
}

private fun translateToOnlyNumbers(line: String): String {
    val numbers = StringBuilder()
    for (index in line.indices) {
        val charValue = line[index]
        if (charValue.isDigit()) {
            numbers.append(charValue)
        } else {
            NumbersDictionary.keys.forEach { word ->
                if (word.length + index <= line.length) {
                    val wordComparison = line.substring(index, index + word.length)
                    if (wordComparison == word) {
                        numbers.append(NumbersDictionary[word])
                    }
                }
            }
        }
    }
    return numbers.toString()
}

val NumbersDictionary = mapOf(
        "one" to '1',
        "two" to '2',
        "three" to '3',
        "four" to '4',
        "five" to '5',
        "six" to '6',
        "seven" to '7',
        "eight" to '8',
        "nine" to '9',
)