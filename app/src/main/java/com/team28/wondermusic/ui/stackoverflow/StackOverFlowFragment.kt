package com.team28.wondermusic.ui.stackoverflow

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.team28.wondermusic.databinding.FragmentStackOverFlowBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StackOverFlowFragment : Fragment() {

    private val viewModel by viewModels<StackOverFlowViewModel>()
    private lateinit var binding: FragmentStackOverFlowBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.fetchData()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentStackOverFlowBinding.inflate(inflater)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        viewModel.listQuestions.observe(viewLifecycleOwner) { list ->
            list.firstOrNull()?.let { question ->
                binding.tvResult.text = question.toString()
            }
        }


        return binding.root
    }
}