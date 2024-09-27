package com.app.studymate.activities;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
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
        setContentView(R.layout.subject_input);

        // Initialize views
        subjectsContainer = findViewById(R.id.subjects_container);
        addSubjectButton = findViewById(R.id.add_subject_button);
        generateTimetableButton = findViewById(R.id.generate_timetable_button);

        addSubjectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addSubjectInput();
            }
        });

        generateTimetableButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generateBalancedTimetable();
            }
        });
    }

    // Dynamically add a subject input with hours
    private void addSubjectInput() {
        LinearLayout subjectLayout = new LinearLayout(this);
        subjectLayout.setOrientation(LinearLayout.HORIZONTAL);

        EditText subjectInput = new EditText(this);
        subjectInput.setHint("Subject Name");
        subjectInput.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1));

        EditText hoursInput = new EditText(this);
        hoursInput.setHint("Hours");
        hoursInput.setInputType(InputType.TYPE_CLASS_NUMBER);
        hoursInput.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1));

        subjectLayout.addView(subjectInput);
        subjectLayout.addView(hoursInput);

        subjectsContainer.addView(subjectLayout);
    }

    // Function to generate a balanced timetable
    @SuppressLint("SetTextI18n")
    private void generateBalancedTimetable() {
        int totalHoursPerWeek = 0;
        Map<String, Integer> subjectHoursMap = new HashMap<>();

        // Iterate over the subjects container to gather subjects and hours
        int childCount = subjectsContainer.getChildCount();
        for (int i = 0; i < childCount; i++) {
            LinearLayout subjectLayout = (LinearLayout) subjectsContainer.getChildAt(i);
            EditText subjectInput = (EditText) subjectLayout.getChildAt(0);
            EditText hoursInput = (EditText) subjectLayout.getChildAt(1);

            String subjectName = subjectInput.getText().toString();
            String studyHoursStr = hoursInput.getText().toString();

            if (!subjectName.isEmpty() && !studyHoursStr.isEmpty()) {
                int studyHours = Integer.parseInt(studyHoursStr);
                subjectHoursMap.put(subjectName, studyHours);
                totalHoursPerWeek += studyHours;
            } else {
                Toast.makeText(this, "Please enter both subject and hours.", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        // Create and display the timetable
        displayTimetable(createTimetable(subjectHoursMap, totalHoursPerWeek));
    }

    private Map<String, Map<String, Integer>> createTimetable(Map<String, Integer> subjectHoursMap, int totalHoursPerWeek) {
        int daysAvailable = 7;
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

    private void displayTimetable(Map<String, Map<String, Integer>> timetable) {
        LinearLayout timetableContainer = findViewById(R.id.timetable_recycler_view);

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
