package advent_of_code_2023

import com.github.h0tk3y.betterParse.combinators.and
import com.github.h0tk3y.betterParse.combinators.oneOrMore
import com.github.h0tk3y.betterParse.combinators.optional
import com.github.h0tk3y.betterParse.combinators.separated
import com.github.h0tk3y.betterParse.combinators.skip
import com.github.h0tk3y.betterParse.combinators.use
import com.github.h0tk3y.betterParse.grammar.Grammar
import com.github.h0tk3y.betterParse.grammar.parseToEnd
import com.github.h0tk3y.betterParse.lexer.literalToken
import com.github.h0tk3y.betterParse.lexer.regexToken

class Day20(private val input: String) {
    sealed class Module {
        class Button() : Module()
        class Output() : Module()
        data class Conjuntion(val remember: MutableMap<String, Pulse> = mutableMapOf()) : Module()

        class Broadcaster() : Module()
        data class FlipFlop(var state: Boolean = false) : Module()
    }
    enum class Pulse {
        LOW,
        High,
    }

    data class ModuleConfiguration(val type: Module, val name: String, val dest: List<String>)
    data class Input(val configuration: List<ModuleConfiguration>)

    class Parser : Grammar<Input>() {

        val comma by literalToken(", ")
        val follows by literalToken(" -> ")
        val id by regexToken("(%|&)?[a-z]+")
        val enter by regexToken("\\n|\\r")

        val line = id and skip(follows) and separated(id, comma) and skip(optional(enter))
        val lineParser = line use {
            val (module, id) = if (this.t1.text.startsWith("&")) {
                Pair(Module.Conjuntion(), this.t1.text.substring(1))
            } else if (this.t1.text.startsWith("%")) {
                Pair(Module.FlipFlop(), this.t1.text.substring(1))
            } else {
                Pair(Module.Broadcaster(), this.t1.text)
            }
            val dest = this.t2.terms.map { it.text }

            ModuleConfiguration(module, id, dest)
        }

        override val rootParser by oneOrMore(lineParser) use {
            val default = listOf(
                ModuleConfiguration(Module.Button(), "button", listOf(this.first { it.name == "broadcaster" }.name)),
                ModuleConfiguration(Module.Output(), "output", emptyList()),
            )
            Input(default + this)
        }
    }

    fun activate(input: Pulse, inputName: String, conf: ModuleConfiguration): List<Triple<Pulse, String, String>> {
        return when (conf.type) {
            is Module.Output -> emptyList()
            is Module.Broadcaster -> conf.dest.map { Pair(input, it) }
            is Module.Button -> conf.dest.map { Pair(Pulse.LOW, it) }
            is Module.Conjuntion -> {
                conf.type.remember[inputName] = input
                val pulseType = if (conf.type.remember.values.all { it == Pulse.High }) Pulse.LOW else Pulse.High
                conf.dest.map { Pair(pulseType, it) }
            }
            is Module.FlipFlop -> {
                if (input == Pulse.LOW) {
                    conf.type.state = !conf.type.state
                    conf.dest.map { Pair(if (conf.type.state) Pulse.High else Pulse.LOW, it) }
                } else {
                    emptyList()
                }
            }
        }.map { tuple -> Triple(tuple.first, tuple.second, conf.name) }
    }

    fun solvePart1(): Long {
        val puzzle = Parser().parseToEnd(input)

        // fill remember
        puzzle.configuration.filter { it.type is Module.Conjuntion }.forEach { module ->
            val name = module.name
            puzzle.configuration.filter { it.dest.contains(name) }.forEach { dep ->
                (module.type as Module.Conjuntion).remember[dep.name] = Pulse.LOW
            }
        }

        val configuration = puzzle.configuration.associate { it.name to it }

        val buttonPressed = runSignals(configuration).take(1000).toList()

        val x = buttonPressed.map { r ->
            val low = r.count { it.first == Pulse.LOW }
            val high = r.count { it.first == Pulse.High }
            Pair(low, high)
        }
        val totalLow = x.sumOf { it.first.toLong() }
        val totalHigh = x.sumOf { it.second.toLong() }

        return totalLow * totalHigh
    }

    private fun runSignals(configuration: Map<String, ModuleConfiguration>): Sequence<List<Triple<Pulse, String, String>>> {
        var i = 0

        return generateSequence {
            i = i + 1
            val queue = ArrayDeque<Triple<Pulse, String, String>>()
            queue.add(Triple(Pulse.LOW, "button", ""))
            val result: MutableList<Triple<Pulse, String, String>> = mutableListOf()
            while (!queue.isEmpty()) {
                val (pulse, name, source) = queue.removeFirst()
                val toAdd = if (name in configuration) {
                    val conf = configuration[name]!!
                    activate(pulse, source, conf)
                } else {
                    emptyList()
                }

                result.addAll(
                    toAdd.map {
                        Triple(it.first, "${it.third} -${it.first}-> ${it.second}", it.second)
                    },
                )
                queue.addAll(toAdd)
            }

            result
        }
    }

    fun solvePart2(): Long {
        val puzzle = Parser().parseToEnd(input)

        val configuration = puzzle.configuration.associate { it.name to it }
        puzzle.configuration.filter { it.type is Module.Conjuntion }.forEach { module ->
            val name = module.name
            puzzle.configuration.filter { it.dest.contains(name) }.forEach { dep ->
                (module.type as Module.Conjuntion).remember[dep.name] = Pulse.LOW
            }
        }

        val buttonPressed = runSignals(configuration).take(8200).toList()

        val kd = buttonPressed.mapIndexed { index, it -> Pair(index + 1, it.filter { it.second.startsWith("kd") && it.first == Pulse.High }) }.filter { !it.second.isEmpty() }.first().first.toLong()
        val zf = buttonPressed.mapIndexed { index, it -> Pair(index + 1, it.filter { it.second.startsWith("zf") && it.first == Pulse.High }) }.filter { !it.second.isEmpty() }.first().first.toLong()
        val vg = buttonPressed.mapIndexed { index, it -> Pair(index + 1, it.filter { it.second.startsWith("vg") && it.first == Pulse.High }) }.filter { !it.second.isEmpty() }.first().first.toLong()
        val gs = buttonPressed.mapIndexed { index, it -> Pair(index + 1, it.filter { it.second.startsWith("gs") && it.first == Pulse.High }) }.filter { !it.second.isEmpty() }.first().first.toLong()

        return kd * zf * vg * gs
    }
}
