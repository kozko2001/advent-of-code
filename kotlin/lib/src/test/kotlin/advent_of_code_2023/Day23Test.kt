package advent_of_code_2323

import advent_of_code_2023.Day23
import advent_of_code_2023.Resources
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("Day 23")
class Day23Test {
    val testInput: String = """
#.#####################
#.......#########...###
#######.#########.#.###
###.....#.>.>.###.#.###
###v#####.#v#.###.#.###
###.>...#.#.#.....#...#
###v###.#.#.#########.#
###...#.#.#.......#...#
#####.#.#.#######.#.###
#.....#.#.#.......#...#
#.#####.#.#.#########v#
#.#...#...#...###...>.#
#.#.#v#######v###.###v#
#...#.>.#...>.>.#.###.#
#####v#.#.###v#.#.###.#
#.....#...#...#.#.#...#
#.#########.###.#.#.###
#...###...#...#...#.###
###.###.#.###v#####v###
#...#...#.#.>.>.#.>.###
#.###.###.#.###.#.#v###
#.....###...###...#...#
#####################.#
    """.trimIndent()

    @Test
    fun `Part 1 example`() {
        val answer = Day23(testInput).solvePart1()

        assertThat(answer).isEqualTo(94)
    }

    @Test
    fun `Part 1 actual`() {
        val input = Resources.resourceAsText("day23.txt")
        val answer = Day23(input).solvePart1()

        assertThat(answer).isEqualTo(2386)
    }

    @Test
    fun `Part 2 example`() {
        val answer = Day23(testInput).solvePart2()

        assertThat(answer).isEqualTo(154)
    }

    @Test
    fun `Part 2 actual`() {
        val input = Resources.resourceAsText("day23.txt")
        val answer = Day23(input).solvePart2()

        assertThat(answer).isEqualTo(6246)
    }
}
