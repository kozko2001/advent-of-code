package advent_of_code_2023

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("Day 20")
class Day20Test {
    val testInput: String = """
broadcaster -> a, b, c
%a -> b
%b -> c
%c -> inv
&inv -> a
    """.trimIndent()

    val testInput2: String = """
broadcaster -> a
%a -> inv, con
&inv -> b
%b -> con
&con -> output
    """.trimIndent()

    @Test
    fun `Part 1 example`() {
        val answer = Day20(testInput).solvePart1()
        val answer2 = Day20(testInput2).solvePart1()

        assertThat(answer).isEqualTo(32000000)
        assertThat(answer2).isEqualTo(11687500)
    }

    @Test
    fun `Part 1 actual`() {
        val input = Resources.resourceAsText("day20.txt")
        val answer = Day20(input).solvePart1()

        assertThat(answer).isEqualTo(0)
    }

    @Test
    fun `Part 2 example`() {
        val answer1 = Day20(testInput).solvePart2()
        assertThat(answer1).isEqualTo(0)
    }

    @Test
    fun `Part 2 actual`() {
        val input = Resources.resourceAsText("day20.txt")
        val answer = Day20(input).solvePart2()

        assertThat(answer).isEqualTo(224602953547789L)
    }
}
