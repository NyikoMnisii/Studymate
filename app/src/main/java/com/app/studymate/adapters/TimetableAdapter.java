package com.app.studymate.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.app.studymate.R;

import java.util.ArrayList;
import java.util.Map;

public class TimetableAdapter extends RecyclerView.Adapter<TimetableAdapter.ViewHolder> {
    private Map<String, Map<String, Integer>> timetable;

    public TimetableAdapter(Map<String, Map<String, Integer>> timetable) {
        this.timetable = timetable;
    }

    @Override
    public TimetableAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.subject_input, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String day = new ArrayList<>(timetable.keySet()).get(position);
        holder.dayTitle.setText(day);

        Map<String, Integer> subjects = timetable.get(day);
        StringBuilder subjectsText = new StringBuilder();
        for (Map.Entry<String, Integer> entry : subjects.entrySet()) {
            subjectsText.append(entry.getKey()).append(": ").append(entry.getValue()).append(" hrs\n");
        }
        holder.subjectsText.setText(subjectsText.toString());
    }

    @Override
    public int getItemCount() {
        return timetable.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView dayTitle;
        public TextView subjectsText;

        public ViewHolder(View view) {
            super(view);
            dayTitle = view.findViewById(R.id.day_title);
            subjectsText = view.findViewById(R.id.subjects_text);
        }
    }
}
