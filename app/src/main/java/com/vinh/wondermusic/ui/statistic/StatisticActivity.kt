package com.vinh.wondermusic.ui.statistic

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.Gravity
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.vinh.wondermusic.R
import com.vinh.wondermusic.adapter.SongClickListener
import com.vinh.wondermusic.adapter.SongStatisticAdapter
import com.vinh.wondermusic.common.Helper
import com.vinh.wondermusic.data.models.Song
import com.vinh.wondermusic.databinding.ActivityStatisticBinding
import dagger.hilt.android.AndroidEntryPoint
import es.dmoral.toasty.Toasty
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class StatisticActivity : AppCompatActivity(), SongClickListener {

    private lateinit var binding: ActivityStatisticBinding
    private val viewModel by viewModels<StatisticViewModel>()

    private lateinit var songAdapter: SongStatisticAdapter
    private var isListen = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStatisticBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Helper.setStatusBarGradiant(this, R.drawable.bg_account)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        setupRecyclerSongs()
        observers()
        actions()

        initData()
    }

    private fun initData() {
        val calendar = Calendar.getInstance()
        val sdf = SimpleDateFormat("yyyy-MM-dd")
        calendar.time = Date()

        val month = calendar.get(Calendar.MONTH) + 1
        val year = calendar.get(Calendar.YEAR)

        binding.tvStart.text = String.format("%d-%02d-%02d", year, month, 1)
        binding.tvEnd.text = sdf.format(Date())

        binding.tvSortBy.text = "Lượt nghe"
        isListen = true
    }

    private fun setupRecyclerSongs() {
        songAdapter = SongStatisticAdapter(this)

        binding.rvSongs.apply {
            adapter = songAdapter
            layoutManager = LinearLayoutManager(this@StatisticActivity)
        }
    }

    private var popup: PopupWindow? = null

    private fun actions() {
        binding.tvSortBy.setOnClickListener {
            val rootDialog = layoutInflater.inflate(R.layout.dialog_popup_filter, null)

            popup = PopupWindow(rootDialog, 400, ViewGroup.LayoutParams.WRAP_CONTENT, true)
            popup?.animationStyle = android.R.style.Animation_Dialog;
            popup?.showAtLocation(
                binding.tvSortBy,
                Gravity.RIGHT or Gravity.TOP,
                0,
                30
            )
            popup?.isTouchModal = true

            val btnListen = rootDialog.findViewById<LinearLayout>(R.id.btnListen)
            val btnHeart = rootDialog.findViewById<LinearLayout>(R.id.btnHeart)

            btnListen.setOnClickListener {
                binding.tvSortBy.text = "Lượt nghe"
                isListen = true
                popup?.dismiss()
            }
            btnHeart.setOnClickListener {
                binding.tvSortBy.text = "Yêu thích"
                isListen = false
                popup?.dismiss()
            }
        }


        binding.btnFetch.setOnClickListener {
            val startDate = binding.tvStart.text.toString()
            val endDate = binding.tvEnd.text.toString()
            if (endDate < startDate) {
                Toasty.error(
                    this,
                    "Ngày bắt đầu phải sớm hơn ngày kết thúc",
                    Toast.LENGTH_SHORT,
                    true
                ).show()
            } else {
                viewModel.getTopListenInRange(
                    binding.tvStart.text.toString(),
                    binding.tvEnd.text.toString()
                )
                binding.progress.smoothToShow()
                binding.rvSongs.visibility = View.GONE
            }
        }

        binding.tvStart.setOnClickListener {
            val sdf = SimpleDateFormat("yyyy-MM-dd")
            val date = sdf.parse(binding.tvStart.text.toString())
            val calendar = Calendar.getInstance()
            calendar.time = date

            DatePickerDialog(
                this,
                { view, year, month, dayOfMonth ->
                    binding.tvStart.text =
                        String.format("%d-%02d-%02d", year, month + 1, dayOfMonth)
                    binding.chipChoose.isChecked = true
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)

            ).show()
        }

        binding.tvEnd.setOnClickListener {
            val sdf = SimpleDateFormat("yyyy-MM-dd")
            val date = sdf.parse(binding.tvStart.text.toString())
            val calendar = Calendar.getInstance()
            calendar.time = date

            DatePickerDialog(
                this,
                { view, year, month, dayOfMonth ->
                    binding.tvEnd.text =
                        String.format("%d-%02d-%02d", year, month + 1, dayOfMonth)
                    binding.chipChoose.isChecked = true
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        binding.chip7.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                val calendar = Calendar.getInstance()
                val endDate = String.format(
                    "%d-%02d-%02d",
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH) + 1,
                    calendar.get(Calendar.DAY_OF_MONTH)
                )
                calendar.add(Calendar.DAY_OF_MONTH, -7)
                val startDate = String.format(
                    "%d-%02d-%02d",
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH) + 1,
                    calendar.get(Calendar.DAY_OF_MONTH)
                )

                binding.tvStart.text = startDate
                binding.tvEnd.text = endDate

                viewModel.getTopListenInRange(startDate, endDate, isListen = isListen)
            }
        }

        binding.chip30.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                val calendar = Calendar.getInstance()
                val endDate = String.format(
                    "%d-%02d-%02d",
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH) + 1,
                    calendar.get(Calendar.DAY_OF_MONTH)
                )
                calendar.add(Calendar.DAY_OF_MONTH, -30)
                val startDate = String.format(
                    "%d-%02d-%02d",
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH) + 1,
                    calendar.get(Calendar.DAY_OF_MONTH)
                )

                binding.tvStart.text = startDate
                binding.tvEnd.text = endDate

                viewModel.getTopListenInRange(startDate, endDate, isListen = isListen)
            }
        }

        binding.chipAll.setOnCheckedChangeListener { buttonView, isChecked ->
            viewModel.getTopListenInRange("", "", isAll = true, isListen = isListen)
        }
    }

    private fun observers() {
        viewModel.isLoading.observe(this) {
            if (it) {
                binding.progress.smoothToShow()
                binding.rvSongs.visibility = View.GONE
            }
        }

        viewModel.listSongs.observe(this) {
            binding.progress.smoothToHide()
            binding.rvSongs.visibility = View.VISIBLE
            songAdapter.setData(it)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> super.onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onSongClick(song: Song) {

    }

    override fun onOpenMenu(song: Song, position: Int) {
    }
}