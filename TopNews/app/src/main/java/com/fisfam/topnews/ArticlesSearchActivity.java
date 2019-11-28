package com.fisfam.topnews;

import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fisfam.topnews.adapter.ArticlesFromCategoryAdapter;
import com.fisfam.topnews.pojo.Articles;
import com.fisfam.topnews.pojo.News;

import java.util.ArrayList;
import java.util.List;

import com.fisfam.topnews.utils.UiTools;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ArticlesSearchActivity extends AppCompatActivity {
    private EditText mEditText;
    private RecyclerView mRecyclerView;
    private View mNoItemFoundedView;
    private ArticlesFromCategoryAdapter mAdapter;
    private List<Articles> mArticlesList = new ArrayList<>();
    private static final String TAG = ArticlesSearchActivity.class.getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        initUIComponents();
        UiTools.setSmartSystemBar(this);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return(super.onOptionsItemSelected(item));
    }

    private void initUIComponents() {
        mRecyclerView = findViewById(R.id.recyclerView_search);
        initRecyclerView();
        mNoItemFoundedView = findViewById(R.id.lyt_failed);

        Toast.makeText(this, R.string.please_enter_text, Toast.LENGTH_LONG).show();

        mEditText = findViewById(R.id.et_search);

        //perform search and hide keyboard after hit the search button
        mEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    performSearch();
                }
                return false;
            }
        });

        ImageButton backButton = findViewById(R.id.button_back_search);
        backButton.setOnClickListener(v -> finish());

    }

    private void performSearch() {
        mAdapter.resetArticles(); //clear the articles from the last search
        String query = mEditText.getText().toString().toLowerCase().trim();
        if (query == null || query.isEmpty() ) {
            Toast.makeText(this, R.string.please_enter_text, Toast.LENGTH_SHORT).show();
        } else {
            requestData(query);
        }

    }

    private void requestData(String query) {
        UserPreference userPrefs = new UserPreference(this);
        NewsService newsService = NewsServiceGenerator.createService(NewsService.class, getString(R.string.api_key));
        Call<News> newsCall = newsService.getTopHeadlines(
                userPrefs.getCountryCode(), null, null, query, 100, 0);
        newsCall.enqueue(new Callback<News>() {
            @Override
            public void onResponse(Call<News> call, Response<News> response) {
                News news = response.body();

                if (news.getTotalResults() == 0) {
                    mRecyclerView.setVisibility(View.INVISIBLE);
                    mNoItemFoundedView.setVisibility(View.VISIBLE);
                    //Log.d(TAG, "NO NEWS");
                    return;
                }

                for (Articles articles : news.getArticles()){
                    mAdapter.addArticles(articles);
                    //Log.d(TAG, "onResponse: "+ articles.toString());
                }
                mRecyclerView.setVisibility(View.VISIBLE);
                mNoItemFoundedView.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<News> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.toString());
            }
        });
    }

    private void initRecyclerView() {
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new ArticlesFromCategoryAdapter(mArticlesList, this);
        mRecyclerView.setAdapter(mAdapter);
    }
}
