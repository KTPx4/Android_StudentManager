package com.example.essay;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.essay.Adapter.StudentAdapter;
import com.example.essay.component.student.CertificateActivity;
import com.example.essay.component.student.SortDialogFragment;
import com.example.essay.component.student.StudentInfo;
import com.example.essay.models.Student;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class StudentFragment extends Fragment {

    private RecyclerView recyclerView;
    private StudentAdapter studentAdapter;
    private List<Student> studentList = new ArrayList<>();
    private FirebaseFirestore db;
    private static boolean isAdmin = false;
    private List<String> sortCriteria = new ArrayList<>();
    private String UserName;
    private String Role;
    public StudentFragment() {
        // Required empty public constructor
    }

    public static StudentFragment newInstance(String param1, String param2) {
        StudentFragment fragment = new StudentFragment();
        Bundle args = new Bundle();
        args.putString("param1", param1);
        args.putString("param2", param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = FirebaseFirestore.getInstance();
        if (getArguments() != null) {
            UserName = getArguments().getString("param1");
            Role = getArguments().getString("param2");
            if (Role.toLowerCase().equals("admin")) isAdmin = true;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        fetchStudentData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_student, container, false);

        // Set up RecyclerView
        recyclerView = rootView.findViewById(R.id.student_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        LinearLayout btnArrange = rootView.findViewById(R.id.btnArrange);
        LinearLayout btnImport =rootView.findViewById(R.id.btnImport);
        if(Role.toLowerCase().equals("employee"))
        {
            btnImport.setVisibility(View.GONE);
        }
        btnArrange.setOnClickListener(v -> {
            SortDialogFragment sortDialogFragment = new SortDialogFragment();
            // Pass the current sort criteria to the dialog
            Bundle args = new Bundle();
            args.putStringArrayList("selectedCriteria", new ArrayList<>(sortCriteria));
            sortDialogFragment.setArguments(args);
            sortDialogFragment.setTargetFragment(StudentFragment.this, 0);
            sortDialogFragment.show(getParentFragmentManager(), "SortDialog");
        });

        LinearLayout btnCertificate = rootView.findViewById(R.id.btnCertificate);

        btnCertificate.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), CertificateActivity.class);

            startActivity(intent);
        });

        EditText editTextSearch = rootView.findViewById(R.id.editTextSearch);
        editTextSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                filterStudentList(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });

        // Fetch data from Firestore
        fetchStudentData();

        return rootView;
    }
    private void filterStudentList(String query) {
        List<Student> filteredList = new ArrayList<>();
        for (Student student : studentList) {
            if (student.getName().toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(student);
            }
        }

        // Update the adapter with the filtered list
        if (studentAdapter != null) {
            studentAdapter.updateStudentList(filteredList);
        }
    }
    private void fetchStudentData() {
        db.collection("students")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        studentList.clear();
                        for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {
                            Student student = document.toObject(Student.class);
                            studentList.add(student);
                        }

                        // Create the StudentAdapter and set the RecyclerView
                        if (getContext() != null) {
                            studentAdapter = new StudentAdapter(getContext(), studentList, Role);
                            recyclerView.setAdapter(studentAdapter);
                        }
                    }
                })
                .addOnFailureListener(e -> {
                    // Handle the error if data fetching fails
                });
    }

    public void sortStudents(List<String> selectedCriteria) {
        // Apply the sorting based on the selected criteria
        if (selectedCriteria.contains("name")) {
            Collections.sort(studentList, (s1, s2) -> s1.getName().compareTo(s2.getName()));
        }
        if (selectedCriteria.contains("score")) {
            Collections.sort(studentList, (s1, s2) -> Double.compare(s1.getGPA(), s2.getGPA()));
        }
        if (selectedCriteria.contains("gender")) {
            Collections.sort(studentList, (s1, s2) -> s1.getGender().compareTo(s2.getGender()));
        }
        if (selectedCriteria.contains("age")) {
            Collections.sort(studentList, (s1, s2) -> Integer.compare(s1.getAge(), s2.getAge()));
        }

        // Update the RecyclerView with sorted data
        if (studentAdapter != null) {
            studentAdapter.notifyDataSetChanged();
        }

        sortCriteria = selectedCriteria;
    }
    public void resetStudentList() {
        fetchStudentData();
    }

}
