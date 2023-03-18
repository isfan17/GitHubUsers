package com.isfan17.githubusers.ui.detail.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.isfan17.githubusers.R
import com.isfan17.githubusers.helper.Result
import com.isfan17.githubusers.data.model.User
import com.isfan17.githubusers.databinding.FragmentDescriptionBinding
import com.isfan17.githubusers.ui.detail.DetailActivity
import com.isfan17.githubusers.ui.viewmodels.DetailViewModel
import com.isfan17.githubusers.ui.viewmodels.ViewModelFactory

class DescriptionFragment : Fragment() {

    private var _binding: FragmentDescriptionBinding? = null
    private val binding get() = _binding!!

    private val TAG = "DescriptionFragment"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDescriptionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val factory: ViewModelFactory = ViewModelFactory.getInstance(requireActivity())
        val viewModel: DetailViewModel by viewModels {
            factory
        }

        val username = arguments?.getString(DetailActivity.EXTRA_USERNAME)

        binding.btnShare.setOnClickListener {
            val intent = Intent()
            intent.action = Intent.ACTION_SEND
            intent.putExtra(Intent.EXTRA_TEXT, "https://github.com/$username")
            intent.type = "text/plain"
            startActivity(Intent.createChooser(intent, "Share To:"))
        }

        if (username != null) {
            viewModel.getUser(username).observe(viewLifecycleOwner) { result ->
                if (result != null)
                {
                    when (result)
                    {
                        is Result.Loading -> {
                            showLoading(true)
                        }
                        is Result.Success -> {
                            showLoading(false)
                            binding.btnFavorite.isChecked = result.data.favorite
                            setUserDescription(result.data)
                        }
                        is Result.Error -> {
                            showLoading(false)
                            Log.e(TAG, result.error)
                            Toast.makeText(context, R.string.network_error_msg, Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }

            binding.btnFavorite.setOnClickListener {
                if (!binding.btnFavorite.isChecked)
                {
                    viewModel.removeFromFavorite(username)
                    Toast.makeText(context, "User Removed Successfully From Favorites", Toast.LENGTH_SHORT).show()
                }
                else
                {
                    viewModel.addToFavorite(username)
                    Toast.makeText(context, "User Added Successfully To Favorites", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun setUserDescription(user: User) {
        binding.apply {
            tvName.text = user.name ?: "-"
            tvUsername.text = user.login
            tvUsername.text = user.login
            tvRepo.text = user.publicRepos.toString()
            tvFollowers.text = user.followers.toString()
            tvFollowing.text = user.following.toString()
            tvLocation.text = user.location ?: "-"
            tvCompany.text = user.company ?: "-"
        }
        Glide.with(this)
            .load(user.avatarUrl)
            .centerCrop()
            .placeholder(R.drawable.ic_person_24)
            .into(binding.ivAvatar)
        binding.apply {
            tvName.visibility = View.VISIBLE
            tvUsername.visibility = View.VISIBLE
            ivAvatar.visibility = View.VISIBLE
            tvLocation.visibility = View.VISIBLE
            tvCompany.visibility = View.VISIBLE
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

}