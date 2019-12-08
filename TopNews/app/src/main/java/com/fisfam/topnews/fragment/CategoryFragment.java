package com.fisfam.topnews.fragment;

import android.content.Intent;
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
import com.fisfam.topnews.CategoryDetailsActivity;
import com.fisfam.topnews.R;
import com.fisfam.topnews.adapter.CategoryAdapter;
import com.fisfam.topnews.pojo.Category;

import static com.fisfam.topnews.adapter.HomeAdapter.CATEGORY_LIST;

public class CategoryFragment extends Fragment {

    private View mRootView;
    private ShimmerFrameLayout mShimmerFrameLayout;
    private RecyclerView mRecyclerView;
    private CountDownTimer mTimer;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_category, container, false);
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
        mShimmerFrameLayout =mRootView.findViewById(R.id.shimmer_category);

        mRecyclerView = mRootView.findViewById(R.id.category_rv_for_categoryfragment);
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this.getActivity());
        CategoryAdapter adapter = new CategoryAdapter(CATEGORY_LIST, new CategoryAdapter.OnCategoryItemClickListener() {
            @Override
            public void onItemClick(Category category) {
                Intent intent = new Intent(getContext(), CategoryDetailsActivity.class);
                intent.putExtra("Category", category.getCategoryName());
                startActivity(intent);
            }
        });
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(adapter);

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
