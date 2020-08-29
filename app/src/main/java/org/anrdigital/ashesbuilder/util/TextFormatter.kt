package org.anrdigital.ashesbuilder.util

import android.content.Context
import android.text.Html
import android.text.Spannable
import android.text.SpannableString
import android.text.TextUtils
import android.text.style.ImageSpan
import org.anrdigital.ashesbuilder.R
import org.anrdigital.ashesbuilder.game.Card
import org.anrdigital.ashesbuilder.game.CardTextItem
import java.util.*
import kotlin.text.Typography.bullet


object TextFormatter {
    const val INEXHAUSTIBLE = "[[inexhaustible]]"
    const val DIVIDER = "[[divider]]"

//    fun FormatCardTitle(card: Card): String {
//        val titleText: String
//        val strUnique = if (card.isUniqueness()) "• " else ""
//        titleText = if (!card.getSubtype().isEmpty()) {
//            strUnique + card.getTitle().toString() + " (" + card.getSubtype().toString() + ")"
//        } else {
//            strUnique + card.getTitle()
//        }
//        return titleText
//    }
//
//    fun FormatCardIcons(context: Context?, card: Card): SpannableString {
//        val iconText: SpannableString
//        iconText = when (card.getTypeCode()) {
//            Card.Type.EVENT, Card.Type.HARDWARE, Card.Type.OPERATION, Card.Type.RESOURCE -> getFormattedString(
//                context,
//                card.getCost().toString() + " [credit]"
//            )
//            Card.Type.AGENDA -> getFormattedString(
//                context,
//                card.getAdvancementCost()
//                    .toString() + " [credit]" + "  " + card.getAgendaPoints() + " [agenda]"
//            )
//            Card.Type.ASSET, Card.Type.UPGRADE -> getFormattedString(
//                context,
//                card.getCost().toString() + " [credit]" + "  " + card.getTrashCost() + " [trash]"
//            )
//            Card.Type.ICE -> getFormattedString(
//                context,
//                card.getCost().toString() + " [credit]" + "  " + card.getStrength() + "[fist]"
//            )
//            Card.Type.PROGRAM -> getFormattedString(
//                context,
//                card.getCost()
//                    .toString() + " [credit]" + "  " + card.getMemoryUnits() + " [mu]" + "  " + card.getStrength() + "[fist]"
//            )
//            else -> SpannableString.valueOf("")
//        }
//        return iconText
//    }

    fun getFormattedString(context: Context, text: String): SpannableString {
        val map = HashMap<String, Int>()
        map["[[main]]"] = R.drawable.main_action
        map["[[side]]"] = R.drawable.side_action
        map["[[basic]]"] = R.drawable.basic_magic
        map["[[ceremonial:class]]"] = R.drawable.ceremonial_class
        map["[[ceremonial:power]]"] = R.drawable.ceremonial_power
        map["[[charm:class]]"] = R.drawable.charm_class
        map["[[charm:power]]"] = R.drawable.charm_power
        map["[[discard]]"] = R.drawable.discard
        map["[[divine:class]]"] = R.drawable.divine_class
        map["[[divine:power]]"] = R.drawable.divine_power
        map["[[exhaust]]"] = R.drawable.exhaust
        map["[[illusion:class]]"] = R.drawable.illusion_class
        map["[[illusion:power]]"] = R.drawable.illusion_power
        map["[[natural:class]]"] = R.drawable.natural_class
        map["[[natural:power]]"] = R.drawable.natural_power
        map["[[sympathy:class]]"] = R.drawable.sympathy_class
        map["[[sympathy:power]]"] = R.drawable.sympathy_power
        map[INEXHAUSTIBLE] = R.drawable.icon_inexhaustible
        map[DIVIDER] = R.drawable.icon_diamond_bullet


        // replace all occurrences
        //todo: probably don't need Html here - is there html in the card data? (ANR legacy)
        val span = SpannableString(Html.fromHtml(text.replace("\n", "<br />")))
        for (txt in map.keys) {
            var index = span.toString().toLowerCase().indexOf(txt)
            while (index >= 0) {
                val imageSpan = ImageSpan(context, map[txt]!!, ImageSpan.ALIGN_BASELINE)
                imageSpan.drawable.setBounds(0, 0, 5, 5)
                span.setSpan(
                    imageSpan,
                    index,
                    index + txt.length,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                index = span.toString().indexOf(txt, index + 1)
            }
        }
        return span
    }

    fun formatCardText(card: Card, context: Context): CharSequence {
        var result: CharSequence = ""
        if (card.text.isNotEmpty()) {
            for ((i, t) in card.text.withIndex()) {
                if (i > 0)
                    result = TextUtils.concat(result, "\n")
                //todo: remove null checks when gson not used
                result = TextUtils.concat(result, formatCardTextItem(t, context))
            }
        }

        return result
    }

    private fun formatCardTextItem(
        t: CardTextItem,
        context: Context
    ): CharSequence {
        val separator = ": "
        var result: CharSequence = ""
        val output = ArrayList<CharSequence>()
        var named = false
        if (t.inexhaustible != null && t.inexhaustible)
            output.add(getFormattedString(context, INEXHAUSTIBLE))

        if (t.name != null && t.name.isNotEmpty()) {
            output.add(TextUtils.concat(result, t.name))
            named = true
        }

        if (t.cost != null && t.cost.isNotEmpty()) {
            output.add(formatCardTextItemCost(t, context))
        }

        if (!named)
            output.add(getFormattedString(context, t.text))

        for ((i, item) in output.withIndex()){
            if (i > 0)
                result = TextUtils.concat(result, separator)
            result = TextUtils.concat(result, item)
        }
        return result
    }

    private fun formatCardTextItemCost(
        t: CardTextItem,
        context: Context
    ): CharSequence {
        var textCost: CharSequence = ""
        for ((i, tc) in t.cost.withIndex()) {
            if (i > 0)
                textCost = TextUtils.concat(
                    textCost,
                    bullet.toString()
                ) // getFormattedString(context, DIVIDER))

            textCost = TextUtils.concat(textCost, getFormattedString(context, tc))
        }
        return textCost
    }

}