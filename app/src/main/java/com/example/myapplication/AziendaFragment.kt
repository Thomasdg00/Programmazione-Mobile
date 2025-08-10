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

    private var companyId: String? = null
    private var currentUserId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        companyId = arguments?.getString(ARG_ITEM)
        // TODO: recupera l'ID utente corrente da auth/session
        currentUserId = com.google.firebase.auth.FirebaseAuth.getInstance().currentUser?.uid
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_azienda, container, false)
        val imageViewLogo = view.findViewById<android.widget.ImageView>(R.id.imageViewCompanyLogo)
        val textViewName = view.findViewById<android.widget.TextView>(R.id.textViewCompanyName)
        val textViewSector = view.findViewById<android.widget.TextView>(R.id.textViewCompanySector)
        val textViewLocation = view.findViewById<android.widget.TextView>(R.id.textViewCompanyLocation)
        val buttonEdit = view.findViewById<android.widget.Button>(R.id.buttonEditCompany)

        // Carica dati azienda da Firestore
        companyId?.let { id ->
            com.google.firebase.firestore.FirebaseFirestore.getInstance()
                .collection("companies").document(id).get()
                .addOnSuccessListener { doc ->
                    val name = doc.getString("name") ?: ""
                    val sector = doc.getString("sector") ?: ""
                    val location = doc.getString("location") ?: ""
                    val logoUrl = doc.getString("logoUrl") ?: ""
                    val createdBy = doc.getString("createdBy") ?: ""
                    textViewName.text = name
                    textViewSector.text = sector
                    textViewLocation.text = location
                    if (logoUrl.isNotEmpty()) {
                        com.bumptech.glide.Glide.with(this).load(logoUrl).into(imageViewLogo)
                    }
                    // Mostra il bottone solo se l'utente è il creatore
                    if (createdBy == currentUserId) {
                        buttonEdit.visibility = View.VISIBLE
                        buttonEdit.setOnClickListener {
                            // Apri dialog di modifica (riusa quella della lista)
                            val dialog = com.example.myapplication.ui.company.CompanyListFragment()
                            dialog.showEditCompanyDialog(
                                com.example.myapplication.data.model.Company(
                                    id = id,
                                    name = name,
                                    sector = sector,
                                    location = location,
                                    logoUrl = logoUrl,
                                    createdBy = createdBy
                                )
                            )
                        }
                    }
                }
        }
        return view
    }
}
