package com.fisfam.topnews.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fisfam.topnews.R;
import com.fisfam.topnews.pojo.Category;

import java.util.ArrayList;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {
    private ArrayList<Category> mCategoryList;

    public static class CategoryViewHolder extends RecyclerView.ViewHolder{
        public ImageView mImageView;
        public TextView mTextView;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.category_icon);
            mTextView = itemView.findViewById(R.id.category_title);
        }
    }

    public CategoryAdapter(ArrayList<Category> categoryList){
        mCategoryList = categoryList;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category, parent,false);
        return new CategoryViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        Category currentItem = mCategoryList.get(position);
        holder.mImageView.setImageResource(currentItem.getIconResource());
        holder.mTextView.setText(currentItem.getCategoryName());
    }

    @Override
    public int getItemCount() {
        return mCategoryList.size();
    }
}