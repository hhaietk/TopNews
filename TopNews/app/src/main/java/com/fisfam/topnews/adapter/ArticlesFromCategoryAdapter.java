package com.fisfam.topnews.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fisfam.topnews.ArticlesDetailsActivity;
import com.fisfam.topnews.R;
import com.fisfam.topnews.pojo.Articles;
import com.fisfam.topnews.utils.UiTools;

import java.lang.ref.WeakReference;
import java.util.List;

public class ArticlesFromCategoryAdapter
        extends RecyclerView.Adapter<ArticlesFromCategoryAdapter.ArticlesFromCategoryViewHolder> {
    private static final String TAG = ArticlesFromCategoryAdapter.class.getSimpleName();
    private List<Articles> mItems;
    private Context mContext;

    public ArticlesFromCategoryAdapter(List<Articles> items, Context context){
        mItems = items;
        mContext = new WeakReference<>(context).get();
    }

    public static class ArticlesFromCategoryViewHolder extends RecyclerView.ViewHolder {
        private TextView title;
        private TextView date;
        private TextView source;
        private ImageView image;

        public ArticlesFromCategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.article_title);
            date = itemView.findViewById(R.id.date);
            source = itemView.findViewById(R.id.txt_type);
            image = itemView.findViewById(R.id.image_articles);
        }
    }

    @NonNull
    @Override
    public ArticlesFromCategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_articles, parent, false);
        return new ArticlesFromCategoryViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ArticlesFromCategoryViewHolder holder, int position) {
        Articles currentItem = mItems.get(position);
        holder.title.setText(currentItem.getTitle());
        holder.date.setText(currentItem.getPublishedAt());
        holder.source.setText(currentItem.getSource().getName());
        if (mContext != null) {
            UiTools.displayImageThumb(mContext, holder.image, currentItem.getUrlToImage(), 0.5f);
        } else {
            Log.e(TAG, "onBindViewHolder: Context must not be null");
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArticlesDetailsActivity.open(mContext,currentItem);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public void addArticles(Articles articles){
        mItems.add(articles);
        int positionStart = getItemCount();
        notifyItemInserted(positionStart);
    }

}
