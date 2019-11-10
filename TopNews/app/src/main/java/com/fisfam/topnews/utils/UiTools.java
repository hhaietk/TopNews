package com.fisfam.topnews.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.annotation.ColorRes;

import com.bumptech.glide.Glide;
import com.fisfam.topnews.R;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class UiTools {

    private static final String TAG = UiTools.class.getSimpleName();

    public static void showDialogAbout(Context context) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_about);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(true);
        dialog.findViewById(R.id.btn_close).setOnClickListener(view -> dialog.dismiss());
        dialog.show();
    }

    public static void rateAction(Activity activity) {
        Uri uri = Uri.parse("market://details?id=" + activity.getPackageName());
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        try {
            activity.startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + activity.getPackageName())));
        }
    }

    public static void displayImageThumb(Context context, ImageView img, String url, float thumb) {
        try {
            Glide.with(context.getApplicationContext())
                    .load(url)
                    .transition(withCrossFade())
                    //TODO: implement SharePref
                    //.diskCacheStrategy(new SharedPref(ctx).getImageCache() ? DiskCacheStrategy.ALL : DiskCacheStrategy.NONE)
                    .thumbnail(thumb)
                    .into(img);
        } catch (Exception e) {
            Log.e(TAG, "displayImageThumb: exception" + e.getMessage());
        }
    }

    public static void setSmartSystemBar(Activity act) {
        if(isDarkTheme(act)){
            setSystemBarColor(act, R.color.colorBackground);
        } else {
            setSystemBarColor(act, R.color.colorBackground);
            setSystemBarLight(act);
        }
    }

    public static void setSystemBarColor(Activity act, @ColorRes int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = act.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(act.getResources().getColor(color));
        }
    }

    public static void setSystemBarLight(Activity act) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            View view = act.findViewById(android.R.id.content);
            int flags = view.getSystemUiVisibility();
            flags |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            view.setSystemUiVisibility(flags);
        }
    }

    private static boolean isDarkTheme(Context context){
        int night_mode = context.getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        return night_mode == Configuration.UI_MODE_NIGHT_YES;
    }
}
