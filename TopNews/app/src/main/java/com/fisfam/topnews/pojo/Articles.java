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
    }

    @Override
    public int describeContents() {
        return hashCode();
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        //write all properties to the parcle
        parcel.writeString(title);
        parcel.writeString(urlToImage);
        parcel.writeString(publishedAt);
        parcel.writeString(content);
        //parcel.writeString(source.name);
        parcel.writeString(description);
        parcel.writeString(url);
        parcel.writeString(author);

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
        //
        String string = publishedAt.replaceAll("[T|Z]", " ");
        return string;
    }

    public String getContent() {
        return content;
    }
}
