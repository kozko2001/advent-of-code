package advent_of_code_2023

import com.github.h0tk3y.betterParse.combinators.*
import com.github.h0tk3y.betterParse.grammar.Grammar
import com.github.h0tk3y.betterParse.grammar.parseToEnd
import com.github.h0tk3y.betterParse.lexer.DefaultTokenizer
import com.github.h0tk3y.betterParse.lexer.literalToken
import com.github.h0tk3y.betterParse.lexer.regexToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking
import java.math.BigInteger

data class InputDay05(val seeds: List<Long>, val transform: List<TransformDay05>)
data class RangeDay05(val destination: Long, val source: Long, val length: Long)
data class TransformDay05(val sourceCatgory: String, val destCategory: String, val ranges: List<RangeDay05>)

class Day05InputParser: Grammar<InputDay05>() {
    val seeds by literalToken("seeds:")

    val enter by regexToken("(\\n|\\r)+")
    val ws by regexToken("(\\s)+")
    val num by regexToken("\\d+")
    val number by num use {
        text.toLong()
    }
    val ws_number = skip(optional(ws)) and number
    val listOfNumbers by oneOrMore(ws_number)

    val enterParser = enter use { this }

    val seedsPart = skip(seeds) and listOfNumbers and skip(optional(enterParser))


    val to by literalToken("-to-")
    val map by literalToken("map:")

    val id by regexToken("\\w+")
    val idValue by id use { text }


    val mapPart by idValue and skip(to) and idValue and skip(ws)  and skip(map) and skip(optional(ws)) and skip(optional(enterParser))
    val range = 3 times ws_number
    val rangeParser = range use {
        RangeDay05(this[0], this[1], this[2])
    }
    val mapListNumbers by oneOrMore(rangeParser) and skip(optional(enterParser))
    val mapListNumbersParser = mapListNumbers use {
        this
    }
    val transformationData by (mapPart and oneOrMore( mapListNumbersParser))
    val transformationDataParser by transformationData use {
        TransformDay05(t1.t1, t1.t2, t2.flatten())
    }
    val allTransformations by oneOrMore(transformationDataParser)


    override val rootParser by seedsPart and allTransformations use { InputDay05(this.t1, this.t2) }
}

class Day05(private val input: String) {
    val parser = Day05InputParser()

    fun applyTransformation(x: Long, t: RangeDay05): Long {
        return if (x in t.source until t.source + t.length) {
            x - t.source + t.destination
        } else {
            x
        }
    }

    fun transformSeed(seed: Long, transforms: List<TransformDay05>): Long {
        return transforms.fold(seed) { acc, transform ->
            transform.ranges.firstOrNull { applyTransformation(acc, it) != acc }
                    ?.let { applyTransformation(acc, it) }
                    ?: acc
        }
    }

    fun solvePart1(): Int {
        val puzzle = parser.parseToEnd(input)

        return puzzle.seeds.minOf { transformSeed(it, puzzle.transform)}.toInt()

    }

    fun solvePart2(): Long {
        val puzzle = parser.parseToEnd(input)
        val seedsRange = puzzle.seeds.chunked(2).map { it[0] until it[0] + it[1] }

        // brute force for each seed using co-routines
        return runBlocking(Dispatchers.Default) {
          seedsRange.map { seedRange ->
                async {
                    seedRange.minOf {
                        transformSeed(it, puzzle.transform)
                    }
                }
            }.awaitAll()
        }.min()

    }


}
