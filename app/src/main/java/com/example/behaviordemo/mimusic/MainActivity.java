package com.example.behaviordemo.mimusic;

import android.os.Bundle;
import android.util.TypedValue;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.example.behaviordemo.R;
import com.example.behaviordemo.mimusic.fragment.SongFragment;
import com.example.behaviordemo.mimusic.fragment.TabFragment;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private final String[] mTitles = {
        "热门", "专辑", "视屏", "资讯"
    };
    private TabLayout mSl;
    private ViewPager2 mViewPager;
    private ArrayList<Fragment> mFragments = new ArrayList<>();
    private MyFragmentAdapter mFragmentAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        StatusBarUtil.setTranslucentForImageView(this, 0, null);
        initData();
        initView();
        initEvent();
    }

    private void initEvent() {
        mViewPager.setAdapter(mFragmentAdapter);

//        mSl.setViewPager(mViewPager, mTitles);

        //反射修改最少滑动距离
//        try {
//            Field mTouchSlop = ViewPager.class.getDeclaredField("mTouchSlop");
//            mTouchSlop.setAccessible(true);
//            mTouchSlop.setInt(mViewPager, dp2px(50));
//        } catch (NoSuchFieldException | IllegalAccessException e) {
//            e.printStackTrace();
//        }
        mViewPager.setOffscreenPageLimit(mFragments.size());
    }

    public int dp2px(float dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
            dpVal, getResources().getDisplayMetrics());
    }

    private void initData() {
        mFragments.add(new SongFragment());
        mFragments.add(TabFragment.newInstance("我是专辑页面"));
        mFragments.add(TabFragment.newInstance("我是视屏页面"));
        mFragments.add(TabFragment.newInstance("我是资讯页面"));
        mFragmentAdapter = new MyFragmentAdapter(this);
    }

    private void initView() {
        mSl = findViewById(R.id.stl);
        mViewPager = findViewById(R.id.vp);
//        ViewCompat.setNestedScrollingEnabled(
//            mViewPager,
//            true
//        );
//
//        ((RecyclerView) mViewPager.getChildAt(0)).setNestedScrollingEnabled(
//            true
//        );
    }

    private class MyFragmentAdapter extends FragmentStateAdapter {
        @NonNull
        @Override
        public Fragment createFragment(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getItemCount() {
            return mFragments.size();
        }

        MyFragmentAdapter(AppCompatActivity activity) {
            super(activity);
        }
    }
}
