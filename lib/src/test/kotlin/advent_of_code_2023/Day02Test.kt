package advent_of_code_2023

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("Day 02")
class Day02Test {

    @Test
    fun `Part 1 example`() {
        val input = """
Game 1: 3 blue, 4 red; 1 red, 2 green, 6 blue; 2 green
Game 2: 1 blue, 2 green; 3 green, 4 blue, 1 red; 1 green, 1 blue
Game 3: 8 green, 6 blue, 20 red; 5 blue, 4 red, 13 green; 5 green, 1 red
Game 4: 1 green, 3 red, 6 blue; 3 green, 6 red; 3 green, 15 blue, 14 red
Game 5: 6 red, 1 blue, 3 green; 2 blue, 1 red, 2 green
    """.trimIndent()
        val answer = Day02(input).solvePart1()

        assertThat(answer).isEqualTo(8)
    }


    @Test
    fun `Part 1 actual`() {
        val input = Resources.resourceAsText("day02.txt")
        val answer = Day02(input).solvePart1()

        assertThat(answer).isEqualTo(2776)
    }

    @Test
    fun `Part 2 example`() {
        val input = """
Game 1: 3 blue, 4 red; 1 red, 2 green, 6 blue; 2 green
Game 2: 1 blue, 2 green; 3 green, 4 blue, 1 red; 1 green, 1 blue
Game 3: 8 green, 6 blue, 20 red; 5 blue, 4 red, 13 green; 5 green, 1 red
Game 4: 1 green, 3 red, 6 blue; 3 green, 6 red; 3 green, 15 blue, 14 red
Game 5: 6 red, 1 blue, 3 green; 2 blue, 1 red, 2 green
    """.trimIndent()
        val answer = Day02(input).solvePart2()

        assertThat(answer).isEqualTo(2286)
    }

    @Test
    fun `Part 2 actual`() {
        val input = Resources.resourceAsText("day02.txt")
        val answer = Day02(input).solvePart2()

        assertThat(answer).isEqualTo(68638)
    }
}
