package advent_of_code_2023

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import java.math.BigInteger

@DisplayName("Day 11")
class Day11Test {

    val testInput: String = """
...#......
.......#..
#.........
..........
......#...
.#........
.........#
..........
.......#..
#...#.....
    """.trimIndent()
    @Test
    fun `Part 1 example`() {
        val answer = Day11(testInput).solvePart1()

        assertThat(answer).isEqualTo(374)
    }


    @Test
    fun `Part 1 actual`() {
        val input = Resources.resourceAsText("day11.txt")
        val answer = Day11(input).solvePart1()

        assertThat(answer).isEqualTo(9965032)
    }

    @Test
    fun `Part 2 example`() {
        val answer1 = Day11(testInput).solvePart2(10)
        assertThat(answer1).isEqualTo(1030)

        val answer2 = Day11(testInput).solvePart2(100)
        assertThat(answer2).isEqualTo(8410)
    }

    @Test
    fun `Part 2 actual`() {
        val input = Resources.resourceAsText("day11.txt")
        val answer = Day11(input).solvePart2(1_000_000)

        assertThat(answer).isEqualTo(550358864332)
    }

}
