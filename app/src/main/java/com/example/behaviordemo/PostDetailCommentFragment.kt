package com.example.behaviordemo

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.behaviordemo.databinding.FragmentDetailCommentBinding
import com.example.behaviordemo.placeholder.PlaceholderContent

class PostDetailCommentFragment : Fragment(R.layout.fragment_detail_comment) {
    companion object {
        fun create(
            scrollingActivity: ScrollingActivity
        ): Fragment {
            return PostDetailCommentFragment()
        }
    }

    private lateinit var binding: FragmentDetailCommentBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentDetailCommentBinding.bind(view)

        val list = mutableListOf<PlaceholderContent.PlaceholderItem>()
        list.addAll(
            PlaceholderContent.ITEMS
        )

        list.add(
            PlaceholderContent.createPlaceholderItem(26)
        )
        list.add(
            PlaceholderContent.createPlaceholderItem(27)
        )
        val adapter = MyItemRecyclerViewAdapter(list)
        binding.commentRecyclerView.adapter = adapter
        binding.commentRecyclerView.isNestedScrollingEnabled = true

        // TODO  1. 问题： 下方的commentFragment没有内容。没有可以触发滑动事件的view。导致滑不上去
        // 暂时通过了下半部分空态内容嵌套一个nestedScrollView来解决了这个问题
        binding.nestedScrollView.isNestedScrollingEnabled = true


        binding.commentHeaderView.setOnClickListener {
            Log.e("TAG", "点击")
        }
    }
}
