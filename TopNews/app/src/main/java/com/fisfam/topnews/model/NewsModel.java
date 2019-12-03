package com.fisfam.topnews.model;

import com.fisfam.topnews.pojo.News;
import com.fisfam.topnews.retrofit.NewsService;
import com.fisfam.topnews.retrofit.NewsServiceGenerator;

import io.reactivex.Single;

public class NewsModel {

    public static final String API_KEY = "2ccc7b03852442ea9cbc29b58ee7903a";
    private NewsService mNewsService;

    public NewsModel() {
        mNewsService = NewsServiceGenerator.createService(NewsService.class, API_KEY);
    }

    public Single<News> fetchNews(final String country, final String category, final String sources,
                                            final String keyword, final int pageSize, final int page) {

        return mNewsService.getTopHeadlines(country, category, sources, keyword, pageSize, page);
    }
}
