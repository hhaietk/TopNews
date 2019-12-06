package com.fisfam.topnews.pojo;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Articles implements Parcelable {
    @SerializedName("source")
    private Source source;
    @SerializedName("author")
    private String author;
    @SerializedName("title")
    private String title;
    @SerializedName("description")
    private String description;
    @SerializedName("url")
    private String url;
    @SerializedName("urlToImage")
    private String urlToImage;
    @SerializedName("publishedAt")
    private String publishedAt;
    @SerializedName("content")
    private String content;

    private int isSaved = 0;

    public Articles() { }

    private Articles(Parcel parcel) {
        //read and set saved values from parcel
        title = parcel.readString();
        urlToImage = parcel.readString();
        publishedAt = parcel.readString();
        content = parcel.readString();
        //source.name = parcel.readString();
        description = parcel.readString();
        url = parcel.readString();
        author = parcel.readString();
        isSaved = parcel.readInt();
    }

    @Override
    public int describeContents() {
        return hashCode();
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        //write all properties to the parcel
        parcel.writeString(title);
        parcel.writeString(urlToImage);
        parcel.writeString(publishedAt);
        parcel.writeString(content);
        //parcel.writeString(source.name);
        parcel.writeString(description);
        parcel.writeString(url);
        parcel.writeString(author);
        parcel.writeInt(isSaved);

    }

    public static final Parcelable.Creator<Articles> CREATOR = new Parcelable.Creator<Articles>() {

        @Override
        public Articles createFromParcel(Parcel parcel) {
            return new Articles(parcel);
        }

        @Override
        public Articles[] newArray(int size) {
            return new Articles[0];
        }
    };

    public class Source {
        @SerializedName("id")
        private String id;
        @SerializedName("name")
        private String name;

        public String getId() {
            return id;
        }

        public String getName() {
            return name;
        }
    }

    public Source getSource() {
        return source;
    }

    public String getAuthor() {
        return author;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getUrl() {
        return url;
    }

    public String getUrlToImage() {
        return urlToImage;
    }

    public String getPublishedAt() {
        String string = publishedAt.replaceAll("[T|Z]", " ");
        return string;
    }

    public int getIsSaved() {
        return isSaved;
    }

    public String getContent() {
        return content;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setUrlToImage(String urlToImage) {
        this.urlToImage = urlToImage;
    }

    public void setPublishedAt(String publishedAt) {
        this.publishedAt = publishedAt;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setIsSaved(int saved) {
        isSaved = saved;
    }
}
