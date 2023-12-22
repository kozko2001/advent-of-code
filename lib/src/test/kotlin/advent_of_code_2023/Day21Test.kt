package advent_of_code_2123

import advent_of_code_2023.Day21
import advent_of_code_2023.Resources
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("Day 21")
class Day21Test {
    val testInput: String = """
...........
.....###.#.
.###.##..#.
..#.#...#..
....#.#....
.##..S####.
.##..#...#.
.......##..
.##.#.####.
.##..##.##.
...........
    """.trimIndent()

    @Test
    fun `Part 1 example`() {
        val answer = Day21(testInput).solvePart1(6)

        assertThat(answer).isEqualTo(16)
    }

    @Test
    fun `Part 1 actual`() {
        val input = Resources.resourceAsText("day21.txt")
        val answer = Day21(input).solvePart1(64)

        assertThat(answer).isEqualTo(3758)
    }

    @Test
    fun `Part 2 example`() {
        val answer1 = Day21(testInput).solvePart2(6)
        val answer2 = Day21(testInput).solvePart2(10)
        val answer3 = Day21(testInput).solvePart2(50)
        val answer4 = Day21(testInput).solvePart2(100)
        val answer5 = Day21(testInput).solvePart2(500)
//        val answer6 = Day21(testInput).solvePart2(1000)
//        val answer7 = Day21(testInput).solvePart2(5000)

        assertThat(answer1).isEqualTo(16)
        assertThat(answer2).isEqualTo(50)
        assertThat(answer3).isEqualTo(1594)
        assertThat(answer4).isEqualTo(6536)
        assertThat(answer5).isEqualTo(167004)
//        assertThat(answer6).isEqualTo(668697)
//        assertThat(answer7).isEqualTo(16733044)
    }

    @Test
    fun `Part 2 actual`() {
        val input = Resources.resourceAsText("day21.txt")
        val answer = Day21(input).solvePart2(26501365)

        assertThat(answer).isEqualTo(0)
    }
}
