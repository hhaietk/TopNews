package com.fisfam.topnews.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fisfam.topnews.R;
import com.fisfam.topnews.adapter.ArticlesListAdapter;
import com.fisfam.topnews.pojo.Articles;
import com.fisfam.topnews.room.AppDatabase;
import com.fisfam.topnews.room.ArticlesEntity;

import java.util.ArrayList;
import java.util.List;

public class SavedFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_saved, container, false);
        initRecyclerView(rootView);
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void initRecyclerView(final View rootView) {

        // Get the saved articles from DB
        AppDatabase db = AppDatabase.getDb(getActivity());
        List<ArticlesEntity> articlesList = db.articlesDao().getAllArticles();

        List<Articles> articles = createArticlesFromEntity(articlesList);

        RecyclerView recyclerView = rootView.findViewById(R.id.recyclerView_saved);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        ArticlesListAdapter adapter = new ArticlesListAdapter(articles, getActivity());
        recyclerView.setAdapter(adapter);
    }

    private List<Articles> createArticlesFromEntity(final List<ArticlesEntity> articlesEntityList) {

        List<Articles> articles = new ArrayList<>();

        for (final ArticlesEntity articlesEntity : articlesEntityList) {
            Articles a = new Articles();
            a.setTitle(articlesEntity.getTitle());
            a.setContent(articlesEntity.getContent());
            a.setPublishedAt(articlesEntity.getPublishedAt());
            a.setUrl(articlesEntity.getUrl());
            a.setUrlToImage(articlesEntity.getUrlToImage());
            a.setIsSaved(articlesEntity.getIsSaved());
            articles.add(a);
        }

        return  articles;
    }
}
