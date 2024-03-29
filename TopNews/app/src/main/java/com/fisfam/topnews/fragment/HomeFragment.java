package com.fisfam.topnews.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.fisfam.topnews.ArticlesDetailsActivity;
import com.fisfam.topnews.R;
import com.fisfam.topnews.UserPreference;
import com.fisfam.topnews.adapter.HomeAdapter;
import com.fisfam.topnews.model.NewsModel;
import com.fisfam.topnews.pojo.Articles;
import com.fisfam.topnews.pojo.Category;
import com.fisfam.topnews.pojo.CategoryList;
import com.fisfam.topnews.pojo.Section;
import com.fisfam.topnews.utils.NetworkCheck;
import com.fisfam.topnews.viewmodel.NewsViewModel;

import java.util.ArrayList;
import java.util.Arrays;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class HomeFragment extends Fragment {

    private static final String TAG = HomeFragment.class.getSimpleName();
    private static final ArrayList<Category> CATEGORY_LIST = new ArrayList<>(Arrays.asList(
            new Category(R.drawable.avatar_placeholder, "Business"),
            new Category(R.drawable.avatar_placeholder, "Entertainment"),
            new Category(R.drawable.avatar_placeholder, "Health"),
            new Category(R.drawable.avatar_placeholder, "Science"),
            new Category(R.drawable.avatar_placeholder, "Sport"),
            new Category(R.drawable.avatar_placeholder, "Technology")));

    private View mRootView;
    private RecyclerView mHomeRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ShimmerFrameLayout mShimmerFrameLayout;
    private HomeAdapter mHomeAdapter;
    private UserPreference mUserPrefs;
    private NewsViewModel mViewModel;
    private CompositeDisposable mDisposables;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_home, container, false);
        mUserPrefs = new UserPreference(getActivity());
        mViewModel = new NewsViewModel(new NewsModel());
        mDisposables = new CompositeDisposable();
        initUiComponents();
        subscribeNews();
        requestNews();
        return mRootView;
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
        mDisposables.dispose();
    }

    private void initUiComponents() {
        mShimmerFrameLayout = mRootView.findViewById(R.id.shimmer_home);
        mSwipeRefreshLayout = mRootView.findViewById(R.id.swipe_refresh);
        mHomeRecyclerView = mRootView.findViewById(R.id.home_recycler_view);

        mHomeRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mHomeRecyclerView.setHasFixedSize(true);

        mHomeAdapter = new HomeAdapter(getActivity());
        mHomeRecyclerView.setAdapter(mHomeAdapter);

        mHomeAdapter.setOnItemClickListener((view, articles, position)
                -> ArticlesDetailsActivity.open(getActivity(), articles));

        mSwipeRefreshLayout.setOnRefreshListener(() -> {
            mHomeAdapter.resetData();
            requestNews();
        });
    }

    private void subscribeNews() {

        Disposable d = mViewModel.getArticlesObservable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(articles -> {
                    mHomeAdapter.addData(new Section(getString(R.string.section_category)));
                    mHomeAdapter.addData(new CategoryList(CATEGORY_LIST));
                    mHomeAdapter.addData(new Section(getString(R.string.section_breaking_news)));
                    for (final Articles a : articles) {
                        mHomeAdapter.addData(a);
                    }
                    showRefreshing(false);
                });

        Disposable d2 = mViewModel.getErrorObservable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(error -> {
                            Log.e(TAG, "subscribeNews: error = " + error);
                            handleFailRequest();
                        });

        mDisposables.addAll(d, d2);
    }

    private void requestNews() {
        showRefreshing(true);
        showFailedView(false, "", R.drawable.img_failed);
        mViewModel.getNews(mUserPrefs.getCountryCode(), null, null, null, 50, 0);
    }

    private void handleFailRequest() {
        showRefreshing(false);
        if (NetworkCheck.isNetworkAvailable(getActivity())){
            showFailedView(true, getString(R.string.failed_text), R.drawable.img_failed);
        } else {
            showFailedView(true, getString(R.string.no_internet_text), R.drawable.img_no_internet);
        }
    }

    private void showFailedView(boolean show, String message, @DrawableRes int icon) {
        View lyt_failed = mRootView.findViewById(R.id.lyt_failed);

        ((ImageView) mRootView.findViewById(R.id.failed_icon)).setImageResource(icon);
        ((TextView) mRootView.findViewById(R.id.failed_message)).setText(message);
        if (show) {
            mHomeRecyclerView.setVisibility(View.INVISIBLE);
            lyt_failed.setVisibility(View.VISIBLE);
        } else {
            mHomeRecyclerView.setVisibility(View.VISIBLE);
            lyt_failed.setVisibility(View.GONE);
        }
        (mRootView.findViewById(R.id.failed_retry)).setOnClickListener(view -> requestNews());
    }

    private void showRefreshing(final boolean show) {
        mSwipeRefreshLayout.setRefreshing(show);
        if (show) {
            mShimmerFrameLayout.setVisibility(View.VISIBLE);
            mShimmerFrameLayout.startShimmer();
            return;
        }
        mShimmerFrameLayout.setVisibility(View.INVISIBLE);
        mShimmerFrameLayout.stopShimmer();
    }
}
