package com.fisfam.topnews.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import com.fisfam.topnews.CategoryDetailsActivity;
import com.fisfam.topnews.R;
import com.fisfam.topnews.pojo.Articles;
import com.fisfam.topnews.pojo.Category;
import com.fisfam.topnews.pojo.CategoryList;
import com.fisfam.topnews.pojo.Section;
import com.fisfam.topnews.utils.UiTools;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HomeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = HomeAdapter.class.getSimpleName();
    private final String EXTRA_CATEGORY = "Category";
    private static final int VIEW_TYPE_ARTICLES = 100;
    private static final int VIEW_TYPE_CATEGORY = 200;
    private static final int VIEW_TYPE_SECTION = 300;

    public static final ArrayList<Category> CATEGORY_LIST = new ArrayList<>(Arrays.asList(
            new Category(R.drawable.ic_business_category, "Business"),
            new Category(R.drawable.ic_entertainment_category, "Entertainment"),
            new Category(R.drawable.ic_health_category, "Health"),
            new Category(R.drawable.ic_science_category, "Science"),
            new Category(R.drawable.ic_sport_category, "Sport"),
            new Category(R.drawable.ic_technology_category, "Technology")
    ));

    private List<Object> mItems = new ArrayList<>();
    private OnItemClickListener mOnItemClickListener;
    private WeakReference<Context> mContext;

    public HomeAdapter(final Context context) {
        super();
        mContext = new WeakReference<>(context);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;

        if (viewType == VIEW_TYPE_ARTICLES) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_articles, parent, false);
            vh = new ItemArticlesViewHolder(v);
        } else if (viewType == VIEW_TYPE_SECTION) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_section_title, parent, false);
            vh = new ItemSectionViewHolder(v);
        } else {
            // only VIEW_TYPE_CATEGORY left
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_section_category_home, parent, false);
            vh = new ItemCategoryViewHolder(v);
        }

        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Object item = mItems.get(position);

        if (holder instanceof ItemArticlesViewHolder) {
            final Articles articles = (Articles) item;
            final ItemArticlesViewHolder vh = (ItemArticlesViewHolder) holder;
            vh.title.setText(articles.getTitle());
            vh.date.setText(articles.getPublishedAt());
            vh.source.setText(articles.getSource().getName());
            if (mContext.get() != null) {
                UiTools.displayImageThumb(mContext.get(), vh.image, articles.getUrlToImage(), 0.5f);
            } else {
                Log.e(TAG, "onBindViewHolder: Context must not be null");
            }
            vh.rippleLayout.setOnClickListener(view -> {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemArticlesClick(view, articles, position);
                }
            });
        } else if (holder instanceof ItemSectionViewHolder) {
            Section section = (Section) item;
            ItemSectionViewHolder vh = (ItemSectionViewHolder) holder;
            vh.title.setText(section.getTitle());
        } else if (holder instanceof ItemCategoryViewHolder) {
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(mContext.get(), RecyclerView.HORIZONTAL, false);
            ItemCategoryViewHolder vh = (ItemCategoryViewHolder) holder;
            vh.recyclerView.setLayoutManager(layoutManager);
            vh.recyclerView.setHasFixedSize(true);
            attachSnapHelper(vh.recyclerView);
            CategoryAdapter categoryAdapter = new CategoryAdapter(CATEGORY_LIST, category -> {
                Intent intent = new Intent(mContext.get(), CategoryDetailsActivity.class);
                intent.putExtra(EXTRA_CATEGORY, category.getCategoryName());
                mContext.get().startActivity(intent);
            });
            vh.recyclerView.setAdapter(categoryAdapter);
        }
    }

    @Override
    public int getItemViewType(int position) {
        Object o = mItems.get(position);

        if (o instanceof Articles) {
            return VIEW_TYPE_ARTICLES;
        }

        if (o instanceof CategoryList) {
            return VIEW_TYPE_CATEGORY;
        }

        if (o instanceof Section) {
            return VIEW_TYPE_SECTION;
        }

        return -1;
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public static class ItemArticlesViewHolder extends RecyclerView.ViewHolder {

        private TextView title;
        private TextView date;
        private TextView source;
        private TextView content;
        private ImageView image;
        private View rippleLayout;

        ItemArticlesViewHolder(@NonNull View v) {
            super(v);
            title = v.findViewById(R.id.article_title);
            date = v.findViewById(R.id.date);
            source = v.findViewById(R.id.txt_type);
            rippleLayout = v.findViewById(R.id.lyt_articles);
            image = v.findViewById(R.id.image_articles);
        }
    }

    public static class ItemCategoryViewHolder extends RecyclerView.ViewHolder {
        private RecyclerView recyclerView;

        ItemCategoryViewHolder(@NonNull View v) {
            super(v);
            recyclerView = v.findViewById(R.id.category_rv_for_home_fragment);
            // TODO: SnapHelper = smooth scroll
        }
    }

    public static class ItemSectionViewHolder extends RecyclerView.ViewHolder {
        private TextView title;

        ItemSectionViewHolder(View v) {
            super(v);
            title = v.findViewById(R.id.section_title);
        }
    }

    public void addData(final Object object) {
        mItems.add(object);
        int positionStart = getItemCount();
        notifyItemInserted(positionStart);
    }

    public void resetData() {
        mItems.clear();
        notifyDataSetChanged();
    }

    public interface OnItemClickListener {
        void onItemArticlesClick(View view, Articles articles, int position);

        //void onItemCategoryClick(View view, Category obj, int position);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        mOnItemClickListener = mItemClickListener;
    }

    //Add default snap to center effect
    private void attachSnapHelper (RecyclerView recyclerView){
        SnapHelper snapHelper = new LinearSnapHelper();
        recyclerView.setOnFlingListener(null);
        snapHelper.attachToRecyclerView(recyclerView);
    }
}
