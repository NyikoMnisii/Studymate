package com.app.studymate.activities;

import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.app.studymate.R;

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

       t
        addSubjectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addSubjectInput();
            }
        });


        generateTimetableButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generateTimetable();
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

    // Function to generate the timetable based on input (for simplicity, just show a toast for now)
    private void generateTimetable() {

        int childCount = subjectsContainer.getChildCount();
        for (int i = 0; i < childCount; i++) {
            LinearLayout subjectLayout = (LinearLayout) subjectsContainer.getChildAt(i);
            EditText subjectInput = (EditText) subjectLayout.getChildAt(0);
            EditText hoursInput = (EditText) subjectLayout.getChildAt(1);

            String subjectName = subjectInput.getText().toString();
            String studyHours = hoursInput.getText().toString();

            if (!subjectName.isEmpty() && !studyHours.isEmpty()) {

                Toast.makeText(this, "Subject: " + subjectName + ", Hours: " + studyHours, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Please enter both subject and hours.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
