package com.isfan17.githubusers.ui.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.StringRes
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.isfan17.githubusers.R
import com.isfan17.githubusers.databinding.ActivityDetailBinding
import com.isfan17.githubusers.ui.adapters.SectionsPagerAdapter

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        binding.btnBack.setOnClickListener {
            finish()
        }

        val username = intent.getStringExtra(EXTRA_USERNAME)

        val bundle = Bundle()
        bundle.putString(EXTRA_USERNAME, username)

        val sectionsPagerAdapter = SectionsPagerAdapter(this, bundle)

        val viewPager: ViewPager2 = binding.viewPager
        viewPager.adapter = sectionsPagerAdapter

        val tabs: TabLayout = binding.tabLayout
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()
    }

    companion object {
        const val EXTRA_USERNAME = "extra_username"

        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_title_0,
            R.string.tab_title_1,
            R.string.tab_title_2
        )
    }
}