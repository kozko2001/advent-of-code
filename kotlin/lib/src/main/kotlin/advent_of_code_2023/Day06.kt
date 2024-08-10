package advent_of_code_2023

import com.github.h0tk3y.betterParse.combinators.*
import com.github.h0tk3y.betterParse.grammar.Grammar
import com.github.h0tk3y.betterParse.grammar.parseToEnd
import com.github.h0tk3y.betterParse.lexer.literalToken
import com.github.h0tk3y.betterParse.lexer.regexToken

data class InputDay06(val races: List<RaceDay06>)

data class RaceDay06(val duration: Int, val maxScore: Int)
class Day06InputParser : Grammar<InputDay06>() {
    val time by literalToken("Time:")
    val distance by literalToken("Distance:")
    val enter by regexToken("\\n|\\r")
    val enterParser by enter
    val ws by regexToken("(\\s)+")
    val num by regexToken("\\d+")
    val number by num use {
        text.toInt()
    }
    val wsParser = skip(optional(ws)) and number
    val listOfNumbers by oneOrMore(wsParser)

    val timeLineParser = skip(time or distance) and listOfNumbers use { this }
    override val rootParser by timeLineParser and skip(enterParser) and timeLineParser use {
        val x = this.t1.zip(this.t2).map { (time, distance) ->
            RaceDay06(time, distance)
        }
        InputDay06(x)
    }
}

class Day06(private val input: String) {
    val parser = Day06InputParser()

    fun countPossibleWaysToWinARace(duration: Long, maxScore: Long): Int =
        (1..< duration).count { holdTime ->
            val move = holdTime * (duration - holdTime)

            move > maxScore
        }

    fun solvePart1(): Int {
        val puzzle = parser.parseToEnd(input)
        return puzzle.races.map { race ->
            countPossibleWaysToWinARace(race.duration.toLong(), race.maxScore.toLong())
        }.reduce { acc, i -> i * acc }
    }

    fun solvePart2(): Int {
        val puzzle = parser.parseToEnd(input)
        val duration = puzzle.races.map { it.duration.toString() }.joinToString("").toLong()
        val maxScore = puzzle.races.map { it.maxScore.toString() }.joinToString("").toLong()

        return countPossibleWaysToWinARace(duration, maxScore)
    }
}
