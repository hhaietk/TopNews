package com.fisfam.topnews.fragment;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.fisfam.topnews.R;
import com.fisfam.topnews.adapter.CategoryAdapter;
import com.fisfam.topnews.pojo.Category;

import static com.fisfam.topnews.adapter.HomeAdapter.CATEGORY_LIST;

public class TopicFragment extends Fragment {

    private static final String TAG = TopicFragment.class.getSimpleName();
    private View mRootView;
    private ShimmerFrameLayout mShimmerFrameLayout;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private CountDownTimer mTimer;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_topic, container, false);
        initUiComponents();
        startShimmerScreen();
        return mRootView;
    }

    private void startShimmerScreen() {

        mTimer = new CountDownTimer(2000, 100) {

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
        };
        mTimer.start();
    }

    private void initUiComponents() {
        mShimmerFrameLayout =mRootView.findViewById(R.id.shimmer_topic);

        mRecyclerView = mRootView.findViewById(R.id.category_rv_for_topicfragment);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this.getActivity());
        mAdapter = new CategoryAdapter(CATEGORY_LIST, new CategoryAdapter.OnCategoryItemClickListener() {
            @Override
            public void onItemClick(Category category) {
                Toast.makeText(getContext(),category.getCategoryName(),Toast.LENGTH_SHORT).show();
                Log.d(TAG, "onItemClick: "+ category.getCategoryName());
            }
        });
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
        if (mTimer != null) mTimer.cancel();
    }
}
