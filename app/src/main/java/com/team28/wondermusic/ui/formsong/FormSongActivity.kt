package com.team28.wondermusic.ui.formsong

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.navigation.findNavController
import com.aceinteract.android.stepper.StepperNavListener
import com.team28.wondermusic.R
import com.team28.wondermusic.base.activities.BaseActivity
import com.team28.wondermusic.base.dialogs.LoadingDialog
import com.team28.wondermusic.common.Constants
import com.team28.wondermusic.common.Helper
import com.team28.wondermusic.data.models.Song
import com.team28.wondermusic.databinding.ActivityFormSongBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FormSongActivity : BaseActivity(), StepperNavListener {

    private lateinit var binding: ActivityFormSongBinding
    private val viewModel by viewModels<FormSongViewModel>()

    private val loadingDialog = LoadingDialog("Đang tải lên bài hát của bạn")

    private val END_STEP = 4

    override fun onStart() {
        super.onStart()
        Helper.setStatusBarGradiant(this, R.drawable.bg_account)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFormSongBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupStepper()

        getData()

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true);
        supportActionBar?.setDisplayShowHomeEnabled(true);

        viewModel.addStatus.observe(this) {
            it?.let {
                Log.d("vinh", viewModel.message!!)
                Toast.makeText(this, viewModel.message, Toast.LENGTH_SHORT).show()
            }
            viewModel.addStatus.postValue(null)
        }

        viewModel.isUploading.observe(this) {
            if (it)
                loadingDialog.show(supportFragmentManager, null)
            else if (loadingDialog.dialog?.isShowing == true)
                loadingDialog.closeDialog()
        }

//
//        setupEditTextLyrics()
//
//        binding.btnChooseAlbum.setOnClickListener {
//            val fragment = ListAlbumDialogFragment()
//            fragment.show(supportFragmentManager, "album")
//        }
//
//        viewModel.album.observe(this) { album ->
//            binding.btnChooseAlbum.text = album.name
//        }
//
//        chooseImage()
//        chooseMusicFile()
    }

    private fun getData() {
        val song = intent?.getParcelableExtra<Song>(Constants.Song)

        song?.let {
            viewModel.initSongInfo(song)
            binding.toolbar.title = "Cập nhật bài hát"
        }
    }

    private fun setupStepper() {
        binding.stepper.setupWithNavController(findNavController(R.id.frame_stepper))
        binding.stepper.stepperNavListener = this

        binding.fabNext.setOnClickListener {
            if (binding.stepper.currentStep == END_STEP) {
                if (viewModel.isFormAdd()) {
                    viewModel.addSong()
                } else {

                }
            } else {
                binding.stepper.goToNextStep()
            }
        }
        binding.fabPrevious.setOnClickListener {
            binding.stepper.goToPreviousStep()
        }
    }


    override fun onCompleted() {
    }

    override fun onStepChanged(step: Int) {
        if (step == END_STEP) {
            binding.fabNext.setImageResource(R.drawable.ic_navigate_finish)
        } else {
            binding.fabNext.setImageResource(R.drawable.ic_navigate_next)
        }

        if (step == 0) {
            binding.fabPrevious.visibility = View.GONE
        } else {
            binding.fabPrevious.visibility = View.VISIBLE
        }
    }

    override fun onBackPressed() {
        if (binding.stepper.currentStep == 0) {
            super.onBackPressed()
        } else {
            binding.stepper.goToPreviousStep()

//            findNavController(R.id.frame_stepper).navigateUp()
        }
    }

//    private fun getData() {
//        songEdit = intent.getParcelableExtra(Constants.Song)
//
//        songEdit?.let { song ->
//            binding.toolbar.title = "Chỉnh sửa"
//            binding.btnSubmit.text = "Chỉnh sửa"
//
//            binding.edSongName.setText(song.name)
//            binding.edSongDescription.setText(song.description)
//            binding.edLyrics.setText(song.lyrics)
//            binding.btnChooseAlbum.text = song.album?.name
//            Picasso.get().load(song.image).fit().into(binding.imgPreview)
//            binding.imgPreview.visibility = View.VISIBLE
//            binding.radioPublic.isChecked = song.songStatus == 0
//        }
//    }

//
//    /*
//     * Cho phép scroll nội dung edittext khi có layout cha là ScrollView
//     */
//    @SuppressLint("ClickableViewAccessibility")
//    private fun setupEditTextLyrics() {
//        binding.edLyrics.setOnTouchListener { view, event ->
//            view.parent.requestDisallowInterceptTouchEvent(true)
//            if ((event.action and MotionEvent.ACTION_MASK) == MotionEvent.ACTION_UP) {
//                view.parent.requestDisallowInterceptTouchEvent(false)
//            }
//            return@setOnTouchListener false
//        }
//    }
//
//    private fun isFormEdit(): Boolean {
//        return songEdit != null
//    }
//
//    override fun onCreateOptionsMenu(menu: Menu): Boolean {
//        if (isFormEdit()) menuInflater.inflate(R.menu.form_menu, menu)
//        return super.onCreateOptionsMenu(menu)
//    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> super.onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }
}