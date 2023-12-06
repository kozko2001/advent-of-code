package advent_of_code_2023

data class Surrounding(val char: Char, val row: Int, val col: Int)
data class Part(val number: Int, val surrounding: List<Surrounding>, val row:Int, val start:Int, val end: Int)

class Day03(private val input: String) {

    fun solvePart1(): Int {
        val p = findParts(input, Regex("\\d+"))
        return p.filter { it.surrounding.any { it.char != '.' } }.sumOf { it.number }
    }

    fun solvePart2(): Int {
        val gearsPossible = findParts(input, Regex("\\*")).filter { it.surrounding.count { it.char.isDigit() } >= 2 }
        val parts = findParts(input, Regex("\\d+"))

        val partsOfGears = gearsPossible.mapNotNull { gear -> gear.surrounding
                .filter { it.char.isDigit() }
                .map { pos -> parts.first { part -> pos.row == part.row && part.start <= pos.col && pos.col <= part.end }}
                .distinct()
                .map{ it.number }
                .takeIf { it.size >= 2 }
        }
        val powers = partsOfGears.map { it.reduce { acc, i -> acc * i } }

        return powers.sum()
    }

    fun getCharAtPosition(lines: List<String>, row: Int, col: Int): Char? =
            lines.getOrNull(row)?.getOrNull(col)

    fun getSurrounding(lines: List<String>, row: Int, startColumn: Int, endColumn: Int ): List<Surrounding> {
        val length = endColumn - startColumn;
        val offsets = (-1..length+1).flatMap { listOf(Pair(-1, it), Pair(1, it)) } +
                listOf(Pair(0, -1), Pair(0, length+1))

        return offsets.mapNotNull {(r, c) ->
            val charRow = row + r
            val charCol = startColumn + c
            val char = getCharAtPosition(lines, charRow, charCol)
            char?.let { Surrounding(char, charRow, charCol )}
        }
    }

    fun findParts(input: String, regex: Regex): List<Part> {
        val lines = input.split("\n")
        return lines.flatMapIndexed { row: Int, line: String ->
            val matches = regex.findAll(line)

            matches.map { match ->
                val startIndex = match.range.start
                val endIndex = match.range.endInclusive
                val number = match.value.toIntOrNull()

                val surrounding = getSurrounding(lines, row, startIndex, endIndex)
                Part(number ?: 0, surrounding, row, startIndex, endIndex)
            }
        }
    }

}
