package org.anrdigital.ashesbuilder.game

import java.net.URL

data class Card(
    val cost: List<String>,
    val dice: List<String>,
    val name: String,
    val placement: String,
    val release: Int,
    val stub: String,
    val text: List<Text>,
    val type: String,
    val weight: Int
) {
    val imageFileName: String
        get() = "$stub.png"
    val imageSrc: URL
        get() = URL("https://ashes.live/images/cards/$imageFileName")

}