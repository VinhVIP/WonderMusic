package com.team28.wondermusic.ui.formsong

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.squareup.picasso.Picasso
import com.team28.wondermusic.R
import com.team28.wondermusic.common.Constants
import com.team28.wondermusic.common.FileUtils
import com.team28.wondermusic.common.Helper
import com.team28.wondermusic.data.models.Song
import com.team28.wondermusic.databinding.ActivityFormSongBinding
import com.team28.wondermusic.ui.formsong.album_choose.ListAlbumDialogFragment
import java.io.File

class FormSongActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFormSongBinding

    private val viewModel by viewModels<FormSongViewModel>()

    private var songEdit: Song? = null

    override fun onStart() {
        super.onStart()
        Helper.setStatusBarGradiant(this, R.drawable.bg_main)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFormSongBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getData()

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true);
        supportActionBar?.setDisplayShowHomeEnabled(true);

        setupEditTextLyrics()

        binding.btnChooseAlbum.setOnClickListener {
            val fragment = ListAlbumDialogFragment()
            fragment.show(supportFragmentManager, "album")
        }

        viewModel.album.observe(this) { album ->
            binding.btnChooseAlbum.text = album.name
        }

        chooseImage()
        chooseMusicFile()
    }

    private fun getData() {
        songEdit = intent.getParcelableExtra(Constants.Song)

        songEdit?.let { song ->
            binding.toolbar.title = "Chỉnh sửa"
            binding.btnSubmit.text = "Chỉnh sửa"

            binding.edSongName.setText(song.name)
            binding.edSongDescription.setText(song.description)
            binding.edLyrics.setText(song.lyrics)
            binding.btnChooseAlbum.text = song.album?.name
            Picasso.get().load(song.image).fit().into(binding.imgPreview)
            binding.imgPreview.visibility = View.VISIBLE
            binding.radioPublic.isChecked = song.songStatus == 0
        }
    }

    private fun chooseImage() {
        binding.btnChooseImage.setOnClickListener {
            try {
                imageChooser.launch("image/*")
            } catch (ex: ActivityNotFoundException) {
                Toast.makeText(this, "Vui lòng cài đặt File Manager", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun chooseMusicFile() {
        binding.btnChooseMusic.setOnClickListener {
            try {
                fileChooser.launch("audio/mpeg")
            } catch (ex: ActivityNotFoundException) {
                Toast.makeText(this, "Vui lòng cài đặt File Manager", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private var fileChooser: ActivityResultLauncher<String> = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let { uri ->
            val file: File? = FileUtils.from(this, uri)
            file?.let {
                binding.btnChooseMusic.text = it.name
            }
        }
    }

    private var imageChooser: ActivityResultLauncher<String> = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let { uri ->
            val file: File? = FileUtils.from(this, uri)
            file?.let {
                binding.btnChooseImage.text = it.name
                Picasso.get().load(it).resize(300, 300).into(binding.imgPreview)
                binding.imgPreview.visibility = View.VISIBLE
            }
        }

    }

    /*
     * Cho phép scroll nội dung edittext khi có layout cha là ScrollView
     */
    @SuppressLint("ClickableViewAccessibility")
    private fun setupEditTextLyrics() {
        binding.edLyrics.setOnTouchListener { view, event ->
            view.parent.requestDisallowInterceptTouchEvent(true)
            if ((event.action and MotionEvent.ACTION_MASK) == MotionEvent.ACTION_UP) {
                view.parent.requestDisallowInterceptTouchEvent(false)
            }
            return@setOnTouchListener false
        }
    }

    private fun isFormEdit(): Boolean {
        return songEdit != null
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        if (isFormEdit()) menuInflater.inflate(R.menu.form_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> super.onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }
}