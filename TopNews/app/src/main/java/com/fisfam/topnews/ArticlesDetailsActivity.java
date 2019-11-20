package com.fisfam.topnews;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.NestedScrollView;

import com.fisfam.topnews.pojo.Articles;
import com.fisfam.topnews.utils.UiTools;

public class ArticlesDetailsActivity extends AppCompatActivity {

    private static final String EXTRA_ARTICLES = "Articles";
    private Articles mArticles;

    public static void open(final Context context, final Articles articles) {
        Intent i = new Intent(context, ArticlesDetailsActivity.class);
        i.putExtra(EXTRA_ARTICLES, articles);
        context.startActivity(i);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_articles_details);

        collectArticles();
        initToolbar();
        initUiComponents();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_activity_news_details, menu);
        MenuItem menu_saved = menu.findItem(R.id.action_saved);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.action_saved:
                // TODO: save to database
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void collectArticles() {
        final Intent i = getIntent();
        mArticles = i.getParcelableExtra(EXTRA_ARTICLES);

    }

    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar_articles_details);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        //toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.colorTextAction), PorterDuff.Mode.SRC_ATOP);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        UiTools.setSmartSystemBar(this);
    }

    private boolean lyt_navigation_hide;

    private void initUiComponents() {
        NestedScrollView nestedScrollView = findViewById(R.id.nested_scroll_view);
        View lyt_bottom_bar = findViewById(R.id.lyt_bottom_bar);
        View lyt_toolbar = findViewById(R.id.lyt_toolbar);

        // handle auto hide toolbar and bottom bar
        nestedScrollView.setOnScrollChangeListener(
                (NestedScrollView.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
            if (scrollY >= oldScrollY) { // down
                if (lyt_navigation_hide) return;
                UiTools.hideBottomBar(lyt_bottom_bar);
                UiTools.hideToolbar(lyt_toolbar);
                lyt_navigation_hide = true;
            } else {
                if (!lyt_navigation_hide) return;
                UiTools.showBottomBar(lyt_bottom_bar);
                UiTools.showToolbar(lyt_toolbar);
                lyt_navigation_hide = false;
            }
        });

        TextView title = findViewById(R.id.title_articles_details);
        TextView date = findViewById(R.id.date_articles_details);
        ImageView image = findViewById(R.id.image_articles_details);
        TextView content = findViewById(R.id.content_articles_details);

        title.setText(mArticles.getTitle());
        date.setText(mArticles.getPublishedAt());
        UiTools.displayImageThumb(this, image, mArticles.getUrlToImage(), 0.5f);
        content.setText(mArticles.getContent());
        content.append("\n");
        content.append(mArticles.getUrl());
    }
}
