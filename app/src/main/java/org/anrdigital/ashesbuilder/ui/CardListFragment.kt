package org.anrdigital.ashesbuilder.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import android.widget.GridView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import org.anrdigital.ashesbuilder.R
import org.koin.android.viewmodel.ext.android.sharedViewModel

class CardListFragment : Fragment() {

    private val vm: CardListViewModel by sharedViewModel()

    companion object {
        fun newInstance() = CardListFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.card_list_items, container, false)

//        activity?.title = viewModel.getDeckTitle()

        val gridView: GridView = view.findViewById<GridView>(R.id.gridView)
        gridView.adapter = CardCountImageAdapter(activity, vm.cardCounts)
        gridView.onItemClickListener = OnItemClickListener { parent, view, position, id ->
            vm.position = position
            var action = CardGridFragmentDirections.HomeToFullscreenCardsFragment()
            findNavController().navigate(action)
        }
//        gridView.onItemLongClickListener = OnItemLongClickListener { adapterView: AdapterView<*>, view: View?, pos: Int, id: Long ->
//            val item = adapterView.getItemAtPosition(pos) as CardCount
//            val card = item.card
//            NrdbHelper.ShowNrdbWebPage(requireContext(), card)
//            true
//        }

        return view
    }

}
