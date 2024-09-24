package com.app.studymate.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.Toast;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import com.app.studymate.R;
import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Objects;

public class PdfViewerActivity extends AppCompatActivity {

    String uri, name;
    File dir, file;
    FrameLayout frameLayout;
    PDFView pdfView;

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_viewer);

        // Initialize PDFView from layout
        pdfView = findViewById(R.id.pdfView);

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        // Get URI and file name from intent
        Intent intent = getIntent();
        uri = intent.getStringExtra("url");  // URL from intent
        name = intent.getStringExtra("name"); // File name

        // Directory to save downloaded PDFs
        dir = getApplicationContext().getFilesDir();
        file = new File(dir, name + ".pdf");

        // Check if the file exists locally, if not download it
        if (file.exists()) {
            loadPdfFromFile();
        } else {
            new DownloadPdfTask().execute(uri);
        }

        frameLayout = findViewById(R.id.adView);  // Assuming you have some ad logic here
    }

    // Load the PDF from file after download or if it already exists
    private void loadPdfFromFile() {
        pdfView.fromFile(file)
                .scrollHandle(new DefaultScrollHandle(this))
                .load();
    }

    // AsyncTask to download the PDF
    @SuppressLint("StaticFieldLeak")
    private class DownloadPdfTask extends AsyncTask<String, Void, Boolean> {

        @Override
        protected Boolean doInBackground(String... strings) {
            try {
                URL url = new URL(strings[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    return false;  // If response is not OK, return failure
                }

                InputStream inputStream = connection.getInputStream();
                FileOutputStream outputStream = new FileOutputStream(file);
                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }

                outputStream.close();
                inputStream.close();
                return true;  // Return success if file was downloaded
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean success) {
            if (success) {
                loadPdfFromFile();  // Load the PDF into PDFView after download
            } else {
                Toast.makeText(PdfViewerActivity.this, "Failed to download PDF", Toast.LENGTH_SHORT).show();
            }
        }
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

        if (id == R.id.action_fullscreen) {
            toggleFullscreenMode();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void toggleFullscreenMode() {
        if (getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
            Objects.requireNonNull(getSupportActionBar()).show();
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        } else {
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
