package com.app.studymate.database;


import android.content.Context;
import android.content.SharedPreferences;

import com.app.studymate.R;

public class SharedPref {

    Context context;
    private final SharedPreferences sharedPreferences;
    private final SharedPreferences.Editor editor;

    public SharedPref(Context context) {
        if (context == null) {
            throw new IllegalArgumentException("Context cannot be null");
        }
        this.context = context;
        sharedPreferences = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public Boolean getIsDarkTheme() {
        return sharedPreferences.getBoolean("theme", false);
    }

    public void setIsDarkTheme(Boolean isDarkTheme) {
        editor.putBoolean("theme", isDarkTheme);
        editor.apply();
    }

    public Integer getInAppReviewToken() {
        return sharedPreferences.getInt("in_app_review_token", 0);
    }

    public void updateInAppReviewToken(int value) {
        editor.putInt("in_app_review_token", value);
        editor.apply();
    }

}