package com.team28.wondermusic.ui.home.highlight

import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import com.team28.wondermusic.data.TempData
import com.team28.wondermusic.data.models.Song
import com.team28.wondermusic.databinding.FragmentHighLightBinding
import com.team28.wondermusic.service.MusicService
import com.team28.wondermusic.ui.menubottom.MenuBottomFragment
import com.team28.wondermusic.ui.player.PlayerActivity
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import kotlin.math.abs
import kotlin.math.roundToInt


@AndroidEntryPoint
class HighLightFragment : Fragment(), SongClickListener {

    private lateinit var binding: FragmentHighLightBinding
    private val viewModel by viewModels<HighLightViewModel>()

    private lateinit var songAdapter: SongAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.genData()

        viewModel.fetchData()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHighLightBinding.inflate(layoutInflater, container, false);
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        songAdapter = SongAdapter(this)
        binding.recyclerSong.apply {
            adapter = songAdapter
            layoutManager = LinearLayoutManager(this@HighLightFragment.context)
        }

        setupChart()

        viewModel.topSongs.observe(viewLifecycleOwner) {
            songAdapter.differ.submitList(it)

            viewModel.getTopSongDrawable(requireContext())
        }

        viewModel.songDrawables.observe(viewLifecycleOwner) {
            binding.chart.data = generateDataLine()
            Log.d("vinh", "load ok")
        }
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
                        return "${value.roundToInt()}"
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

            animateX(1500)
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

    init {
        for (i in 0..2) {
            values.add(ArrayList())
            for (d in 0..9) {
                values[i].add(Entry(d.toFloat(), ((Math.random() * 65).toInt() + 40).toFloat()))
            }
        }
    }

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
                val randomIndex = abs(Random().nextInt() % 10)

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

    private fun sendMusicAction(
        action: Int,
        song: Song? = null,
        songList: ArrayList<Song> = arrayListOf()
    ) {
        val intent = Intent(requireActivity().applicationContext, MusicService::class.java)

        intent.putExtra("action", action)
        song?.let {
            val bundle = Bundle().apply {
                putParcelable(Constants.Song, it)
                putParcelableArrayList(Constants.SongList, songList)
            }
            intent.putExtra(Constants.Data, bundle)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            requireActivity().startForegroundService(intent);
        } else {
            requireActivity().startService(intent);
        }
    }

    override fun onSongClick(song: Song) {
        startActivity(Intent(context, PlayerActivity::class.java))
        sendMusicAction(MusicService.ACTION_PLAY, song, TempData.songs)
    }

    override fun onOpenMenu(song: Song) {
        MenuBottomFragment().apply {
            arguments = Bundle().apply {
                putParcelable(Constants.Song, song)
            }
        }.show(requireActivity().supportFragmentManager, null)
    }
}