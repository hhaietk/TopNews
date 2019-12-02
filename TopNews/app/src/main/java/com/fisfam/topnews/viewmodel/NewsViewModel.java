package com.fisfam.topnews.viewmodel;

import com.fisfam.topnews.model.NewsModel;
import com.fisfam.topnews.pojo.Articles;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;

import java.util.List;

public class NewsViewModel {

    private NewsModel mNewsModel;
    private PublishSubject<List<Articles>> mArticlesObservable;
    private CompositeDisposable mDisposables = new CompositeDisposable();

    public NewsViewModel(final NewsModel model) {
        mNewsModel = model;
        mArticlesObservable = PublishSubject.create();
    }

    private void getNews(final String country, final String category, final String sources,
                         final String keyword, final int pageSize, final int page) {

        final Disposable disposable = mNewsModel.fetchNews(country, category, sources, keyword, pageSize, page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<List<Articles>>() {
                    @Override
                    public void onSuccess(List<Articles> articles) {
                        mArticlesObservable.onNext(articles);
                    }

                    @Override
                    public void onError(Throwable e) {
                        //TODO: handle error, maybe passing the error message?
                    }
                });

        mDisposables.add(disposable);
    }
}
