package com.app.studymate;

import android.util.Log;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class Config {

    /** OneSignal ID */
    public static String ONE_SIGNAL_ID = "e539a6be-68fa-4ab0-aefb-683890f305bb";

    /** App Configuration */
    public static final String API_KEY = "6087d76ec1e9be1b68e57a61daccac95243acdda0a9a3bc31b0baa757f4951f4";
    public static final boolean USE_RANDOM_COLORS_FOR_TEXT_ICONS = false;
    public static final int SPLASH_DELAY = 2000; // Milliseconds

    /** Firebase Firestore instance */
    private static final FirebaseFirestore db = FirebaseFirestore.getInstance();

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

    /** Method to fetch JSON from Firebase Firestore */
    public static void fetchRemoteJSON(final OnFetchCompleteListener listener) {
        // Reference to the Firestore document
        DocumentReference docRef = db.collection("eMaterials").document("materialsJSON");

        // Fetch the JSON from Firestore
        docRef.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                String json = documentSnapshot.getString("json");
                listener.onFetchComplete(json);
            } else {
                Log.e("Config", "No JSON document found in Firestore");
                listener.onFetchComplete(null);
            }
        }).addOnFailureListener(e -> {
            Log.e("Config", "Failed to fetch JSON from Firestore", e);
            listener.onFetchComplete(null);
        });
    }

    /** Listener interface to handle fetch completion */
    public interface OnFetchCompleteListener {
        void onFetchComplete(String json);
    }
}
