package com.example.behaviordemo

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.behaviordemo.databinding.FragmentDetailBinding
import com.example.behaviordemo.placeholder.PlaceholderContent

class PostDetailFragment : Fragment(R.layout.fragment_detail) {
    companion object {
        fun create(
            scrollingActivity: ScrollingActivity
        ): Fragment {
            return PostDetailFragment()
        }
    }

    private lateinit var binding: FragmentDetailBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentDetailBinding.bind(view)

        val adapter = MyItemRecyclerViewAdapter(PlaceholderContent.ITEMS)
        binding.postDetailRecyclerView.adapter = adapter
        binding.postDetailRecyclerView.isNestedScrollingEnabled = true
    }
}
