package com.team28.wondermusic.ui.formsong

import android.content.ActivityNotFoundException
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.team28.wondermusic.common.FileUtils
import com.team28.wondermusic.databinding.FragmentStep1Binding
import dagger.hilt.android.AndroidEntryPoint
import java.io.File

@AndroidEntryPoint
class Step1Fragment : Fragment() {

    private lateinit var binding: FragmentStep1Binding
    private val viewModel by viewModels<FormSongViewModel>({ requireActivity() })

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentStep1Binding.inflate(inflater, container, false)

        chooseMusicFile()

        return binding.root
    }

    private fun chooseMusicFile() {
        binding.btnChooseMusic.setOnClickListener {
            try {
                fileChooser.launch("audio/mpeg")
            } catch (ex: ActivityNotFoundException) {
                Toast.makeText(context, "Vui lòng cài đặt File Manager", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private var fileChooser: ActivityResultLauncher<String> = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let { uri ->
            val file: File? = FileUtils.from(requireContext(), uri)
            file?.let {
                viewModel.songFile = it
                showSongFileInfo(it)
            }
        }
    }

    private fun showSongFileInfo(file: File) {
        binding.btnChooseMusic.text = file.name
    }

    override fun onStart() {
        super.onStart()
        viewModel.songFile?.let {
            showSongFileInfo(it)
        }
        binding.edSongName.setText(viewModel.songName)
    }

    override fun onStop() {
        super.onStop()
        viewModel.songName = binding.edSongName.text.toString().trim()
    }
}