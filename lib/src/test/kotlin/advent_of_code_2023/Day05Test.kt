package advent_of_code_2023

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import java.math.BigInteger

@DisplayName("Day 05")
class Day05Test {

    val testInput: String = """
seeds: 79 14 55 13

seed-to-soil map:
50 98 2
52 50 48

soil-to-fertilizer map:
0 15 37
37 52 2
39 0 15

fertilizer-to-water map:
49 53 8
0 11 42
42 0 7
57 7 4

water-to-light map:
88 18 7
18 25 70

light-to-temperature map:
45 77 23
81 45 19
68 64 13

temperature-to-humidity map:
0 69 1
1 0 69

humidity-to-location map:
60 56 37
56 93 4
    """.trimIndent()
    @Test
    fun `Part 1 example`() {

        val answer = Day05(testInput).solvePart1()

        assertThat(answer).isEqualTo(35)
    }

    @Test
    fun `range Transformation`() {
        val r = RangeDay05(50L, 98L, 2L)

        val p = Day05(testInput).applyTransformation(99L, r)
        assertThat(p).isEqualTo(51)
    }


    @Test
    fun `Part 1 actual`() {
        val input = Resources.resourceAsText("day05.txt")
        val answer = Day05(input).solvePart1()

        assertThat(answer).isEqualTo(226172555)
    }

    @Test
    fun `Part 2 example`() {
        val answer = Day05(testInput).solvePart2()

        assertThat(answer).isEqualTo(46)
    }

    @Test
    fun `Part 2 actual`() {
        val input = Resources.resourceAsText("day05.txt")
        val answer = Day05(input).solvePart2()

        assertThat(answer).isEqualTo(47909639L)
    }
}
