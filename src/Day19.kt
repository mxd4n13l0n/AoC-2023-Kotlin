fun main() {

    fun part1(input: List<String>): Int {
        var readEmptyLineFlag = true
        val workflows = mutableMapOf<String, List<Statement>>()
        val machineParts = mutableSetOf<MachinePart>()
        input.forEach { line ->
            if (line.isEmpty()) {
                readEmptyLineFlag = false
            } else {
                when (readEmptyLineFlag) {
                    true -> {
                        val workflowLine = parseWorkflowLine(line)
                        workflows[workflowLine.first] = workflowLine.second
                    }
                    false -> {
                        val machinePart = parseMachinePart(line)
                        machineParts.add(machinePart)
                    }
                }
            }
        }
        val acceptedMachineParts = mutableSetOf<MachinePart>()
        machineParts.forEach { machinePart ->
            val result = runProgram(machinePart, workflows)
            if (result == "A") {
                acceptedMachineParts.add(machinePart)
            }
        }
        val sumAll = acceptedMachineParts.sumOf { it.sumAll() }
        return sumAll
    }

    fun part2(input: List<String>): Long {
        val workflows = mutableMapOf<String, List<Statement>>()
        for (i in input.indices) {
            val line = input[i]
            if (line.isEmpty()) {
                break
            } else {
                val workflowLine = parseWorkflowLine(line)
                workflows[workflowLine.first] = workflowLine.second
            }
        }
        val xmasAcceptedNumbers = mutableListOf<AcceptedRanges>()
        calculateAllAcceptedWorkflows(workflows, xmasAcceptedNumbers)
        val products = xmasAcceptedNumbers.map { 1L * it.xRange.count() * it.mRange.count() * it.aRange.count() * it.sRange.count() }
        return products.sum()
    }

    val input = readInput("Day19")
    part1(input).println()
    part2(input).println()
}


/**
 * First element of the pair is for when the statement is true
 * Second element for when the statement is false
 */
private fun splitRanges(range: AcceptedRanges, statement: Statement.ConditionalStatement): Pair<AcceptedRanges, AcceptedRanges> {
    val oldRange = when(statement.variable) {
        Variable.X -> range.xRange
        Variable.M -> range.mRange
        Variable.A -> range.aRange
        Variable.S -> range.sRange
    }
    val newIntRange = when (statement.operand) {
        Operand.LessThan -> Pair(IntRange(oldRange.first, statement.value - 1), IntRange(statement.value, oldRange.last))
        Operand.GreaterThan -> Pair(IntRange(statement.value + 1, oldRange.last), IntRange(oldRange.first, statement.value))
    }
    val newAcceptedRangePair = when(statement.variable) {
        Variable.X -> Pair(
                AcceptedRanges(newIntRange.first, range.mRange, range.aRange, range.sRange),
                AcceptedRanges(newIntRange.second, range.mRange, range.aRange, range.sRange)
        )
        Variable.M -> Pair(
                AcceptedRanges(range.xRange, newIntRange.first, range.aRange, range.sRange),
                AcceptedRanges(range.xRange, newIntRange.second, range.aRange, range.sRange)
        )
        Variable.A -> Pair(
                AcceptedRanges(range.xRange, range.mRange, newIntRange.first, range.sRange),
                AcceptedRanges(range.xRange, range.mRange, newIntRange.second, range.sRange)
        )
        Variable.S -> Pair(
                AcceptedRanges(range.xRange, range.mRange, range.aRange, newIntRange.first),
                AcceptedRanges(range.xRange, range.mRange, range.aRange, newIntRange.second)
        )
    }
    return newAcceptedRangePair
}

private fun runProgram(machinePart: MachinePart, workflows: Map<String, List<Statement>>): String {
    var nextWorkflow: String? = "in"
    while (nextWorkflow != null) {
        when (val nextInstruction = runAgainstStatementList(machinePart, workflows[nextWorkflow]!!)) {
            is Instruction.MoveTo -> nextWorkflow = nextInstruction.destination
            is Instruction.Accept -> return "A"
            is Instruction.Reject -> return "R"
        }
    }
    return "?"
}

private fun calculateAllAcceptedWorkflows(workflows: Map<String, List<Statement>>, acceptedRanges: MutableList<AcceptedRanges>, currentRange: AcceptedRanges = AcceptedRanges(), nextStatement: String = "in") {
    val currentStatement = workflows[nextStatement]!!
    var pendingAcceptedRange = AcceptedRanges()
    currentStatement.forEach { statement ->
        when (statement) {
            is Statement.ConditionalStatement -> {
                val newCurrentRange = intersectAcceptedRanges(currentRange, pendingAcceptedRange)
                val splitSet = splitRanges(newCurrentRange, statement)
                val trueRange = splitSet.first
                val falseRange = splitSet.second
                pendingAcceptedRange = falseRange
                when (statement.instruction) {
                    is Instruction.Accept -> {
                        acceptedRanges.add(trueRange)
                    }
                    is Instruction.MoveTo -> {
                        calculateAllAcceptedWorkflows(workflows, acceptedRanges, trueRange, statement.instruction.destination)
                    }
                    is Instruction.Reject -> { /* do nothing */ }
                }
            }
            is Statement.InstructionalStatement -> {
                val nextRange = intersectAcceptedRanges(currentRange, pendingAcceptedRange)
                when (statement.instruction) {
                    is Instruction.Accept -> {
                        acceptedRanges.add(nextRange)
                    }
                    is Instruction.MoveTo -> {
                        calculateAllAcceptedWorkflows(workflows, acceptedRanges, nextRange, statement.instruction.destination)
                    }
                    is Instruction.Reject -> { /* do nothing */ }
                }
            }
        }
    }
}

