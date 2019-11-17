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
    private final OnCategoryItemClickListener mListener;

    public static class CategoryViewHolder extends RecyclerView.ViewHolder {
        private ImageView mImageView;
        private TextView mTextView;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.category_icon);
            mTextView = itemView.findViewById(R.id.category_title);
        }
    }

    public CategoryAdapter(ArrayList<Category> categoryList, OnCategoryItemClickListener listener){
        mCategoryList = categoryList;
        mListener = listener;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        //Condition to choose item_category layout,either for the RV in HomeFragment, or for the RV in TopicFragment
        if (parent.getId() == R.id.category_rv_for_home_fragment) {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category_for_homefragment, parent, false);
        } else {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category_for_topicfragment,parent,false);
        }
        return new CategoryViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        Category currentItem = mCategoryList.get(position);
        holder.mImageView.setImageResource(currentItem.getIconResource());
        holder.mTextView.setText(currentItem.getCategoryName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onItemClick(currentItem);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mCategoryList.size();
    }

    public interface OnCategoryItemClickListener {
        void onItemClick(Category category);
    }

}
