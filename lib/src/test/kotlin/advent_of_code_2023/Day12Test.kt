package advent_of_code_2023

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import java.math.BigInteger

@DisplayName("Day 12")
class Day12Test {

    val testInput: String = """
???.### 1,1,3
.??..??...?##. 1,1,3
?#?#?#?#?#?#?#? 1,3,1,6
????.#...#... 4,1,1
????.######..#####. 1,6,5
?###???????? 3,2,1
    """.trimIndent()
    @Test
    fun `Part 1 example`() {
        val answer = Day12(testInput).solvePart1()

        assertThat(answer).isEqualTo(21)
    }


    @Test
    fun `Part 1 actual`() {
        val input = Resources.resourceAsText("day12.txt")
        val answer = Day12(input).solvePart1()

        assertThat(answer).isEqualTo(7716)
    }

    @Test
    fun `Part 2 example`() {
        val answer1 = Day12(testInput).solvePart2()
        assertThat(answer1).isEqualTo(525152L)

    }

    @Test
    fun `Part 2 actual`() {
        val input = Resources.resourceAsText("day12.txt")
        val answer = Day12(input).solvePart2()

        assertThat(answer).isEqualTo(18716325559999L)
    }

}
