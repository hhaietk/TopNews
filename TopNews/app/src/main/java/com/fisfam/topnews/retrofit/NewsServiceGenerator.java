package com.fisfam.topnews.retrofit;

import android.util.Log;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class NewsServiceGenerator {
    private static final String TAG = NewsServiceGenerator.class.getSimpleName();

    private static final String BASE_URL = "https://newsapi.org";

    private static Retrofit.Builder builder = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create());

    private static Retrofit retrofit = builder.build();

    private static OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

    private static HttpLoggingInterceptor logging = new HttpLoggingInterceptor();

    public static <S> S createService(Class<S> serviceClass, final String apiKey) {

        if (!httpClient.interceptors().contains(logging)) {
            logging.level(HttpLoggingInterceptor.Level.BASIC);
            httpClient.addInterceptor(logging);
        }

        if (apiKey != null) {
            httpClient.interceptors().clear();
            httpClient.addInterceptor(chain -> {
                Request original = chain.request();
                Request request = original.newBuilder()
                        .header("Authorization", apiKey)
                        .build();
                return chain.proceed(request);
            });
            builder.client(httpClient.build());
            retrofit = builder.build();
        } else {
            Log.e(TAG, "Api Key is null");
        }
        return retrofit.create(serviceClass);
    }
}
