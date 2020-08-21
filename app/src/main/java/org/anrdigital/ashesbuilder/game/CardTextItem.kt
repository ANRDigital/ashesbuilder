package org.anrdigital.ashesbuilder.game

data class CardTextItem(
    val cost: List<String>,
    val inexhaustible: Boolean,
    val name: String,
    val text: String
)