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

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertArticle(ArticlesEntity article);

    @Query("DELETE FROM articles")
    void deleteAllArticles();

    @Query("DELETE FROM articles WHERE title = :title")
    void deleteArticleWithTitle(String title);

    @Query("SELECT * FROM articles WHERE sourceName = :source ORDER BY publishedAt")
    List<ArticlesEntity> getArticlesBySource(final String source);

    @Query("SELECT * FROM articles WHERE title = :title ORDER BY publishedAt")
    List<ArticlesEntity> getArticlesByTitle(final String title);
}
