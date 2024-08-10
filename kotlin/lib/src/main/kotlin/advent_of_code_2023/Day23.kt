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

class Day23(private val input: String) {

    data class Point2D(val x: Int, val y: Int) {
        fun add(other: Point2D) =
            Point2D(this.x + other.x, this.y + other.y)

        fun inBoard(boardSize: Point2D): Boolean =
            this.x >= 0 && this.y >= 0 && this.x < boardSize.x && this.y < boardSize.y
    }
    enum class CellType(val directions: List<Point2D>) {
        Rock(emptyList()),
        Good(listOf(Point2D(-1, 0), Point2D(1, 0), Point2D(0, 1), Point2D(0, -1))),
        SliceUp(listOf(Point2D(0, -1))),
        SliceDown(listOf(Point2D(0, 1))),
        SliceRight(listOf(Point2D(1, 0))),
        SliceLeft(listOf(Point2D(-1, 0))),
    }
    data class Input(val start: Point2D, val goal: Point2D, val boardSize: Point2D, val rocks: Map<Point2D, CellType>)

    class Parser : Grammar<Input>() {

        val dot by literalToken(".")
        val rock by literalToken("#")
        val sliceUp by literalToken("^")
        val sliceDown by literalToken("v")
        val sliceRight by literalToken(">")
        val sliceLeft by literalToken("<")

        val enter by regexToken("\\n|\\r")

        val cell = dot or rock or sliceUp or sliceDown or sliceRight or sliceLeft
        val cellParser = cell use {
            when (this.text) {
                "#" -> CellType.Rock
                "." -> CellType.Good
                "^" -> CellType.SliceUp
                "v" -> CellType.SliceDown
                "<" -> CellType.SliceLeft
                ">" -> CellType.SliceRight
                else -> CellType.Good
            }
        }
        val line = oneOrMore(cellParser) and skip(optional(enter))

        override val rootParser by oneOrMore(line) use {
            val rocks = this.flatMapIndexed { y: Int, tokenMatches: List<CellType> ->
                tokenMatches.mapIndexed { x: Int, cell ->
                    Point2D(x, y) to cell
                }
            }.toMap()

            val start = Point2D(1, 0)
            val boardSize = Point2D(this[0].size, this.size)
            val goal = Point2D(boardSize.x - 2, boardSize.y - 1)

            Input(start, goal, boardSize, rocks)
        }
    }

    fun toGraph(rocks: Map<Point2D, CellType>, start: Point2D, ignoreSlopes: Boolean = false): Graph<Point2D> {
        val processed: MutableSet<Point2D> = mutableSetOf()
        val queue: MutableList<Point2D> = mutableListOf(start)
        val graph = Graph<Point2D>(direct = true)
        while (queue.isNotEmpty()) {
            val currVertex = queue.first()
            queue.remove(currVertex)

            if (currVertex in processed || currVertex !in rocks) {
                continue
            }
            processed.add(currVertex)

            val cell = rocks[currVertex]!!
            val allDirections = listOf(Point2D(1, 0), Point2D(-1, 0), Point2D(0, 1), Point2D(0, -1))
            val directions = if (ignoreSlopes) allDirections else cell.directions
            val nextPos = directions.map { it.add(currVertex) }
                .filter { rocks[it] != CellType.Rock }
                .filter { it.x >= 0 && it.y >= 0 }

            nextPos.forEach { graph.addEdge(currVertex, it, 1) }
            queue.addAll(nextPos)
        }
        return graph
    }

    fun dij(graph: Graph<Point2D>, node: Point2D, goal: Point2D): Int {
        val queue: MutableList<Triple<Point2D, Int, List<Point2D>>> = mutableListOf(Triple(node, 0, emptyList()))
        val paths: MutableList<Int> = mutableListOf()

        while (!queue.isEmpty()) {
            val t = queue.maxBy { it.second }
            queue.remove(t)
            val (currPos, currCost, path) = t
            if (currPos == goal) {
                paths.add(currCost)
            } else {
                val nextPos = graph.adjacencyMap[currPos]!!
                    .filter {
                        it.key !in path
                    }
                    .map { Triple(it.key, currCost + it.value, path + listOf(currPos)) }

                queue.addAll(nextPos)
            }
        }

        return paths.max()
    }
    fun solvePart1(): Int {
        val puzzle = Parser().parseToEnd(input)
        val graph = toGraph(puzzle.rocks, puzzle.start)
        val paths = dij(graph, puzzle.start, puzzle.goal)
        return paths
    }

    fun solvePart2(): Int {
        val puzzle = Parser().parseToEnd(input)
        val graph = toGraph(puzzle.rocks, puzzle.start, true)
        clean(graph)
        simplify(graph)
        val paths = dij(graph, puzzle.start, puzzle.goal)
        return paths
    }

    fun clean(graph: Graph<Point2D>): Graph<Point2D> {
        graph.adjacencyMap.keys.forEach { origin ->
            graph.adjacencyMap[origin]!!.keys.forEach { dest ->
                if (graph.adjacencyMap[dest] == null) {
                    graph.adjacencyMap[origin]?.remove(dest)
                }
            }
        }
        return graph
    }

    private fun simplify(graph: Graph<Point2D>): Graph<Point2D> {
        var next = graph.adjacencyMap.entries.firstOrNull { it.value.size == 2 }
        while (next != null) {
            val nodeToSimplify = graph.adjacencyMap.entries.firstOrNull { it.value.size == 2 }!!
            val destNodes = nodeToSimplify.value.keys.toList()

            val first = destNodes[0]
            val second = destNodes[1]

            val firstWeight = graph.adjacencyMap[first]?.get(nodeToSimplify.key)!!
            val secondWeight = graph.adjacencyMap[second]?.get(nodeToSimplify.key)!!

            graph.adjacencyMap[first]!![second] = firstWeight + secondWeight
            graph.adjacencyMap[second]!![first] = firstWeight + secondWeight

            graph.adjacencyMap[nodeToSimplify.key]!!.remove(first)
            graph.adjacencyMap[nodeToSimplify.key]!!.remove(second)
            graph.adjacencyMap[first]!!.remove(nodeToSimplify.key)
            graph.adjacencyMap[second]!!.remove(nodeToSimplify.key)

            next = graph.adjacencyMap.entries.firstOrNull { it.value.size == 2 }
        }

        graph.adjacencyMap.entries.filter { it.value.size == 0 }.forEach {
            graph.adjacencyMap.remove(it.key)
        }
        return graph
    }
}
