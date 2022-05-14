package com.team28.wondermusic.ui.search

import android.content.Context
import android.os.Bundle
import android.os.SystemClock
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView.OnEditorActionListener
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.team28.wondermusic.R
import com.team28.wondermusic.adapter.ViewPagerAdapter
import com.team28.wondermusic.base.activities.BaseActivity
import com.team28.wondermusic.databinding.ActivitySearchBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchActivity : BaseActivity() {

    private lateinit var binding: ActivitySearchBinding
    private val viewModel by viewModels<SearchViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        this.setStatusBarGradiant(R.drawable.bg_search_page)

        binding.btnBack.setOnClickListener { finish() }

        binding.editText.setOnEditorActionListener(OnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                if (binding.editText.text.toString().trim().isNotEmpty()) {
                    viewModel.search(binding.editText.text.toString().trim())
                }
                binding.editText.hideKeyboard()
                return@OnEditorActionListener true
            }
            false
        })

        viewModel.isLoading.observe(this) {
            binding.pbLoading.visibility = if (it) View.VISIBLE else View.GONE
        }

        viewModel.isSearchDone.observe(this) {
            if (it) {
                binding.history.visibility = View.GONE

                binding.tabLayout.visibility = View.VISIBLE
                binding.viewPager.visibility = View.VISIBLE
            }
        }

        setupViewPager()
        binding.editText.requestFocus()

        showSoftKeyboard(binding.editText)
    }

    private fun showSoftKeyboard(view: View) {
        lifecycleScope.launch {
            delay(200)
            view.dispatchTouchEvent(
                MotionEvent.obtain(
                    SystemClock.uptimeMillis(),
                    SystemClock.uptimeMillis(),
                    MotionEvent.ACTION_DOWN,
                    0f,
                    0f,
                    0
                )
            )
            view.dispatchTouchEvent(
                MotionEvent.obtain(
                    SystemClock.uptimeMillis(),
                    SystemClock.uptimeMillis(),
                    MotionEvent.ACTION_UP,
                    0f,
                    0f,
                    0
                )
            )
        }
    }

    private fun setupViewPager() {
        binding.viewPager.orientation = ViewPager2.ORIENTATION_HORIZONTAL
        val fragmentsList = arrayListOf(
            SongSearchFragment(),
            PlaylistSearchFragment(),
            AccountSearchFragment()
        )
        binding.viewPager.adapter = ViewPagerAdapter(fragmentsList, this)
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            when (position) {
                0 -> {
                    tab.text = "Bài hát"
                }
                1 -> {
                    tab.text = "Playlist"
                }
                2 -> {
                    tab.text = "Tài khoản"
                }
            }
        }.attach()
    }
}

fun EditText.showKeyboard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
}

fun EditText.hideKeyboard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(this.windowToken, 0)
}