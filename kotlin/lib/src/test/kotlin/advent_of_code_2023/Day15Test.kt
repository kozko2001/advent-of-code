package advent_of_code_2023

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("Day 15")
class Day15Test {

    val testInput: String = "rn=1,cm-,qp=3,cm=2,qp-,pc=4,ot=9,ab=5,pc-,pc=6,ot=7"
    @Test
    fun `Part 1 example`() {
        val answer = Day15(testInput).solvePart1()

        assertThat(answer).isEqualTo(1320)
    }


    @Test
    fun `Part 1 actual`() {
        val input = Resources.resourceAsText("day15.txt")
        val answer = Day15(input).solvePart1()

        assertThat(answer).isEqualTo(507769)
    }

    @Test
    fun `Part 2 example`() {
        val answer1 = Day15(testInput).solvePart2()
        assertThat(answer1).isEqualTo(145)

    }

    @Test
    fun `Part 2 actual`() {
        val input = Resources.resourceAsText("day15.txt")
        val answer = Day15(input).solvePart2()

        assertThat(answer).isEqualTo(269747)
    }

}
