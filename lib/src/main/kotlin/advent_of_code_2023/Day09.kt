package advent_of_code_2023

import com.github.h0tk3y.betterParse.combinators.*
import com.github.h0tk3y.betterParse.grammar.Grammar
import com.github.h0tk3y.betterParse.grammar.parseToEnd
import com.github.h0tk3y.betterParse.lexer.literalToken
import com.github.h0tk3y.betterParse.lexer.regexToken
import org.checkerframework.checker.units.qual.K

data class InputDay09(val histories: List<List<Long>>)

class Day09InputParser : Grammar<InputDay09>() {
    val number by regexToken("-?\\d+")
    val numberParser by number use {
        this.text.toLong()
    }
    val enter by regexToken("\\n|\\r")
    val ws by regexToken("(\\s)+")

    val numberWithWS = numberParser and -optional(ws)
    val line = oneOrMore(numberWithWS) and skip(optional(enter))

    override val rootParser by oneOrMore(line) use {
        InputDay09(this)
    }
}

class Day09(private val input: String) {
    val parser = Day09InputParser()

    fun generateSubstractionSeq(readings: List<Long>): Sequence<List<Long>> =
        generateSequence(readings) {
            it.zipWithNext { a, b ->  b - a }
        }.takeWhile { !it.all { reading -> reading == 0L } }

    fun solvePart1(): Long {
        val puzzle = parser.parseToEnd(input)

        return puzzle.histories.sumOf { history ->
            generateSubstractionSeq(history).sumOf { it.last() }
        }

    }
    fun solvePart2(): Long {
        val puzzle = parser.parseToEnd(input)
        return puzzle.histories.sumOf { history ->
            generateSubstractionSeq(history)
                    .map { it.first() }
                    .toList()
                    .reversed()
                    .fold(0L) { acc, i -> i - acc }
        }
    }
}
