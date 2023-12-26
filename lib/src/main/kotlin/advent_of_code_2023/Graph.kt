package advent_of_code_2023

class Graph<T>(val direct: Boolean = false) {
    val adjacencyMap: HashMap<T, HashMap<T, Int>> = HashMap()

    fun addEdge(sourceVertex: T, destinationVertex: T, weight: Int = 1) {
        // Add edge to source vertex / node.
        adjacencyMap
            .computeIfAbsent(sourceVertex) { HashMap() }
            .set(destinationVertex, weight)
        // Add edge to destination vertex / node.
        if (!direct) {
            adjacencyMap
                .computeIfAbsent(destinationVertex) { HashMap() }
                .set(sourceVertex, weight)
        }
    }

//    override fun toString(): String = StringBuffer().apply {
//        for (key in adjacencyMap.keys) {
//            append("$key -> ")
//            append(adjacencyMap[key]?.joinToString(", ", "[", "]\n"))
//        }
//    }.toString()
}
