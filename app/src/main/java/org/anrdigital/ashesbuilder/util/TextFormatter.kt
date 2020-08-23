package org.anrdigital.ashesbuilder.util

import android.content.Context
import android.graphics.drawable.Drawable
import android.text.Html
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ImageSpan
import org.anrdigital.ashesbuilder.R
import java.util.*


object TextFormatter {
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

        // replace all occurrences
        val span = SpannableString(Html.fromHtml(text.replace("\n", "<br />")))
        for (txt in map.keys) {
            var index = span.toString().toLowerCase().indexOf(txt)
            while (index >= 0) {
                val imageSpan = ImageSpan(context, map[txt]!!, ImageSpan.ALIGN_BOTTOM)
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

}