package com.fisfam.topnews.retrofit;

import com.fisfam.topnews.pojo.News;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface NewsService {
    @GET("/v2/top-headlines")
    Call<News> getTopHeadlines(
            @Query("country") String country,
            @Query("category") String category,
            @Query("sources") String sources,
            @Query("q") String keyWord,
            @Query("pageSize") int pageSize,
            @Query("page") int page);
}
