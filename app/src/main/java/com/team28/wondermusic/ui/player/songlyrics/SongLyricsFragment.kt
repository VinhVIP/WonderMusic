package com.team28.wondermusic.ui.player.songlyrics

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.team28.wondermusic.adapter.EventBusModel.MusicTimeSeekEvent
import com.team28.wondermusic.adapter.LyricAdapter
import com.team28.wondermusic.adapter.LyricsClickListener
import com.team28.wondermusic.common.Constants
import com.team28.wondermusic.common.Helper
import com.team28.wondermusic.data.entities.convertToLineLyric
import com.team28.wondermusic.data.models.LineLyric
import com.team28.wondermusic.databinding.FragmentSongLyricsBinding
import com.team28.wondermusic.service.MusicService
import com.team28.wondermusic.ui.player.PlayerViewModel
import org.greenrobot.eventbus.EventBus

class SongLyricsFragment : Fragment(), LyricsClickListener {

    private lateinit var binding: FragmentSongLyricsBinding

    private val viewModel by viewModels<PlayerViewModel>({ requireActivity() })

    private lateinit var lyricAdapter: LyricAdapter
    private lateinit var centerLayoutManager: CenterLayoutManager

    private var currentLine: Int = -1;
    private var songLyrics: ArrayList<LineLyric> = arrayListOf()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSongLyricsBinding.inflate(layoutInflater, container, false)

        lyricAdapter = LyricAdapter(songLyrics, requireContext(), this)
        centerLayoutManager = CenterLayoutManager(context)

        binding.recyclerLyrics.apply {
            adapter = lyricAdapter
            layoutManager = centerLayoutManager
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.song.observe(requireActivity()) {
            songLyrics = getSongLyrics(it.lyrics)
            lyricAdapter.setData(songLyrics)
        }

        viewModel.currentSongTime.observe(requireActivity()) { time ->
            binding.recyclerLyrics.post {
                if (viewModel.isUserTouchedSlider) {
                    scrollLyrics(time)
                } else {
                    smartScrollLyric(time)
                }
            }
        }

    }

    private fun scrollLyrics(time: Int) {
        val indexLine = indexLine(time, songLyrics)

        if (indexLine != -1 && indexLine != currentLine && indexLine < songLyrics.size) {
            lyricAdapter.currentLine(indexLine)

            // Bỏ in đậm dòng cũ
            lyricAdapter.notifyItemChanged(currentLine)

            // In đậm dòng mới
            if (indexLine > 0) lyricAdapter.notifyItemChanged(indexLine)

            // Lưu lại chỉ số dòng lyric đang hiển thị
            currentLine = indexLine

            // Scroll đến dòng hiện tại
            binding.recyclerLyrics.smoothScrollToPosition(indexLine)
            binding.tvLyricsTop.visibility = View.GONE

            viewModel.isUserTouchedSlider = false

        }
    }

    private fun smartScrollLyric(time: Int) {
        val indexLine = indexLine(time, songLyrics)

        if (indexLine != -1 && indexLine != currentLine && indexLine < songLyrics.size) {
            lyricAdapter.currentLine(indexLine)

            // Bỏ in đậm dòng cũ
            lyricAdapter.notifyItemChanged(currentLine)

            // In đậm dòng mới
            if (indexLine > 0) lyricAdapter.notifyItemChanged(indexLine)

            // Lưu lại chỉ số dòng lyric đang hiển thị
            currentLine = indexLine

            // Nếu nằm ngoài danh sách các item lyric đang hiển thị trên màn hình
            // thì ta hiển thị dòng lyric hiện tại ở trên cùng
            // Ngược lại, smoothScroll tới vị trí lyric hiện tại và ẩn dòng lyric top
            if (indexLine < centerLayoutManager.findFirstVisibleItemPosition() ||
                indexLine > centerLayoutManager.findLastVisibleItemPosition()
            ) {
                binding.tvLyricsTop.text = songLyrics[indexLine].text
                binding.tvLyricsTop.visibility = View.VISIBLE
            } else {
                binding.recyclerLyrics.smoothScrollToPosition(indexLine)
                binding.tvLyricsTop.visibility = View.GONE
            }
        }
    }

    private fun indexLine(time: Int, lyrics: ArrayList<LineLyric>): Int {
        var l = 0
        var r = lyrics.size - 1

        while (l <= r) {
            val m = (l + r) / 2
            if (time < lyrics[m].startTime) {
                r = m - 1
            } else {
                if (m + 1 < lyrics.size) {
                    if (time < lyrics[m + 1].startTime) {
                        return m
                    } else {
                        l = m + 1
                    }
                } else {
                    return m
                }
            }
        }
        return -1
    }

    private fun getSongLyrics(text: String): ArrayList<LineLyric> {
        val lyrics = arrayListOf<LineLyric>()

        lyrics.add(LineLyric(0, "..."))

        val list = text.split('\n') as ArrayList<String>
        for (line in list) {
            lyrics.add(line.convertToLineLyric())
        }

        return lyrics
    }

    override fun onLineLyricClick(lineLyric: LineLyric) {
        EventBus.getDefault().post(MusicTimeSeekEvent((lineLyric.startTime)))

        if (viewModel.isPlaying.value == false) {
            Intent(requireContext(), MusicService::class.java).apply {
                putExtra(Constants.Action, MusicService.ACTION_PLAY)
            }.also {
                Helper.startMusicService(requireContext(), it)
            }

        }
    }

}
