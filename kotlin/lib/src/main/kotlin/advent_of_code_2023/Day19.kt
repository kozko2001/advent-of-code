package advent_of_code_2023

import com.github.h0tk3y.betterParse.combinators.and
import com.github.h0tk3y.betterParse.combinators.oneOrMore
import com.github.h0tk3y.betterParse.combinators.optional
import com.github.h0tk3y.betterParse.combinators.or
import com.github.h0tk3y.betterParse.combinators.separated
import com.github.h0tk3y.betterParse.combinators.skip
import com.github.h0tk3y.betterParse.combinators.use
import com.github.h0tk3y.betterParse.grammar.Grammar
import com.github.h0tk3y.betterParse.grammar.parseToEnd
import com.github.h0tk3y.betterParse.lexer.literalToken
import com.github.h0tk3y.betterParse.lexer.regexToken

class Day19(private val input: String) {

    enum class PartAttribute {
        A,
        M,
        X,
        S,
    }
    enum class Operation {
        GreaterThan,
        LessThan,
    }

    data class Part(val x: Int, val m: Int, val a: Int, val s: Int) {
        fun get(partAttribute: PartAttribute) = when (partAttribute) {
            PartAttribute.M -> m
            PartAttribute.X -> x
            PartAttribute.S -> s
            PartAttribute.A -> a
        }
    }
    data class Condition(val op: Operation, val s: PartAttribute, val num: Int, val negate: Boolean = false) {
        fun valid(part: Part): Boolean {
            val p1 = part.get(s)
            val p2 = num

            val r = when (op) {
                Operation.LessThan -> p1 < p2
                Operation.GreaterThan -> p1 > p2
            }
            return if (negate) !r else r
        }
    }
    data class Rule(val condition: Condition?, val destination: String) {
        fun valid(part: Part): Boolean = condition?.valid(part) ?: true
    }
    data class Workflow(val name: String, val rules: List<Rule>)

    data class Input(val workflows: Map<String, Workflow>, val parts: List<Part>)

    class Parser : Grammar<Input>() {
        val lcurlyBracket by literalToken("{")
        val rcurlyBracket by literalToken("}")
        val gt by literalToken(">")
        val lt by literalToken("<")
        val colon by literalToken(":")
        val comma by literalToken(",")
        val equal by literalToken("=")
        val id by regexToken("[a-z]+|A|R")
        val enter by regexToken("\\n|\\r")
        val digit by regexToken("[0-9]+")
        val digitParser = digit use { this.text.toInt() }

        val comparision = gt or lt
        val comprisonParser = comparision.use {
            when (this.text) {
                ">" -> Operation.GreaterThan
                else -> Operation.LessThan
            }
        }
        val condition = id and comprisonParser and digitParser and skip(colon)
        val conditionParser = condition use {
            val part = when (this.t1.text) {
                "a" -> PartAttribute.A
                "m" -> PartAttribute.M
                "x" -> PartAttribute.X
                else -> PartAttribute.S
            }
            Condition(this.t2, part, this.t3)
        }
        val rule = optional(conditionParser) and id
        val ruleParser = rule use {
            Rule(this.t1, this.t2.text)
        }

        val workflowLine = id and skip(lcurlyBracket) and separated(ruleParser, comma) and skip(rcurlyBracket) and skip(enter)
        val workflowLineParser = workflowLine use {
            Workflow(this.t1.text, this.t2.terms)
        }

        val partDefinition = id and skip(equal) and digitParser
        val partDefinitionParser = partDefinition use {
            this.t2
        }

        val part = skip(lcurlyBracket) and separated(partDefinitionParser, comma) and skip(rcurlyBracket) and skip(optional(enter))
        val partParser = part use {
            Part(this.terms[0], this.terms[1], this.terms[2], this.terms[3])
        }

        override val rootParser by oneOrMore(workflowLineParser) and skip(enter) and oneOrMore(partParser) use {
            Input(this.t1.associate { it.name to it }, this.t2)
        }
    }

    fun followWorkflow(workflows: Map<String, Workflow>, part: Part): List<String> {
        return generateSequence("in") {
            if (it == "A" || it == "R") {
                null
            } else {
                val workflow = workflows[it]!!
                val rule = workflow.rules.first { it.valid(part) }
                rule.destination
            }
        }.toList()
    }

    fun solvePart1(): Int {
        val puzzle = Parser().parseToEnd(input)

        val paths = puzzle.parts.map {
            Pair(it, followWorkflow(puzzle.workflows, it))
        }
            .filter { it.second.last() == "A" }
            .map { it.first }
            .map { it.s + it.x + it.a + it.m }

        println(paths.sum())
        return paths.sum()
    }

    fun unifyFormula(workflows: Map<String, Workflow>, rules: List<Rule>, pathSoFar: List<Condition>): List<Pair<List<Condition>, String>> {
        val x = rules.flatMapIndexed { index, rule ->
            val prevConditions = rules.filterIndexed { i, _ -> i < index }.mapNotNull { it.condition }.map { Condition(it.op, it.s, it.num, true) }
            val currCondition = if (rule.condition != null) listOf(rule.condition) else emptyList()
            if (rule.destination == "A" || rule.destination == "R") {
                listOf(Pair(currCondition + prevConditions + pathSoFar, rule.destination))
            } else {
                val nextWorkflow = workflows[rule.destination]!!
                unifyFormula(workflows, nextWorkflow.rules, prevConditions + currCondition + pathSoFar)
            }
        }
        return x
    }

    data class PartRange(val x: IntRange, val a: IntRange, val m: IntRange, val s: IntRange) {
        fun size(): Long = (x.last - x.first + 1).toLong() * (m.last - m.first + 1) * (a.last - a.first + 1) * (s.last - s.first + 1)
    }

    fun createPartRange(condition: List<Condition>): PartRange {
        val partTypes = listOf(PartAttribute.X, PartAttribute.A, PartAttribute.M, PartAttribute.S)

        val r = partTypes.map { partAttribute ->
            val interestingConditions = condition.filter { it.s == partAttribute }
            if (interestingConditions.isEmpty()) {
                1..4000
            } else {
                val gt = interestingConditions.filter { (it.op == Operation.GreaterThan && !it.negate) || (it.op == Operation.LessThan && it.negate) }
                    .maxOfOrNull { if (it.negate) it.num else it.num +1 } ?: 1
                val lt = interestingConditions.filter { (it.op == Operation.LessThan && !it.negate) || (it.op == Operation.GreaterThan && it.negate) }
                    .minOfOrNull { if (it.negate) it.num else it.num - 1 } ?: 4000

                gt..lt
            }
        }
        return PartRange(r[0], r[1], r[2], r[3])
    }

    fun solvePart2(): Long {
        val puzzle = Parser().parseToEnd(input)

        val cond = unifyFormula(puzzle.workflows, puzzle.workflows["in"]!!.rules, emptyList())
        val xxx = cond
            .map { Pair(it.second, createPartRange(it.first)) }
            .filter { it.first == "A" }
            .map {
                it.second.size()
            }
        return xxx.sum()
    }
}
