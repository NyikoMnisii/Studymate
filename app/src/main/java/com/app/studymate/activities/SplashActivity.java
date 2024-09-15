package com.app.studymate.activities;

import static com.app.studymate.Config.SPLASH_DELAY;
import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ProgressBar;

import com.app.studymate.R;
import com.app.studymate.tools.Utilities;

@SuppressLint("CustomSplashScreen")
public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utilities.getTheme(this);
        Utilities.setNavigation(this);
        setContentView(R.layout.activity_splash);

        ProgressBar progressBar = findViewById(R.id.progressBar);
        progressBar.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.text_icon_color), android.graphics.PorterDuff.Mode.MULTIPLY);

        new Handler().postDelayed(() -> {
            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(intent);
            finish(); // Close the splash activity to prevent going back to it
        }, SPLASH_DELAY);
    }

}