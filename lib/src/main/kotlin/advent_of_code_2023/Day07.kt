package advent_of_code_2023

import com.github.h0tk3y.betterParse.combinators.*
import com.github.h0tk3y.betterParse.grammar.Grammar
import com.github.h0tk3y.betterParse.grammar.parseToEnd
import com.github.h0tk3y.betterParse.lexer.regexToken
import org.checkerframework.checker.units.qual.K

data class InputDay07(val hands: List<HandsDay07>)
data class HandsDay07(val hand: List<Day07.Card>, val bid: Int)

class Day07InputParser : Grammar<InputDay07>() {
    val digit by regexToken("\\d")
    val card by regexToken("A|K|Q|J|T")
    val possibleCard = digit or card use {
        this.text[0]
    }

    val enter by regexToken("\\n|\\r")
    val ws by regexToken("(\\s)+")

    val num = oneOrMore(digit) use {
        this.map { it.text }.joinToString("").toInt()
    }
    val hand = 5 times possibleCard

    val line = hand and skip(ws) and num and skip(optional(enter))

    override val rootParser by oneOrMore(line) use {
        InputDay07(
            this.map {
                HandsDay07(
                    it.t1.map { ch ->
                        when (ch) {
                            'A' -> Day07.Card.A
                            'K' -> Day07.Card.K
                            'Q' -> Day07.Card.Q
                            'J' -> Day07.Card.J
                            'T' -> Day07.Card.T
                            '9' -> Day07.Card.Nine
                            '8' -> Day07.Card.Eight
                            '7' -> Day07.Card.Seven
                            '6' -> Day07.Card.Six
                            '5' -> Day07.Card.Five
                            '4' -> Day07.Card.Four
                            '3' -> Day07.Card.Three
                            '2' -> Day07.Card.Two
                            else -> Day07.Card.Two
                        }
                    },
                    it.t2,
                )
            },
        )
    }
}

class Day07(private val input: String) {
    val parser = Day07InputParser()

    enum class Strength(val strength: Int) {
        FiveOfAKind(10),
        FourOfAKind(9),
        FullHouse(8),
        ThreeOfAKind(7),
        TwoPair(6),
        OnePair(5),
        HighCard(4),
    }

    enum class Card(val value: Int) {
        A(14),
        K(13),
        Q(12),
        J(11),
        T(10),
        Nine(9),
        Eight(8),
        Seven(7),
        Six(6),
        Five(5),
        Four(4),
        Three(3),
        Two(2),
    }

    fun cardStrength(cards: List<Card>): Strength {
        val counts = cards.groupingBy { it }.eachCount()
            .toList().sortedWith(compareByDescending { (_, count) -> count })

        val firstCount = counts[0].second
        val secondCount = counts.getOrNull(1)?.second ?: 0
        return when {
            firstCount == 5 -> Strength.FiveOfAKind
            firstCount == 4 -> Strength.FourOfAKind
            firstCount == 3 && secondCount == 2 -> Strength.FullHouse
            firstCount == 3 -> Strength.ThreeOfAKind
            firstCount == 2 && secondCount == 2 -> Strength.TwoPair
            firstCount == 2 -> Strength.OnePair
            else -> Strength.HighCard
        }
    }

    fun solvePart1(): Int {
        val puzzle = parser.parseToEnd(input)
        val strength = puzzle.hands.map { cardStrength(it.hand) }
        val sorted = puzzle.hands.zip(strength).sortedWith(compareBy({ it.second.strength }, { it.first.hand[0].value }, { it.first.hand[1].value }, { it.first.hand[2].value }, { it.first.hand[3].value }, { it.first.hand[4].value }))

        return sorted.mapIndexed { index, pair ->
            pair.first.bid * (index + 1)
        }.sum()
    }

    fun valueJoker(cards: List<Card>, index: Int): Int =
        when (cards[index]) {
            Card.J -> 1
            else -> cards[index].value
        }


    fun jokerWildcardProbability(cards: List<Card>): List<List<Card>> {
        val countOfJokers = cards.count { c -> c == Card.J }
        val otherCards = cards.firstOrNull { c -> c != Card.J }
        return when (countOfJokers) {
            0 -> listOf(cards)
            4 -> listOf(listOf(otherCards!!, otherCards, otherCards, otherCards, otherCards))
            5 -> listOf(listOf(Card.A, Card.A, Card.A, Card.A, Card.A))
            else -> {
                val i = cards.indexOfFirst { c -> c == Card.J }
                val possibleCards = listOf(
                    Card.A,
                    Card.Q,
                    Card.T,
                    Card.K,
                    Card.Nine,
                    Card.Eight,
                    Card.Seven,
                    Card.Six,
                    Card.Five,
                    Card.Four,
                    Card.Three,
                    Card.Two,
                )
                possibleCards.map { wildcard ->
                    cards.mapIndexed { index, card ->
                        when (index) {
                            i -> wildcard
                            else -> card
                        }
                    }
                }.flatMap { jokerWildcardProbability(it) }
            }
        }
    }

    fun solvePart2(): Int {
        val puzzle = parser.parseToEnd(input)
        val bestCards = puzzle.hands.map { jokerWildcardProbability(it.hand) }.map { hands ->
            val strength = hands.map { cardStrength(it) }
            val sorted = hands.zip(strength).sortedWith(compareBy({ it.second.strength }, { valueJoker(it.first, 0) }, { valueJoker(it.first, 1) }, { valueJoker(it.first, 2) }, { valueJoker(it.first, 3) }, { valueJoker(it.first, 4) }))
            sorted.last()
        }.map { it.first }

        val hands = bestCards.zip(puzzle.hands).map { HandsDay07(it.first, it.second.bid) }

        val strength = hands.map { cardStrength(it.hand) }

        val sorted = puzzle.hands.zip(strength).sortedWith(compareBy({ it.second.strength }, { valueJoker(it.first.hand, 0) }, { valueJoker(it.first.hand, 1) }, { valueJoker(it.first.hand, 2) }, { valueJoker(it.first.hand, 3) }, { valueJoker(it.first.hand, 4) }))

        return sorted.mapIndexed { index, pair ->
            pair.first.bid * (index + 1)
        }.sum()
    }
}
