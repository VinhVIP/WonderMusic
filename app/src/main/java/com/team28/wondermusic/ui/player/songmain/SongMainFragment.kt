package com.team28.wondermusic.ui.player.songmain

import android.animation.ObjectAnimator
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.squareup.picasso.Picasso
import com.team28.wondermusic.R
import com.team28.wondermusic.databinding.FragmentSongMainBinding
import com.team28.wondermusic.ui.player.PlayerViewModel
import com.team28.wondermusic.ui.songplaylist.SongPlaylistFragment


class SongMainFragment : Fragment() {

    private lateinit var binding: FragmentSongMainBinding

    private val viewModel by viewModels<PlayerViewModel>({ requireActivity() })

    private lateinit var rotateAnimation: ObjectAnimator

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSongMainBinding.inflate(layoutInflater, container, false)

        animation()

        return binding.root
    }

    private fun animation() {
        rotateAnimation =
            ObjectAnimator.ofFloat(binding.imgSongAvatar, "rotation", 0f, 360f)

        viewModel.isPlaying.observe(requireActivity()) {
            if (it) {
                rotateAnimation.cancel()
                rotateAnimation =
                    ObjectAnimator.ofFloat(
                        binding.imgSongAvatar,
                        "rotation",
                        viewModel.currentRotate,
                        viewModel.currentRotate + 360f
                    )

                val i: Float = binding.imgSongAvatar.measuredHeight.toFloat()

                rotateAnimation.duration = 10000
                binding.imgSongAvatar.pivotX = i / 2
                binding.imgSongAvatar.pivotY = i / 2
                rotateAnimation.repeatCount = ObjectAnimator.INFINITE
                rotateAnimation.interpolator = LinearInterpolator()
                rotateAnimation.start()
            } else {
                rotateAnimation.pause()
                viewModel.currentRotate = rotateAnimation.animatedValue as Float
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.song.observe(requireActivity()) { song ->
            Picasso.get().load(song.image).fit().into(binding.imgSongAvatar)
            binding.songDescription.text = song.description
        }

        binding.imgHeart.setOnClickListener {
            viewModel.song.value?.let { song ->
                song.loveStatus = !song.loveStatus

                if (song.loveStatus) {
                    binding.imgHeart.apply {
                        setImageResource(R.drawable.ic_heart_red)
                        setColorFilter(ContextCompat.getColor(context, R.color.red))
                    }

                } else {
                    binding.imgHeart.apply {
                        setImageResource(R.drawable.ic_heart)
                        setColorFilter(ContextCompat.getColor(context, R.color.icon_tint))
                    }
                }
            }

        }

        binding.btnShowPlaylist.setOnClickListener {
            showPlaylistFragment()
        }
    }

    private fun showPlaylistFragment() {
        val playlistFragment = SongPlaylistFragment()
        playlistFragment.show(requireActivity().supportFragmentManager, "playlist")
    }

}