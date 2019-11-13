package com.fisfam.topnews.adapter;

import android.appwidget.AppWidgetProviderInfo;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fisfam.topnews.R;
import com.fisfam.topnews.pojo.Articles;
import com.fisfam.topnews.pojo.Category;
import com.fisfam.topnews.pojo.Section;
import com.fisfam.topnews.pojo.TopicList;
import com.fisfam.topnews.utils.UiTools;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class HomeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_TYPE_ARTICLES = 100;
    private static final int VIEW_TYPE_TOPIC = 200;
    private static final int VIEW_TYPE_SECTION = 300;
    private static final int VIEW_TYPE_PROGRESS = 400;
    public static final ArrayList<Category> CATEGORY_LIST = new ArrayList<>(Arrays.asList(
            new Category(R.drawable.avatar_placeholder, "Business"),
            new Category(R.drawable.avatar_placeholder, "Entertainment"),
            new Category(R.drawable.avatar_placeholder, "Health"),
            new Category(R.drawable.avatar_placeholder, "Science"),
            new Category(R.drawable.avatar_placeholder, "Sport"),
            new Category(R.drawable.avatar_placeholder, "Technology")
    ));

    private List mItems = new ArrayList<>();
    private OnLoadMoreListener mOnLoadMoreListener;
    private OnItemClickListener mOnItemClickListener;
    private Context mContext;
    private boolean mIsLoading;

    public HomeAdapter(final Context context, final RecyclerView recyclerView) {
        super();
        mItems.add(0, CATEGORY_LIST);
        mContext = new WeakReference<>(context).get();
        initLastItemDetector(recyclerView);
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
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_section_topic_home, parent, false);
            vh = new ItemTopicViewHolder(v);
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
            if (mContext != null) {
                UiTools.displayImageThumb(mContext, vh.image, articles.getUrlToImage(), 0.5f);
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
        } else if (holder instanceof ItemTopicViewHolder) {
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(mContext, RecyclerView.HORIZONTAL, false);
            ItemTopicViewHolder vh = (ItemTopicViewHolder) holder;
            vh.recyclerView.setLayoutManager(layoutManager);
            vh.recyclerView.setHasFixedSize(true);
            CategoryAdapter categoryAdapter = new CategoryAdapter(CATEGORY_LIST);
            vh.recyclerView.setAdapter(categoryAdapter);
        }
    }

    @Override
    public int getItemViewType(int position) {
        Object o = mItems.get(position);

        if (o instanceof Articles) {
            return VIEW_TYPE_ARTICLES;
        }

        if (o.equals(CATEGORY_LIST)) {
            return VIEW_TYPE_TOPIC;
        }

        if (o instanceof Section) {
            return VIEW_TYPE_SECTION;
        }

/*        if (o instanceof Progress) {
            return VIEW_TYPE_PROGRESS;
        }*/

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

        public ItemArticlesViewHolder(@NonNull View v) {
            super(v);
            title = v.findViewById(R.id.article_title);
            date = v.findViewById(R.id.date);
            source = v.findViewById(R.id.txt_type);
            rippleLayout = v.findViewById(R.id.lyt_articles);
            image = v.findViewById(R.id.image_articles);
        }
    }

    public static class ItemTopicViewHolder extends RecyclerView.ViewHolder {
        private RecyclerView recyclerView;

        public ItemTopicViewHolder(@NonNull View v) {
            super(v);
            recyclerView = v.findViewById(R.id.topic_recycler_view);
        }
    }

    public static class ItemSectionViewHolder extends RecyclerView.ViewHolder {
        private TextView title;

        public ItemSectionViewHolder(View v) {
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
        mItems.add(0, CATEGORY_LIST); //to keep the RecyclerView for categories after refresh
        notifyDataSetChanged();
    }

    private void initLastItemDetector(RecyclerView recyclerView) {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                final LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                if (layoutManager == null) {
                    Log.e(TAG, "lastItemViewDetector: Layout Manager is null");
                    return;
                }

                int lastPos = layoutManager.findLastVisibleItemPosition();
                if (!mIsLoading && lastPos == getItemCount() - 1 && mOnLoadMoreListener != null) {
                    mOnLoadMoreListener.onLoadMore();
                    mIsLoading = true;
                }
            }
        });
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        mOnLoadMoreListener = onLoadMoreListener;
    }

    public interface OnLoadMoreListener {
        void onLoadMore();
    }

    public interface OnItemClickListener {
        void onItemArticlesClick(View view, Articles articles, int position);

        //void onItemTopicClick(View view, Topic obj, int position);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        mOnItemClickListener = mItemClickListener;
    }
}
