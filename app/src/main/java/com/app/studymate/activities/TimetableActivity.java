package com.app.studymate.activities;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.app.studymate.R;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TimetableActivity extends AppCompatActivity {

    private LinearLayout subjectsContainer;
    private Button addSubjectButton, generateTimetableButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.subject_input); // Main layout

        // Initialize views
        subjectsContainer = findViewById(R.id.subjects_container);
        addSubjectButton = findViewById(R.id.add_subject_button);
        generateTimetableButton = findViewById(R.id.generate_timetable_button);

        // Add subject button listener
        addSubjectButton.setOnClickListener(v -> addSubjectInput());

        // Generate timetable button listener
        generateTimetableButton.setOnClickListener(v -> generateBalancedTimetable());
    }

    // Function to add a subject input using timetable_item.xml
    private void addSubjectInput() {
        // Inflate timetable_item.xml layout
        LayoutInflater inflater = LayoutInflater.from(this);
        View subjectView = inflater.inflate(R.layout.timetable_item, subjectsContainer, false);

        // Find the Spinner, SeekBar, and TextView in the inflated view
        Spinner subjectSpinner = subjectView.findViewById(R.id.subject_spinner);
        SeekBar hoursSlider = subjectView.findViewById(R.id.hours_slider);
        TextView hoursDisplay = subjectView.findViewById(R.id.hours_display);

        // Set up listener for the SeekBar to update the hours display
        hoursSlider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                hoursDisplay.setText("Hours: " + progress); // Update hours text when slider is moved
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // Not used
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // Not used
            }
        });

        // Add the inflated view to the subjects container
        subjectsContainer.addView(subjectView);
    }

    // Function to generate a balanced timetable
    @SuppressLint("SetTextI18n")
    private void generateBalancedTimetable() {
        int totalHoursPerWeek = 0;
        Map<String, Integer> subjectHoursMap = new HashMap<>();

        // Iterate over the subjects container to gather subjects and hours
        int childCount = subjectsContainer.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View subjectLayout = subjectsContainer.getChildAt(i);

            // Find the subject spinner and hours display in the child layout
            Spinner subjectSpinner = subjectLayout.findViewById(R.id.subject_spinner);
            SeekBar hoursSlider = subjectLayout.findViewById(R.id.hours_slider);

            String subjectName = subjectSpinner.getSelectedItem().toString();
            int studyHours = hoursSlider.getProgress();

            if (!subjectName.isEmpty() && studyHours > 0) {
                subjectHoursMap.put(subjectName, studyHours);
                totalHoursPerWeek += studyHours;
            } else {
                Toast.makeText(this, "Please select a subject and allocate hours.", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        // Create and display the timetable
        displayTimetable(createTimetable(subjectHoursMap, totalHoursPerWeek));
    }

    // Function to create a timetable
    private Map<String, Map<String, Integer>> createTimetable(Map<String, Integer> subjectHoursMap, int totalHoursPerWeek) {
        int daysAvailable = 7; // Days of the week
        int hoursPerDay = totalHoursPerWeek / daysAvailable;

        List<String> daysOfWeek = Arrays.asList("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday");
        Map<String, Map<String, Integer>> timetable = new HashMap<>();

        int currentDay = 0;
        for (Map.Entry<String, Integer> entry : subjectHoursMap.entrySet()) {
            String subject = entry.getKey();
            int remainingHours = entry.getValue();

            while (remainingHours > 0) {
                String currentDayName = daysOfWeek.get(currentDay);

                if (!timetable.containsKey(currentDayName)) {
                    timetable.put(currentDayName, new HashMap<>());
                }

                int hoursForToday = Math.min(hoursPerDay, remainingHours);
                timetable.get(currentDayName).put(subject, hoursForToday);

                remainingHours -= hoursForToday;

                // Move to the next day
                currentDay = (currentDay + 1) % daysAvailable;
            }
        }

        return timetable;
    }

    // Update the view that holds the timetable
    LinearLayout timetableContainer = findViewById(R.id.timetable_container); // This will hold the timetable entries

    // Function to display the timetable
    private void displayTimetable(Map<String, Map<String, Integer>> timetable) {
        // Clear any previous timetable entries
        timetableContainer.removeAllViews();

        // Iterate through the timetable and display each day and its subjects
        for (String day : timetable.keySet()) {
            // Create a TextView for the day
            TextView dayTitle = new TextView(this);
            dayTitle.setText(day);
            dayTitle.setTextSize(18);
            dayTitle.setTypeface(null, Typeface.BOLD);
            timetableContainer.addView(dayTitle);

            // Get subjects and hours for the day
            Map<String, Integer> subjectsForDay = timetable.get(day);
            for (Map.Entry<String, Integer> subjectEntry : subjectsForDay.entrySet()) {
                // Create a TextView for each subject and its hours
                TextView subjectText = new TextView(this);
                subjectText.setText(subjectEntry.getKey() + ": " + subjectEntry.getValue() + " hrs");
                subjectText.setPadding(20, 10, 0, 10); // Add some padding for better UI
                timetableContainer.addView(subjectText);
            }

            // Add a divider between days
            View divider = new View(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, 2);
            divider.setLayoutParams(params);
            divider.setBackgroundColor(Color.GRAY);
            timetableContainer.addView(divider);
        }
    }
}
