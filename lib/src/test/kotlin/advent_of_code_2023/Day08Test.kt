package advent_of_code_2023

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import java.math.BigInteger

@DisplayName("Day 08")
class Day08Test {

    val testInput: String = """
RL

AAA = (BBB, CCC)
BBB = (DDD, EEE)
CCC = (ZZZ, GGG)
DDD = (DDD, DDD)
EEE = (EEE, EEE)
GGG = (GGG, GGG)
ZZZ = (ZZZ, ZZZ)
    """.trimIndent()
    @Test
    fun `Part 1 example`() {
        val answer = Day08(testInput).solvePart1()

        assertThat(answer).isEqualTo(2)
    }


    @Test
    fun `Part 1 actual`() {
        val input = Resources.resourceAsText("day08.txt")
        val answer = Day08(input).solvePart1()

        assertThat(answer).isEqualTo(21797)
    }

    @Test
    fun `Part 2 example`() {
        val answer = Day08("""
LR

11A = (11B, XXX)
11B = (XXX, 11Z)
11Z = (11B, XXX)
22A = (22B, XXX)
22B = (22C, 22C)
22C = (22Z, 22Z)
22Z = (22B, 22B)
XXX = (XXX, XXX)
        """.trimIndent()).solvePart2()

        assertThat(answer).isEqualTo(6)
    }

    @Test
    fun `Part 2 actual`() {
        val input = Resources.resourceAsText("day08.txt")
        val answer = Day08(input).solvePart2()

        assertThat(answer).isEqualTo(23977527174353L)
    }

}
