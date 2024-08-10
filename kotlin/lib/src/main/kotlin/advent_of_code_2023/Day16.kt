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

    val Reflections = mapOf(
        Pair(CellType.SplitterH, Direction.North) to listOf(Direction.West, Direction.East),
        Pair(CellType.SplitterH, Direction.South) to listOf(Direction.West, Direction.East),
        Pair(CellType.SplitterV, Direction.West) to listOf(Direction.North, Direction.South),
        Pair(CellType.SplitterV, Direction.East) to listOf(Direction.North, Direction.South),
        Pair(CellType.MirrorA, Direction.North) to listOf(Direction.East),
        Pair(CellType.MirrorA, Direction.South) to listOf(Direction.West),
        Pair(CellType.MirrorA, Direction.West) to listOf(Direction.South),
        Pair(CellType.MirrorA, Direction.East) to listOf(Direction.North),
        Pair(CellType.MirrorB, Direction.North) to listOf(Direction.West),
        Pair(CellType.MirrorB, Direction.South) to listOf(Direction.East),
        Pair(CellType.MirrorB, Direction.West) to listOf(Direction.North),
        Pair(CellType.MirrorB, Direction.East) to listOf(Direction.South),
    )

    enum class CellType {
        SplitterH,
        SplitterV,
        MirrorA,
        MirrorB,
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

    fun followBeam(mirrors: Map<Point2D, CellType>, boardSize: Point2D, pos: Point2D, direction: Direction): Set<Point2D> {
        val pointsSeen: MutableSet<Pair<Direction, Point2D>> = mutableSetOf()

        val queue = ArrayDeque<Pair<Direction, Point2D>>()
        queue.add(Pair(direction, pos))

        while (queue.isEmpty().not()) {
            val current = queue.removeFirst()
            val (currDirection, currPos) = current

            val inBoard = (currPos.x <= boardSize.x && currPos.x >= 0 && currPos.y <= boardSize.y && currPos.y >= 0) || pointsSeen.isEmpty()
            val alreadySeen = current in pointsSeen

            if (inBoard && !alreadySeen) {
                pointsSeen.add(current)
                val nextPos = currPos.nextByDirection(currDirection)
                val nextCell = mirrors[nextPos]

                val nextDirections = nextCell?.let {
                    Reflections[Pair(it, currDirection)]
                } ?: listOf(currDirection)

                queue.addAll(nextDirections.map { Pair(it, nextPos) })
            }
        }
        return pointsSeen.map { it.second }.toSet()
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

        return startPoints.map { (startPos, direction) ->
            followBeam(m, puzzle.size, startPos, direction).count { it.x >= 0 && it.y >= 0 }
        }.max()
    }
}
