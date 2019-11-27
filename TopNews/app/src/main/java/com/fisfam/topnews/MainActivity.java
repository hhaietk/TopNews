package com.fisfam.topnews;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.core.view.MenuItemCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.fisfam.topnews.fragment.HomeFragment;
import com.fisfam.topnews.fragment.SavedFragment;
import com.fisfam.topnews.fragment.TopicFragment;
import com.fisfam.topnews.utils.UiTools;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private Toolbar mToolbar;
    private ActionBar mActionBar;
    private DrawerLayout mDrawer;
    private TextView mName;
    public UserPreference mUserPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initToolbar();
        initNavigationDrawer();
        loadFragment(new HomeFragment());

        //TODO: check app version
        //TODO: get Database for notification
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mName != null) {
            mName.setText(new UserPreference(this).getUser());
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer);
        if (!drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.openDrawer(GravityCompat.START);
        } else {
            doExitApp();
        }
    }

    //private View notif_badge = null, notif_badge_menu = null;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_activity_main, menu);
        final MenuItem menuItem = menu.findItem(R.id.action_notification);
        View actionView = MenuItemCompat.getActionView(menuItem);
        //notif_badge = actionView.findViewById(R.id.notif_badge);

        //setupBadge();
        actionView.setOnClickListener(view -> onOptionsItemSelected(menuItem));

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int menu_id = item.getItemId();
        if (menu_id == android.R.id.home) {
            if (!mDrawer.isDrawerOpen(GravityCompat.START)) {
                mDrawer.openDrawer(GravityCompat.START);
            } else {
                mDrawer.openDrawer(GravityCompat.END);
            }
        } else if(menu_id == R.id.action_choose_language){
            chooseCountry();
        } else if (menu_id == R.id.action_search) {
            Intent intent = new Intent(MainActivity.this, ArticlesSearchActivity.class);
            startActivity(intent);
        } else if (menu_id == R.id.action_notification) {
            //TODO: open notification
        } else {
            Toast.makeText(getApplicationContext(), item.getTitle(), Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

    //Choose country of news source
    private void chooseCountry() {
        mUserPref = new UserPreference(getApplicationContext());
        // setup the alert builder
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AlertDialogTheme);
        builder.setTitle(getString(R.string.choose_a_country));
        // add a radio button list
        String[] countries = getResources().getStringArray(R.array.countries_news_source);
        //the button is always checked by the value stored in mUserPref
        builder.setSingleChoiceItems(countries, Arrays.asList(countries).indexOf(mUserPref.getCountry()),
                new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mUserPref.setCountry(countries[which]);
            }
        });
        // add Done button to close dialog and reload HomeFragment
        builder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                loadFragment(new HomeFragment());
            }
        });
        // create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private long exitTime = 0;

    public void doExitApp() {
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            Toast.makeText(this, R.string.press_again_exit_app, Toast.LENGTH_SHORT).show();
            exitTime = System.currentTimeMillis();
        } else {
            finish();
        }
    }

    private void initToolbar() {
        mToolbar = findViewById(R.id.toolbar);
        mToolbar.setNavigationIcon(R.drawable.ic_toolbar_menu);
        //mToolbar.getNavigationIcon().setColorFilter(new BlendModeColorFilter(R.color.colorTextAction, BlendMode.SRC_ATOP));
        mToolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.colorTextAction), PorterDuff.Mode.SRC_ATOP);
        setSupportActionBar(mToolbar);

        mActionBar = getSupportActionBar();
        if (mActionBar == null) {
            Log.e(TAG, "initToolbar: mActionBar = null");
            return;
        }
        mActionBar.setTitle(R.string.app_name);
        mActionBar.setDisplayHomeAsUpEnabled(true);

        //UiTools.changeOverflowMenuIconColor(mToolbar, getResources().getColor(R.color.colorTextAction));
        UiTools.setSmartSystemBar(this);
    }

    private void initNavigationDrawer() {
        mDrawer = findViewById(R.id.drawer);
        TextView settings = findViewById(R.id.settings);
        TextView login_logout = findViewById(R.id.login_logout);
        mName = findViewById(R.id.name_drawer);

        settings.setOnClickListener(v -> SettingsActivity.open(this));
        login_logout.setOnClickListener(v -> LoginActivity.open(this));
    }

    private void loadFragment(final Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.addToBackStack(null);
        transaction.commitAllowingStateLoss();
    }

    private Fragment mHomeFragment, mSavedFragment, mTopicFragment;

    public void onDrawerMenuClick(@NotNull View view) {

        Fragment fragment = null;
        int menu_id = view.getId();
        String title = mActionBar.getTitle().toString();

        switch (menu_id) {
            case R.id.nav_menu_home:
                if (mHomeFragment == null) mHomeFragment = new HomeFragment();
                fragment = mHomeFragment;
                title = getString(R.string.title_menu_home);
                break;
            case R.id.nav_menu_topic:
                if (mTopicFragment == null) mTopicFragment = new TopicFragment();
                fragment = mTopicFragment;
                title = getString(R.string.title_menu_topic);
                break;
            /*case R.id.nav_menu_notif:
                ActivityNotification.navigate(this);
                break;*/
            case R.id.nav_menu_saved:
                if (mSavedFragment == null) mSavedFragment = new SavedFragment();
                fragment = mSavedFragment;
                title = getString(R.string.title_menu_saved);
                break;
            /*case R.id.nav_menu_more_app:
                break;*/
            case R.id.nav_menu_rate:
                UiTools.rateAction(this);
                break;
            case R.id.nav_menu_about:
                UiTools.showDialogAbout(this);
                break;
        }
        mActionBar.setTitle(title);
        mDrawer.closeDrawers();
        if (fragment != null) loadFragment(fragment);
    }
}
