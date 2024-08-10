package advent_of_code_2023

import com.github.h0tk3y.betterParse.combinators.*
import com.github.h0tk3y.betterParse.grammar.Grammar
import com.github.h0tk3y.betterParse.grammar.parseToEnd
import com.github.h0tk3y.betterParse.lexer.literalToken
import com.github.h0tk3y.betterParse.lexer.regexToken
import kotlin.math.abs


class Day11(private val input: String) {
    val parser = Day11InputParser()


    data class Point2D(val x: Long, val y: Long): Comparable<Point2D> {
        override fun compareTo(other: Point2D): Int =
            if (this.x > other.x) {
                1
            } else if (this.x < other.x) {
                 -1
            } else {
                if (this.y > other.y) {
                    1
                } else if (this.y < other.y) {
                     -1
                } else {
                    0
                }
            }
        }

    data class Cell(val point: Point2D, val item: CellItem)
    data class InputDay11(val map: List<Cell>)

    enum class CellItem {
        Empty,
        Galaxy
    }

    class Day11InputParser : Grammar<InputDay11>() {
        val enter by regexToken("\\n|\\r")
        val empty by literalToken(".")
        val galaxy by literalToken("#")

        val position = (empty or galaxy) use {
            when(this.text) {
                "#" -> CellItem.Galaxy
                else -> CellItem.Empty
            }
        }

        val line = oneOrMore(position) and skip(optional(enter))

        override val rootParser by oneOrMore(line) use {
            val cells = this.flatMapIndexed { col: Int, items: List<CellItem> ->
                items.mapIndexed { row: Int, item: CellItem ->
                    Cell(Point2D(row.toLong(), col.toLong()), item)
                }
            }
            InputDay11(cells)
        }
    }

    fun shortestPath(start: Point2D, dest: Point2D): Long = abs(start.y - dest.y) + abs(start.x - dest.x)


    fun solvePart1(): Long {
        val puzzle = Day11InputParser().parseToEnd(input)

        val map = expandMap(puzzle.map)
        val pairs = map.flatMap { i -> map.map { Pair(it, i)} }
                .filter { p -> p.first != p.second }
                .map { if(it.first.point > it.second.point) Pair(it.first.point, it.second.point) else { Pair(it.second.point, it.first.point) }}
                .distinct()


        val str = printMap(map)

        return pairs.sumOf { (start, dest) ->
            shortestPath(start, dest)
        }
    }

    private fun expandMap(map: List<Day11.Cell>, multiplier: Int = 2): List<Day11.Cell> {
        val h = map.associate { it.point to it.item }
        val maxCols = map.maxOf { it.point.x }
        val maxRows = map.maxOf { it.point.y }

        val emptyRows = (0..maxCols).filter { col ->
            (0..maxRows)
                .map { Point2D(it, col) }
                .map { h[it] }
                .count { it != null && it == CellItem.Galaxy } == 0
        }

        val emptyCols = (0..maxRows).filter { row ->
            (0..maxCols)
                    .map { Point2D(row, it) }
                    .map { h[it] }
                    .count { it != null && it == CellItem.Galaxy } == 0
        }

        val newMap = map
                .filter { it.item == CellItem.Galaxy }
                .map {cell ->
                    val pos = cell.point
                    val addToRow = emptyRows.count { it < pos.y } * (multiplier -1)
                    val addToCol = emptyCols.count { it < pos.x } * (multiplier -1)
                    Cell(Point2D(cell.point.x + addToCol, cell.point.y + addToRow), cell.item)
        }

        return newMap
    }

    fun solvePart2(multiplier: Int): Long {
        val puzzle = Day11InputParser().parseToEnd(input)

        val map = expandMap(puzzle.map, multiplier)
        val pairs = map.flatMap { i -> map.map { Pair(it, i)} }
                .filter { p -> p.first != p.second }
                .map { if(it.first.point > it.second.point) Pair(it.first.point, it.second.point) else { Pair(it.second.point, it.first.point) }}
                .distinct()


        return pairs.sumOf { (start, dest) ->
            shortestPath(start, dest)
        }
    }

    private fun printMap(positions: List<Cell>): String {
        val h = positions.associate { it.point to it.item }

        val maxR = positions.maxOf { it.point.y }
        val maxC = positions.maxOf { it.point.x }

        return (0..maxR).map {row ->
            (0..maxC).map {col ->
                val p = Point2D(col, row)
                val item = h[p]
                if(item == CellItem.Galaxy) '#' else '.'
            }.joinToString("")
        }.joinToString("\n")
    }

}
