package advent_of_code_2023

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("Day 01")
class Day01Test {

    @Test
    fun `Part 1 example`() {
        val input = """
1abc2
pqr3stu8vwx
a1b2c3d4e5f
treb7uchet
    """.trimIndent()
        val answer = Day01(input).solvePart1()

        assertThat(answer).isEqualTo(142)
    }


    @Test
    fun `Part 1 actual`() {
        val input = Resources.resourceAsText("day01.txt")
        val answer = Day01(input).solvePart1()

        assertThat(answer).isEqualTo(53194)
    }


    @Test
    fun `Part 2 example`() {
        val input = """
two1nine
eightwothree
abcone2threexyz
xtwone3four
4nineeightseven2
zoneight234
7pqrstsixteen
    """.trimIndent()
        val answer = Day01(input).solvePart2()

        assertThat(answer).isEqualTo(281)
    }

    @Test
    fun `Part 2 actual`() {
        val input = Resources.resourceAsText("day01.txt")
        val answer = Day01(input).solvePart2()

        assertThat(answer).isEqualTo(54249)
    }
}
