package advent_of_code_2023

class Day13(private val input: String) {

    fun transpose(lines: List<String>): List<String> {
        return (0 until lines[0].length).map {
            lines.map { l -> l[it]}.joinToString("")
        }
    }

    fun findReflection(map: List<String>, valid: (up:List<String>, down:List<String>, x: Int) -> Int?): List<Int?> {
        return (1 ..map.lastIndex)
                .map { rowsUp ->
                    val linesUp = (0 until rowsUp).map { map[it] }
                    val linesDown = (rowsUp..(map.lastIndex)).map { map[it] }

                    val up = if (linesUp.size > linesDown.size) {
                        linesUp.drop(linesUp.size - linesDown.size)
                    } else {
                        linesUp
                    }

                    val down = if (linesDown.size > linesUp.size) {
                        linesDown.dropLast(linesDown.size - linesUp.size)
                    } else {
                        linesDown
                    }

                    // generate all the mirrors
                    valid(up, down.asReversed(), rowsUp)
                }
    }

    fun solvePart1(): Int {
        val maps = input.split("\n\n").map { it.split("\n") }


        val validF = { up: List<String>, down: List<String>, rowsUp: Int ->
            val valid = down.zip(up).all { (down, up) -> down == up }
            if(valid) rowsUp else null
        }

        return maps.sumOf {
            val rowReflection = findReflection(it, validF).mapNotNull { (it ?: 0) * 100 }
            val colReflection = findReflection(transpose(it), validF).mapNotNull { it ?: 0 }

            rowReflection.sum() + colReflection.sum()
        }
    }

    private fun numErrorsInString(str1: String, str2: String): List<Int> =
            str1.indices.filter { str1[it] != str2[it] }

    fun solvePart2(): Int {
        val validF = { up: List<String>, down: List<String>, rowsUp: Int ->
            val errors = down.zip(up).map {
                numErrorsInString(it.first, it.second)
            }.flatten().count()

            if(errors == 1) {
                rowsUp
            } else {
                null
            }
        }

        return input.split("\n\n")
                .map { it.split("\n") }.sumOf { it ->
                    val rowReflection = findReflection(it, validF).map { (it ?: 0) * 100 }
                    val colReflection = findReflection(transpose(it), validF).map { it ?: 0 }

                    rowReflection.sum() + colReflection.sum()
                }
    }


}
