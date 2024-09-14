package models

import android.util.Log
import utils.DEFAULT_ICONS

class MemoryGame(boardSize: BoardSize) {
    companion object {
        private const val TAG = "MemoryGame"
    }

    val cards: List<MemoryCard> = DEFAULT_ICONS.shuffled().take(boardSize.pairsCount)
        .let { ids -> (ids + ids).shuffled() }
        .map { id -> MemoryCard(id) }

    var numPairsFound = 0
    var numMoves = 0
    private var firstFlippedIndex: Int? = null
    private var secondFlippedIndex: Int? = null


    init {
        Log.i(TAG, "images length: ${cards.size}")
    }

    fun flipCard(position: Int): List<Int> {
        Log.i(TAG, "flipCard: $position")
        val ids = mutableListOf(position)
        val card = cards[position]
        if (card.isMatched || card.isFaceUp) return listOf()
        card.isFaceUp = true
        if (firstFlippedIndex == null) {
            firstFlippedIndex = position
        } else if (secondFlippedIndex == null) {
            secondFlippedIndex = position
            if (cards[firstFlippedIndex!!].identifier == cards[secondFlippedIndex!!].identifier) {
                cards[firstFlippedIndex!!].isMatched = true
                cards[secondFlippedIndex!!].isMatched = true
                ids.add(firstFlippedIndex!!)
                firstFlippedIndex = null
                secondFlippedIndex = null
                numPairsFound++
            }
        } else {
            cards[firstFlippedIndex!!].isFaceUp = false
            cards[secondFlippedIndex!!].isFaceUp = false
            ids.add(firstFlippedIndex!!)
            ids.add(secondFlippedIndex!!)
            firstFlippedIndex = position
            secondFlippedIndex = null
        }
        numMoves++
        return ids
    }
}
