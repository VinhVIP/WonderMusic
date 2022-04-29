package com.team28.wondermusic.ui.formsong

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.team28.wondermusic.databinding.FragmentStep5Binding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class Step5Fragment : Fragment() {

    private lateinit var binding: FragmentStep5Binding
    private val viewModel by viewModels<FormSongViewModel>({ requireActivity() })

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentStep5Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        binding.radioPublic.isChecked = viewModel.songStatus == 0
        binding.radioPrivate.isChecked = viewModel.songStatus == 1
    }

    override fun onStop() {
        super.onStop()
        viewModel.songStatus = if (binding.radioPublic.isChecked) 0 else 1
    }

}