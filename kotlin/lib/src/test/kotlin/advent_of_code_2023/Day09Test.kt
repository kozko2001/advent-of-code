package advent_of_code_2023

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import java.math.BigInteger

@DisplayName("Day 09")
class Day09Test {

    val testInput: String = """
0 3 6 9 12 15
1 3 6 10 15 21
10 13 16 21 30 45
    """.trimIndent()
    @Test
    fun `Part 1 example`() {
        val answer = Day09(testInput).solvePart1()

        assertThat(answer).isEqualTo(114L)
    }


    @Test
    fun `Part 1 actual`() {
        val input = Resources.resourceAsText("day09.txt")
        val answer = Day09(input).solvePart1()

        assertThat(answer).isEqualTo(2075724761L)
    }

    @Test
    fun `Part 2 example`() {
        val answer = Day09(testInput).solvePart2()

        assertThat(answer).isEqualTo(2)
    }

    @Test
    fun `Part 2 actual`() {
        val input = Resources.resourceAsText("day09.txt")
        val answer = Day09(input).solvePart2()

        assertThat(answer).isEqualTo(1072L)
    }

}
