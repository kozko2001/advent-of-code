package advent_of_code_2023

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("Day 02")
class Day03Test {

    @Test
    fun `Part 1 example`() {
        val input = """
467..114..
...*......
..35..633.
......#...
617*......
.....+.58.
..592.....
......755.
...$.*....
.664.598..
    """.trimIndent()
        val answer = Day03(input).solvePart1()

        assertThat(answer).isEqualTo(4361)
    }


    @Test
    fun `Part 1 actual`() {
        val input = Resources.resourceAsText("day03.txt")
        val answer = Day03(input).solvePart1()

        assertThat(answer).isEqualTo(521515)
    }

    @Test
    fun `Part 2 example`() {
        val input = """
467..114..
...*......
..35..633.
......#...
617*......
.....+.58.
..592.....
......755.
...$.*....
.664.598..
    """.trimIndent()
        val answer = Day03(input).solvePart2()

        assertThat(answer).isEqualTo(467835)
    }

    @Test
    fun `Part 2 actual`() {
        val input = Resources.resourceAsText("day03.txt")
        val answer = Day03(input).solvePart2()

        assertThat(answer).isEqualTo(69527306)
    }
}
