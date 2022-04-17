package com.team28.wondermusic.ui.comment

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.team28.wondermusic.R
import com.team28.wondermusic.adapter.CommentAdapter
import com.team28.wondermusic.adapter.CommentClickListener
import com.team28.wondermusic.common.Constants
import com.team28.wondermusic.common.Helper
import com.team28.wondermusic.data.TempData
import com.team28.wondermusic.data.models.Account
import com.team28.wondermusic.data.models.Album
import com.team28.wondermusic.data.models.Comment
import com.team28.wondermusic.data.models.Song
import com.team28.wondermusic.databinding.ActivityCommentBinding

class CommentActivity : AppCompatActivity(), CommentClickListener {

    private lateinit var binding: ActivityCommentBinding

    lateinit var commentAdapter: CommentAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCommentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Helper.setStatusBarGradiant(this, R.color.main_bg_color)

        binding.toolbar.title = "Bình luận"
        setSupportActionBar(binding.toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true);
        supportActionBar?.setDisplayShowHomeEnabled(true);

        commentAdapter = CommentAdapter(this)

        binding.recyclerView.apply {
            adapter = commentAdapter
            layoutManager = LinearLayoutManager(this@CommentActivity)
        }

        populateCommentData()
    }

    override fun onViewChildrenComment(parentComment: Comment) {
        val intent = Intent(this, CommentReplyActivity::class.java).apply {
            putExtra(Constants.Comment, parentComment)
        }
        startActivity(intent)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> super.onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun populateCommentData() {
        val song = TempData.songs[0]

        val comments = arrayListOf(
            Comment(
                idComment = 1,
                song = song,
                account = Account(1, "huy@gmail.com", "Phát Huy T4", "", "01/01/2020", 0, 0),
                content = "Bài hát rất hay, ủng hộ bạn!",
                dateTime = "01/01/2021",
                idCommentParent = 0,
                children = arrayListOf(
                    Comment(
                        idComment = 5,
                        song = song,
                        account = Account(
                            1,
                            "huy@gmail.com",
                            "Phát Huy T4",
                            "",
                            "01/01/2020",
                            0,
                            0
                        ),
                        content = "Đông quan điểm với bạn <3",
                        dateTime = "01/01/2021",
                        idCommentParent = 0
                    ),
                    Comment(
                        idComment = 6,
                        song = song,
                        account = Account(
                            1,
                            "huy@gmail.com",
                            "Phát Huy T4",
                            "",
                            "01/01/2020",
                            0,
                            0
                        ),
                        content = "Quá tuyệt luôn ấy chứ!!!",
                        dateTime = "01/01/2021",
                        idCommentParent = 0
                    ),
                    Comment(
                        idComment = 7,
                        song = song,
                        account = Account(
                            1,
                            "huy@gmail.com",
                            "Phát Huy T4",
                            "",
                            "01/01/2020",
                            0,
                            0
                        ),
                        content = "Cảm ơn các bạn rất nhiều, tương lai mình sẽ ra thêm nhiều bài hay hơn nữa, mong các bạn ủng hộ",
                        dateTime = "01/01/2021",
                        idCommentParent = 0
                    )
                )
            ),
            Comment(
                idComment = 2,
                song = song,
                account = Account(1, "huy@gmail.com", "Phát Huy T4", "", "01/01/2020", 0, 0),
                content = "Bạn cần phát âm tròn hơn",
                dateTime = "01/01/2021",
                idCommentParent = 0
            ),
            Comment(
                idComment = 4,
                song = song,
                account = Account(1, "huy@gmail.com", "Phát Huy T4", "", "01/01/2020", 0, 0),
                content = "Bài hát rất hay, nhưng mà cần chỉnh chu hơn trong việc phát âm, như thế sẽ hay hơn nhiều! Nói chung là tuyệt với",
                dateTime = "01/01/2021",
                idCommentParent = 0
            ),
            Comment(
                idComment = 4,
                song = song,
                account = Account(1, "huy@gmail.com", "Phát Huy T4", "", "01/01/2020", 0, 0),
                content = "Nắm đôi tay kiêu sa, được 1 lần không ta :v",
                dateTime = "01/01/2021",
                idCommentParent = 0,
                children = arrayListOf(
                    Comment(
                        idComment = 8,
                        song = song,
                        account = Account(
                            1,
                            "huy@gmail.com",
                            "Phát Huy T4",
                            "",
                            "01/01/2020",
                            0,
                            0
                        ),
                        content = "Nghĩ qua thôi con tim trong anh đập tung lên rung nóc rung nhà",
                        dateTime = "01/01/2021",
                        idCommentParent = 0
                    ),
                    Comment(
                        idComment = 9,
                        song = song,
                        account = Account(
                            1,
                            "huy@gmail.com",
                            "Phát Huy T4",
                            "",
                            "01/01/2020",
                            0,
                            0
                        ),
                        content = "Hóa ra yêu đơn phương 1 người, hóa ra khi tơ vương 1 người, 3h đêm vẫn ngồi cười hahahahahahahahahahahahahahahahahahahaha",
                        dateTime = "01/01/2021",
                        idCommentParent = 0
                    )
                )
            ),
        )

        commentAdapter.differ.submitList(comments)
    }


}