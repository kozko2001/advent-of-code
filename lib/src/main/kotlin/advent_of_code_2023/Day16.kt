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

class Day16(private val input: String) {

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
    data class Cell(val pos: Point2D, val type: CellType)
    data class Input(val cells: List<Cell>, val size: Point2D)

    enum class Direction {
        North,
        East,
        South,
        West,
    }
    enum class CellType {
        SplitterH {
            override fun directions(dir: Direction): List<Direction> {
                return if (dir == Direction.North || dir == Direction.South) {
                    listOf(Direction.West, Direction.East)
                } else {
                    listOf(dir)
                }
            }
        },
        SplitterV {
            override fun directions(dir: Direction): List<Direction> {
                return if (dir == Direction.West || dir == Direction.East) {
                    listOf(Direction.North, Direction.South)
                } else {
                    listOf(dir)
                }
            }
        },
        MirrorA {
            override fun directions(dir: Direction): List<Direction> {
                return listOf(
                    when (dir) {
                        Direction.North -> Direction.East
                        Direction.South -> Direction.West
                        Direction.West -> Direction.South
                        Direction.East -> Direction.North
                    },
                )
            }
        },
        MirrorB {
            override fun directions(dir: Direction): List<Direction> {
                return listOf(
                    when (dir) {
                        Direction.North -> Direction.West
                        Direction.South -> Direction.East
                        Direction.West -> Direction.North
                        Direction.East -> Direction.South
                    },
                )
            }
        }, ;
        abstract fun directions(dir: Direction): List<Direction>
    }
    class Parser : Grammar<Input>() {
        val enter by regexToken("\\n|\\r")
        val splitterH by literalToken("-")
        val splitterV by literalToken("|")
        val mirrorA by literalToken("/")
        val mirrorB by literalToken("\\")
        val empty by literalToken(".")

        val cell = (splitterV or splitterH or mirrorA or mirrorB or empty) use {
            when (this.text) {
                "-" -> CellType.SplitterH
                "|" -> CellType.SplitterV
                "/" -> CellType.MirrorA
                "\\" -> CellType.MirrorB
                else -> null
            }
        }

        val line = oneOrMore(cell) and skip(optional(enter))

        override val rootParser by oneOrMore(line) use {
            val x = this.flatMapIndexed { row, line ->
                line.mapIndexed { col, cell ->
                    cell?.let { Cell(Point2D(col, row), cell) }
                }
            }.filterNotNull()

            val boardSize = Point2D(x.maxOf { it.pos.x }, x.maxOf { it.pos.y })
            Input(x, boardSize)
        }
    }

    var pointsSeen: MutableSet<Pair<Direction, Point2D>> = mutableSetOf()

    fun followBeam(mirrors: Map<Point2D, CellType>, boardSize: Point2D, pos: Point2D, direction: Direction): Set<Point2D> {
        if ((pos.x > boardSize.x || pos.x < 0 || pos.y > boardSize.y || pos.y < 0) && pointsSeen.isEmpty().not()) {
            return setOf()
        }
        val key = Pair(direction, pos)
        if (key in pointsSeen) {
            return setOf()
        }
        pointsSeen.add(key)

        val nextPos = pos.nextByDirection(direction)
        val nextCell = mirrors[nextPos]

        val newDirections = nextCell.let {
            it?.directions(direction)
        } ?: listOf(direction)

        val points = newDirections.flatMap {
            followBeam(mirrors, boardSize, nextPos, it)
        }
        return (points + listOf(pos)).toSet().distinct().toSet()
    }

    fun print(map: List<Point2D>, boardSize: Point2D): String {
        val height = boardSize.y
        val width = boardSize.x
        return (0..height).map { y ->
            (0..width).map { x ->
                val c = map.firstOrNull { it.x == x && it.y == y }
                if (c == null) '.' else '#'
            }.joinToString("")
        }.joinToString("\n")
    }

    fun solvePart1(): Int {
        val puzzle = Parser().parseToEnd(input)
        val m = puzzle.cells.associate { it.pos to it.type }
        val p = followBeam(m, puzzle.size, Point2D(-1, 0), Direction.East)
            .filter { it.x >= 0 }
        val ss = print(p)
        return p.distinct().count()
    }

    fun solvePart2(): Int {
        val puzzle = Parser().parseToEnd(input)
        val m = puzzle.cells.associate { it.pos to it.type }

        val startPoints = (0..puzzle.size.x).flatMap { listOf(Pair(Point2D(it, -1), Direction.South), Pair(Point2D(it, puzzle.size.y + 1), Direction.North)) } +
            (0..puzzle.size.y).flatMap { listOf(Pair(Point2D(-1, it), Direction.East), Pair(Point2D(puzzle.size.x + 1, it), Direction.West)) }

        val p = startPoints.map { (startPos, direction) ->
            println("$startPos $direction")
            pointsSeen = mutableSetOf()

            followBeam(m, puzzle.size, startPos, direction)
                .filter { it.x >= 0 && it.y >= 0 }
        }
        val pp = p.map { it.distinct().count() }
        return pp.max()
    }
}
