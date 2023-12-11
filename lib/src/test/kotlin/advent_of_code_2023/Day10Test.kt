package advent_of_code_2023

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import java.math.BigInteger

@DisplayName("Day 10")
class Day10Test {

    val testInput: String = """
7-F7-
.FJ|7
SJLL7
|F--J
LJ.LJ
    """.trimIndent()
    @Test
    fun `Part 1 example`() {
        val answer = Day10(testInput).solvePart1()

        assertThat(answer).isEqualTo(8)
    }


    @Test
    fun `Part 1 actual`() {
        val input = Resources.resourceAsText("day10.txt")
        val answer = Day10(input).solvePart1()

        assertThat(answer).isEqualTo(6897)
    }

    @Test
    fun `Part 2 example`() {
        val answer = Day10("""
.F----7F7F7F7F-7....
.|F--7||||||||FJ....
.||.FJ||||||||L7....
FJL7L7LJLJ||LJ.L-7..
L--J.L7...LJS7F-7L7.
....F-J..F7FJ|L7L7L7
....L7.F7||L7|.L7L7|
.....|FJLJ|FJ|F7|.LJ
....FJL-7.||.||||...
....L---J.LJ.LJLJ...
        """.trimIndent()).solvePart2()
        assertThat(answer).isEqualTo(8)

        val answer2 = Day10("""
FF7FSF7F7F7F7F7F---7
L|LJ||||||||||||F--J
FL-7LJLJ||||||LJL-77
F--JF--7||LJLJ7F7FJ-
L---JF-JLJ.||-FJLJJ7
|F|F-JF---7F7-L7L|7|
|FFJF7L7F-JF7|JL---7
7-L-JL7||F7|L7F-7F7|
L.L7LFJ|||||FJL7||LJ
L7JLJL-JLJLJL--JLJ.L
        """.trimIndent()).solvePart2()

        assertThat(answer2).isEqualTo(10)


    }

    @Test
    fun `Part 2 actual`() {
        val input = Resources.resourceAsText("day10.txt")
        val answer = Day10(input).solvePart2()

        assertThat(answer).isEqualTo(376)
    }

}
