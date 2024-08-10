package advent_of_code_2023

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("Day 19")
class Day19Test {
//    px{a<2006:qkq,m>2090:A,rfg}
    val testInput: String = """
px{a<2006:qkq,m>2090:A,rfg}
pv{a>1716:R,A}
lnx{m>1548:A,A}
rfg{s<537:gd,x>2440:R,A}
qs{s>3448:A,lnx}
qkq{x<1416:A,crn}
crn{x>2662:A,R}
in{s<1351:px,qqz}
qqz{s>2770:qs,m<1801:hdj,R}
gd{a>3333:R,R}
hdj{m>838:A,pv}

{x=787,m=2655,a=1222,s=2876}
{x=1679,m=44,a=2067,s=496}
{x=2036,m=264,a=79,s=2244}
{x=2461,m=1339,a=466,s=291}
{x=2127,m=1623,a=2188,s=1013}
    """.trimIndent()

    @Test
    fun `Part 1 example`() {
        val answer = Day19(testInput).solvePart1()

        assertThat(answer).isEqualTo(19114)
    }

    @Test
    fun `Part 1 actual`() {
        val input = Resources.resourceAsText("day19.txt")
        val answer = Day19(input).solvePart1()

        assertThat(answer).isEqualTo(432427)
    }

    @Test
    fun `Part 2 example`() {
        val answer1 = Day19(testInput).solvePart2()
        assertThat(answer1).isEqualTo(167409079868000L)
    }

    @Test
    fun `Part 2 actual`() {
        val input = Resources.resourceAsText("day19.txt")
        val answer = Day19(input).solvePart2()

        assertThat(answer).isEqualTo(143760172569135L)
    }
}
