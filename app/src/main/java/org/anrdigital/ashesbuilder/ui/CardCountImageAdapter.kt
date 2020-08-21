package org.anrdigital.ashesbuilder.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import org.anrdigital.ashesbuilder.R
import org.anrdigital.ashesbuilder.game.CardCount
import org.anrdigital.ashesbuilder.util.ImageDisplayer

/**
 * Created by sebast on 21/02/15.
 */
class CardCountImageAdapter(
    context: Context?,
    objects: List<CardCount?>?
) : ArrayAdapter<CardCount?>(context!!, 0, objects!!) {
    private class CardViewHolder {
        var imgCard: ImageView? = null
        var lblAmount: TextView? = null
    }

    override fun getView(
        position: Int,
        convertView: View?,
        parent: ViewGroup
    ): View {
        var convertView = convertView
        val viewHolder: CardViewHolder
        if (convertView == null) {
            convertView = LayoutInflater.from(context)
                .inflate(R.layout.card_list_grid_item, parent, false)
            viewHolder = CardViewHolder()
            viewHolder.imgCard =
                convertView.findViewById<View>(R.id.imgCard) as ImageView
            viewHolder.lblAmount =
                convertView.findViewById<View>(R.id.lblAmount) as TextView
            convertView.tag = viewHolder
        } else {
            viewHolder =
                convertView.tag as CardViewHolder
        }

        // Set the image]
        val cc = getItem(position)
        val card = cc!!.card
        ImageDisplayer.fill(viewHolder.imgCard!!, card, context)
        if (cc.count > 0) viewHolder.lblAmount!!.text =
            cc.count.toString() else viewHolder.lblAmount!!.visibility = View.GONE

        // Return the image
        return convertView!!
    }
}