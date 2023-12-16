package advent_of_code_2023

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("Day 16")
class Day16Test {

    val testInput: String = """
        .|...\....
        |.-.\.....
        .....|-...
        ........|.
        ..........
        .........\
        ..../.\\..
        .-.-/..|..
        .|....-|.\
        ..//.|....
    """.trimIndent()

    @Test
    fun `Part 1 example`() {
        val answer = Day16(testInput).solvePart1()

        assertThat(answer).isEqualTo(46)
    }

    @Test
    fun `Part 1 actual`() {
        val input = Resources.resourceAsText("day16.txt")
        val answer = Day16(input).solvePart1()

        assertThat(answer).isEqualTo(6740)
    }

    @Test
    fun `Part 2 example`() {
        val answer1 = Day16(testInput).solvePart2()
        assertThat(answer1).isEqualTo(51)
    }

    @Test
    fun `Part 2 actual`() {
        val input = Resources.resourceAsText("day16.txt")
        val answer = Day16(input).solvePart2()

        assertThat(answer).isEqualTo(7041)
    }
}
