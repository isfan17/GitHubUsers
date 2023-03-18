package com.isfan17.githubusers.ui.main.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.isfan17.githubusers.databinding.FragmentFavoritesBinding
import com.isfan17.githubusers.ui.adapters.UserAdapter
import com.isfan17.githubusers.ui.detail.DetailActivity
import com.isfan17.githubusers.ui.viewmodels.MainViewModel
import com.isfan17.githubusers.ui.viewmodels.ViewModelFactory

class FavoritesFragment : Fragment() {

    private var _binding: FragmentFavoritesBinding? = null
    private val binding get() = _binding!!

    private lateinit var userAdapter: UserAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val factory: ViewModelFactory = ViewModelFactory.getInstance(requireActivity())
        val viewModel: MainViewModel by viewModels {
            factory
        }

        setupRecyclerView()

        userAdapter.setOnItemClickListener { user ->
            val intent = Intent(activity, DetailActivity::class.java)
            intent.putExtra(DetailActivity.EXTRA_USERNAME, user.login)
            startActivity(intent)
        }

        viewModel.getFavoriteUsers().observe(viewLifecycleOwner) { favoriteUsers ->
            if (favoriteUsers.isNotEmpty())
            {
                binding.ivNoData.visibility = View.GONE
                binding.tvNoData.visibility = View.GONE
            }
            else
            {
                binding.ivNoData.visibility = View.VISIBLE
                binding.tvNoData.visibility = View.VISIBLE
            }
            binding.progressBar.visibility = View.GONE
            userAdapter.differ.submitList(favoriteUsers)
        }
    }

    private fun setupRecyclerView() {
        userAdapter = UserAdapter()
        binding.rvUsers.apply {
            adapter = userAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}