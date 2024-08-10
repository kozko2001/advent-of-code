package advent_of_code_2023

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import java.math.BigInteger

@DisplayName("Day 06")
class Day06Test {

    val testInput: String = """
Time:      7  15   30
Distance:  9  40  200
    """.trimIndent()
    @Test
    fun `Part 1 example`() {
        val answer = Day06(testInput).solvePart1()

        assertThat(answer).isEqualTo(288)
    }


    @Test
    fun `Part 1 actual`() {
        val input = Resources.resourceAsText("day06.txt")
        val answer = Day06(input).solvePart1()

        assertThat(answer).isEqualTo(503424)
    }

    @Test
    fun `Part 2 example`() {
        val answer = Day06(testInput).solvePart2()

        assertThat(answer).isEqualTo(71503)
    }

    @Test
    fun `Part 2 actual`() {
        val input = Resources.resourceAsText("day06.txt")
        val answer = Day06(input).solvePart2()

        assertThat(answer).isEqualTo(32607562)
    }
}
