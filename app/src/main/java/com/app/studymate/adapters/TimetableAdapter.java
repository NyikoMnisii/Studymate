package com.app.studymate.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.app.studymate.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TimetableAdapter extends RecyclerView.Adapter<TimetableAdapter.ViewHolder> {

    private Map<String, Map<String, Integer>> timetable;
    private List<String> availableSubjects;  // Dynamically passed subject list

    // Constructor to initialize the timetable map and available subjects
    public TimetableAdapter(Map<String, Map<String, Integer>> timetable, List<String> availableSubjects) {
        this.timetable = timetable;
        this.availableSubjects = availableSubjects;  // Use this for dynamic subjects
    }

    @Override
    public TimetableAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.subject_input, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TimetableAdapter.ViewHolder holder, int position) {
        String subject = new ArrayList<>(timetable.keySet()).get(position);
        Map<String, Integer> subjectData = timetable.get(subject);

        // Dynamically create ArrayAdapter using the provided subject list
        ArrayAdapter<String> adapter = new ArrayAdapter<>(holder.itemView.getContext(),
                android.R.layout.simple_spinner_item, availableSubjects);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        holder.subjectSpinner.setAdapter(adapter);

        // Set the spinner to the correct subject
        int subjectPosition = availableSubjects.indexOf(subject);
        if (subjectPosition >= 0) {
            holder.subjectSpinner.setSelection(subjectPosition);
        }

        // Set SeekBar progress and display the selected hours
        holder.hoursSlider.setProgress(subjectData.get("hours"));
        holder.hoursDisplay.setText("Hours: " + subjectData.get("hours"));

        // Update the timetable when the SeekBar changes
        holder.hoursSlider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                holder.hoursDisplay.setText("Hours: " + progress);
                subjectData.put("hours", progress);  // Update the hours in the timetable
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // Can add actions when user starts interacting with the SeekBar
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // Can notify a callback here if necessary
            }
        });
    }

    // Return the size of the timetable (subject count)
    @Override
    public int getItemCount() {
        return timetable.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public Spinner subjectSpinner;
        public SeekBar hoursSlider;
        public TextView hoursDisplay;

        public ViewHolder(View itemView) {
            super(itemView);
            subjectSpinner = itemView.findViewById(R.id.subject_spinner);
            hoursSlider = itemView.findViewById(R.id.hours_slider);
            hoursDisplay = itemView.findViewById(R.id.hours_display);
        }
    }
}
