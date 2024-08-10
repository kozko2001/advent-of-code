package advent_of_code_2023

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("Day 17")
class Day17Test {

    val testInput: String = """
        2413432311323
        3215453535623
        3255245654254
        3446585845452
        4546657867536
        1438598798454
        4457876987766
        3637877979653
        4654967986887
        4564679986453
        1224686865563
        2546548887735
        4322674655533
    """.trimIndent()

    @Test
    fun `Part 1 example`() {
        val answer = Day17(testInput).solvePart1()

        assertThat(answer).isEqualTo(102)
    }

    @Test
    fun `Part 1 actual`() {
        val input = Resources.resourceAsText("day17.txt")
        val answer = Day17(input).solvePart1()

        assertThat(answer).isEqualTo(0)
    }

    @Test
    fun `Part 2 example`() {
        val answer1 = Day17(testInput).solvePart2()
        assertThat(answer1).isEqualTo(0)
    }

    @Test
    fun `Part 2 actual`() {
        val input = Resources.resourceAsText("day17.txt")
        val answer = Day17(input).solvePart2()

        assertThat(answer).isEqualTo(0)
    }
}
