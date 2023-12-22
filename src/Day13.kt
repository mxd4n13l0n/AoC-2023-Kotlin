fun main() {

    fun part1(input: List<String>): Int {
        val notes = parseNotes(input)
        val verticalSum = notes.map { calculateVerticalReflection(it) }.filterNot { it == -1 }.sum()
        val horizontalSum =
                notes.map { calculateHorizontalReflection(it) }.filterNot { it == -1 }
                        .sumOf { it * 100 }
        return verticalSum + horizontalSum
    }

    fun part2(input: List<String>): Int {
        val notes = parseNotes(input)
        val verticalSum = notes.map { calculateVerticalReflection(it, 1) }.filterNot { it == -1 }.sum()
        val horizontalSum =
                notes.map { calculateHorizontalReflection(it, 1) }.filterNot { it == -1 }
                        .sumOf { it * 100 }
        return verticalSum + horizontalSum
    }

    val input = readInput("Day13")
    part1(input).println()
    part2(input).println()
}


private fun parseNotes(lines: List<String>): List<List<String>> {
    val notes = mutableListOf<List<String>>()
    var currentNote = mutableListOf<String>()
    lines.forEach { line ->
        if (line.isNotEmpty()) {
            currentNote.add(line)
        } else {
            notes.add(currentNote)
            currentNote = mutableListOf()
        }
    }
    notes.add(currentNote)
    return notes
}

private fun calculateVerticalReflection(note: List<String>, withSmudgeCount: Int = 0): Int {
    var perfectReflectionIndex = -1
    val midIndex = if (note.first().length % 2 == 0) note.first().length / 2 - 1 else note.first().length / 2
    var leftIndex = midIndex - 1
    var rightIndex = midIndex + 1
    var bestWeight = -1
    while (leftIndex >= 0 && rightIndex <= note.first().lastIndex) {
        var weight = calculateVerticalMirrorWeight(note, rightIndex -1, rightIndex, withSmudgeCount)
        if (weight > bestWeight) {
            bestWeight = weight
            perfectReflectionIndex = rightIndex
        }
        weight = calculateVerticalMirrorWeight(note, leftIndex, leftIndex + 1, withSmudgeCount)
        if (weight > bestWeight) {
            bestWeight = weight
            perfectReflectionIndex = leftIndex + 1
        }
        leftIndex -= 1
        rightIndex += 1
    }
    return perfectReflectionIndex
}

private fun calculateHorizontalReflection(note: List<String>, withSmudgeCount: Int = 0): Int {
    var perfectReflectionIndex = -1
    val midIndex = if (note.size % 2 == 0) note.size / 2 - 1 else note.size / 2
    var upperIndex = midIndex - 1
    var lowerIndex = midIndex + 1
    var bestWeight = -1
    while (upperIndex >= 0 && lowerIndex <= note.lastIndex) {
        var weight = calculateHorizontalMirrorWeight(note, lowerIndex -1, lowerIndex, withSmudgeCount)
        if (weight > bestWeight) {
            bestWeight = weight
            perfectReflectionIndex = lowerIndex
        }
        weight = calculateHorizontalMirrorWeight(note, upperIndex, upperIndex + 1, withSmudgeCount)
        if (weight > bestWeight) {
            bestWeight = weight
            perfectReflectionIndex = upperIndex + 1
        }
        upperIndex -= 1
        lowerIndex += 1
    }
    return perfectReflectionIndex
}

private fun calculateVerticalMirrorWeight(note: List<String>, index1: Int, index2: Int, withSmudgeCount: Int = 0): Int {
    var leftIndex = index1
    var rightIndex = index2
    var weight = -1
    var smudgeCount = 0
    while (leftIndex >= 0 && rightIndex <= note.first().lastIndex) {
        smudgeCount += calculateSmudgesForColumns(note, leftIndex, rightIndex)
        if (smudgeCount <= withSmudgeCount) {
            leftIndex -= 1
            rightIndex += 1
            weight += 1
        } else {
            return -1
        }
    }
    return if (smudgeCount == withSmudgeCount) {
        weight
    } else {
        -1
    }
}

private fun calculateHorizontalMirrorWeight(note: List<String>, index1: Int, index2: Int, withSmudgeCount: Int = 0): Int {
    var upperIndex = index1
    var lowerIndex = index2
    var weight = -1
    var smudgeCount = 0
    while (upperIndex >= 0 && lowerIndex <= note.lastIndex) {
        smudgeCount += calculateSmudgesForRows(note, upperIndex, lowerIndex)
        if (smudgeCount <= withSmudgeCount) {
            upperIndex -= 1
            lowerIndex += 1
            weight += 1
        } else {
            return -1
        }
    }
    return if (smudgeCount == withSmudgeCount) {
        weight
    } else {
        -1
    }
}

private fun calculateSmudgesForColumns(note: List<String>, index1: Int, index2: Int): Int {
    var smudges = 0
    val list1 = note.map { it[index1] }
    val list2 = note.map { it[index2] }
    for(i in list1.indices) {
        if (list1[i] != list2[i]) {
            smudges += 1
        }
    }
    return smudges
}

private fun calculateSmudgesForRows(note: List<String>, index1: Int, index2: Int): Int {
    var smudges = 0
    val list1 = note[index1]
    val list2 = note[index2]
    for(i in list1.indices) {
        if (list1[i] != list2[i]) {
            smudges += 1
        }
    }
    return smudges
}