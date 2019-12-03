package com.fisfam.topnews.model;

import android.content.Context;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.fisfam.topnews.R;
import com.fisfam.topnews.pojo.Articles;
import com.fisfam.topnews.pojo.News;
import com.fisfam.topnews.retrofit.NewsService;
import com.fisfam.topnews.retrofit.NewsServiceGenerator;
import io.reactivex.Single;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.ArrayList;
import java.util.List;

public class NewsModel {

    private static final String TAG = NewsModel.class.getSimpleName();
    private NewsService mNewsService;
    private List<Articles> mArticles;

    public NewsModel(final Context context) {
        //TODO: should I remove the context dependency?
        mNewsService = NewsServiceGenerator.createService(NewsService.class, context.getString(R.string.api_key));
    }

    public Single<List<Articles>> fetchNews(final String country, final String category, final String sources,
                                            final String keyword, final int pageSize, final int page) {

        Call<News> news = mNewsService.getTopHeadlines(country, category, sources, keyword, pageSize, page);

        news.enqueue(new Callback<News>() {
            @Override
            public void onResponse(@Nullable Call<News> call, @NonNull Response<News> response) {
                News news = response.body();

                if (news == null) {
                    Log.e(TAG, "onResponse: No news is good news");
                    //TODO: handle fail request?
                    return;
                }

                mArticles = news.getArticles();
            }

            @Override
            public void onFailure(@Nullable Call<News> call, @NonNull Throwable t) {
                Log.e(TAG, "onFailure: " + t.toString());
                if (!call.isCanceled()) {
                    //TODO: handle fail request?
                }
            }
        });

    return Single.just(mArticles);
    }
}
