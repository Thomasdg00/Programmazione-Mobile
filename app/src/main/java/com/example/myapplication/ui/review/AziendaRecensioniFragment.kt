package com.example.myapplication.ui.review

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.data.model.Review
import com.example.myapplication.ui.azienda.AziendaRecensioniViewModel

class AziendaRecensioniFragment : Fragment() {
    private val viewModel: ReviewViewModel by viewModels()
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ReviewAdapter
    private lateinit var buttonAddReview: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_azienda_recensioni, container, false)
        recyclerView = view.findViewById(R.id.recyclerViewReviews)
        buttonAddReview = view.findViewById(R.id.buttonAddReview)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = ReviewAdapter()
        recyclerView.adapter = adapter

        // Set current user ID for adapter
        adapter.currentUserId = com.google.firebase.auth.FirebaseAuth.getInstance().currentUser?.uid

        // Edit callback
        adapter.onEdit = { review ->
            showEditReviewDialog(review)
        }
        // Delete callback
        adapter.onDelete = { review ->
            androidx.appcompat.app.AlertDialog.Builder(requireContext())
                .setTitle("Elimina recensione")
                .setMessage("Sei sicuro di voler eliminare questa recensione?")
                .setPositiveButton("Elimina") { _, _ ->
                    viewModel.deleteReview(review)
                }
                .setNegativeButton("Annulla", null)
                .show()
        }

        buttonAddReview.setOnClickListener {
            // Prevent multiple reviews per user per company
            val userId = com.google.firebase.auth.FirebaseAuth.getInstance().currentUser?.uid ?: ""
            val companyId = arguments?.getString("companyId") ?: return@setOnClickListener
            val alreadyReviewed = adapter.currentUserId != null &&
                    adapter.currentUserId?.let { id ->
                        adapter.itemCount > 0 &&
                                adapter.reviews.any { it.userId == id }
                    } == true
            if (alreadyReviewed) {
                androidx.appcompat.app.AlertDialog.Builder(requireContext())
                    .setTitle("Recensione già presente")
                    .setMessage("Hai già inserito una recensione per questa azienda. Puoi modificarla o eliminarla.")
                    .setPositiveButton("OK", null)
                    .show()
            } else {
                showAddReviewDialog()
            }
        }

        return view
    }

    private var selectedMediaUris: MutableList<android.net.Uri> = mutableListOf()
    private val pickMediaLauncher =
        registerForActivityResult(androidx.activity.result.contract.ActivityResultContracts.GetMultipleContents()) { uris ->
            if (uris != null) {
                selectedMediaUris.clear()
                selectedMediaUris.addAll(uris)
            }
        }

    private fun showAddReviewDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_add_review, null)
        val ratingBarAmbiente =
            dialogView.findViewById<android.widget.RatingBar>(R.id.ratingBarAmbiente)
        val ratingBarRetribuzione =
            dialogView.findViewById<android.widget.RatingBar>(R.id.ratingBarRetribuzione)
        val ratingBarCrescita =
            dialogView.findViewById<android.widget.RatingBar>(R.id.ratingBarCrescita)
        val ratingBarWLB = dialogView.findViewById<android.widget.RatingBar>(R.id.ratingBarWLB)
        val editTextRole = dialogView.findViewById<android.widget.EditText>(R.id.editTextReviewRole)
        val editTextComment =
            dialogView.findViewById<android.widget.EditText>(R.id.editTextReviewComment)
        val checkBoxAnonymous =
            dialogView.findViewById<android.widget.CheckBox>(R.id.checkBoxAnonymous)
        val buttonAddMedia = dialogView.findViewById<android.widget.Button>(R.id.buttonAddMedia)
        val mediaPreviewContainer =
            dialogView.findViewById<android.widget.LinearLayout>(R.id.mediaPreviewContainer)

        selectedMediaUris.clear()
        buttonAddMedia.setOnClickListener {
            pickMediaLauncher.launch("image/*")
        }

        androidx.appcompat.app.AlertDialog.Builder(requireContext())
            .setTitle("Aggiungi Recensione")
            .setView(dialogView)
            .setPositiveButton("Salva") { _, _ ->
                val ambiente = ratingBarAmbiente.rating.toInt()
                val retribuzione = ratingBarRetribuzione.rating.toInt()
                val crescita = ratingBarCrescita.rating.toInt()
                val wlb = ratingBarWLB.rating.toInt()
                val comment = editTextComment.text.toString()
                val role = editTextRole.text.toString()
                val anonymous = checkBoxAnonymous.isChecked
                val companyId = arguments?.getString("companyId") ?: return@setPositiveButton
                val userId =
                    com.google.firebase.auth.FirebaseAuth.getInstance().currentUser?.uid ?: ""
                val userName =
                    com.google.firebase.auth.FirebaseAuth.getInstance().currentUser?.displayName
                        ?: ""
                // Upload immagini su Firebase Storage e salva url
                val storage = com.google.firebase.storage.FirebaseStorage.getInstance().reference
                val mediaUrls = mutableListOf<String>()
                if (selectedMediaUris.isNotEmpty()) {
                    for (uri in selectedMediaUris) {
                        val ref =
                            storage.child("review_media/${System.currentTimeMillis()}_${uri.lastPathSegment}")
                        ref.putFile(uri).addOnSuccessListener {
                            ref.downloadUrl.addOnSuccessListener { downloadUri ->
                                mediaUrls.add(downloadUri.toString())
                                if (mediaUrls.size == selectedMediaUris.size) {
                                    saveReview(
                                        companyId,
                                        userId,
                                        userName,
                                        ambiente,
                                        retribuzione,
                                        crescita,
                                        wlb,
                                        comment,
                                        role,
                                        anonymous,
                                        mediaUrls
                                    )
                                }
                            }
                        }
                    }
                } else {
                    saveReview(
                        companyId,
                        userId,
                        userName,
                        ambiente,
                        retribuzione,
                        crescita,
                        wlb,
                        comment,
                        role,
                        anonymous,
                        mediaUrls
                    )
                }
            }
            .setNegativeButton("Annulla", null)
            .show()
    }

    private fun showEditReviewDialog(review: Review) {
        val dialogView = layoutInflater.inflate(R.layout.dialog_add_review, null)
        val ratingBarAmbiente =
            dialogView.findViewById<android.widget.RatingBar>(R.id.ratingBarAmbiente)
        val ratingBarRetribuzione =
            dialogView.findViewById<android.widget.RatingBar>(R.id.ratingBarRetribuzione)
        val ratingBarCrescita =
            dialogView.findViewById<android.widget.RatingBar>(R.id.ratingBarCrescita)
        val ratingBarWLB = dialogView.findViewById<android.widget.RatingBar>(R.id.ratingBarWLB)
        val editTextRole = dialogView.findViewById<android.widget.EditText>(R.id.editTextReviewRole)
        val editTextComment =
            dialogView.findViewById<android.widget.EditText>(R.id.editTextReviewComment)
        val checkBoxAnonymous =
            dialogView.findViewById<android.widget.CheckBox>(R.id.checkBoxAnonymous)
        val buttonAddMedia = dialogView.findViewById<android.widget.Button>(R.id.buttonAddMedia)
        val mediaPreviewContainer =
            dialogView.findViewById<android.widget.LinearLayout>(R.id.mediaPreviewContainer)

        // Pre-fill fields
        ratingBarAmbiente.rating = review.ratingAmbiente.toFloat()
        ratingBarRetribuzione.rating = review.ratingRetribuzione.toFloat()
        ratingBarCrescita.rating = review.ratingCrescita.toFloat()
        ratingBarWLB.rating = review.ratingWLB.toFloat()
        editTextRole.setText(review.role)
        editTextComment.setText(review.comment)
        checkBoxAnonymous.isChecked = review.anonymous
        // TODO: Show existing media if needed

        selectedMediaUris.clear()
        buttonAddMedia.setOnClickListener {
            pickMediaLauncher.launch("image/*")
        }

        androidx.appcompat.app.AlertDialog.Builder(requireContext())
            .setTitle("Modifica Recensione")
            .setView(dialogView)
            .setPositiveButton("Salva") { _, _ ->
                val ambiente = ratingBarAmbiente.rating.toInt()
                val retribuzione = ratingBarRetribuzione.rating.toInt()
                val crescita = ratingBarCrescita.rating.toInt()
                val wlb = ratingBarWLB.rating.toInt()
                val comment = editTextComment.text.toString()
                val role = editTextRole.text.toString()
                val anonymous = checkBoxAnonymous.isChecked
                val companyId = review.companyId
                val userId = review.userId
                val userName = review.userName
                val storage = com.google.firebase.storage.FirebaseStorage.getInstance().reference
                val mediaUrls = review.mediaUrls.toMutableList()
                if (selectedMediaUris.isNotEmpty()) {
                    for (uri in selectedMediaUris) {
                        val ref =
                            storage.child("review_media/${System.currentTimeMillis()}_${uri.lastPathSegment}")
                        ref.putFile(uri).addOnSuccessListener {
                            ref.downloadUrl.addOnSuccessListener { downloadUri ->
                                mediaUrls.add(downloadUri.toString())
                                if (mediaUrls.size == review.mediaUrls.size + selectedMediaUris.size) {
                                    saveEditedReview(
                                        review.copy(
                                            rating = ((ambiente + retribuzione + crescita + wlb) / 4),
                                            ratingAmbiente = ambiente,
                                            ratingRetribuzione = retribuzione,
                                            ratingCrescita = crescita,
                                            ratingWLB = wlb,
                                            comment = comment,
                                            role = role,
                                            anonymous = anonymous,
                                            mediaUrls = mediaUrls,
                                            timestamp = System.currentTimeMillis()
                                        )
                                    )
                                }
                            }
                        }
                    }
                } else {
                    saveEditedReview(
                        review.copy(
                            rating = ((ambiente + retribuzione + crescita + wlb) / 4),
                            ratingAmbiente = ambiente,
                            ratingRetribuzione = retribuzione,
                            ratingCrescita = crescita,
                            ratingWLB = wlb,
                            comment = comment,
                            role = role,
                            anonymous = anonymous,
                            timestamp = System.currentTimeMillis()
                        )
                    )
                }
            }
            .setNegativeButton("Annulla", null)
            .show()
    }

    private fun saveReview(
        companyId: String,
        userId: String,
        userName: String,
        ambiente: Int,
        retribuzione: Int,
        crescita: Int,
        wlb: Int,
        comment: String,
        role: String,
        anonymous: Boolean,
        mediaUrls: List<String>
    ) {
        val review = com.example.myapplication.data.model.Review(
            id = "",
            companyId = companyId,
            userId = userId,
            userName = userName,
            rating = ((ambiente + retribuzione + crescita + wlb) / 4),
            ratingAmbiente = ambiente,
            ratingRetribuzione = retribuzione,
            ratingCrescita = crescita,
            ratingWLB = wlb,
            comment = comment,
            role = role,
            anonymous = anonymous,
            mediaUrls = mediaUrls,
            timestamp = System.currentTimeMillis()
        )
        (viewModel as? com.example.myapplication.ui.review.ReviewViewModel)?.addReview(review)
    }

    private fun saveEditedReview(review: Review) {
        (viewModel as? com.example.myapplication.ui.review.ReviewViewModel)?.updateReview(review)
    }
    //return view


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val companyId = arguments?.getString("companyId") ?: return
        viewModel.getReviewsForCompany(companyId)
        viewModel.reviews.observe(viewLifecycleOwner) { reviews ->
            adapter.submitList(reviews)
        }
    }
}