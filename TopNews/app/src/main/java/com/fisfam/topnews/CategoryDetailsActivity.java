package com.fisfam.topnews;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.DrawableRes;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fisfam.topnews.adapter.ArticlesListAdapter;
import com.fisfam.topnews.model.NewsModel;
import com.fisfam.topnews.pojo.Articles;
import com.fisfam.topnews.utils.NetworkCheck;
import com.fisfam.topnews.utils.UiTools;
import com.fisfam.topnews.viewmodel.NewsViewModel;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class CategoryDetailsActivity extends AppCompatActivity {

    private static final String TAG = CategoryDetailsActivity.class.getSimpleName();
    private String mCategory;
    private RecyclerView mRecyclerView;
    private ArticlesListAdapter mAdapter;
    private List<Articles> mItems = new ArrayList<>();
    private UserPreference mUserPrefs;
    private NewsViewModel mViewModel;
    private CompositeDisposable mDisposables;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_details);

        mUserPrefs = new UserPreference(this);
        mCategory = getIntent().getStringExtra("Category");
        mDisposables = new CompositeDisposable();
        mViewModel = new NewsViewModel(new NewsModel());

        initToolbar();
        initRecyclerView();
        subscribeNews();
        requestNews();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDisposables.dispose();
    }

    private void initToolbar() {

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(mCategory);
        actionBar.setDisplayHomeAsUpEnabled(true);
        UiTools.setSmartSystemBar(this);
    }

    private void subscribeNews() {

        Disposable d = mViewModel.getArticlesObservable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(articles -> {
                    for (final Articles a : articles) {
                        mAdapter.addArticles(a);
                    }
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
        showFailedView(false, "", R.drawable.img_failed);
        mViewModel.getNews(mUserPrefs.getCountryCode(), mCategory.toLowerCase(), null, null, 50, 0);
    }


    private void initRecyclerView() {
        mRecyclerView = findViewById(R.id.category_articles_recyclerview);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new ArticlesListAdapter(mItems,this);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    private void handleFailRequest() {

        if (NetworkCheck.isNetworkAvailable(this)){
            showFailedView(true, getString(R.string.failed_text), R.drawable.img_failed);
        } else {
            showFailedView(false, getString(R.string.no_internet_text), R.drawable.img_no_internet);
        }
    }

    private void showFailedView(boolean show, String message, @DrawableRes int icon) {
        View view_failed = findViewById(R.id.category_details_failed);

        ((ImageView) findViewById(R.id.failed_icon)).setImageResource(icon);
        ((TextView) findViewById(R.id.failed_message)).setText(message);
        if (show) {
            mRecyclerView.setVisibility(View.INVISIBLE);
            view_failed.setVisibility(View.VISIBLE);
        } else {
            mRecyclerView.setVisibility(View.VISIBLE);
            view_failed.setVisibility(View.GONE);
        }
        findViewById(R.id.failed_retry).setOnClickListener(view -> requestNews());
    }


}
