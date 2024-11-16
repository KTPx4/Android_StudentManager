package com.example.essay.component.student;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.example.essay.models.Certificate;
import androidx.appcompat.app.AppCompatActivity;

import com.example.essay.R;
import com.google.firebase.firestore.FirebaseFirestore;

public class AddCertificate extends AppCompatActivity {

    private EditText editTextCertificateID, editTextCertificateName, editTextIssuingOrganization;
    private Button buttonSaveCertificate, buttonClose;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.component_certificate_info);

        // Initialize views
        editTextCertificateID = findViewById(R.id.editText_CertificateID);
        editTextCertificateName = findViewById(R.id.editText_CertificateName);
        editTextIssuingOrganization = findViewById(R.id.editText_IssuingOrganization);
        buttonSaveCertificate = findViewById(R.id.buttonSaveCertificate);
        buttonClose = findViewById(R.id.btn_info_close);

        // Initialize Firestore
        db = FirebaseFirestore.getInstance();

        // Check if editing an existing certificate
        if (getIntent().hasExtra("certificateId")) {
            // Populate the fields with the existing certificate data
            String certificateId = getIntent().getStringExtra("certificateId");
            String certificateName = getIntent().getStringExtra("certificateName");
            String issuingOrganization = getIntent().getStringExtra("issuingOrganization");

            editTextCertificateID.setText(certificateId);
            editTextCertificateName.setText(certificateName);
            editTextIssuingOrganization.setText(issuingOrganization);

            // Set save button to update the certificate
            buttonSaveCertificate.setOnClickListener(v -> updateCertificate(certificateId));
        } else {
            // Set save button to add a new certificate
            buttonSaveCertificate.setOnClickListener(v -> saveCertificate());
        }

        buttonClose.setOnClickListener(v -> finish());

    }

    private void saveCertificate() {
        // Retrieve input data
        String certificateId = editTextCertificateID.getText().toString().trim();
        String certificateName = editTextCertificateName.getText().toString().trim();
        String issuingOrganization = editTextIssuingOrganization.getText().toString().trim();

        // Check if any field is empty
        if (!validateCertificateInput(certificateId, certificateName, issuingOrganization)) {
            return; // Stop further execution if validation fails
        }

        // Create a new Certificate object
        Certificate certificate = new Certificate(certificateId, certificateName, issuingOrganization);

        // Add a new certificate to Firestore using certificateId as the document ID
        db.collection("certificates")
                .document(certificateId)  // Use certificateId as the document ID
                .set(certificate)  // Add the certificate data to the document
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(AddCertificate.this, "Certificate added successfully", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(AddCertificate.this, "Error adding certificate", Toast.LENGTH_SHORT).show();
                });
    }

    private void updateCertificate(String certificateId) {
        // Retrieve input data
        String certificateName = editTextCertificateName.getText().toString().trim();
        String issuingOrganization = editTextIssuingOrganization.getText().toString().trim();

        // Check if any field is empty
        if (!validateCertificateInput(certificateId, certificateName, issuingOrganization)) {
            return; // Stop further execution if validation fails
        }

        // Create a new Certificate object with updated data
        Certificate certificate = new Certificate(certificateId, certificateName, issuingOrganization);

        // Update the certificate in Firestore
        db.collection("certificates")
                .document(certificateId) // Use the certificateId to update
                .set(certificate)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(AddCertificate.this, "Certificate updated successfully", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(AddCertificate.this, "Error updating certificate", Toast.LENGTH_SHORT).show();
                });
    }

    private boolean validateCertificateInput(String certificateId, String certificateName, String issuingOrganization) {
        boolean isValid = true;

        // Reset previous errors
        editTextCertificateID.setError(null);
        editTextCertificateName.setError(null);
        editTextIssuingOrganization.setError(null);

        // Check if any required fields are empty
        if (certificateId.isEmpty() || certificateName.isEmpty() || issuingOrganization.isEmpty()) {
            isValid = false;
            Toast.makeText(this, "Please fill out all fields.", Toast.LENGTH_SHORT).show();

            // Highlight the empty fields in red and set error message
            if (certificateId.isEmpty()) {
                editTextCertificateID.setError("Certificate ID is required");
            }
            if (certificateName.isEmpty()) {
                editTextCertificateName.setError("Certificate Name is required");
            }
            if (issuingOrganization.isEmpty()) {
                editTextIssuingOrganization.setError("Issuing Organization is required");
            }
        }

        return isValid; // Returns true if all fields are filled, false otherwise
    }

}
