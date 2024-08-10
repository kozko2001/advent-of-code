package advent_of_code_2023

import com.github.h0tk3y.betterParse.combinators.*
import com.github.h0tk3y.betterParse.grammar.Grammar
import com.github.h0tk3y.betterParse.grammar.parseToEnd
import com.github.h0tk3y.betterParse.lexer.literalToken
import com.github.h0tk3y.betterParse.lexer.regexToken
import kotlin.math.pow

data class ScratchCard(val game: Int, val winning: List<Int>, val numbers: List<Int>)

class Day04InputParser: Grammar<ScratchCard>() {
    val card by literalToken("Card")
    val ws by regexToken("\\s+")
    val colon by literalToken(":")
    val pipe by literalToken("|")
    val num by regexToken("\\d+")
    val number by num use {
        text.toInt()
    }
    val cardId by skip(card) and skip(ws) and number and skip(colon)
    val ws_number = skip(optional(ws)) and number
    val listOfNumbers by oneOrMore(ws_number)


    val line by cardId and listOfNumbers and skip(ws) and skip(pipe) and listOfNumbers and skip(optional(ws)) use {
        ScratchCard(t1, t2, t3)
    }

    override val rootParser by line
}

class Day04(private val input: String) {
    val parser = Day04InputParser()
    val scratches =  input.split("\n").map { parser.parseToEnd(it) }

    fun solvePart1(): Int {
        return scratches.sumOf {
            val winning = it.winning.toSet()
            val numWon = it.numbers.count { winning.contains(it) }
            if (numWon > 0) {
                2.0.pow((numWon - 1).toDouble())
            } else {
                0.0
            }
        }.toInt()

    }

    fun rec(gameToScratches: Map<Int, Int>, game: Int): Int =
            gameToScratches[game]?.takeIf { it > 0 }
                ?.let { n ->
                    (game + 1 until (game + 1 + n)).sumOf { rec(gameToScratches, it) } + n
                } ?: 0

    fun solvePart2(): Int {
        val parser = Day04InputParser()
        val scratches =  input.split("\n").map { parser.parseToEnd(it) }

        val gameToScratches = scratches.associate {
            it.game to it.winning.toSet().intersect(it.numbers).count()
        }

        return  scratches.sumOf{ rec(gameToScratches, it.game) } + scratches.size

    }


}
