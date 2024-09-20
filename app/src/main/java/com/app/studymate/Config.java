package com.app.studymate;

import android.content.Context;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class Config {


    /** OneSignal ID */
    public static String ONE_SIGNAL_ID = "e539a6be-68fa-4ab0-aefb-683890f305bb";

    /** App Configuration */
    public static final String API_KEY = "6087d76ec1e9be1b68e57a61daccac95243acdda0a9a3bc31b0baa757f4951f4";
    public static final boolean USE_REMOTE_JSON = false; // Set this to false to use local JSON
    public static final String REMOTE_JSON_URL = "https://demo.poshnool.com/ebook/config.json";
    public static final boolean USE_RANDOM_COLORS_FOR_TEXT_ICONS = false;
    public static final int SPLASH_DELAY = 2000; // Milliseconds

    /** Ad-Network Configuration */
    public static final String AD_STATUS = "1";
    public static final String AD_NETWORK = "admob";
    public static final String BACKUP_AD_NETWORK = "fan";
    public static final String ADMOB_BANNER_ID = "ca-app-pub-4327931294792470/3565382163";
    public static final String ADMOB_INTERSTITIAL_ID = "ca-app-pub-4327931294792470/6383117194";
    public static final String ADMOB_NATIVE_ID = "ca-app-pub-4327931294792470/6790311302";
    public static final String ADMOB_APP_OPEN_AD_ID = "ca-app-pub-4327931294792470/2443872180";
    public static final String FAN_BANNER_ID = "248004194683672_248004281350330";
    public static final String FAN_INTERSTITIAL_ID = "248004194683672_248004271350331";
    public static final String FAN_NATIVE_ID = "248004194683672_248004278016997";
    public static final int INTERSTITIAL_FREQUENCY = 5;
    public static final Boolean ENABLE_GDPR_EU_CONSENT = true;

    /**
     * Method to read config.json from assets folder
     * @param context the application context
     * @return JSON string
     */
    public static String loadLocalJsonConfig(Context context) {
        String json = null;
        try {
            InputStream is = context.getAssets().open("config.json"); // Load the config.json from assets
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, StandardCharsets.UTF_8); // Convert byte array to string
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return json;
    }
}
