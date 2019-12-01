package com.fisfam.topnews.room;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ArticlesDao {

    @Query("SELECT * FROM articles")
    List<ArticlesEntity> getAllArticles();

    // TODO: Could the Articles Entity be the same?
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertArticle(ArticlesEntity article);

    @Query("DELETE FROM articles")
    void deleteAllArticles();

    @Query("SELECT * FROM articles WHERE sourceName = :source ORDER BY publishedAt")
    List<ArticlesEntity> getArticlesBySource(final String source);

    @Query("SELECT * FROM articles WHERE title = :title ORDER BY publishedAt")
    List<ArticlesEntity> getArticlesByTitle(final String title);
}
