package com.app.studymate.adapters;

import static com.app.studymate.Config.USE_RANDOM_COLORS_FOR_TEXT_ICONS;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.app.studymate.Config;
import com.app.studymate.R;
import com.app.studymate.activities.PdfViewerActivity;
import com.app.studymate.models.ItemModel;
import com.app.studymate.tools.AdManager;
import com.app.studymate.tools.Utilities;
import com.downloader.Error;
import com.downloader.OnDownloadListener;
import com.downloader.PRDownloader;
import com.google.android.material.progressindicator.CircularProgressIndicator;

import java.io.File;
import java.util.ArrayList;

import xposed.app.utils.ColorGenerator;
import xposed.app.utils.TextDrawable;
import xposed.app.utils.ToastManager;
import xposed.app.utils.Tools;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.MyViewHolder> {

    private final Context context;
    private ArrayList<ItemModel> mList;
    private final AdManager adManager;

    public ItemAdapter(Context context, ArrayList<ItemModel> mList) {
        this.mList = mList;
        this.context = context;
        adManager = new AdManager((Activity) context); // Cast context to Activity
        adManager.initAds();
        adManager.loadInterstitialAd();
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_item_layout, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ColorGenerator generator = ColorGenerator.MATERIAL;
        int color = generator.getRandomColor();
        String icon = String.valueOf(mList.get(position).getIcon());

        int colorInt = ContextCompat.getColor(context, R.color.color_light_primary);
        int iconColor = USE_RANDOM_COLORS_FOR_TEXT_ICONS ? color : colorInt;
        TextDrawable textDrawable;
        textDrawable = TextDrawable.builder()
                .beginConfig()
                .bold()
                .useFont(Typeface.DEFAULT)
                .withBorder(3)
                .endConfig()
                .buildRoundRect(icon, iconColor, 20);

        holder.progressBar.setVisibility(View.INVISIBLE);
        holder.speedTxt.setVisibility(View.INVISIBLE);

        holder.catName.setText(mList.get(position).getName());
        holder.catIcon.setImageDrawable(textDrawable);

        String file_name = mList.get(position).getName();
        String file_url = mList.get(position).getUrl();
        File dir = context.getApplicationContext().getFilesDir();
        File file = new File(dir, file_name + ".pdf");

        updateItemState(holder, file);

        holder.parent.setOnClickListener(v -> {
            if (file.exists()) {
                openPdfViewerActivity(position);
            } else {
                downloadFile(holder, file_url, dir, file_name);
            }
        });

        holder.parent.setOnLongClickListener(v -> {
            if (file.exists()) {
                showDeleteConfirmationDialog(file, holder);
            }
            return false;
        });
    }

    private void openPdfViewerActivity(int position) {
        Intent module = new Intent(context, PdfViewerActivity.class);
        module.putExtra("name", mList.get(position).getName());
        module.putExtra("uri", mList.get(position).getUrl());
        context.startActivity(module);
        adManager.showInterstitialAd(Config.INTERSTITIAL_FREQUENCY);
    }

    private void downloadFile(MyViewHolder holder, String file_url, File dir, String file_name) {
        if (Tools.isOnline(context)) {
            holder.progressBar.setVisibility(View.VISIBLE);
            holder.isDownloaded.setVisibility(View.INVISIBLE);

            PRDownloader.download(file_url, String.valueOf(dir), file_name + ".pdf")
                    .build()
                    .setOnStartOrResumeListener(() -> {
                        //Toasty.info(context, "Download Started", Toast.LENGTH_SHORT).show();
                    })
                    .setOnProgressListener(progress -> {
                        long progressPer = progress.currentBytes * 100 / progress.totalBytes;
                        holder.isDownloaded.setVisibility(View.INVISIBLE);
                        holder.progressBar.setVisibility(View.VISIBLE);
                        holder.speedTxt.setVisibility(View.VISIBLE);
                        holder.speedTxt.setText(Utilities.getProgressText(progress.currentBytes));
                        holder.progressBar.setProgressCompat((int) progressPer, true);
                    })
                    .start(new OnDownloadListener() {
                        @Override
                        public void onDownloadComplete() {
                            updateItemState(holder, dir);
                            holder.progressBar.setVisibility(View.GONE);
                            holder.speedTxt.setVisibility(View.GONE);
                            holder.isDownloaded.setVisibility(View.VISIBLE);
                            adManager.showInterstitialAd(1);
                        }

                        @Override
                        public void onError(Error error) {
                            ToastManager.showToast(context, "Error: " + error.isConnectionError());
                            updateItemState(holder, dir);
                            holder.progressBar.setVisibility(View.GONE);
                            holder.speedTxt.setVisibility(View.GONE);
                            holder.isDownloaded.setVisibility(View.VISIBLE);
                        }
                    });
        } else {
            ToastManager.showToast(context, "Network Error!");
        }
    }

    private void showDeleteConfirmationDialog(File file, MyViewHolder holder) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.custom_delete_dialog, null);

        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setView(view);
        dialog.setCancelable(false);
        AlertDialog alertDialog = dialog.create();
        alertDialog.show();

        // Handle button clicks
        view.findViewById(R.id.btn_cancel).setOnClickListener(v -> {
            alertDialog.dismiss();
        });
        view.findViewById(R.id.btn_delete).setOnClickListener(v -> {
            file.delete();
            ToastManager.showToast(context, "File Deleted");
            updateItemState(holder, file);
            alertDialog.dismiss();
        });

    }

    private void updateItemState(MyViewHolder holder, File file) {
        holder.isDownloaded.setImageDrawable(file.exists()
                ? ResourcesCompat.getDrawable(context.getResources(), R.drawable.see, null)
                : ResourcesCompat.getDrawable(context.getResources(), R.drawable.download, null));
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setFilter (ArrayList<ItemModel> newList) {
        mList = new ArrayList<>();
        mList.addAll(newList);
        notifyDataSetChanged();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {

        private final LinearLayout parent;
        private final TextView catName, speedTxt;
        private final ImageView catIcon, isDownloaded;
        private final CircularProgressIndicator progressBar;

        public MyViewHolder(View view) {
            super(view);
            parent = view.findViewById(R.id.lytParent);
            catName = view.findViewById(R.id.nameTxt);
            catIcon = view.findViewById(R.id.iconImg);
            isDownloaded = view.findViewById(R.id.fileLoader);
            progressBar = view.findViewById(R.id.progress_circular);
            speedTxt = view.findViewById(R.id.speedText);
        }
    }

}
