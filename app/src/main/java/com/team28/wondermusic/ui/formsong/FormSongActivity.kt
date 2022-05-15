package com.team28.wondermusic.ui.formsong

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.navigation.findNavController
import com.aceinteract.android.stepper.StepperNavListener
import com.team28.wondermusic.R
import com.team28.wondermusic.base.activities.BaseActivity
import com.team28.wondermusic.base.dialogs.ConfirmDialog
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

        observers()
    }

    private fun getData() {
        val song = intent?.getParcelableExtra<Song>(Constants.Song)

        song?.let {
            viewModel.initSongInfo(song)
            binding.toolbar.title = "Cập nhật bài hát"
        }
    }

    private fun observers() {
        viewModel.addStatus.observe(this) {
            it?.let {
                Log.d("vinh", viewModel.message!!)
                Toast.makeText(this, viewModel.message, Toast.LENGTH_SHORT).show()
                if (it) finish()
            }
            viewModel.addStatus.postValue(null)
        }

        viewModel.deleteStatus.observe(this) {
            it?.let {
                Toast.makeText(this, viewModel.message, Toast.LENGTH_SHORT).show()
                if (it) finish()
            }
        }

        viewModel.isUploading.observe(this) {
            if (it)
                loadingDialog.show(supportFragmentManager, null)
            else if (loadingDialog.dialog?.isShowing == true)
                loadingDialog.closeDialog()
        }
    }

    private fun setupStepper() {
        binding.stepper.setupWithNavController(findNavController(R.id.frame_stepper))
        binding.stepper.stepperNavListener = this

        binding.fabNext.setOnClickListener {
            if (binding.stepper.currentStep == END_STEP) {
                if (viewModel.isFormAdd()) {
                    addOrShowError()
                } else {
                    updateOrShowError()
                }
            } else {
                binding.stepper.goToNextStep()
            }
        }
        binding.fabPrevious.setOnClickListener {
            binding.stepper.goToPreviousStep()
        }
    }

    private fun addOrShowError() {
        var hasError = true
        var message = ""

        if (viewModel.songFile.value == null) {
            message = "Chưa chọn file bài hát"
            viewModel.songFile.postValue(null)
        } else if (viewModel.songName.trim().isEmpty()) {
            message = "Chưa nhập tên bài hát"
        } else if (viewModel.album.value == null) {
            message = "Chưa chọn album"
            viewModel.album.postValue(null)
        } else if (viewModel.types.value == null || viewModel.types.value!!.isEmpty()) {
            message = "Chưa chọn thể loại"
        } else {
            hasError = false
        }

        if (hasError) {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            while (binding.stepper.currentStep != 0) binding.stepper.goToPreviousStep()
        } else {
            viewModel.addSong()
        }
    }

    private fun updateOrShowError() {
        var hasError = true
        var message = ""

        if (viewModel.songName.trim().isEmpty()) {
            Toast.makeText(this, "Chưa nhập tên bài hát", Toast.LENGTH_SHORT).show()
        } else if (viewModel.types.value == null || viewModel.types.value!!.isEmpty()) {
            message = "Chưa chọn thể loại"
        } else {
            hasError = false
        }

        if (hasError) {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            while (binding.stepper.currentStep != 0) binding.stepper.goToPreviousStep()
        } else {
            viewModel.updateSong()
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


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        if (!viewModel.isFormAdd()) menuInflater.inflate(R.menu.form_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> super.onBackPressed()
            R.id.actionRemove -> {
                showConfirmDialog(
                    "Xác nhận xóa",
                    "Mọi thông tin liên quan đến bài hát cũng sẽ bị xóa!\nBạn chắc chắn muốn xóa bài hát này?",
                    "Xóa",
                    "Hủy",
                    "",
                    object : ConfirmDialog.ConfirmCallback {
                        override fun negativeAction() {
                        }

                        override fun positiveAction() {
                            viewModel.deleteSong(viewModel.songEdit!!.idSong)
                        }
                    }
                )
            }
        }
        return super.onOptionsItemSelected(item)
    }
}