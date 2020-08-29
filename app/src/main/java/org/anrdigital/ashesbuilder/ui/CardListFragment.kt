package org.anrdigital.ashesbuilder.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.anrdigital.ashesbuilder.R
import org.anrdigital.ashesbuilder.game.Card
import org.anrdigital.ashesbuilder.game.CardCount
import org.anrdigital.ashesbuilder.util.ImageDisplayer
import org.anrdigital.ashesbuilder.util.TextFormatter
import org.anrdigital.ashesbuilder.util.TextFormatter.getFormattedString
import org.koin.android.viewmodel.ext.android.sharedViewModel

class CardListFragment : Fragment(){
    private lateinit var mAdapter: CardListRecyclerViewAdapter
    private val vm: CardListViewModel by sharedViewModel()

    companion object {
        fun newInstance() = CardListFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.card_list_items, container, false)

//        activity?.title = viewModel.getDeckTitle()

//        val gridView: GridView = view.findViewById<GridView>(R.id.gridView)
//        gridView.adapter = CardCountImageAdapter(activity, vm.cardCounts)
//        gridView.onItemClickListener = OnItemClickListener { parent, view, position, id ->
//            vm.position = position
//            var action = CardGridFragmentDirections.HomeToFullscreenCardsFragment()
//            findNavController().navigate(action)
//        }
//        gridView.onItemLongClickListener = OnItemLongClickListener { adapterView: AdapterView<*>, view: View?, pos: Int, id: Long ->
//            val item = adapterView.getItemAtPosition(pos) as CardCount
//            val card = item.card
//            NrdbHelper.ShowNrdbWebPage(requireContext(), card)
//            true
//        }

        if (view is RecyclerView) {
            val context = view.getContext()
            view.layoutManager = LinearLayoutManager(context)
            mAdapter = CardListRecyclerViewAdapter(vm.cardCounts, context){
                showCardDialog(it.card!!)
            }
            view.adapter = mAdapter

        }
        return view
    }

    private fun showCardDialog(card: Card) {
        SimpleImageAlertDialog(requireContext(), card).show()
    }

    private class SimpleImageAlertDialog (context: Context, card: Card) :
        AlertDialog(context) {
        init {
            val view = layoutInflater.inflate(R.layout.simple_image_dialog, null)
            val img: ImageView = view.findViewById(R.id.dialog_imageview)
            ImageDisplayer.fill(img, card, context)
            setView(view)
            img.setOnClickListener(View.OnClickListener { // I want the dialog to close at this point
                dismiss()
            })
        }
    }

    class CardListRecyclerViewAdapter(
        private val cardCounts: List<CardCount>,
        private val context: Context,
        private val listener: (CardCount) -> Unit
    ):
        RecyclerView.Adapter<CardListRecyclerViewAdapter.CardListViewHolder>() {
        class CardListViewHolder(itemView: View, private val context: Context): RecyclerView.ViewHolder(itemView) {
            private lateinit var item: CardCount
            private var cardImage: ImageView = itemView.findViewById(R.id.imgImage)
            private var lblTitle: TextView = itemView.findViewById(R.id.lblTitle)
            private var lblTextView: TextView = itemView.findViewById(R.id.lblText)
            private var lblCost: TextView = itemView.findViewById(R.id.lblCost)
            private var lblCardType: TextView = itemView.findViewById(R.id.lblCardType)
            private var lblPlacement: TextView = itemView.findViewById(R.id.lblPlacement)
            private var lblAttack: TextView = itemView.findViewById(R.id.lblAttack)
            private var lblLife: TextView = itemView.findViewById(R.id.lblLife)
            private var lblRecover: TextView = itemView.findViewById(R.id.lblRecover)

            fun setItem(cardCount: CardCount) {
                item = cardCount
                val card = cardCount.card
                lblTitle.text = card?.name!!
                ImageDisplayer.fillSmall(cardImage, card, context)
                lblTextView.text = TextFormatter.formatCardText(card, context)

                lblAttack.text = if (card.attack != null) {
                    "Attack: ${card.attack}"
                }
                else if (card.battlefield != null){
                    "Battlefield: ${card.battlefield}"
                }
                else ""

                lblLife.text = if (card.life != null) "Life: ${card.life}" else ""

                lblRecover.text = if (card.recover != null) {
                    "Recover: ${card.recover}"
                }
                else if (card.spellboard != null){
                    "Spellboard: ${card.spellboard}"
                }
                else ""
                
                lblCost.text = ""
                if (card.cost != null && card.cost.isNotEmpty()) {
                    for ((i, c) in card.cost.withIndex()) {

                        lblCost.append(getFormattedString(context, c))
                        lblCost.append("\n")
                    }
                }
                lblCardType.text = card.type
                lblPlacement.text = card.placement
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardListViewHolder {
            var view = LayoutInflater.from(parent.context).inflate(
                R.layout.card_list_item,
                parent,
                false
            )

            return CardListViewHolder(view, context)
        }

        override fun onBindViewHolder(holder: CardListViewHolder, position: Int) {
            val item = cardCounts[position]
            holder.setItem(item)
            holder.itemView.setOnClickListener { listener(item) }
        }

        override fun getItemCount(): Int {
            return cardCounts.size
        }
    }
}
