package com.vinh.wondermusic.ui.formsong

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.vinh.wondermusic.databinding.FragmentStep4Binding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class Step4Fragment : Fragment() {

    private lateinit var binding: FragmentStep4Binding
    private val viewModel by viewModels<FormSongViewModel>({ requireActivity() })

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentStep4Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        binding.edLyrics.setText(viewModel.lyric)
    }

    override fun onStop() {
        super.onStop()
        viewModel.lyric = binding.edLyrics.text.toString()
    }

}