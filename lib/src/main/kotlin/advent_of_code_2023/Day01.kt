package advent_of_code_2023

class Day01(private val input: String) {

    fun solvePart1(): Int {
        val digits = input.split("\n").map { findDigits(it) { null } }
        return parse(digits).sum()
    }

    fun solvePart2(): Int {
        val digits = input.split("\n").map {
            findDigits(it) {
                when {
                    it.startsWith("one") -> 1
                    it.startsWith("two") -> 2
                    it.startsWith("three") -> 3
                    it.startsWith("four") -> 4
                    it.startsWith("five") -> 5
                    it.startsWith("six") -> 6
                    it.startsWith("seven") -> 7
                    it.startsWith("eight") -> 8
                    it.startsWith("nine") -> 9
                    else -> null
                }
            }
        }
        return parse(digits).sum()
    }

    fun parse(digits: List<List<Int>>): List<Int> =
            digits.map {
                val first = it.first()
                val last = it.last()

                first * 10 + last
            }

    private fun findDigits(line: String, stringNumbers: (String) -> Int?): List<Int> =
            line.mapIndexedNotNull { index, c ->
                if (c.isDigit()) {
                    c.digitToInt()
                } else {
                    val partString = line.substring(index)
                    stringNumbers(partString)
                }
            }
}
