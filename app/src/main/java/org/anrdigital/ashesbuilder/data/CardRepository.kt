package org.anrdigital.ashesbuilder.data

import android.content.Context
import com.google.gson.Gson
import org.anrdigital.ashesbuilder.R
import org.anrdigital.ashesbuilder.game.Card

class CardRepository(val context: Context) {
    lateinit var allCards: List<Card>

    init {
        loadCards()
    }

    private fun loadCards() {
        val text = context.resources.openRawResource(R.raw.cards).bufferedReader().use { it.readText() }
        val gson = Gson()
        val cardArray = gson.fromJson(text, Array<Card>::class.java)
        allCards = cardArray.toList()
    }

    fun getCard(code: String): Card? {
        return allCards.find { c -> c.stub == code }
    }
}