private fun intersectAcceptedRanges(ac1: AcceptedRanges, ac2: AcceptedRanges): AcceptedRanges {
    val x = IntRange(
            maxOf(ac1.xRange.first, ac2.xRange.first),
            minOf(ac1.xRange.last, ac2.xRange.last),
    )
    val m = IntRange(
            maxOf(ac1.mRange.first, ac2.mRange.first),
            minOf(ac1.mRange.last, ac2.mRange.last),
    )
    val a = IntRange(
            maxOf(ac1.aRange.first, ac2.aRange.first),
            minOf(ac1.aRange.last, ac2.aRange.last),
    )
    val s = IntRange(
            maxOf(ac1.sRange.first, ac2.sRange.first),
            minOf(ac1.sRange.last, ac2.sRange.last),
    )
    return AcceptedRanges(x,m,a,s)
}



private fun runAgainstStatementList(machinePart: MachinePart, statements: List<Statement>): Instruction {
    return statements.first { statement -> isStatementTrue(machinePart, statement) }.instruction
}

private fun isStatementTrue(machinePart: MachinePart, statement: Statement): Boolean {
    return when(statement) {
        is Statement.ConditionalStatement -> {
            when (statement.operand) {
                Operand.GreaterThan -> readVariable(machinePart, statement.variable) > statement.value
                Operand.LessThan -> readVariable(machinePart, statement.variable) < statement.value
            }
        }
        is Statement.InstructionalStatement -> true
    }
}

private fun readVariable(machinePart: MachinePart, variable: Variable) = when(variable) {
    Variable.X -> machinePart.x
    Variable.M -> machinePart.m
    Variable.A -> machinePart.a
    Variable.S -> machinePart.s
}

private fun parseWorkflowLine(line: String): Pair<String, List<Statement>> {
    val split = line.removeSuffix("}").split("{")
    val workflowName = split.first()
    val statementList = split.last().split(",").map { parseStatement(it) }
    return Pair(workflowName, statementList)
}

private fun parseStatement(line: String): Statement {
    when(line.contains(":")) {
        true -> {
            val splitLine = line.split(":")
            val instruction = parseInstruction(splitLine.last())
            val secondSplit = splitLine.first()
            return if (secondSplit.contains("<")) {
                val operand = Operand.LessThan
                val thirdSplit = secondSplit.split("<")
                val variable = parseVariable(thirdSplit.first())
                val value = thirdSplit.last().toInt()
                Statement.ConditionalStatement(variable, operand, value, instruction)
            } else {
                val operand = Operand.GreaterThan
                val thirdSplit = secondSplit.split(">")
                val variable = parseVariable(thirdSplit.first())
                val value = thirdSplit.last().toInt()
                Statement.ConditionalStatement(variable, operand, value, instruction)
            }
        }
        false -> {
            val instruction = parseInstruction(line)
            return Statement.InstructionalStatement(instruction)
        }
    }
}

private fun parseInstruction(line: String) = when {
    line.contains("A") -> Instruction.Accept
    line.contains("R") -> Instruction.Reject
    else -> Instruction.MoveTo(line)
}

private fun parseVariable(line: String) = when(line) {
    "x" -> Variable.X
    "m" -> Variable.M
    "a" -> Variable.A
    else -> Variable.S
}

private fun parseMachinePart(line: String): MachinePart {
    val splitLine = line.removeSurrounding("{", "}").split(",")
    return MachinePart(
            x = splitLine[0].split("=").last().toInt(),
            m = splitLine[1].split("=").last().toInt(),
            a = splitLine[2].split("=").last().toInt(),
            s = splitLine[3].split("=").last().toInt(),
    )
}

data class AcceptedRanges(
        val xRange: IntRange = IntRange(1, 4000),
        val mRange: IntRange = IntRange(1, 4000),
        val aRange: IntRange = IntRange(1, 4000),
        val sRange: IntRange = IntRange(1, 4000),
)
data class MachinePart(
        val x: Int,
        val m: Int,
        val a: Int,
        val s: Int,
) {
    fun sumAll() = x + m + a + s
}

sealed class Instruction {
    data class MoveTo(val destination: String): Instruction()
    object Reject: Instruction()
    object Accept: Instruction()
}

sealed class Statement(val instruction: Instruction) {
    data class ConditionalStatement(
            val variable: Variable,
            val operand: Operand,
            val value: Int,
            private val _instruction: Instruction
    ): Statement(_instruction)

    data class InstructionalStatement(
            private val _instruction: Instruction
    ): Statement(_instruction)
}

enum class Operand {
    GreaterThan,
    LessThan,
}

enum class Variable {
    X, M, A, S
}