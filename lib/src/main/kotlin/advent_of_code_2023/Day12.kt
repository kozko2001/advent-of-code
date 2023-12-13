package advent_of_code_2023

import com.github.h0tk3y.betterParse.combinators.*
import com.github.h0tk3y.betterParse.grammar.Grammar
import com.github.h0tk3y.betterParse.grammar.parseToEnd
import com.github.h0tk3y.betterParse.lexer.literalToken
import com.github.h0tk3y.betterParse.lexer.regexToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking


class Day12(private val input: String) {
    val parser = Day12InputParser()


    data class Line(val sprins: List<Spring>, val correctnes: List<Int>)
    data class InputDay12(val lines: List<Line>)

    enum class Spring {
        Good,
        Damaged,
        Unknown
    }

    class Day12InputParser : Grammar<InputDay12>() {
        val enter by regexToken("\\n|\\r")
        val unkown by literalToken("?")
        val damaged by literalToken("#")
        val good by literalToken(".")
        val comma by literalToken(",")
        val ws by regexToken("\\s+")
        val number by regexToken("[0-9]+")
        val numberParser = number use {
            text.toInt()
        }

        val spring = (unkown or damaged or good) use {
            when(this.text) {
                "#" -> Spring.Damaged
                "?" -> Spring.Unknown
                else -> Spring.Good
            }
        }

        val listOfNumbers = separated(numberParser, comma)
        val listOfNumbersParser =listOfNumbers use {
            this.terms
        }

        val line = oneOrMore(spring) and -ws and listOfNumbersParser and skip(optional(enter))

        override val rootParser by oneOrMore(line) use {
            val lines = this.map { Line(it.t1, it.t2) }
            InputDay12(lines)
        }
    }

    fun generateVerifyLine(springs: List<Spring>): List<Int> =
        springs.fold(listOf(0)) { l, element ->
            when (element) {
                Spring.Damaged -> l.mapIndexed { index, i -> if(index==l.lastIndex) l.last() + 1 else i }
                else -> l + listOf(0)
            }
        }.filter { it != 0 }


    fun verifyLine(springs: List<Spring>, verification: List<Int>): Boolean =
            generateVerifyLine(springs) == verification


    data class CacheItem(val pos: Int, val groups: List<Int>)
    fun getSolutionsRec(springs: List<Spring>, correctnes: List<Int>, pos: Int, groups: List<Int>, cache: MutableMap<CacheItem, Long>): Long {
        return cache.getOrPut(CacheItem(pos, groups)) {
            var sum = 0L

            if (pos == springs.size) {
                val g = if(groups.last() == 0) groups.dropLast(1) else groups
                val valid = g == correctnes
                if (valid ) 1 else 0
            } else if(groups.size > 1 && groups.dropLast(1) != correctnes.take(groups.size - 1)) {
                0
            }
            else {
                val curr = springs[pos]


                if (curr == Spring.Good || curr==Spring.Unknown) {
                    if(groups.last() == 0) {
                        sum += getSolutionsRec(springs, correctnes, pos + 1, groups, cache)
                    } else {
                        sum += getSolutionsRec(springs, correctnes, pos + 1, groups + listOf(0), cache)
                    }
                }

                if (curr == Spring.Damaged || curr==Spring.Unknown) {
                    val newGroups = groups.dropLast(1) + listOf(groups.last() + 1)
                    if(correctnes.zip(newGroups).all { (a, b) -> (b > a).not()}) {
                        sum += getSolutionsRec(springs, correctnes, pos + 1, newGroups, cache)
                    }
                }

                sum
            }
        }
    }

    fun solvePart1(): Long {
        val puzzle = Day12InputParser().parseToEnd(input)

        val possibles = puzzle.lines.map {
            val cache: MutableMap<CacheItem, Long> = mutableMapOf()
            getSolutionsRec(it.sprins, it.correctnes, 0, listOf(0), cache)
        }
        return possibles.sum()
    }


    fun solvePart2(): Long {
        val puzzle = Day12InputParser().parseToEnd(input)

        val expandedLines = puzzle.lines.map {
            val unk = listOf(Spring.Unknown)
            val s = it.sprins + unk + it.sprins + unk + it.sprins + unk + it.sprins + unk + it.sprins
            val c = it.correctnes + it.correctnes + it.correctnes + it.correctnes + it.correctnes
            Line(s, c)
        }


        return expandedLines.mapIndexed { index, it ->
            val cache: MutableMap<CacheItem, Long> = mutableMapOf()
            getSolutionsRec(it.sprins, it.correctnes, 0, listOf(0), cache)
        }.sum()
    }


}
