package com.fisfam.topnews.fragment;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.fisfam.topnews.R;
import com.fisfam.topnews.adapter.CategoryAdapter;
import com.fisfam.topnews.pojo.Category;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class TopicFragment extends Fragment {

    private View mRootView;
    private ShimmerFrameLayout mShimmerFrameLayout;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_topic, container, false);
        initUiComponents();
        startShimmerScreen();
        return mRootView;
    }

    private void startShimmerScreen() {

        new CountDownTimer(2000, 100) {

            public void onTick(long millisUntilFinished) {
                mRecyclerView.setVisibility(View.INVISIBLE);
                mShimmerFrameLayout.setVisibility(View.VISIBLE);
                mShimmerFrameLayout.startShimmer();
            }

            public void onFinish() {
                mRecyclerView.setVisibility(View.VISIBLE);
                mShimmerFrameLayout.setVisibility(View.GONE);
                mShimmerFrameLayout.stopShimmer();
            }
        }.start();

//        TimerTask task = new TimerTask() {
//            @Override
//            public void run() {
//                mShimmerFrameLayout.setVisibility(View.INVISIBLE);
//                mShimmerFrameLayout.stopShimmer();
//            }
//        };
//        new Timer().schedule(task, 1000);
    }

    private void initUiComponents() {
        mShimmerFrameLayout =mRootView.findViewById(R.id.shimmer_topic);

        ArrayList<Category> topicList = new ArrayList<>();
        topicList.add(new Category(R.drawable.avatar_placeholder, "Business"));
        topicList.add(new Category(R.drawable.avatar_placeholder, "Entertainment"));
        topicList.add(new Category(R.drawable.avatar_placeholder, "Health"));
        topicList.add(new Category(R.drawable.avatar_placeholder, "Science"));
        topicList.add(new Category(R.drawable.avatar_placeholder, "Sport"));
        topicList.add(new Category(R.drawable.avatar_placeholder, "Technology"));

        mRecyclerView = mRootView.findViewById(R.id.categoryRecyclerView);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this.getActivity());
        mAdapter = new CategoryAdapter(topicList);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
