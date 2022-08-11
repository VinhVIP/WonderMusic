package com.vinh.wondermusic.ui.formsong

import android.content.ActivityNotFoundException
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.chip.Chip
import com.vinh.wondermusic.common.FileUtils
import com.vinh.wondermusic.common.exists
import com.vinh.wondermusic.data.models.Type
import com.vinh.wondermusic.databinding.FragmentStep1Binding
import com.vinh.wondermusic.ui.formsong.album_choose.ListAlbumDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import java.io.File

@AndroidEntryPoint
class Step1Fragment : Fragment() {

    private lateinit var binding: FragmentStep1Binding
    private val viewModel by viewModels<FormSongViewModel>({ requireActivity() })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.getAllTypes()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentStep1Binding.inflate(inflater, container, false)

        clickEvents()
        observers()
        textWatchers()

        return binding.root
    }

    private fun clickEvents() {
        binding.btnChooseAlbum.setOnClickListener {
            val fragment = ListAlbumDialogFragment()
            fragment.show(requireActivity().supportFragmentManager, "album")
        }
        binding.btnChooseMusic.setOnClickListener {
            try {
                fileChooser.launch("audio/mpeg")
            } catch (ex: ActivityNotFoundException) {
                Toast.makeText(context, "Vui lòng cài đặt File Manager", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun observers() {
        viewModel.album.observe(viewLifecycleOwner) {
            it?.let {
                binding.btnChooseAlbum.text = it.name
            }
        }

        viewModel.allTypes.observe(viewLifecycleOwner) {
            binding.chipGroup.removeAllViews()

            var listSongTypes = listOf<Type>()
            viewModel.types.value?.let { types ->
                listSongTypes = types
            }

            for (type in it) {
                val chip = createTypeChip(type)
                if (listSongTypes.exists(type)) {
                    chip.isChecked = true
                }
                binding.chipGroup.addView(chip)
            }
        }

        viewModel.songFile.observe(viewLifecycleOwner) {
            if (it == null) {
                if (viewModel.isFormAdd())
                    binding.tvSongFileError.visibility = View.VISIBLE
            } else {
                if (viewModel.isFormAdd())
                    binding.tvSongFileError.visibility = View.GONE
                showSongFileInfo(it)
            }
        }
    }

    private fun textWatchers() {
        binding.edSongName.addTextChangedListener {
            if (it == null || it.toString().trim().isEmpty()) {
                binding.tvSongNameError.visibility = View.VISIBLE
            } else {
                binding.tvSongNameError.visibility = View.GONE
            }
        }
    }

    private var fileChooser: ActivityResultLauncher<String> = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let { uri ->
            val file: File? = FileUtils.from(requireContext(), uri)
            file?.let {
                viewModel.songFile.postValue(it)
                showSongFileInfo(it)
            }
        }
    }

    private fun showSongFileInfo(file: File) {
        binding.btnChooseMusic.text = file.name
    }

    private fun createTypeChip(type: Type): Chip {
        return Chip(context).apply {
            text = type.name
            isCheckable = true

            setOnCheckedChangeListener { _, b ->
                if (b) {
                    viewModel.addType(type)
                } else {
                    viewModel.removeType(type)
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        binding.edSongName.setText(viewModel.songName)
    }

    override fun onStop() {
        super.onStop()
        viewModel.songName = binding.edSongName.text.toString().trim()
    }
}