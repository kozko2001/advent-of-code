package advent_of_code_2023

import com.github.h0tk3y.betterParse.combinators.*
import com.github.h0tk3y.betterParse.grammar.Grammar
import com.github.h0tk3y.betterParse.grammar.parseToEnd
import com.github.h0tk3y.betterParse.lexer.literalToken
import com.github.h0tk3y.betterParse.lexer.regexToken
import org.checkerframework.checker.units.qual.K

data class InputDay08(val instructions: List<Day08.Instruction>, val paths: Map<String, Pair<String, String>>)

class Day08InputParser : Grammar<InputDay08>() {
    val equal by literalToken("=")
    val lpar by literalToken("(")
    val rpar by literalToken(")")
    val comma by literalToken(",")
    val char by regexToken("([A-Z]|[0-9])+")
    val enter by regexToken("\\n|\\r")
    val ws by regexToken("(\\s)+")

    val directions by oneOrMore(char) and skip(enter) and skip(enter) use {
        this[0].text.mapNotNull {
            when (it) {
                'R' -> Day08.Instruction.Right
                'L' -> Day08.Instruction.Left
                else -> null
            }
        }
    }

    val path by oneOrMore(char) and -ws and -equal and -ws and -lpar and oneOrMore(char) and -comma and -ws and oneOrMore(char) and -rpar and skip(optional(enter)) use {
        val origin = t1.map { it.text }.joinToString("")
        val lpath = t2.map { it.text }.joinToString("")
        val rpath = t3.map { it.text }.joinToString("")

        Pair(origin, Pair(lpath, rpath))
    }

    val allPaths by oneOrMore(path) use {
        this.toMap()
    }

    override val rootParser by directions and allPaths use {
        InputDay08(t1, t2)
    }
}

class Day08(private val input: String) {
    val parser = Day08InputParser()

    enum class Instruction() {
        Left,
        Right
    }

    private fun follow(start: String, instructions: List<Instruction>, paths: Map<String, Pair<String, String>>): Sequence<Pair<Int, String>> {
        return generateSequence(Pair(0, start)) { it ->
            val currentDirection = instructions[(it.first) % instructions.size]
            val path = paths[it.second]!!

            val nextPosition = if (currentDirection == Instruction.Right) path.second else path.first

            Pair(it.first + 1, nextPosition)

        }
    }

    fun solvePart1(): Int {
        val puzzle = parser.parseToEnd(input)
        val instructions = follow("AAA", puzzle.instructions, puzzle.paths)

        return instructions.takeWhile {
            it.second != "ZZZ"
        }.last().first + 1
    }

    private fun gcd(a: Long, b: Long): Long =
            if (b == 0L) a else gcd(b, a % b)

    private fun lcm(a: Long, b: Long): Long =
            (a * b) / gcd(a, b)

    fun solvePart2(): Long {
        val puzzle = parser.parseToEnd(input)
        val starts = puzzle.paths.keys.filter { it.endsWith("A") }

        return starts.map {
            follow(it, puzzle.instructions, puzzle.paths)
                .takeWhile { step -> step.second.last() != 'Z' }
                .last().first+1
        }
        .map { it.toLong() }
        .reduce { acc, num -> lcm(acc, num) }
    }
}
