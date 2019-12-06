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

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fisfam.topnews.adapter.ArticlesListAdapter;
import com.fisfam.topnews.model.NewsModel;
import com.fisfam.topnews.pojo.Articles;
import com.fisfam.topnews.utils.UiTools;
import com.fisfam.topnews.viewmodel.NewsViewModel;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class SearchActivity extends AppCompatActivity {
    private EditText mEditText;
    private RecyclerView mRecyclerView;
    private View mNoItemFoundedView;
    private ArticlesListAdapter mAdapter;
    private List<Articles> mArticlesList = new ArrayList<>();
    private static final String TAG = SearchActivity.class.getSimpleName();

    UserPreference mUserPrefs;
    private NewsViewModel mViewModel;
    private CompositeDisposable mDisposables;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        mUserPrefs = new UserPreference(this);
        mViewModel = new NewsViewModel(new NewsModel());
        mDisposables = new CompositeDisposable();

        initUIComponents();
        UiTools.setSmartSystemBar(this);
        subscribeNews();
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

    private void subscribeNews() {

        Disposable d = mViewModel.getArticlesObservable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(articles -> {
                    if (articles.size() == 0) {
                        mRecyclerView.setVisibility(View.INVISIBLE);
                        mNoItemFoundedView.setVisibility(View.VISIBLE);
                        ((TextView) findViewById(R.id.failed_message)).setText(R.string.no_results);
                    } else {
                        for (Articles a : articles){
                            mAdapter.addArticles(a);
                        }
                        mRecyclerView.setVisibility(View.VISIBLE);
                        mNoItemFoundedView.setVisibility(View.GONE);
                    }
                });

        Disposable d2 = mViewModel.getErrorObservable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(error -> {
                    Log.e(TAG, "subscribeNews: error = " + error);

                });

        mDisposables.addAll(d, d2);
    }

    private void performSearch() {
        mAdapter.resetArticles(); //clear the articles from the last search
        String query = mEditText.getText().toString().toLowerCase().trim();
        if (query.isEmpty() ) {
            Toast.makeText(this, R.string.please_enter_text, Toast.LENGTH_SHORT).show();
        } else {
            requestNews(query);
        }

    }

    private void requestNews(final String query) {
        mViewModel.getNews(mUserPrefs.getCountryCode(), null, null, query, 50, 0);
    }

    private void initRecyclerView() {
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new ArticlesListAdapter(mArticlesList, this);
        mRecyclerView.setAdapter(mAdapter);
    }
}
