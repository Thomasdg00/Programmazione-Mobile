package com.example.myapplication.ui.account

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myapplication.R
import com.example.myapplication.data.model.Review
import com.example.myapplication.data.model.UserProfile
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class AccountFragment : Fragment() {
    private val viewModel: AccountViewModel by viewModels()
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: UserReviewsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_account, container, false)
        recyclerView = view.findViewById(R.id.recyclerViewUserReviews)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = UserReviewsAdapter()
        recyclerView.adapter = adapter
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val userId = arguments?.getString("userId") ?: return
        viewModel.loadUserData(userId)

        viewModel.userProfile.observe(viewLifecycleOwner, Observer { profile ->
            if (profile != null) {
                view.findViewById<TextView>(R.id.textViewUserName).text = profile.fullName
                view.findViewById<TextView>(R.id.textViewUserAge).text = "Età: ${profile.age}"
                view.findViewById<TextView>(R.id.textViewUserJob).text = "Lavoro attuale: ${profile.currentJob}"
                view.findViewById<TextView>(R.id.textViewUserPastJobs).text = "Lavori passati: ${profile.pastJobs.joinToString(", ")}"
                val imageView = view.findViewById<ImageView>(R.id.imageViewProfile)
                if (profile.profileImageUrl.isNotEmpty()) {
                    Glide.with(this).load(profile.profileImageUrl).into(imageView)
                }
            }
        })

        viewModel.userReviews.observe(viewLifecycleOwner, Observer { reviews ->
            adapter.submitList(reviews)
        })

        // Logica modifica profilo
        val buttonEdit = view.findViewById<View>(R.id.buttonEditProfile)
        buttonEdit.setOnClickListener {
            val currentProfile = viewModel.userProfile.value ?: return@setOnClickListener
            showEditProfileDialog(currentProfile)
        }

        viewModel.updateProfileState.observe(viewLifecycleOwner, Observer { success ->
            if (success == true) {
                // Mostra feedback di successo
                android.widget.Toast.makeText(requireContext(), "Profilo aggiornato!", android.widget.Toast.LENGTH_SHORT).show()
            } else if (success == false) {
                android.widget.Toast.makeText(requireContext(), "Errore aggiornamento profilo", android.widget.Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun showEditProfileDialog(profile: UserProfile) {
        val dialogView = layoutInflater.inflate(R.layout.dialog_edit_profile, null)

        val inputName = dialogView.findViewById<TextInputEditText>(R.id.editTextName)
        val inputAge = dialogView.findViewById<TextInputEditText>(R.id.editTextAge)
        val inputJob = dialogView.findViewById<TextInputEditText>(R.id.editTextJob)
        val inputBio = dialogView.findViewById<TextInputEditText>(R.id.editTextBio)
        val inputProfileImage = dialogView.findViewById<TextInputEditText>(R.id.editTextProfileImage)

        inputName.setText(profile.fullName)
        inputAge.setText(profile.age.toString())
        inputJob.setText(profile.currentJob)
        inputBio.setText(profile.bio)
        inputProfileImage.setText(profile.profileImageUrl)

        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.edit_profile))
            .setView(dialogView)
            .setPositiveButton(getString(R.string.save)) { _, _ ->
                val updated = profile.copy(
                    fullName = inputName.text.toString(),
                    age = inputAge.text.toString().toIntOrNull() ?: 0,
                    currentJob = inputJob.text.toString(),
                    bio = inputBio.text.toString(),
                    profileImageUrl = inputProfileImage.text.toString()
                )
                viewModel.updateUserProfile(updated)
            }
            .setNegativeButton(getString(R.string.cancel), null)
            .show()
    }
}

class UserReviewsAdapter : RecyclerView.Adapter<UserReviewsAdapter.UserReviewViewHolder>() {
    private var reviews: List<Review> = emptyList()

    fun submitList(list: List<Review>) {
        reviews = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserReviewViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(android.R.layout.simple_list_item_2, parent, false)
        return UserReviewViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserReviewViewHolder, position: Int) {
        val review = reviews[position]
        holder.itemView.findViewById<TextView>(android.R.id.text1).text = "Azienda: ${review.companyId} - Voto: ${review.rating}"
        holder.itemView.findViewById<TextView>(android.R.id.text2).text = review.comment
    }

    override fun getItemCount() = reviews.size

    class UserReviewViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}
