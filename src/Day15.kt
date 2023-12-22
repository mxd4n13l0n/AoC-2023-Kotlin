fun main() {

    fun part1(input: List<String>): Long {
        var sum = 0L
        input.forEach { line ->
            line.split(',').forEach { item ->
                sum += calculateHash(item)
            }
        }
        return sum
    }

    fun part2(input: List<String>): Long {
        val boxes = mutableListOf<MutableMap<String, Int>>()
        for (i in 0..255) {
            boxes.add(mutableMapOf())
        }
        input.forEach { line ->
            line.split(',').forEach { item ->
                executeOperation(boxes, item)
            }
        }
        val sum = calculateFocusingPower(boxes)
        return sum
    }

    val input = readInput("Day15")
    part1(input).println()
    part2(input).println()
}


private fun calculateHash(input: String): Int {
    var currentValue = 0
    input.forEach { character ->
        val ascii = character.code
        currentValue += ascii
        currentValue *= 17
        currentValue %= 256
    }
    return currentValue
}

private fun executeOperation(boxes: MutableList<MutableMap<String, Int>>, operation: String) {
    when {
        operation.contains('-') -> {
            val label = operation.removeSuffix("-")
            executeDashOperation(boxes, label)
        }
        operation.contains('=') -> {
            val operationSplit = operation.split('=')
            executeEqualsOperation(boxes, operationSplit.first(), operationSplit.last().toInt())
        }
    }
}

private fun executeDashOperation(boxes: MutableList<MutableMap<String, Int>>, label: String) {
    val boxIndex = calculateHash(label)
    val relevantBox = boxes[boxIndex]
    relevantBox.remove(label)

}

private fun executeEqualsOperation(boxes: MutableList<MutableMap<String, Int>>, label: String, value: Int) {
    val boxIndex = calculateHash(label)
    val relevantBox = boxes[boxIndex]
    relevantBox[label] = value
}

private fun calculateFocusingPower(boxes: MutableList<MutableMap<String, Int>>): Long {
    var sum = 0L
    for (boxNumber in boxes.indices) {
        val box = boxes[boxNumber]
        var slotNumber = 0
        box.forEach { (_, focalLength) ->
            slotNumber += 1
            val result = (boxNumber + 1) * slotNumber * focalLength
            sum += result
        }
    }
    return sum
}