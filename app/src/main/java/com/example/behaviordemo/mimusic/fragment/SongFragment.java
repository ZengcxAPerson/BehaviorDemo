package com.example.behaviordemo.mimusic.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.behaviordemo.R;

import java.util.ArrayList;

public class SongFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private SongAdater mAdater;
    private ArrayList<Integer> mDatas = new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_song_layout, container, false);
        initView(view);
        initEvent();
        return view;
    }

    private void initView(View view) {
        mRecyclerView = view.findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    private void initEvent() {
        mRecyclerView.setAdapter(mAdater);
    }

    private void initData() {
        for (int i = 0; i < 32; i++) {
            mDatas.add(i);
        }
        mAdater = new SongAdater(mDatas);
    }
}
