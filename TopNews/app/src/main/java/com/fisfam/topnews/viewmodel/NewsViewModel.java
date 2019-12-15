package com.fisfam.topnews.viewmodel;

import com.fisfam.topnews.model.NewsModel;
import com.fisfam.topnews.pojo.Articles;
import com.fisfam.topnews.pojo.News;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;

public class NewsViewModel {

    private NewsModel mNewsModel;
    private PublishSubject<List<Articles>> mArticlesObservable;
    private PublishSubject<String> mErrorObservable;
    private CompositeDisposable mDisposables = new CompositeDisposable();

    public NewsViewModel(final NewsModel model) {
        mNewsModel = model;
        mArticlesObservable = PublishSubject.create();
        mErrorObservable = PublishSubject.create();
    }

    public void getNews(final String country, final String category, final String sources,
                         final String keyword, final int pageSize, final int page) {

        final Disposable disposable = mNewsModel.fetchNews(country, category, sources, keyword, pageSize, page)
                .map(News::getArticles)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<List<Articles>>() {
                    @Override
                    public void onSuccess(List<Articles> articles) {
                        mArticlesObservable.onNext(articles);
                    }

                    @Override
                    public void onError(Throwable e) {
                        mErrorObservable.onNext(e.toString());
                    }
                });

        mDisposables.add(disposable);
    }

    public PublishSubject<List<Articles>> getArticlesObservable() {
        return mArticlesObservable;
    }

    public PublishSubject<String> getErrorObservable() {
        return mErrorObservable;
    }
}
