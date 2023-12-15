package advent_of_code_2023

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import java.math.BigInteger

@DisplayName("Day 14")
class Day14Test {

    val testInput: String = """
O....#....
O.OO#....#
.....##...
OO.#O....O
.O.....O#.
O.#..O.#.#
..O..#O..O
.......O..
#....###..
#OO..#....
    """.trimIndent()
    @Test
    fun `Part 1 example`() {
        val answer = Day14(testInput).solvePart1()

        assertThat(answer).isEqualTo(136)
    }


    @Test
    fun `Part 1 actual`() {
        val input = Resources.resourceAsText("day14.txt")
        val answer = Day14(input).solvePart1()

        assertThat(answer).isEqualTo(105249)
    }

    @Test
    fun `Part 2 example`() {
        val answer1 = Day14(testInput).solvePart2()
        assertThat(answer1).isEqualTo(64)

    }

    @Test
    fun `Part 2 actual`() {
        val input = Resources.resourceAsText("day14.txt")
        val answer = Day14(input).solvePart2()

        assertThat(answer).isEqualTo(88680)
    }

}
