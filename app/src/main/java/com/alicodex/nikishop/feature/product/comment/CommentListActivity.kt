package com.alicodex.nikishop.feature.product.comment

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alicodex.nikishop.common.EXTRA_KEY_ID
import com.alicodex.nikishop.common.NikeActivity
import com.alicodex.nikishop.data.Comment
import com.alicodex.nikishop.databinding.ActivityCommentListBinding
import com.alicodex.nikishop.feature.product.CommentAdapter
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class CommentListActivity : NikeActivity() {

    private lateinit var binding: ActivityCommentListBinding

    val viewModel: CommentListViewModel by viewModel {
        parametersOf(
            intent.extras!!.getInt(
                EXTRA_KEY_ID
            )
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCommentListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.progressBarLiveData.observe(this){
            setProgressIndicator(it)
        }

        viewModel.commentsLiveData.observe(this) {
            val adapter = CommentAdapter(true)
            binding.commentsRv.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
            adapter.comments = it as ArrayList<Comment>
            binding.commentsRv.adapter = adapter
        }

        binding.commentListToolbar.onBackButtonClickListener= View.OnClickListener {
            finish()
        }
    }
}