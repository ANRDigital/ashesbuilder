package org.anrdigital.ashesbuilder.ui

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.anrdigital.ashesbuilder.R
import org.anrdigital.ashesbuilder.game.Card
import org.anrdigital.ashesbuilder.game.CardCount
import org.anrdigital.ashesbuilder.util.ImageDisplayer
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
        MaterialAlertDialogBuilder(requireContext()).apply{
            val view = layoutInflater.inflate(R.layout.simple_image_dialog, null)
            val img: ImageView = view.findViewById(R.id.dialog_imageview)
            ImageDisplayer.fill(img, card, context)
            this.setView(view)
        }.show()
    }

    class CardListRecyclerViewAdapter(
        private val cardCounts: List<CardCount>,
        private val context: Context,
        private val listener: (CardCount) -> Unit
    ):
        RecyclerView.Adapter<CardListRecyclerViewAdapter.CardViewHolder>() {
        class CardViewHolder(itemView: View, val context: Context): RecyclerView.ViewHolder(itemView) {
            private lateinit var item: CardCount
            private var cardImage: ImageView = itemView.findViewById(R.id.imgImage)
            private var lblTitle: TextView = itemView.findViewById(R.id.lblTitle)
            private var lblTextView: TextView = itemView.findViewById(R.id.lblText)
            private var lblCost: TextView = itemView.findViewById(R.id.lblCost)
            private var lblCardType: TextView = itemView.findViewById(R.id.lblCardType)
            private var lblPlacement: TextView = itemView.findViewById(R.id.lblPlacement)

            fun setItem(cardCount: CardCount) {
                item = cardCount
                val card = cardCount.card
                lblTitle.text = card?.name!!
                ImageDisplayer.fillSmall(cardImage, card, itemView.context)
                if (card.text.isNotEmpty())
                    lblTextView.text = card.text[0].text

                lblCost.text = ""
                if (card.cost.isNotEmpty()) {
                    for ((i, c) in card.cost.withIndex()) {
                        if (i > 0)
                            lblCost.append("\n")

                        lblCost.append(getFormattedString(context, c))
                    }
                }
                lblCardType.text = card.type
                lblPlacement.text = card.placement
            }
        }

        interface IListItemListener {
            fun onListItemClicked(cardCount: CardCount) {
                TODO("Not yet implemented")
            }

        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
            var view = LayoutInflater.from(parent.context).inflate(R.layout.card_list_item, parent, false)

            return CardViewHolder(view, context)
        }

        override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
            val item = cardCounts[position]
            holder.setItem(item)
            holder.itemView.setOnClickListener { listener(item) }
        }

        override fun getItemCount(): Int {
            return cardCounts.size
        }
    }
}
