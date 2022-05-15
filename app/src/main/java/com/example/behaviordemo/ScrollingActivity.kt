package com.example.behaviordemo

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.behaviordemo.databinding.ActivityScrollingBinding
import com.google.android.material.appbar.AppBarLayout

class ScrollingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityScrollingBinding

    private val postDetailFragment by lazy {
        PostDetailFragment.create(this)
    }

    private val postDetailCommentFragment by lazy {
        PostDetailCommentFragment.create(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityScrollingBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        val adapter2 = MyItemRecyclerViewAdapter(PlaceholderContent.ITEMS)
//        binding.toolbarRecycler.adapter = adapter2

        initFragment()
    }

    private fun initFragment() {
        postDetailFragment.let {
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(
                R.id.postDetailFragmentContainerView,
                it,
                it.generateDefaultTag()
            )
            transaction.commitNowAllowingStateLoss()
        }
        postDetailCommentFragment.let {
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(
                R.id.postDetailCommentFragmentContainerView,
                it,
                it.generateDefaultTag()
            )
            transaction.commitNowAllowingStateLoss()
        }
    }
}

/**
 * 生成通用Tag，已知问题如果一个页面有多个相同的fragment实例则tag会有问题
 */
fun Class<*>.generateDefaultTag() = "HoYoLab:${this.canonicalName}"

/**
 * 生成通用Tag，已知问题如果一个页面有多个相同的fragment实例则tag会有问题
 */
fun Fragment.generateDefaultTag() = this::class.java.generateDefaultTag()
