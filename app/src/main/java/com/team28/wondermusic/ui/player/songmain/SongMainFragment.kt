package com.team28.wondermusic.ui.player.songmain

import android.animation.ObjectAnimator
import android.content.Intent
import android.media.audiofx.AudioEffect
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.squareup.picasso.Picasso
import com.team28.wondermusic.R
import com.team28.wondermusic.common.Helper
import com.team28.wondermusic.databinding.FragmentSongMainBinding
import com.team28.wondermusic.ui.player.PlayerViewModel
import com.team28.wondermusic.ui.songplaylist.SongPlaylistFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
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
        equalizer()

        return binding.root
    }

    private fun animationRotate(rotate:Boolean){
        if (rotate) {
            rotateAnimation.cancel()
            rotateAnimation =
                ObjectAnimator.ofFloat(
                    binding.imgSongAvatar,
                    "rotation",
                    viewModel.currentRotate,
                    viewModel.currentRotate + 360f
                )

//            val i: Float = binding.imgSongAvatar.measuredHeight.toFloat()
            // Lấy giá trị cố định :)))
            val i :Float = Helper.dpToPixel(300f, requireContext()).toFloat()

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

    private fun animation() {
        rotateAnimation =
            ObjectAnimator.ofFloat(binding.imgSongAvatar, "rotation", 0f, 360f)

        viewModel.isPlaying.observe(requireActivity()) {
            animationRotate(it)
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
                if (song.loveStatus) {
                    viewModel.unLoveSong(song)
                } else {
                    viewModel.loveSong(song)
                }
            }
        }

        viewModel.song.observe(requireActivity()) {
            Log.e("vinh", "${it.loveStatus}")
            if (it.loveStatus) {
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

        viewModel.loveResponseStatus.observe(viewLifecycleOwner) {
            it?.let {
                if (it) {
                    viewModel.song.value?.let { song ->
                        song.loveStatus = !song.loveStatus
                        viewModel.song.postValue(song)
                    }
                } else {
                    Toast.makeText(context, viewModel.message, Toast.LENGTH_SHORT).show()
                }
                viewModel.loveResponseStatus.postValue(null)
            }
        }

        binding.btnShowPlaylist.setOnClickListener {
            showPlaylistFragment()
        }

    }

    private fun equalizer() {
        binding.btnEqualizer.setOnClickListener {
            Intent(AudioEffect.ACTION_DISPLAY_AUDIO_EFFECT_CONTROL_PANEL).apply {
                Log.d("vinh", "send audio session id: ${viewModel.audioSessionId.value} - ${requireActivity().packageName}")
                putExtra(AudioEffect.EXTRA_AUDIO_SESSION, viewModel.audioSessionId.value)
                putExtra(AudioEffect.EXTRA_PACKAGE_NAME, requireActivity().packageName)
                putExtra(AudioEffect.EXTRA_CONTENT_TYPE, AudioEffect.CONTENT_TYPE_MUSIC)
            }.also {
                startActivityForResult(it, 113)
            }
        }
    }

    private fun showPlaylistFragment() {
        val playlistFragment = SongPlaylistFragment()
        playlistFragment.show(requireActivity().supportFragmentManager, "playlist")
    }

}
