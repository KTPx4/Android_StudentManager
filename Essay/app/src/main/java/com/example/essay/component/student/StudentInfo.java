package com.example.essay.component.student;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.essay.Adapter.CertificateAdapter2;
import com.example.essay.R;
import com.example.essay.component.student.ChooseCertificate;
import com.example.essay.models.CertificateModel;
import com.example.essay.models.Student;
import com.example.essay.services.CallbackGetCertificate;
import com.example.essay.services.StudentService;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StudentInfo extends AppCompatActivity {
    private EditText txtStudentId, txtName, txtAge, txtAddress, txtEmail, txtGPA,edit_certificatetext;
    private Spinner spinnerGender;
    private RecyclerView recyclerViewCertificates;
    private Button buttonAddCertificate, btnClose, buttonAddStudent;
    private FirebaseFirestore db;
    private String studentId;
    private LinearLayout certificatesLayout;
    private RecyclerView recyCertificate;
    private String typeStart = "create";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.component_student_info);

        // Initialize Firebase Firestore
        db = FirebaseFirestore.getInstance();

        // Initialize UI components
        txtStudentId = findViewById(R.id.txt_StudentId);
        txtName = findViewById(R.id.txtName);
        txtAge = findViewById(R.id.txt_Age);
        txtAddress = findViewById(R.id.txt_Address);
        txtEmail = findViewById(R.id.txt_Email);
        txtGPA = findViewById(R.id.txt_GPA);
        spinnerGender = findViewById(R.id.spinner_Gender);
        certificatesLayout = findViewById(R.id.certificates_Layout);
        buttonAddCertificate = findViewById(R.id.btn_add_certificate);
        edit_certificatetext = findViewById(R.id.edit_certificatetext);
        btnClose = findViewById(R.id.btn_info_close);
        buttonAddStudent = findViewById(R.id.buttonAddStudent);
        recyCertificate = findViewById(R.id.list_certificate);
        recyCertificate.setLayoutManager(new LinearLayoutManager(this));
        // Set up Gender Spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.gender_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerGender.setAdapter(adapter);

        // Get the studentId and mode (add or edit) from intent
        studentId = getIntent().getStringExtra("studentId");
        String mode = getIntent().getStringExtra("mode");

        if ("edit".equals(mode)) {
            // If in edit mode, load the student data
            loadStudentData(studentId);
//            certificatesLayout.setVisibility(View.VISIBLE);
            typeStart = "edit";

        } else if ("info".equals(mode)) {
            // If in info mode, load the student data but make fields non-editable
            loadStudentData(studentId);
            setFieldsNonEditable();
//            certificatesLayout.setVisibility(View.VISIBLE);
            typeStart = "info";

        } else {
            // If in add mode, generate a new student ID
            getMaxStudentId();
            typeStart = "create";

        }
        buttonAddCertificate.setOnClickListener(v -> {
            Intent intent = new Intent(StudentInfo.this, CertificateInfo.class);
            intent.putExtra("type", "create");
            intent.putExtra("studentId", studentId); // Truyền studentId nếu cần
          startActivityForResult(intent, 1); // Request code là 1
        });

        // Close button event
        btnClose.setOnClickListener(v -> finish());

        // Add student button event (works for both adding, editing and viewing)
        buttonAddStudent.setOnClickListener(v -> {
            if ("edit".equals(mode)) {
                updateStudent();
            } else if ("info".equals(mode)) {
                // Do nothing in "info" mode, just show the info
            } else {
                addStudent();
            }
        });
    }

    // Method to load student data for editing
    private void loadStudentData(String studentId) {
        db.collection("students").document(studentId).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        Student student = documentSnapshot.toObject(Student.class);
                        if (student != null) {
                            txtStudentId.setText(student.getId());
                            txtName.setText(student.getName());
                            txtAge.setText(String.valueOf(student.getAge()));
                            txtAddress.setText(student.getAddress());
                            txtEmail.setText(student.getEmail());
                            txtGPA.setText(String.valueOf(student.getGPA()));

                            // Set the gender spinner based on the student's gender
                            ArrayAdapter<CharSequence> adapter = (ArrayAdapter<CharSequence>) spinnerGender.getAdapter();
                            int position = adapter.getPosition(student.getGender());
                            spinnerGender.setSelection(position);

                            // Disable student ID field for editing
                            txtStudentId.setEnabled(false);
                        }
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(StudentInfo.this, "Failed to load student data", Toast.LENGTH_SHORT).show();
                });

        StudentService studentService = new StudentService();
        studentService.getAllCertificate(studentId, new CallbackGetCertificate() {
            @Override
            public void onSuccess(List<CertificateModel> listCertificate) {
                boolean isEdit = typeStart.equals("edit");
                CertificateAdapter2 adap = new CertificateAdapter2(listCertificate, isEdit);
                Log.d("Student info", "onSuccess: ");
                recyCertificate.setAdapter(adap);
            }

            @Override
            public void onFailure(String mess) {
                Toast.makeText(getApplicationContext(), "Load list certificate failed", Toast.LENGTH_SHORT).show();
                Log.d("STUDENT INFO - LOAD CERTIFICATE", "onFailure: " + mess);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
//            ArrayList<String> selectedCertificates = data.getStringArrayListExtra("selectedCertificates");
//            if (selectedCertificates != null) {
//                String certificatesText = String.join(", ", selectedCertificates);
//                edit_certificatetext.setText(certificatesText);
//            }
            loadStudentData(studentId);

        }

        // Kiểm tra requestCode và resultCode

    }
    // Method to update student data
    private void updateStudent() {
        // Get the values from the input fields
        String name = txtName.getText().toString();
        String ageString = txtAge.getText().toString();
        String address = txtAddress.getText().toString();
        String email = txtEmail.getText().toString();
        String gpaString = txtGPA.getText().toString();

        // Validate the input
        if (!validateStudentInput(name, ageString, address, email, gpaString)) {
            return; // Exit if validation fails
        }

        // Convert age and GPA to proper types
        int age = Integer.parseInt(ageString);
        float gpa = Float.parseFloat(gpaString);

        // Create a map with the updated student data
        Map<String, Object> updatedStudent = new HashMap<>();
        updatedStudent.put("name", name);
        updatedStudent.put("age", age);
        updatedStudent.put("address", address);
        updatedStudent.put("email", email);
        updatedStudent.put("gpa", gpa);
        updatedStudent.put("gender", spinnerGender.getSelectedItem().toString());

        // Update the student document in Firestore
        db.collection("students").document(studentId)
                .update(updatedStudent)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(StudentInfo.this, "Student updated successfully!", Toast.LENGTH_SHORT).show();
                    finish(); // Close the activity
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(StudentInfo.this, "Failed to update student: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }



    // Phương thức lấy StudentID lớn nhất
    private void getMaxStudentId() {
        CollectionReference studentsRef = db.collection("students");
        studentsRef.get().addOnCompleteListener(new OnCompleteListener<>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    int maxId = 0; // Biến lưu ID lớn nhất
                    for (DocumentSnapshot document : task.getResult()) {
                        String id = document.getId();
                        if (id.startsWith("SV")) {
                            // Chỉ lấy ID sinh viên
                            int currentId = Integer.parseInt(id.substring(2));
                            if (currentId > maxId) {
                                maxId = currentId;
                            }
                        }
                    }
                    studentId = String.format("SV%04d", maxId + 1); // Tăng lên 1
                    txtStudentId.setText(studentId);
                    txtStudentId.setEnabled(false); // Đặt ô ID sinh viên thành không thể chỉnh sửa
                } else {
                    Toast.makeText(StudentInfo.this, "Failed to get student IDs.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // Phương thức thêm sinh viên mới vào Firebase
    private void addStudent() {
        String name = txtName.getText().toString();
        String ageString = txtAge.getText().toString(); // Define ageString
        String gender = spinnerGender.getSelectedItem().toString();
        String address = txtAddress.getText().toString();
        String email = txtEmail.getText().toString();
        String gpaString = txtGPA.getText().toString(); // Define gpaString

        if (!validateStudentInput(name, ageString, address, email, gpaString)) {
            return; // Exit if validation fails
        }

        // Convert age and GPA to proper types
        int age = Integer.parseInt(ageString);
        float gpa = Float.parseFloat(gpaString);

        // Create Student object
        Student newStudent = new Student(studentId, name, age, gender, address, email, gpa);

        // Save to Firestore
        db.collection("students").document(studentId)
                .set(newStudent)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(StudentInfo.this, "Student added successfully!", Toast.LENGTH_SHORT).show();
                    finish();
                    // You can reset the ID or return to the student list activity
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(StudentInfo.this, "Failed to add student: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }


    private boolean validateStudentInput(String name, String ageString, String address, String email, String gpaString) {
        boolean isValid = true;

        // Check if any required fields are empty
        if (name.isEmpty() || ageString.isEmpty() || address.isEmpty() || email.isEmpty() || gpaString.isEmpty()) {
            isValid = false;
            Toast.makeText(this, "Please fill in all fields.", Toast.LENGTH_SHORT).show();

            // Highlight the empty fields in red
            if (name.isEmpty()) {
                txtName.setError("Name is required");
            }
            if (ageString.isEmpty()) {
                txtAge.setError("Age is required");
            }
            if (address.isEmpty()) {
                txtAddress.setError("Address is required");
            }
            if (email.isEmpty()) {
                txtEmail.setError("Email is required");
            }
            if (gpaString.isEmpty()) {
                txtGPA.setError("GPA is required");
            }
        }

        // Validate GPA if all fields are non-empty
        if (!gpaString.isEmpty()) {
            float gpa = Float.parseFloat(gpaString);
            if (gpa < 0 || gpa > 10) {
                isValid = false;
                Toast.makeText(this, "GPA must be between 0 and 10.", Toast.LENGTH_SHORT).show();
                txtGPA.setError("Invalid GPA");
            }
        }

        return isValid;
    }


    private void setFieldsNonEditable() {
        txtStudentId.setEnabled(false);
        txtName.setEnabled(false);
        txtAge.setEnabled(false);
        txtAddress.setEnabled(false);
        txtEmail.setEnabled(false);
        txtGPA.setEnabled(false);
        spinnerGender.setEnabled(false);
        buttonAddCertificate.setVisibility(View.GONE);
        buttonAddStudent.setVisibility(View.GONE);
    }


}