package com.app.studymate.activities;

import android.content.Context;

import com.app.studymate.database.SharedPref;

public class AdManager {
    private SharedPref sharedPref;

    public AdManager(Context context) {
        // Ensure the context is passed correctly and not null
        if (context == null) {
            throw new IllegalArgumentException("Context cannot be null");
        }
        this.sharedPref = new SharedPref(context);
    }

    public void initAds() {
        // Initialize ads here
    }

    public void updateConsentStatus() {
        // Update consent status logic here
    }

    public void loadBannerAd(int adViewId) {
        // Load banner ad logic here
    }
}
