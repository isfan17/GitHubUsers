package com.isfan17.githubusers.ui.detail.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.isfan17.githubusers.R
import com.isfan17.githubusers.helper.Result
import com.isfan17.githubusers.databinding.FragmentFollowBinding
import com.isfan17.githubusers.ui.adapters.UserAdapter
import com.isfan17.githubusers.ui.detail.DetailActivity
import com.isfan17.githubusers.ui.viewmodels.DetailViewModel
import com.isfan17.githubusers.ui.viewmodels.ViewModelFactory

class FollowersFragment: Fragment() {

    private var _binding: FragmentFollowBinding? = null
    private val binding get() = _binding!!

    private lateinit var userAdapter: UserAdapter
    private val TAG = "FollowersFragment"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFollowBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val factory: ViewModelFactory = ViewModelFactory.getInstance(requireActivity())
        val viewModel: DetailViewModel by viewModels {
            factory
        }

        setupRecyclerView()

        binding.tvNoData.text = getString(R.string.no_followers)

        val username = arguments?.getString(DetailActivity.EXTRA_USERNAME)
        if (username != null) {
            viewModel.getUserFollowers(username).observe(viewLifecycleOwner) { result ->
                if (result != null)
                {
                    when (result)
                    {
                        is Result.Loading -> {
                            showLoading(true)
                        }
                        is Result.Success -> {
                            showLoading(false)
                            binding.tvNoData.visibility =  if (result.data.isNotEmpty()) View.GONE else View.VISIBLE
                            userAdapter.differ.submitList(result.data)
                        }
                        is Result.Error -> {
                            showLoading(false)
                            Log.e(TAG, result.error)
                            Toast.makeText(
                                context,
                                "Error Occurred, Please Check Your Internet Connection",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
        }

        userAdapter.setOnItemClickListener { user ->
            val intent = Intent(activity, DetailActivity::class.java)
            intent.putExtra(DetailActivity.EXTRA_USERNAME, user.login)
            startActivity(intent)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun setupRecyclerView() {
        userAdapter = UserAdapter()
        binding.rvUsers.apply {
            adapter = userAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }
}