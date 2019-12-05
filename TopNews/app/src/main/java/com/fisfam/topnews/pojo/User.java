package com.fisfam.topnews.pojo;

public class User {

    private String mName;
    private String mEmail;
    private String mCountry;

    public User() {
    }

    public User(String name, String email, String country) {
        mName = name;
        mEmail = email;
        mCountry = country;
    }

    public String getName() {
        return mName;
    }

    public String getEmail() {
        return mEmail;
    }

    public String getCountry() {
        return mCountry;
    }
}
