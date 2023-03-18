package com.isfan17.githubusers.ui.search

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.isfan17.githubusers.helper.Result
import com.isfan17.githubusers.databinding.ActivitySearchBinding
import com.isfan17.githubusers.ui.adapters.UserAdapter
import com.isfan17.githubusers.ui.detail.DetailActivity
import com.isfan17.githubusers.ui.viewmodels.SearchViewModel
import com.isfan17.githubusers.ui.viewmodels.ViewModelFactory

class SearchActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchBinding
    private lateinit var userAdapter: UserAdapter
    private val TAG = "SearchActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        val factory: ViewModelFactory = ViewModelFactory.getInstance(this)
        val viewModel: SearchViewModel by viewModels {
            factory
        }

        setupRecyclerView()

        userAdapter.setOnItemClickListener { user ->
            val intent = Intent(this@SearchActivity, DetailActivity::class.java)
            intent.putExtra(DetailActivity.EXTRA_USERNAME, user.login)
            startActivity(intent)
        }

        binding.btnBack.setOnClickListener {
            finish()
        }

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                viewModel.getSearchUsers(query).observe(this@SearchActivity) { result ->
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
                                if (users.isEmpty()) {
                                    showNoResultMsg(true)
                                } else {
                                    showNoResultMsg(false)
                                }
                                userAdapter.differ.submitList(users)
                            }
                            is Result.Error -> {
                                showLoading(false)
                                Log.e(TAG, result.error)
                                Toast.makeText(
                                    this@SearchActivity,
                                    "Error Occurred, Please Check Your Internet Connection",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
                }

                binding.searchView.clearFocus()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })

    }

    private fun showNoResultMsg(isNoResult: Boolean) {
        binding.ivNoResult.visibility = if (isNoResult) View.VISIBLE else View.GONE
        binding.tvNoResult.visibility = if (isNoResult) View.VISIBLE else View.GONE
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun setupRecyclerView() {
        userAdapter = UserAdapter()
        binding.rvUsers.apply {
            adapter = userAdapter
            layoutManager = LinearLayoutManager(this@SearchActivity)
        }
    }
}