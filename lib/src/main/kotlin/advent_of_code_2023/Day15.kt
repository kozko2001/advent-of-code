package advent_of_code_2023

class Day15(private val input: String) {

    data class Sequence(val text: String, val label: String, val operation: Char, val focalLength: Int)

    data class Box(val id: Int) {
        var lense: List<Pair<Int, Sequence>> = listOf()

        fun apply(s: Sequence) {
            if (s.operation == '-') {
                val l = lense.firstOrNull { it.second.label == s.label }
                l?.let {
                    lense = lense
                        .filter { it.first != l.first }
                        .map {
                            if (it.first < l.first) {
                                it
                            } else {
                                Pair(it.first - 1, it.second)
                            }
                        }
                }
            } else {
                val l = lense.firstOrNull { it.second.label == s.label }
                if (l != null) {
                    lense = lense.filter { it.first != l.first } + listOf(Pair(l.first, s))
                } else {
                    val newPos = if (lense.isEmpty()) 1 else { lense.maxOf { it.first } + 1 }
                    lense = lense + Pair(newPos, s)
                }
            }
        }
    }
    fun parse(): List<Sequence> = input.split(",").map {
        if ("-" in it) {
            Sequence(it, it.replace("-", ""), '-', 0)
        } else {
            val s = it.split("=")
            Sequence(it, s[0], '=', s[1].toInt())
        }
    }

    fun hash(str: String): Int {
        return str.fold(0) { acc, c ->
            ((acc + c.code) * 17) % 256
        }
    }

    fun solvePart1(): Int {
        val sequences = parse()
        val p = sequences.map { hash(it.text) }
        val s = p.sum()
        return s
    }

    fun solvePart2(): Int {
        val sequences = parse()
        val boxes = (0..255).map { Box(it) }
        sequences.forEach {
            val boxId = hash(it.label)
            val b = boxes[boxId]
            b.apply(it)
        }

        val r = boxes.flatMap { box ->
            box.lense.map { it.second.focalLength * it.first * (box.id + 1) }
        }
        return r.sum()
    }
}
