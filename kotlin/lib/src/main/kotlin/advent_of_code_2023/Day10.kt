package advent_of_code_2023

import com.github.h0tk3y.betterParse.combinators.*
import com.github.h0tk3y.betterParse.grammar.Grammar
import com.github.h0tk3y.betterParse.grammar.parseToEnd
import com.github.h0tk3y.betterParse.lexer.literalToken
import com.github.h0tk3y.betterParse.lexer.regexToken


class Day10(private val input: String) {
    val parser = Day10InputParser()

    enum class Pipe {
        Vertical,
        Horizontal,
        Ground,
        Starting,
        NorthEast,
        NorthWest,
        SouthWest,
        SouthEast
    }


    data class Cell(val x: Int, val y: Int, val pipe: Pipe)
    data class InputDay10(val map: List<Cell>, val start: Pair<Int, Int>)

    class Day10InputParser : Grammar<InputDay10>() {
        val enter by regexToken("\\n|\\r")
        val h by literalToken("-")
        val v by literalToken("|")
        val g by literalToken(".")
        val s by literalToken("S")
        val ne by literalToken("L")
        val nw by literalToken("J")
        val sw by literalToken("7")
        val se by literalToken("F")

        val position = h or v or g or s or ne or nw or sw or se use {
            when(this.text) {
                "-" -> Pipe.Horizontal
                "|" -> Pipe.Vertical
                "S" -> Pipe.Starting
                "." -> Pipe.Ground
                "L" -> Pipe.NorthEast
                "J" -> Pipe.NorthWest
                "7" -> Pipe.SouthWest
                "F" -> Pipe.SouthEast
                else -> Pipe.Ground
            }
        }

        val line = oneOrMore(position) and skip(optional(enter))

        override val rootParser by oneOrMore(line) use {
            val cells = this.flatMapIndexed { col: Int, pipes: List<Pipe> ->
                pipes.mapIndexed { row: Int, pipe: Pipe ->
                    Cell(row, col, pipe)
                }
            }
            val starting = cells.first { it.pipe == Pipe.Starting }
            InputDay10(cells, Pair(starting.x, starting.y))
        }
    }

    fun connects(pos: Pair<Int, Int>, pipe: Pipe): List<Pair<Int, Int>> {
        val (x, y) = pos
        return when (pipe) {
            Pipe.Vertical -> listOf(Pair(x, y - 1), Pair(x, y + 1))
            Pipe.Horizontal -> listOf(Pair(x - 1, y), Pair(x + 1, y))
            Pipe.NorthWest -> listOf(Pair(x - 1, y), Pair(x , y - 1))
            Pipe.NorthEast -> listOf(Pair(x + 1, y), Pair(x , y - 1))
            Pipe.SouthWest -> listOf(Pair(x - 1, y), Pair(x , y + 1))
            Pipe.SouthEast -> listOf(Pair(x + 1, y), Pair(x , y + 1))
            Pipe.Starting -> listOf(Pair(x , y))
            else -> listOf()
        }
    }

    fun next(currentPos: Pair<Int, Int>, pos: Pair<Int, Int>, pipe: Pipe): Pair<Int, Int> =
        connects(pos, pipe).first {
            it != currentPos
        }

    fun generateCycle(prev: Pair<Int, Int>, current: Pair<Int, Int>, cells: List<Cell>): Sequence<Triple<Int, Pair<Int, Int>, Pair<Int, Int>>> {
        return generateSequence(Triple(0, prev, current)) {
            val c = cells.first { c -> c.x == it.third.first && c.y == it.third.second }
            Triple(it.first + 1, it.third, next(it.second, it.third, c.pipe))
        }
    }

    private fun getCycle(puzzle: InputDay10, startPipe: Pipe, startPos: Pair<Int, Int>, both: Boolean= true) =
            connects(puzzle.start, startPipe).reversed().take(if(both) 2 else 1).map {
                generateCycle(startPos, it, puzzle.map)
                        .takeWhile {
                            it.second != startPos || it.first == 0
                        }.toList()
            }.flatten()

    fun solvePart2(): Int {
        val puzzle = parser.parseToEnd(input)
        val startPipe = guessStartPipe(puzzle)
        val startPos = Pair(puzzle.start.first, puzzle.start.second)


        val cellHash = puzzle.map.associate { Pair(it.x, it.y) to it.pipe }

        val positions = getCycle(puzzle, startPipe, startPos, both = false)

        val positionsSet = positions.map {it.second}.distinct().toSet()
        val maxRow = positionsSet.maxOf { it.second }
        val maxCol = positionsSet.maxOf { it.first }


        val allPositions = (0..maxRow).flatMap { row -> (0..maxCol).map { col -> Pair(col, row)} }


        val inCycle = allPositions.filter { pos ->
            val hPrev = (0 until pos.first)
                    .map { Pair(it, pos.second) }
                    .filter { it in positionsSet }
                    .map { cellHash[it]!! }

            val inside = hPrev.filter { it == Pipe.Vertical || it == Pipe.NorthEast || it == Pipe.NorthWest }.size % 2 == 1

            inside && pos !in positionsSet
        }
        return inCycle.size
    }

    private fun printCycle(positions: Set<Pair<Int, Int>>, inCycle: List<Pair<Int, Int>>): String {
        val maxR = positions.maxOf { it.second }
        val maxC = positions.maxOf { it.first }

        return (0..maxR).map {row ->
            (0..maxC).map {col ->
                val pos = Pair(col, row)
                if(pos in positions) '#' else '.'
            }.joinToString("")
        }.joinToString("\n")
    }


    private fun guessStartPipe(puzzle: InputDay10): Pipe {
        val possibleStart = listOf(Pipe.Vertical, Pipe.Horizontal, Pipe.SouthEast, Pipe.SouthWest, Pipe.NorthEast, Pipe.NorthWest)

        val startPipe = possibleStart
                .map {
                    Pair(it, connects(puzzle.start, it)
                            .mapNotNull { p -> puzzle.map.firstOrNull { it.x == p.first && it.y == p.second } }
                            .filter { it.pipe in possibleStart }
                            .filter { connects(Pair(it.x, it.y), it.pipe).filter { it == puzzle.start }.size == 1 }
                    )
                }

        return startPipe.first { it.second.size == 2 }
                .first
    }

    fun solvePart1(): Int {
        val puzzle = parser.parseToEnd(input)
        val startPipe = guessStartPipe(puzzle)
        val startPos = Pair(puzzle.start.first, puzzle.start.second)

        val positions = getCycle(puzzle, startPipe, startPos)
                .groupBy { it.second }
                .map { Pair(it.key, it.value.minOf { it.first })}


       return positions.maxOf { it.second }
    }
}
