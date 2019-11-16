package com.fisfam.topnews;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;

import com.fisfam.topnews.utils.UiTools;
import com.google.android.material.snackbar.Snackbar;

public class SettingsActivity extends AppCompatActivity {

    private static final String TAG = SettingsActivity.class.getSimpleName();
    private View mParentView;
    private SwitchCompat mSwitchPushNotification, mSwitchVibrate, mSwitchImageCache;
    private String[] mThemes;
    private UserPreference mUserPref;
    private TextView mTvTheme;

    static void open(final Context context) {
        final Intent i = new Intent(context, SettingsActivity.class);
        context.startActivity(i);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        mUserPref = new UserPreference(this);
        initToolbar();
        initUiComponents();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.colorTextAction), PorterDuff.Mode.SRC_ATOP);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar == null) {
            Log.e(TAG, "initToolbar: action bar is null");
            return;
        }
        actionBar.setTitle(R.string.title_activity_settings);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        UiTools.changeOverflowMenuIconColor(toolbar, getResources().getColor(R.color.colorTextAction));
        UiTools.setSmartSystemBar(this);
    }

    private void initUiComponents() {
        mParentView = findViewById(R.id.parent_view_settings);
        mSwitchPushNotification = findViewById(R.id.switch_push_notif);
        mSwitchVibrate = findViewById(R.id.switch_vibrate);
        mSwitchImageCache = findViewById(R.id.switch_image_cache);

        mSwitchPushNotification.setChecked(mUserPref.getPushNotification());
        mSwitchVibrate.setChecked(mUserPref.getVibration());
        mSwitchImageCache.setChecked(mUserPref.getImageCache());

        mSwitchPushNotification.setOnCheckedChangeListener(
                (buttonView, isChecked) -> mUserPref.setPushNotification(isChecked));

        mSwitchVibrate.setOnCheckedChangeListener(
                (buttonView, isChecked) -> mUserPref.setVibration(isChecked));

        mSwitchImageCache.setOnCheckedChangeListener(
                (buttonView, isChecked) -> mUserPref.setImageCache(isChecked));

        mThemes = getResources().getStringArray(R.array.themes);
        mTvTheme = findViewById(R.id.tv_theme);
        mTvTheme.setText(mThemes[mUserPref.getSelectedTheme()]);

    }

    public void onClickLayout(final View view) {
        int id = view.getId();
        switch (id) {
            /*case R.id.lyt_ringtone:
                Intent intent = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
                intent.putExtra(RingtoneManager.EXTRA_RINGTONE_EXISTING_URI, Uri.parse(mUserPref.getRingtone()));
                intent.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_SILENT, false);
                intent.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_DEFAULT, true);
                intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_NOTIFICATION);
                startActivityForResult(intent, 999);
                break;*/
            case R.id.lyt_img_cache:
                showDialogClearImageCache();
                break;
            case R.id.lyt_contact_us:
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText(getString(R.string.pref_title_contact_us), getString(R.string.developer_email));
                if (clipboard == null) {
                    Log.e(TAG, "onClickLayout: clipboard is null");
                    return;
                }
                clipboard.setPrimaryClip(clip);
                Snackbar.make(mParentView, R.string.email_copied, Snackbar.LENGTH_SHORT).show();
                break;
            case R.id.lyt_term_policies:
                /*Tools.openInAppBrowser(this, getString(R.string.privacy_policy_url), false);
                break;*/
            case R.id.lyt_rate_this:
                UiTools.rateAction(this);
                break;
            case R.id.lyt_more_app:
                /*Tools.openInAppBrowser(this, Constant.MORE_APP_URL, false);
                break;*/
            case R.id.lyt_about:
                UiTools.showDialogAbout(this);
                break;
            case R.id.lyt_theme:
                showDialogTheme();
                break;
        }
    }

    private void showDialogClearImageCache() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.dialog_confirm_title));
        builder.setMessage(getString(R.string.message_clear_image_cache));
        builder.setPositiveButton(R.string.OK, (dialog, i) -> {
            UiTools.clearImageCacheOnBackground(getApplicationContext());
            Snackbar.make(mParentView, getString(R.string.message_after_clear_image_cache), Snackbar.LENGTH_SHORT).show();
        });
        builder.setNegativeButton(R.string.CANCEL, null);
        builder.show();
    }

    private void showDialogTheme() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.setting_title_theme);
        builder.setSingleChoiceItems(mThemes, mUserPref.getSelectedTheme(), (dialog, i) -> {
            mUserPref.setSelectedTheme(i);
            mTvTheme.setText(mThemes[mUserPref.getSelectedTheme()]);
            UiTools.refreshTheme(this);
            dialog.dismiss();
            restartActivity();
        });
        builder.show();
    }

    private void restartActivity() {
        Intent intent = new Intent(getApplicationContext(), SplashActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
