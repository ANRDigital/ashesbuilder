package org.anrdigital.ashesbuilder.ui

import androidx.lifecycle.ViewModel
import org.anrdigital.ashesbuilder.data.CardRepository
import org.anrdigital.ashesbuilder.game.Card
import org.anrdigital.ashesbuilder.game.CardCount

class CardListViewModel(private val cardRepository: CardRepository) : ViewModel() {
    val cardCounts: List<CardCount>
            get(){return cardRepository.allCards
                .filter { card -> !card.isConjuration }
                .sortedBy { card -> card.type }
                .map { card -> CardCount(card, 0) }}

    var size: Int = 0
        get() = this.cardCounts.size

    var setName: String? = null
    private var cardCode: String? = null

    var position = 0
    // NO Card Sort
//    var cardCodes: ArrayList<String> = ArrayList()
//        set(cardCodes) {
//            field.addAll(cardCodes)
//            cardCounts.clear()
//
//            for (code: String in cardCodes){
//                cardCounts.add(CardCount(cardRepository.getCard(code), 0))
//            }
//            // NO Card Sort
//        }
//
//    fun setCardCode(cardCode: String?) {
//        cardCode?.let {
//            this.cardCode = it
//            cardCounts.add(CardCount(cardRepository.getCard(it), 0))
//        }
//    }

    fun isEmpty(): Boolean {
        return cardCounts.isEmpty()
    }

    fun getCurrentCardTitle(): String {
        return cardCounts[position].card!!.name
    }

}