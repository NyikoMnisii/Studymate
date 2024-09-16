package com.app.studymate.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.app.studymate.database.SharedPref;
import com.app.studymate.tools.AdManager;
import com.app.studymate.tools.Utilities;
import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;
import java.io.File;
import java.util.Objects;
import com.app.studymate.R;

public class PdfViewerActivity extends AppCompatActivity {

    String uri, name;
    File dir, file;
    FrameLayout frameLayout;


    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_viewer);

        SharedPref sharedPref = new SharedPref(this);
        Utilities.getTheme(this);
        Utilities.setNavigation(this);

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        Intent intent = getIntent();
        uri = intent.getStringExtra("url");
        name = intent.getStringExtra("name");

        dir = getApplicationContext().getFilesDir();
        file = new File(dir, name + ".pdf");

        PDFView pdfView = findViewById(R.id.pdfView);
        Toolbar toolbar = findViewById(R.id.toolbar);
        Utilities.setupToolbar(this, toolbar, name, true);

        // Load PDF file
        if (file.exists()) {
            pdfView.fromFile(file)
                    .scrollHandle(new DefaultScrollHandle(this))
                    .nightMode(sharedPref.getIsDarkTheme())
                    .load();
        }

        pdfView.setOnClickListener(v -> toggleActionBarVisibility());

        frameLayout = findViewById(R.id.adView);
        AdManager adManager = new AdManager();
        adManager.initAds();
        adManager.loadBannerAd(R.id.bannerAd);
        adManager.loadInterstitialAd();

    }

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