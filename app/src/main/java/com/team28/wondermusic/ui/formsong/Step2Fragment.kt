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
import com.squareup.picasso.Picasso
import com.team28.wondermusic.R
import com.team28.wondermusic.common.FileUtils
import com.team28.wondermusic.databinding.FragmentStep2Binding
import dagger.hilt.android.AndroidEntryPoint
import java.io.File

@AndroidEntryPoint
class Step2Fragment : Fragment() {

    private lateinit var binding: FragmentStep2Binding
    private val viewModel by viewModels<FormSongViewModel>({ requireActivity() })

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentStep2Binding.inflate(inflater, container, false)

        chooseImage()

        return binding.root
    }

    private fun chooseImage() {
        binding.btnChooseImage.setOnClickListener {
            try {
                imageChooser.launch("image/*")
            } catch (ex: ActivityNotFoundException) {
                Toast.makeText(context, "Vui lòng cài đặt File Manager", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private var imageChooser: ActivityResultLauncher<String> = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let { uri ->
            val file: File? = FileUtils.from(requireContext(), uri)
            file?.let {
                viewModel.imageFile = it
                showImageFileInfo(it)
            }
        }
    }

    private fun showImageFileInfo(file: File) {
        binding.btnChooseImage.text = file.name
        Picasso.get().load(file).resize(300, 300).into(binding.imgPreview)
        binding.imgPreview.visibility = View.VISIBLE
    }

    override fun onStart() {
        super.onStart()
        binding.imgPreview.visibility = View.INVISIBLE
        viewModel.imageFile?.let {
            showImageFileInfo(it)
        }
        binding.edSongDescription.setText(viewModel.description)

        if (viewModel.songEdit != null && viewModel.imageFile == null) {
            viewModel.songEdit?.let {
                if (it.image.isNotEmpty()) {
                    Picasso.get().load(it.image).placeholder(R.drawable.bitmap_music).fit()
                        .into(binding.imgPreview)
                }
            }
        }
    }

    override fun onStop() {
        super.onStop()
        viewModel.description = binding.edSongDescription.text.toString().trim()
    }
}