package com.example.essay.component.student;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;

import androidx.fragment.app.DialogFragment;

import com.example.essay.StudentFragment;
import com.example.essay.R;

import java.util.ArrayList;
import java.util.List;

public class SortDialogFragment extends DialogFragment {

    private CheckBox sortByName, sortByScore, sortByGender, sortByAge;
    private Button applySortButton;
    private List<String> sortCriteria; // List to hold the passed criteria

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.component_sort_students, container, false);

        // Initialize the checkboxes and apply button
        sortByName = rootView.findViewById(R.id.sortByName);
        sortByScore = rootView.findViewById(R.id.sortByScore);
        sortByGender = rootView.findViewById(R.id.sortBygender);
        sortByAge = rootView.findViewById(R.id.sortByAge);
        applySortButton = rootView.findViewById(R.id.applySortButton);

        // Get the selected criteria from the parent fragment (if any)
        if (getArguments() != null) {
            sortCriteria = getArguments().getStringArrayList("selectedCriteria");
            restoreCheckboxes(sortCriteria);
        }

        // Apply sorting when button is clicked
        applySortButton.setOnClickListener(v -> {
            applySorting();
            dismiss();
        });

        return rootView;
    }

    private void restoreCheckboxes(List<String> sortCriteria) {
        if (sortCriteria != null) {
            sortByName.setChecked(sortCriteria.contains("name"));
            sortByScore.setChecked(sortCriteria.contains("score"));
            sortByGender.setChecked(sortCriteria.contains("gender"));
            sortByAge.setChecked(sortCriteria.contains("age"));
        }
    }

    private void applySorting() {
        // Fetch sorting criteria based on selected checkboxes
        List<String> selectedCriteria = new ArrayList<>();
        if (sortByName.isChecked()) selectedCriteria.add("name");
        if (sortByScore.isChecked()) selectedCriteria.add("score");
        if (sortByGender.isChecked()) selectedCriteria.add("gender");
        if (sortByAge.isChecked()) selectedCriteria.add("age");

        if (selectedCriteria.isEmpty()) {
            if (getTargetFragment() != null) {
                ((StudentFragment) getTargetFragment()).resetStudentList();
            }
        } else {
            if (getTargetFragment() != null) {
                ((StudentFragment) getTargetFragment()).sortStudents(selectedCriteria);
            }
        }

        dismiss();
    }

}
