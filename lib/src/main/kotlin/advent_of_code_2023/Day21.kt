package advent_of_code_2023

import com.github.h0tk3y.betterParse.combinators.and
import com.github.h0tk3y.betterParse.combinators.oneOrMore
import com.github.h0tk3y.betterParse.combinators.optional
import com.github.h0tk3y.betterParse.combinators.or
import com.github.h0tk3y.betterParse.combinators.skip
import com.github.h0tk3y.betterParse.combinators.use
import com.github.h0tk3y.betterParse.grammar.Grammar
import com.github.h0tk3y.betterParse.grammar.parseToEnd
import com.github.h0tk3y.betterParse.lexer.TokenMatch
import com.github.h0tk3y.betterParse.lexer.literalToken
import com.github.h0tk3y.betterParse.lexer.regexToken

class Day21(private val input: String) {

    data class Point2D(val x: Int, val y: Int) {
        fun add(other: Point2D) =
            Point2D(this.x + other.x, this.y + other.y)

        fun inBoard(boardSize: Point2D): Boolean =
            this.x >= 0 && this.y >= 0 && this.x < boardSize.x && this.y < boardSize.y

        fun dist(other: Point2D) = Math.abs(this.x - other.x) + Math.abs(this.y - other.y)
    }
    data class Input(val start: Point2D, val boardSize: Point2D, val rocks: List<Point2D>)

    class Parser : Grammar<Input>() {

        val comma by literalToken(".")
        val rock by literalToken("#")
        val start by literalToken("S")
        val enter by regexToken("\\n|\\r")

        val cell = comma or rock or start
        val line = oneOrMore(cell) and skip(optional(enter))

        override val rootParser by oneOrMore(line) use {
            val rocks = this.flatMapIndexed { y: Int, tokenMatches: List<TokenMatch> ->
                tokenMatches.mapIndexed { x: Int, tokenMatch ->
                    if (tokenMatch.text == "#") {
                        Point2D(x, y)
                    } else {
                        null
                    }
                }
            }.filterNotNull()

            val start = this.flatMapIndexed { y: Int, tokenMatches: List<TokenMatch> ->
                tokenMatches.mapIndexed { x: Int, tokenMatch ->
                    if (tokenMatch.text == "S") {
                        Point2D(x, y)
                    } else {
                        null
                    }
                }
            }.filterNotNull().first()

            val boardSize = Point2D(this[0].size, this.size)

            Input(start, boardSize, rocks)
        }
    }

    fun flood(start: Point2D, boardSize: Point2D, rocks: Set<Point2D>): Sequence<List<Point2D>> {
        val directions = listOf(-1, 1, 0, 0).zip(listOf(0, 0, -1, 1)).map { Point2D(it.first, it.second) }
        return generateSequence(listOf(start)) { acc ->
            acc.flatMap { pos ->
                directions.map { dir -> pos.add(dir) }
                    .filter {
                        val x = it.x.mod(boardSize.x)
                        val y = it.y.mod(boardSize.y)

                        Point2D(x, y) !in rocks
                    }
            }.distinct()
        }
    }

    fun area(): Sequence<Pair<Int, Long>> = generateSequence(Pair(1, 1L)) {
        val (step, prevArea) = it
        val area = prevArea + (step + 1L) * 2 + (step - 1L) * 2L
        Pair(step + 1, area.toLong())
    }.map { Pair(it.first - 1, it.second) }

    fun solvePart1(steps: Int): Int {
        val puzzle = Parser().parseToEnd(input)
        val x = flood(puzzle.start, puzzle.boardSize, puzzle.rocks.toSet()).drop(steps).take(1)
        return x.first().count()
    }

    fun areaPoints(start: Point2D, steps: Int): List<Point2D> {
        return (-steps..steps).flatMap { x ->
            (-steps..steps)
                .map { y -> start.add(Point2D(x, y)) }
        }
            .filter { it.dist(start) <= steps }
            .distinct()
    }

    fun periferiaPoints(start: Point2D, steps: Int, rocks: Set<Point2D>): Long {
        return (0..steps).flatMap {
            listOf(
                Point2D(-steps + it + start.x, it + start.y),
                Point2D(-steps + it + start.x, -it + start.y),
                Point2D(steps - it + start.x, it + start.y),
                Point2D(steps - it + start.x, -it + start.y),
            )
                .filter { it in rocks }
        }
            .distinct().count().toLong()
    }

    fun rocksInStep(start: Point2D, rocks: Set<Point2D>): Sequence<Pair<Int, Long>> {
        return generateSequence(Pair(0, periferiaPoints(start, 0, rocks))) { acc ->
            val (lastSteps, rocksBefore) = acc
            val steps = lastSteps + 1
            Pair(steps, rocksBefore + periferiaPoints(start, steps, rocks))
        }
    }

    fun solvePart2(steps: Int): Int {
        val puzzle = Parser().parseToEnd(input)
        val rocks = puzzle.rocks.toSet()
//        val x = flood2(puzzle.start, puzzle.boardSize, puzzle.rocks.toSet()).drop(steps).take(1)
//        val r = x.first().count()
//        val p = area().take(10).toList()
//        val xx = (1..30)
//            .map { areaPoints(puzzle.start, it) }
//            .map {
//                it.filter {
//                    val x = it.x.mod(puzzle.boardSize.x)
//                    val y = it.y.mod(puzzle.boardSize.y)
//
//                    Point2D(x, y) in rocks
//                }.count()
//            }

        val ppp = area().zip(rocksInStep(puzzle.start, rocks)).take(1000)
            .fold(1L) { acc, value ->
                val area = value.first.second
                val rocks = value.second.second
                area - rocks - acc
            }
//        val xxx = (1..10)
//            .map { periferiaPoints(puzzle.start, it, rocks) }
//            .map {
//                it.filter {
//                    val x = it.x.mod(puzzle.boardSize.x)
//                    val y = it.y.mod(puzzle.boardSize.y)
//
//                    Point2D(x, y) in rocks
//                }.count()
//            }
        return 0
    }
}
