package com.fisfam.topnews;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.fisfam.topnews.utils.UiTools;

import java.util.Timer;
import java.util.TimerTask;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        UiTools.setSmartSystemBar(this);
        // in case app is upgraded, theme is reset, this check the theme and set the settings accordingly
        UiTools.checkTheme(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        startMainActivityDelay();
    }

    private void startMainActivityDelay() {
        // Show splash screen for 2 seconds
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                Intent i = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        };
        new Timer().schedule(task, 1500);
    }
}
