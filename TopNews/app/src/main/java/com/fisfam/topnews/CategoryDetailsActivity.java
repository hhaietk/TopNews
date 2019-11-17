package com.fisfam.topnews;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.fisfam.topnews.adapter.ArticlesFromCategoryAdapter;
import com.fisfam.topnews.pojo.Articles;
import com.fisfam.topnews.pojo.News;
import com.fisfam.topnews.utils.UiTools;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoryDetailsActivity extends AppCompatActivity {
    private static final String TAG = CategoryDetailsActivity.class.getSimpleName();
    private Toolbar mToolbar;
    private String mCategory;
    private RecyclerView mRecyclerView;
    private ArticlesFromCategoryAdapter mAdapter;
    private List<Articles> mItems = new ArrayList<>();
    private Call<News> mCallNews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCategory = getIntent().getStringExtra("Category");
        setContentView(R.layout.activity_category_details);
        initToolbar();
        requestData();
        initRecyclerView();
    }

    private void initToolbar() {
        mToolbar = findViewById(R.id.toolbar);
        mToolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(mCategory);
        actionBar.setDisplayHomeAsUpEnabled(true);
        UiTools.setSmartSystemBar(this);
    }

    private void requestData(){
        NewsService newsService =
                NewsServiceGenerator.createService(NewsService.class, getString(R.string.api_key));
        mCallNews = newsService.getTopHeadlines(
                "gb", mCategory.toLowerCase(), null, null, 10, 0);
        mCallNews.enqueue(new Callback<News>() {
            @Override
            public void onResponse(@Nullable Call<News> call, @NonNull Response<News> response) {
                News news = response.body();

                if (news == null) {
                    Log.e(TAG, "onResponse: No news is good news");
                    //handleFailRequest();
                    return;
                }

                for (final Articles articles : news.getArticles()) {
                    mItems.add(articles);
                    Log.d(TAG, "Category:" + mCategory + "" + articles.getTitle());
                }

                //showRefreshing(false);
            }

            @Override
            public void onFailure(@Nullable Call<News> call, @NonNull Throwable t) {
                Log.e(TAG, t.toString());
                if (!call.isCanceled()) {
                    //handleFailRequest();
                }
            }
        });

    }

    private void initRecyclerView() {
        mRecyclerView = findViewById(R.id.category_articles_recyclerview);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new ArticlesFromCategoryAdapter(mItems,this);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

}
