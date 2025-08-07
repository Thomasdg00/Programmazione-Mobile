package com.example.myapplication

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AziendaFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AziendaFragment : Fragment() {

    companion object {
        private const val ARG_ITEM = "arg_item"

        fun newInstance(item: String): AziendaFragment {
            val fragment = AziendaFragment()
            val args = Bundle()
            args.putString(ARG_ITEM, item)
            fragment.arguments = args
            return fragment
        }
    }

    private var item: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        item = arguments?.getString(ARG_ITEM)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_azienda, container, false)
        // Usa 'item' per aggiornare la UI
        return view
    }
}
