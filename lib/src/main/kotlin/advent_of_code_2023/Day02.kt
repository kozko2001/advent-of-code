package advent_of_code_2023

import com.github.h0tk3y.betterParse.combinators.*
import com.github.h0tk3y.betterParse.grammar.Grammar
import com.github.h0tk3y.betterParse.grammar.parseToEnd
import com.github.h0tk3y.betterParse.lexer.literalToken
import com.github.h0tk3y.betterParse.lexer.regexToken

data class RecordInformation(val red: Int, val blue: Int, val green: Int)
data class Game(val gameId: Int, val records: List<RecordInformation>)

class InputParser: Grammar<Game>() {
    val game by literalToken("Game", ignore = true)
    val red by literalToken("red")
    val blue by literalToken("blue")
    val green by literalToken("green")
    val colours by red or blue or green
    val ws by regexToken("\\s+", ignore = true)
    val colon by literalToken(":", ignore = true)
    val comma by literalToken(",", ignore = true)
    val semmiColon by literalToken(";", ignore = true)
    val num by regexToken("-?\\d+")

    val number by num use { text.toInt() }
    val gameId by skip(game) and skip(ws) and number and skip(colon)

    val colorTerm by skip(ws) and number and skip(ws) and colours
    val colorTerms by separated(colorTerm, comma) use {
        val colorCounts:Map<String, Int> = terms.associate { it.t2.text to it.t1 }

        RecordInformation(
                red = colorCounts["red"] ?: 0,
                blue = colorCounts["blue"] ?: 0,
                green = colorCounts["green"] ?: 0
        )
    }

    val tries by separated(colorTerms, semmiColon)
    val line by (gameId and tries) use { Game(t1, t2.terms)}
    override val rootParser by line
}

class Day02(private val input: String) {
    val parser = InputParser()
    val games =  input.split("\n").map { parser.parseToEnd(it) }

    fun solvePart1(): Int {
        return games.filter {
            it.records.all { it.red <= 12 && it.green <= 13 && it.blue <= 14 }
        }.sumOf { it.gameId }
    }

    fun solvePart2(): Int {
        return games.sumOf {
            val blue = it.records.maxOf { it.blue }
            val red = it.records.maxOf { it.red }
            val green = it.records.maxOf { it.green }

            blue * green * red
        }
    }

}
