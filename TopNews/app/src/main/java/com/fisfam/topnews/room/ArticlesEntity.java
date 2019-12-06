package com.fisfam.topnews.room;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "articles", indices = @Index(value = {"title"}, unique = true))
public class ArticlesEntity {

    @PrimaryKey(autoGenerate = true)
    private int mId;

    @ColumnInfo(name = "sourceName")
    private String mSourceName;

    @ColumnInfo(name = "author")
    private String mAuthor;

    @ColumnInfo(name = "title")
    private String mTitle;

    @ColumnInfo(name = "description")
    private String mDescription;

    @ColumnInfo(name = "url")
    private String mUrl;

    @ColumnInfo(name = "urlToImage")
    private String mUrlToImage;

    @ColumnInfo(name = "publishedAt")
    private String mPublishedAt;

    @ColumnInfo(name = "content")
    private String mContent;

    @ColumnInfo(name = "isSaved")
    private int mIsSaved;

    public ArticlesEntity() {}

    public ArticlesEntity(String publishedAt, String title, String url,
                          String urlToImage, String content, int saved) {
        mPublishedAt = publishedAt;
        mTitle = title;
        mUrl = url;
        mUrlToImage = urlToImage;
        mContent = content;
        mIsSaved = saved;
    }

    public int getId() {
        return mId;
    }

    public String getSourceName() {
        return mSourceName;
    }

    public String getAuthor() {
        return mAuthor;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getDescription() {
        return mDescription;
    }

    public String getUrl() {
        return mUrl;
    }

    public String getUrlToImage() {
        return mUrlToImage;
    }

    public String getPublishedAt() {
        return mPublishedAt;
    }

    public String getContent() {
        return mContent;
    }

    public int getIsSaved() {
        return mIsSaved;
    }

    public void setId(int id) {
        mId = id;
    }

    public void setSourceName(String source) {
        mSourceName = source;
    }

    public void setAuthor(String author) {
        mAuthor = author;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    public void setUrl(String url) {
        mUrl = url;
    }

    public void setUrlToImage(String urlToImage) {
        mUrlToImage = urlToImage;
    }

    public void setPublishedAt(String publishedAt) {
        mPublishedAt = publishedAt;
    }

    public void setContent(String content) {
        mContent = content;
    }

    public void setIsSaved(int isSaved) {
        mIsSaved = isSaved;
    }
}
