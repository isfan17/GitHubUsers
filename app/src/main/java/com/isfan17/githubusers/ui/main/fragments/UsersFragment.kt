package com.isfan17.githubusers.ui.main.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.isfan17.githubusers.databinding.FragmentUsersBinding
import com.isfan17.githubusers.ui.viewmodels.ViewModelFactory
import com.isfan17.githubusers.ui.adapters.UserAdapter
import com.isfan17.githubusers.ui.viewmodels.MainViewModel
import com.isfan17.githubusers.helper.Result
import com.isfan17.githubusers.ui.detail.DetailActivity
import com.isfan17.githubusers.ui.search.SearchActivity

class UsersFragment : Fragment() {

    private var _binding: FragmentUsersBinding? = null
    private val binding get() = _binding!!

    private lateinit var userAdapter: UserAdapter
    private val TAG = "UsersFragment"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUsersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val factory: ViewModelFactory = ViewModelFactory.getInstance(requireActivity())
        val viewModel: MainViewModel by viewModels {
            factory
        }

        viewModel.getThemeSetting().observe(viewLifecycleOwner) { isNightMode ->
            if (isNightMode) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                binding.btnAppTheme.isChecked = true
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                binding.btnAppTheme.isChecked = false
            }
        }

        binding.btnAppTheme.setOnCheckedChangeListener { _, isChecked ->
            viewModel.saveThemeSetting(isChecked)
        }

        setupRecyclerView()

        binding.btnSearch.setOnClickListener {
            startActivity(Intent(activity, SearchActivity::class.java))
        }

        userAdapter.setOnItemClickListener { user ->
            val intent = Intent(activity, DetailActivity::class.java)
            intent.putExtra(DetailActivity.EXTRA_USERNAME, user.login)
            startActivity(intent)
        }

        viewModel.getUsers().observe(viewLifecycleOwner) { result ->
            if (result != null)
            {
                when (result)
                {
                    is Result.Loading -> {
                        showLoading(true)
                    }
                    is Result.Success -> {
                        showLoading(false)
                        val users = result.data
                        userAdapter.differ.submitList(users)
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}