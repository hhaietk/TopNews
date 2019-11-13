package com.fisfam.topnews.pojo;

import com.fisfam.topnews.R;

import java.util.ArrayList;

public class Category {
    private int mIconResource;
    private String mCategoryName;

    public Category(int iconResource, String categoryName){
        mIconResource = iconResource;
        mCategoryName = categoryName;
    }

    public int getIconResource() {
        return mIconResource;
    }

    public String getCategoryName() {
        return mCategoryName;
    }

}
