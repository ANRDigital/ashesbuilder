package org.anrdigital.ashesbuilder

import com.google.gson.Gson
import org.anrdigital.ashesbuilder.game.Card
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    private val testString = """{
        "name": "Abundance",
        "stub": "abundance",
        "release": 0,
        "type": "Ready Spell",
        "placement": "Spellboard",
        "cost": [
          "[[main]]",
          "1 [[illusion:class]]"
        ],
        "weight": 106,
        "text": [
          {
            "cost": [
              "[[main]]",
              "[[exhaust]]"
            ],
            "text": "All players may draw up to 2 cards. For each card they cannot or do not draw, deal 1 damage to their Phoenixborn."
          },
          {
            "name": "Focus 1",
            "text": "Reduce the damage your Phoenixborn receives from this spell by 1."
          },
          {
            "name": "Focus 2",
            "text": "Reduce the damage your Phoenixborn receives from this spell by an additional 1."
          }
        ],
        "dice": [
          "illusion"
        ]
    }"""

    @Test
    fun addition_isCorrect() {
        val gson = Gson()
        val card = gson.fromJson<Card>(testString, Card::class.java)
        assertEquals("abundance", card.stub)
    }
}