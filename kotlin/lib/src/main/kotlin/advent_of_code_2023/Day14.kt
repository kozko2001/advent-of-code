package advent_of_code_2023

import com.github.h0tk3y.betterParse.combinators.and
import com.github.h0tk3y.betterParse.combinators.oneOrMore
import com.github.h0tk3y.betterParse.combinators.optional
import com.github.h0tk3y.betterParse.combinators.or
import com.github.h0tk3y.betterParse.combinators.skip
import com.github.h0tk3y.betterParse.combinators.use
import com.github.h0tk3y.betterParse.grammar.Grammar
import com.github.h0tk3y.betterParse.grammar.parseToEnd
import com.github.h0tk3y.betterParse.lexer.literalToken
import com.github.h0tk3y.betterParse.lexer.regexToken

class Day14(private val input: String) {

    enum class CellType {
        Empty,
        RoundRock,
        CubeRock,
    }
    data class Point2D(val x: Int, val y: Int) {
        infix fun plus(other: Point2D) =
            Point2D(other.x + x, other.y + y)
    }
    data class Cell(val pos: Point2D, val type: CellType)
    data class Input(val cells: List<Cell>, val size: Point2D)

    class Parser : Grammar<Input>() {
        val enter by regexToken("\\n|\\r")
        val round by literalToken("O")
        val squared by literalToken("#")
        val empty by literalToken(".")

        val rock = (round or squared or empty) use {
            when (this.text) {
                "." -> CellType.Empty
                "#" -> CellType.CubeRock
                else -> CellType.RoundRock
            }
        }

        val line = oneOrMore(rock) and skip(optional(enter))

        override val rootParser by oneOrMore(line) use {
            val x = this.flatMapIndexed { row, line ->
                line.mapIndexed { col, rock ->
                    Cell(Point2D(col, row), rock)
                }
            }.filter { it.type != CellType.Empty }
            val boardSize = Point2D(x.maxOf { it.pos.x }, x.maxOf { it.pos.y })
            Input(x, boardSize)
        }
    }

    fun roll(direction: Point2D, position: Point2D, map: List<Cell>, size: Point2D): Point2D {
        val newPoint = position.plus(direction)
        val isOutOfPuzzle = newPoint.x < 0 || newPoint.y < 0 || newPoint.x > size.x || newPoint.y > size.y
        val stoppedByRock = map.firstOrNull { it.pos == newPoint } != null
        val canWeContinueMoving = !isOutOfPuzzle && !stoppedByRock

        return if (canWeContinueMoving) {
            roll(direction, newPoint, map, size)
        } else {
            position
        }
    }

    fun print(map: List<Cell>): String {
        val height = map.maxOf { it.pos.y }
        val width = map.maxOf { it.pos.x }
        return (0..height).map { y ->
            (0..width).map { x ->
                val c = map.firstOrNull { it.pos.x == x && it.pos.y == y }
                if (c == null) '.' else if (c.type == CellType.RoundRock) 'O' else '#'
            }.joinToString("")
        }.joinToString("\n")
    }

    fun rollAllRocks(map: List<Cell>, boardSize: Point2D, direction: Point2D): List<Cell> {
        val rocksToMove = map.filter { it.type == CellType.RoundRock }.sortedWith(compareBy { if (direction.x == 0) it.pos.y else { it.pos.x } }).let {
            if (direction.x > 0 || direction.y > 0) it.reversed() else it
        }
        return rocksToMove.fold(map) { acc, rock ->
            val newPos = roll(direction, rock.pos, acc, boardSize)
            val newRock = Cell(newPos, rock.type)
            val newMap = acc.map { if (it.pos == rock.pos) newRock else it }
            newMap
        }
    }

    fun solvePart1(): Int {
        val puzzle = Parser().parseToEnd(input)

        val final = rollAllRocks(puzzle.cells, puzzle.size, Point2D(0, -1))
        val maxHeight = puzzle.size.y
        val rocksToScore = final.filter { it.type == CellType.RoundRock }
        return rocksToScore.sumOf { puzzle.size.y - it.pos.y + 1 }
    }

    fun solvePart2(): Int {
        val puzzle = Parser().parseToEnd(input)
        val cache: HashMap<Set<Cell>, Pair<Int, Set<Cell>>> = hashMapOf()
        val directions = listOf(Point2D(0, -1), Point2D(-1, 0), Point2D(0, 1), Point2D(1, 0))

        // Fill cache until we find a cycle...
        (1..300000).fold(Pair(puzzle.cells.toSet(), false)) { acc, round ->
            if (acc.second) {
                acc
            } else if (acc.first in cache) {
                Pair(cache[acc.first]!!.second, true)
            } else {
                val r: Pair<Set<Cell>, Boolean>

                val executionTime = kotlin.system.measureTimeMillis {
                    val a1 = rollAllRocks(acc.first.toList(), puzzle.size, directions[0])
                    val a2 = rollAllRocks(a1, puzzle.size, directions[1])
                    val a3 = rollAllRocks(a2, puzzle.size, directions[2])
                    val a4 = rollAllRocks(a3, puzzle.size, directions[3]).toSet()

                    cache[acc.first] = Pair(round, a4)
                    r = Pair(a4, false)
                }
                println("round $round took $executionTime ms")
                r
            }
        }

        // Cycle found! lets extrapolate which round would be the 1000000000
        val lastRound = cache.values.maxOf { it.first }
        val nextRound = cache[cache.values.find { it.first == lastRound }!!.second]!!
        val cycleLength = lastRound - nextRound.first + 1

        val r = ((1000000000 - lastRound - 1) % cycleLength) + nextRound.first

        // retrieve and score!
        val final = cache.values.first { it.first == r }.second
        val rocksToScore = final.filter { it.type == CellType.RoundRock }
        return rocksToScore.sumOf { puzzle.size.y - it.pos.y + 1 }
    }
}
