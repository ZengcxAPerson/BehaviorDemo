package com.example.behaviordemo

import android.os.Bundle
import android.view.View
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
        val adapter = MyItemRecyclerViewAdapter(emptyList())
        binding.commentRecyclerView.adapter = adapter
        binding.commentRecyclerView.isNestedScrollingEnabled = true
    }
}
