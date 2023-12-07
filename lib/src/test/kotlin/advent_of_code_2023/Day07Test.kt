package advent_of_code_2023

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import java.math.BigInteger

@DisplayName("Day 07")
class Day07Test {

    val testInput: String = """
32T3K 765
T55J5 684
KK677 28
KTJJT 220
QQQJA 483
    """.trimIndent()
    @Test
    fun `Part 1 example`() {
        val answer = Day07(testInput).solvePart1()

        assertThat(answer).isEqualTo(6440)
    }


    @Test
    fun `Part 1 actual`() {
        val input = Resources.resourceAsText("day07.txt")
        val answer = Day07(input).solvePart1()

        assertThat(answer).isEqualTo(246795406)
    }

    @Test
    fun `Part 2 example`() {
        val answer = Day07(testInput).solvePart2()

        assertThat(answer).isEqualTo(5905)
    }

    @Test
    fun `Part 2 actual`() {
        val input = Resources.resourceAsText("day07.txt")
        val answer = Day07(input).solvePart2()

        assertThat(answer).isEqualTo(249601893)
    }

    @Test
    fun `test multiple cards with jokers`() {
        val answer = Day07("744JJ 87").solvePart2()

        assertThat(answer).isEqualTo(5905)
    }
}
