package com.app.studymate.tools;

import android.app.Activity;
import android.view.View;

import com.app.studymate.BuildConfig;
import com.app.studymate.Config;
import com.app.studymate.database.SharedPref;
import com.solodroid.ads.sdk.format.AdNetwork;
import com.solodroid.ads.sdk.format.BannerAd;
import com.solodroid.ads.sdk.format.InterstitialAd;
import com.solodroid.ads.sdk.format.NativeAdFragment;
import com.solodroid.ads.sdk.gdpr.GDPR;
import com.solodroid.ads.sdk.gdpr.LegacyGDPR;

public class AdManager {

    AdNetwork.Initialize adNetwork;
    BannerAd.Builder bannerAd;
    InterstitialAd.Builder interstitialAd;
    NativeAdFragment.Builder nativeAdView;
    Activity activity;
    SharedPref sharedPref;
    LegacyGDPR legacyGDPR;
    GDPR gdpr;

    // Constructor to initialize the activity context and other components
    public AdManager(Activity activity) {
        if (activity == null) {
            throw new IllegalArgumentException("Activity cannot be null");
        }
        this.activity = activity;
        this.sharedPref = new SharedPref(activity);  // Initialize SharedPref with Activity context
        this.legacyGDPR = new LegacyGDPR(activity);
        this.gdpr = new GDPR(activity);
    }

    public void initAds() {
        adNetwork = new AdNetwork.Initialize(activity)
                .setAdStatus(Config.AD_STATUS)
                .setAdNetwork(Config.AD_NETWORK)
                .setBackupAdNetwork(Config.BACKUP_AD_NETWORK)
                .setAdMobAppId(null)
                .setDebug(BuildConfig.DEBUG)
                .build();
    }

    public void updateConsentStatus() {
        if (Config.ENABLE_GDPR_EU_CONSENT) {
            gdpr.updateGDPRConsentStatus();
        }
    }

    public void loadBannerAd(int placement) {
        bannerAd = new BannerAd.Builder(activity)
                .setAdStatus(Config.AD_STATUS)
                .setAdNetwork(Config.AD_NETWORK)
                .setBackupAdNetwork(Config.BACKUP_AD_NETWORK)
                .setAdMobBannerId(Config.ADMOB_BANNER_ID)
                .setFanBannerId(Config.FAN_BANNER_ID)
                .setPlacementStatus(placement)
                .setDarkTheme(sharedPref.getIsDarkTheme())
                .build();
    }

    public void loadInterstitialAd() {
        interstitialAd = new InterstitialAd.Builder(activity)
                .setAdStatus(Config.AD_STATUS)
                .setAdNetwork(Config.AD_NETWORK)
                .setBackupAdNetwork(Config.BACKUP_AD_NETWORK)
                .setAdMobInterstitialId(Config.ADMOB_INTERSTITIAL_ID)
                .setFanInterstitialId(Config.FAN_INTERSTITIAL_ID)
                .build();
    }

    public void showInterstitialAd(int freq) {
        interstitialAd.setInterval(freq);
        interstitialAd.show();
    }

    public void loadNativeAdView(View view, int placement) {
        nativeAdView.setAdStatus(Config.AD_STATUS)
                .setAdNetwork(Config.AD_NETWORK)
                .setAdMobNativeId(Config.ADMOB_NATIVE_ID)
                .setFanNativeId(Config.FAN_NATIVE_ID)
                .setPlacementStatus(placement)
                .setNativeAdStyle("news")
                .setDarkTheme(sharedPref.getIsDarkTheme())
                .setView(view)
                .build();
    }

}
