package com.app.studymate;

import android.content.Context;
import java.io.IOException;
import java.io.InputStream;

public class Config {

    /** OneSignal ID */
    public static String ONE_SIGNAL_ID = "e539a6be-68fa-4ab0-aefb-683890f305bb";

    /** App Configuration */
    public static final String API_KEY = "6087d76ec1e9be1b68e57a61daccac95243acdda0a9a3bc31b0baa757f4951f4";
    public static final boolean USE_REMOTE_JSON = false; // Set to false to use local JSON
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

    /** Method to read the local JSON file */
    public static String loadJSONFromAsset(Context context, String fileName) {
        String json = null;
        try {
            InputStream is = context.getAssets().open(fileName);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }


}
//https://drive.google.com/uc?id=
//type of link to use in config.json