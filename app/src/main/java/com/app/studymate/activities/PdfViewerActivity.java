package com.app.studymate.activities;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.app.studymate.R;

import java.io.File;
import java.util.Objects;

public class PdfViewerActivity extends AppCompatActivity {

    String uri, name;
    File dir, file;
    FrameLayout frameLayout;


    @SuppressLint("UseCompatLoadingForDrawables")


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.details_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
        }

        if (id == R.id.action_fullscreen){
            toggleFullscreenMode();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void toggleFullscreenMode() {
        if (getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
            // Exit fullscreen mode
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
            Objects.requireNonNull(getSupportActionBar()).show();
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        } else {
            // Enter fullscreen mode
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            Objects.requireNonNull(getSupportActionBar()).hide();
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
    }

    private void toggleActionBarVisibility() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            if (actionBar.isShowing()) {
                actionBar.hide();
                hideStatusBar();
                frameLayout.setVisibility(View.GONE);
            } else {
                actionBar.show();
                showStatusBar();
                frameLayout.setVisibility(View.VISIBLE);
            }
        }
    }

    private void hideStatusBar() {
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
    }

    private void showStatusBar() {
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
    }

}