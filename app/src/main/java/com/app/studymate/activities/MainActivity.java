package com.app.studymate.activities;


import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.app.studymate.BuildConfig;
import com.app.studymate.Config;
import com.app.studymate.R;
import com.app.studymate.adapters.ItemAdapter;
import com.app.studymate.database.SharedPref;
import com.app.studymate.models.ItemModel;
import com.app.studymate.tools.AdManager;
import com.app.studymate.tools.Utilities;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.android.play.core.review.ReviewInfo;
import com.google.android.play.core.review.ReviewManager;
import com.google.android.play.core.review.ReviewManagerFactory;
import com.solodroid.push.sdk.provider.OneSignalPush;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import xposed.app.utils.ToastManager;
import xposed.app.utils.Tools;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, SwipeRefreshLayout.OnRefreshListener {


    private static final String JSON_NAME = "name";
    private static final String JSON_ICON = "icon";
    private static final String JSON_URL = "url";
    private static final int IMMEDIATE_APP_UPDATE_REQ_CODE = 123;
    private ItemAdapter myRecyclerViewAdapter;
    private final ArrayList<ItemModel> itemModelArrayList = new ArrayList<>();
    private DrawerLayout mDrawerLayout;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private NavigationView mNavigationView;
    private Toolbar toolbar;
    private SharedPref sharedPref;
    private AdManager adManager;
    private AppUpdateManager appUpdateManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupUI();
        loadData();
        setupAds();
        setupOneSignal();
        checkForAppUpdate();

    }

    private void setupUI() {
        Utilities.getTheme(this);
        Utilities.setNavigation(this);
        setContentView(R.layout.activity_main);
        sharedPref = new SharedPref(this);
        mDrawerLayout = findViewById(R.id.drawer_layout);
        mSwipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        mNavigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);
        Utilities.setupToolbar(this, toolbar, getString(R.string.app_name), false);
        initDrawer();
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.color_light_primary, R.color.color_light_accent);
    }

    private void loadData() {
        if (!Config.USE_REMOTE_JSON) {
            getDataFromAssets();
        } else {
            loadDataBasedOnInternetConnectivity();
        }
    }



    private void setupAds() {
        adManager = new AdManager(this);  // Pass the MainActivity context
        adManager.initAds();
        adManager.updateConsentStatus();
        adManager.loadBannerAd(R.id.bannerAd);
    }



    private void setupOneSignal() {
        new OneSignalPush.Builder(this).requestNotificationPermission();
    }

    private void checkForAppUpdate() {
        appUpdateManager = AppUpdateManagerFactory.create(getApplicationContext());
        if (!BuildConfig.DEBUG) {
            inAppUpdate();
            inAppReview();
        }
    }

    @Override
    public void onRefresh() {
        if (Tools.isOnline(this)) {
            getDataFromServer();
        } else {
            mSwipeRefreshLayout.setRefreshing(false);
            ToastManager.showToast(this, "No internet connection!");
        }
    }

    private void loadDataBasedOnInternetConnectivity() {
        if (Tools.isOnline(this)) {
            getDataFromServer();
        } else {
            loadFromSharedPreferences();
        }
    }

    private void getDataFromServer() {
        mSwipeRefreshLayout.setRefreshing(true);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Config.REMOTE_JSON_URL,
                response -> {
                    mSwipeRefreshLayout.setRefreshing(false); // Hide refreshing animation
                    try {
                        JSONArray array = new JSONArray(response);
                        itemModelArrayList.clear(); // Clear the existing data

                        for (int i = 0; i < array.length(); i++) {
                            ItemModel model = new ItemModel();
                            JSONObject jsonObject = array.getJSONObject(i);
                            model.setName(jsonObject.getString(JSON_NAME));
                            model.setIcon(jsonObject.getString(JSON_ICON));
                            model.setUrl(jsonObject.getString(JSON_URL));
                            itemModelArrayList.add(model);
                        }

                        JSONArray jsonArray = convertArrayListToJson(itemModelArrayList);
                        saveDataToSharedPreferences(jsonArray.toString());
                        updateRecyclerView();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    mSwipeRefreshLayout.setRefreshing(false);
                    ToastManager.showToast(MainActivity.this, "Error loading data from server");
                });

        // Creating a request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private JSONArray convertArrayListToJson(ArrayList<ItemModel> itemList) {
        JSONArray jsonArray = new JSONArray();
        try {
            for (ItemModel itemModel : itemList) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put(JSON_NAME, itemModel.getName());
                jsonObject.put(JSON_ICON, itemModel.getIcon());
                jsonObject.put(JSON_URL, itemModel.getUrl());
                jsonArray.put(jsonObject);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonArray;
    }


    private void getDataFromAssets(){
        String myJSONStr = Tools.loadJSONFromAssets(this, "config.json");

        try {
            JSONArray array = new JSONArray(myJSONStr);
            for (int i = 0; i < array.length(); i++) {
                ItemModel model = new ItemModel();
                JSONObject jsonObject = array.getJSONObject(i);
                model.setName(jsonObject.getString(JSON_NAME));
                model.setIcon(jsonObject.getString(JSON_ICON));
                model.setUrl(jsonObject.getString(JSON_URL));
                itemModelArrayList.add(model);
            }

            // Disable SwipeRefreshLayout when data is loaded from assets
            mSwipeRefreshLayout.setEnabled(false);

            // Update RecyclerView after loading data from assets
            updateRecyclerView();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void saveDataToSharedPreferences(String jsonData) {
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("json_data", jsonData);
        editor.apply();
    }

    private void loadFromSharedPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String jsonData = sharedPreferences.getString("json_data", "");
        itemModelArrayList.clear();

        try {
            JSONArray jsonArray = new JSONArray(jsonData);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                ItemModel model = new ItemModel();
                model.setName(jsonObject.getString(JSON_NAME));
                model.setIcon(jsonObject.getString(JSON_ICON));
                model.setUrl(jsonObject.getString(JSON_URL));
                itemModelArrayList.add(model);
            }
            // Update the RecyclerView adapter here if needed
            updateRecyclerView();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void updateRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.mRecycler);
        myRecyclerViewAdapter = new ItemAdapter(MainActivity.this, itemModelArrayList);
        recyclerView.setAdapter(myRecyclerViewAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), linearLayoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_theme) {
            // Get the current night mode state
            boolean isDarkTheme = sharedPref.getIsDarkTheme();
            sharedPref.setIsDarkTheme(!isDarkTheme);

            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }, 250);
        }

        else if (id == R.id.action_feedback) {
            Tools.startEmailActivity(this, getResources().getString(R.string.developer_email), "", "");
        }

        else if (id == R.id.action_rate_app) {
            Tools.startWebActivity(this, "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID);
        }

        else if (id == R.id.action_more_app) {
            Tools.startWebActivity(this, getString(R.string.more_apps_url));
        }

        else if (id == R.id.action_share_app) {
            String shareMsg = ("Download\n" + this.getResources().getString(R.string.app_name) + "\nhttps://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID);
            Tools.shareApp(this, shareMsg);
        }

        else if (id == R.id.action_exit) {
            exitDialog();
        }

        if (mDrawerLayout != null && mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        }
        return true;

    }

    public NavigationView getNavigationView() {
        return mNavigationView;
    }

    public void initDrawer() {

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.openDrawer, R.string.closeDrawer) {
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
            }

            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };

        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        getNavigationView().setNavigationItemSelectedListener(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        MenuItem searchViewItem = menu.findItem(R.id.action_search);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) searchViewItem.getActionView();
        assert searchView != null;
        searchView.setQueryHint("Search...");
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(false);
        SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                newText = newText.toLowerCase();
                ArrayList<ItemModel> newList = new ArrayList<>();
                for (ItemModel parseItem : itemModelArrayList) {
                    String title = parseItem.getName().toLowerCase();

                    if (title.contains(newText)) {
                        newList.add(parseItem);
                    }
                }
                // Call the setFilter method in your adapter
                myRecyclerViewAdapter.setFilter(newList);
                return true;
            }
        };

        searchView.setOnQueryTextListener(queryTextListener);
        return true;
    }

    private void inAppReview() {
        if (sharedPref.getInAppReviewToken() <= 3) {
            sharedPref.updateInAppReviewToken(sharedPref.getInAppReviewToken() + 1);
        } else {
            ReviewManager manager = ReviewManagerFactory.create(this);
            Task<ReviewInfo> request = manager.requestReviewFlow();
            request.addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    ReviewInfo reviewInfo = task.getResult();
                    manager.launchReviewFlow(MainActivity.this, reviewInfo).addOnFailureListener(e -> {
                    }).addOnCompleteListener(complete -> {
                            }
                    ).addOnFailureListener(failure -> {
                    });
                }
            }).addOnFailureListener(failure -> Log.d("In-App Review", "In-App Request Failed " + failure));
        }
    }

    private void inAppUpdate() {
        Task<AppUpdateInfo> appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();
        appUpdateInfoTask.addOnSuccessListener(appUpdateInfo -> {
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                    && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {
                startUpdateFlow(appUpdateInfo);
            } else if (appUpdateInfo.updateAvailability() == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS) {
                startUpdateFlow(appUpdateInfo);
            }
        });
    }

    private void startUpdateFlow(AppUpdateInfo appUpdateInfo) {
        try {
            appUpdateManager.startUpdateFlowForResult(appUpdateInfo, AppUpdateType.IMMEDIATE, this, IMMEDIATE_APP_UPDATE_REQ_CODE);
        } catch (IntentSender.SendIntentException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        if (requestCode == IMMEDIATE_APP_UPDATE_REQ_CODE) {
            if (resultCode == RESULT_CANCELED) {
                ToastManager.showToast(this, getString(R.string.cancel_update));
            } else if (resultCode == RESULT_OK) {
                ToastManager.showToast(this, getString(R.string.success_update));
            } else {
                ToastManager.showToast(this, getString(R.string.failed_update));
                inAppUpdate();
            }
        }
    }

    public void exitDialog() {
        LayoutInflater inflater = LayoutInflater.from(this);

        // Inflate the layout to create the view
        View view = inflater.inflate(R.layout.custom_exit_dialog, null);

        // Initialize adManager if not already done
        adManager = new AdManager(this);

        // Load the native ad view
        adManager.loadNativeAdView(view, 1);

        // Create and show the AlertDialog
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setView(view);
        dialog.setCancelable(false);
        AlertDialog alertDialog = dialog.create();
        alertDialog.show();

        // Handle button clicks
        view.findViewById(R.id.btn_exit).setOnClickListener(v -> {
            finish();
        });
        view.findViewById(R.id.btn_cancel).setOnClickListener(v -> {
            alertDialog.dismiss();
        });
    }


    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            exitDialog();
        }
    }

}





















