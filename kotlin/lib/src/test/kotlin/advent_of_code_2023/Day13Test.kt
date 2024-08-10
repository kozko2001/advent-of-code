package advent_of_code_2023

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import java.math.BigInteger

@DisplayName("Day 13")
class Day13Test {

    val testInput: String = """
#.##..##.
..#.##.#.
##......#
##......#
..#.##.#.
..##..##.
#.#.##.#.

#...##..#
#....#..#
..##..###
#####.##.
#####.##.
..##..###
#....#..#
    """.trimIndent()
    @Test
    fun `Part 1 example`() {
        val answer = Day13(testInput).solvePart1()

        assertThat(answer).isEqualTo(405)
    }


    @Test
    fun `Part 1 actual`() {
        val input = Resources.resourceAsText("day13.txt")
        val answer = Day13(input).solvePart1()

        assertThat(answer).isEqualTo(33122)
    }

    @Test
    fun `Part 2 example`() {
        val answer1 = Day13(testInput).solvePart2()
        assertThat(answer1).isEqualTo(400)

    }

    @Test
    fun `Part 2 actual`() {
        val input = Resources.resourceAsText("day13.txt")
        val answer = Day13(input).solvePart2()

        assertThat(answer).isEqualTo(32312)
    }

}
