package com.team28.wondermusic.ui.home.highlight

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.makeramen.roundedimageview.RoundedImageView
import com.team28.wondermusic.R
import com.team28.wondermusic.adapter.SongAdapter
import com.team28.wondermusic.adapter.SongClickListener
import com.team28.wondermusic.common.Constants
import com.team28.wondermusic.common.Helper
import com.team28.wondermusic.data.models.ListenOfDay
import com.team28.wondermusic.data.models.Song
import com.team28.wondermusic.databinding.FragmentHighLightBinding
import com.team28.wondermusic.service.MusicService
import com.team28.wondermusic.ui.menubottom.MenuBottomFragment
import com.team28.wondermusic.ui.menubottom.MenuBottomViewModel
import com.team28.wondermusic.ui.player.PlayerActivity
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import kotlin.math.abs


@AndroidEntryPoint
class HighLightFragment : Fragment(), SongClickListener {

    private lateinit var binding: FragmentHighLightBinding
    private val viewModel by viewModels<HighLightViewModel>()
    private val menuViewModel by viewModels<MenuBottomViewModel>({ requireActivity() })

    private lateinit var songAdapter: SongAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.fetchData()
    }

    override fun onResume() {
        super.onResume()
        binding.shimmerTopSong.startShimmer()
    }

    override fun onPause() {
        super.onPause()
        binding.shimmerTopSong.stopShimmer()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHighLightBinding.inflate(layoutInflater, container, false);

        menuViewModel.actionLoveStatus.observe(viewLifecycleOwner) {
            it?.let {
                if (it) {
                    menuViewModel.position?.let { position ->
                        val list = songAdapter.differ.currentList
                        list[position].loveStatus = !list[position].loveStatus
                        songAdapter.differ.submitList(list)
                    }
                }
                Toast.makeText(context, menuViewModel.message, Toast.LENGTH_SHORT).show()
                menuViewModel.actionLoveStatus.value = null
            }
        }

        binding.swipeRefresh.setOnRefreshListener {
            viewModel.fetchData()
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        songAdapter = SongAdapter(this)
        binding.recyclerSong.apply {
            adapter = songAdapter
            layoutManager = LinearLayoutManager(context)
        }

        viewModel.topSongsChart.observe(viewLifecycleOwner) {
            // Chỉ lấy top 3
            values.clear()
            for (topIndex in 0 until 3) {
                values.add(ArrayList())

                // Lấy 10 ngày gần nhất
                // Duyệt từ 10 ngày trước, cho đến hiện tại
                it[topIndex].listenDetail?.let { listListen ->
                    val calendar = Calendar.getInstance()
                    calendar.add(Calendar.DAY_OF_MONTH, -10)
                    for (cnt in 0 until 10) {
                        calendar.add(Calendar.DAY_OF_MONTH, 1)
                        val numListen = getListenOfDay(calendar.time, listListen)
                        values[topIndex].add(Entry(cnt.toFloat(), numListen.toFloat()))
                    }
                }
            }

            setupChart()
        }

        viewModel.topSongs.observe(viewLifecycleOwner) {
            binding.swipeRefresh.isRefreshing = false

            binding.shimmerTopSong.stopShimmer()
            binding.shimmerTopSong.visibility = View.GONE
            binding.recyclerSong.visibility = View.VISIBLE
            songAdapter.differ.submitList(it)

            viewModel.getTopSongDrawable(requireContext())
        }

        viewModel.songDrawables.observe(viewLifecycleOwner) {
            binding.chart.data = generateDataLine()
            Log.d("vinh", "load drawable ok")
        }
    }

    private fun getListenOfDay(checkDate: Date, listens: List<ListenOfDay>): Int {
        for (listenOfDay in listens) {
            val date = Helper.stringToDate(listenOfDay.day)
            val calendar1 = Calendar.getInstance()
            val calendar2 = Calendar.getInstance()

            calendar1.time = checkDate
            date?.let {
                calendar2.time = it
                if (calendar1.get(Calendar.DAY_OF_MONTH) == calendar2.get(Calendar.DAY_OF_MONTH)
                    && calendar1.get(Calendar.MONTH) == calendar2.get(Calendar.MONTH)
                ) return listenOfDay.listen
            }
        }
        return 0
    }

    private fun setupChart() {
        binding.chart.apply {
            description.isEnabled = false
            setDrawGridBackground(false)
            isDragEnabled = false
            setScaleEnabled(false)
            setPinchZoom(false)
            legend.isEnabled = false

            xAxis.apply {
                labelCount = 9
                position = XAxis.XAxisPosition.BOTTOM
                setDrawGridLines(false)
                textColor = ContextCompat.getColor(context, R.color.text_primary)
                valueFormatter = object : ValueFormatter() {
                    override fun getFormattedValue(value: Float): String {
                        val calendar = Calendar.getInstance()
                        calendar.add(Calendar.DAY_OF_MONTH, value.toInt() - 9)
                        return "${calendar.get(Calendar.DAY_OF_MONTH)}"
                    }
                }
            }
            axisLeft.apply {
                isEnabled = false
            }
            axisRight.apply {
                labelCount = 4
                setDrawGridLines(false)
                textColor = ContextCompat.getColor(context, R.color.text_primary)
            }

//            animateX(1500)
            animateY(1500)
            data = generateDataLine()

            setOnChartValueSelectedListener(object : OnChartValueSelectedListener {
                override fun onValueSelected(e: Entry?, h: Highlight?) {
                    Log.d("vinh", "data index: ${h?.dataSetIndex}")
                    binding.chart.data = generateDataLine(h?.dataSetIndex ?: -1)
                }

                override fun onNothingSelected() {

                }
            })
        }
    }

    private val values: ArrayList<ArrayList<Entry>> = ArrayList()

    private fun generateDataLine(highlightSetIndex: Int = -1): LineData {

        val sets: ArrayList<ILineDataSet> = ArrayList()
        val dataSets: ArrayList<LineDataSet> = ArrayList()
        val colors = listOf(
            Color.rgb(47, 148, 240),
            Color.rgb(56, 202, 147),
            Color.rgb(227, 121, 68),
        )

        for (i in 0..2) {
            var lineDataSet: LineDataSet
            if (highlightSetIndex == i) {
                val highLightValue = values[i].toMutableList()
                val randomIndex = abs(Random().nextInt() % 9 + 1)

                val a = RoundedImageView(context)
                a.setImageDrawable(viewModel.songDrawables.value?.get(i))
                a.cornerRadius = 24f
                a.borderWidth = Helper.dpToPixel(2f, requireContext()).toFloat()
                a.borderColor = colors[i]

                highLightValue[randomIndex] = Entry(
                    randomIndex.toFloat(),
                    highLightValue[randomIndex].y,
                    a.drawable
                )

                lineDataSet = LineDataSet(highLightValue, "Top ${i + 1}").apply {
                    setDrawCircles(true)
                    setDrawCircleHole(true)
                    circleRadius = 3.5f
                    setCircleColor(colors[i])
                }
            } else {
                lineDataSet = LineDataSet(values[i], "Top ${i + 1}").apply {
                    setDrawCircles(false)
                    setDrawCircleHole(false)
                }
            }

            lineDataSet.apply {
                lineWidth = 1.5f
                color = colors[i]
                setDrawValues(false)
                setDrawHorizontalHighlightIndicator(false)
                setDrawVerticalHighlightIndicator(false)
            }
            dataSets.add(lineDataSet)
            sets.add(dataSets[i])
        }

        return LineData(sets)
    }

    override fun onSongClick(song: Song) {
        startActivity(Intent(context, PlayerActivity::class.java))
        Helper.sendMusicAction(
            requireContext(),
            MusicService.ACTION_PLAY,
            song,
            viewModel.topSongs.value as ArrayList<Song>
        )
    }

    override fun onOpenMenu(song: Song, position: Int) {
        MenuBottomFragment().apply {
            arguments = Bundle().apply {
                putParcelable(Constants.Song, song)
                putInt(Constants.Position, position)
            }
        }.show(requireActivity().supportFragmentManager, null)
    }
}