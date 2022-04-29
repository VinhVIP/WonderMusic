package com.team28.wondermusic.ui.formsong

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.size
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.chip.Chip
import com.team28.wondermusic.databinding.FragmentStep3Binding
import com.team28.wondermusic.ui.formsong.album_choose.ListAlbumDialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class Step3Fragment : Fragment() {

    private lateinit var binding: FragmentStep3Binding
    private val viewModel by viewModels<FormSongViewModel>({ requireActivity() })

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentStep3Binding.inflate(inflater, container, false)

        chooseAlbum()

        binding.btnChooseSingers.setOnClickListener {
            chooseSingers()
        }

        return binding.root
    }

    private fun chooseAlbum() {
        binding.btnChooseAlbum.setOnClickListener {
            val fragment = ListAlbumDialogFragment()
            fragment.show(requireActivity().supportFragmentManager, "album")
        }
    }

    val listChip = ArrayList<Chip>()
    val name = listOf("Quang Vinh", "Hải Đăng", "Hồng Phúc", "Nhật Huy", "Văn Trọng")

    private fun chooseSingers() {
        val chip: Chip = Chip(context)
        chip.text = name[binding.chipGroup.size]
        chip.isCloseIconVisible = true

        chip.setOnCloseIconClickListener {
            binding.chipGroup.removeView(it)
        }

        binding.chipGroup.addView(chip)
    }

}