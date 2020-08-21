package org.anrdigital.ashesbuilder.game

import java.net.URL

data class Card(
    // any conjurations linked to this card
    val conjurations: List<String>,
    // cost to play this card
    val cost: List<String>,
    // dice used by this card
    val dice: List<String>,
    // title of the card
    val name: String,
    // placement of the card
    val placement: String,
    // what product release the card came in
    val release: Int,
    // programmatic identifier e.g. for image url
    val stub: String,
    // text items that appear on the card - these can be actions, simple text, etc
    val text: List<CardTextItem>,
    // type of the card e.g ally, phoenixborn
    val type: String,
    // weight of the card - ??
    val weight: Int
) {
    val imageFileName: String
        get() = "$stub.png"
    val imageSrc: URL
        get() = URL("https://ashes.live/images/cards/$imageFileName")

}