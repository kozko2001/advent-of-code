package advent_of_code_2023

import com.github.h0tk3y.betterParse.combinators.and
import com.github.h0tk3y.betterParse.combinators.oneOrMore
import com.github.h0tk3y.betterParse.combinators.optional
import com.github.h0tk3y.betterParse.combinators.skip
import com.github.h0tk3y.betterParse.combinators.use
import com.github.h0tk3y.betterParse.grammar.Grammar
import com.github.h0tk3y.betterParse.grammar.parseToEnd
import com.github.h0tk3y.betterParse.lexer.regexToken

class Day17(private val input: String) {

    enum class Direction {
        East,
        West,
        South,
        North,
    }

    data class Point2D(val x: Int, val y: Int) {
        fun nextByDirection(dir: Direction): Point2D {
            val other = when (dir) {
                Direction.North -> Point2D(0, -1)
                Direction.South -> Point2D(0, 1)
                Direction.East -> Point2D(1, 0)
                Direction.West -> Point2D(-1, 0)
            }
            return this.add(other)
        }

        fun add(other: Point2D): Point2D = Point2D(x + other.x, y + other.y)
    }
    data class Input(val cells: List<List<Int>>, val size: Point2D)

    class Parser : Grammar<Input>() {
        val enter by regexToken("\\n|\\r")
        val digit by regexToken("[0-9]")
        val digitParser = digit use { this.text.toInt() }

        val line = oneOrMore(digitParser) and skip(optional(enter))

        override val rootParser by oneOrMore(line) use {

            val boardSize = Point2D(this[0].size, this.size)
            Input(this, boardSize)
        }
    }

    fun possibleChangeDirection(dir: Direction): List<Direction> {
        return when (dir) {
            Direction.North -> listOf(Direction.East, Direction.West)
            Direction.South -> listOf(Direction.East, Direction.West)
            Direction.West -> listOf(Direction.North, Direction.South)
            Direction.East -> listOf(Direction.North, Direction.South)
        }
    }

    data class NextPoint(val pos: Point2D, val direction: Direction, val timesInThisDirection: Int, val weightAccumulated: Int)

    fun solve(weights: List<List<Int>>, start: Point2D, goal: Point2D): Int {
        val nexts: MutableSet<NextPoint> = mutableSetOf()
        val visited: MutableMap<Point2D, Int> = mutableMapOf()

        val w = weights.getOrNull(start.y)?.get(start.x) ?: 0
        nexts.add(NextPoint(start, Direction.East, 0, 0))

        val result = mutableListOf<Int>()
        var i = 0
        var ignored = 0

        val hashWeights: MutableMap<Point2D, Int> = mutableMapOf()

        weights.mapIndexed { row, ints ->
            ints.mapIndexed { col, w ->
                val p = Point2D(row, col)
                hashWeights[p] = w
                visited[p] = Int.MAX_VALUE
            }
        }
        visited[goal] = Int.MAX_VALUE

        while (!nexts.isEmpty()) {
            i = i + 1
            val ppp = nexts.minBy { it.weightAccumulated }
            nexts.remove(ppp)
            val (pos, dir, timeInThisDire, accWeight) = ppp
//            println("pos: $pos accWeight: $accWeight")
            if (i % 1000 == 0) {
                println("$i queue length ${nexts.size} $ignored")
            }

            if (!(pos in visited)) {
                println(pos)
            }
            val visitedWeight = visited[pos]!!

            if (accWeight < visitedWeight) {
//                println("accWeight $accWeight visitedWeight $visitedWeight")
                val w = weights.getOrNull(pos.y)?.get(pos.x) ?: 0
                visited[pos] = accWeight

                if (pos == goal) {
                    return accWeight
//                    result.add(accWeight)
//                    continue
                }

                val possibleDirections = possibleChangeDirection(dir).map { Pair(0, it) } +
                    if (timeInThisDire >= 3) listOf() else listOf(Pair(timeInThisDire + 1, dir))
                val nextPositions = possibleDirections
                    .map { NextPoint(pos.nextByDirection(it.second), it.second, it.first, accWeight + w) }
                    .filter { weights.getOrNull(it.pos.y)?.getOrNull(it.pos.x) != null || it.pos == goal }
                    .filter {
                        it.weightAccumulated <= (visited[it.pos]!!)
                    }
                nexts.addAll(nextPositions)
            } else {
                ignored = ignored + 1
            }
        }

//        println(visited[goal])
        return -1
    }

    fun solvePart1(): Int {
        val puzzle = Parser().parseToEnd(input)
        val r = solve(puzzle.cells, Point2D(0, 0), Point2D(puzzle.cells.size - 1, puzzle.cells[0].size))

        return r
    }

    fun solvePart2(): Int {
        val puzzle = Parser().parseToEnd(input)
        return 0
    }
}
