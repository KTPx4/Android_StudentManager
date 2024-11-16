package com.example.essay.component.student;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.essay.Adapter.CertificateAdapter;
import com.example.essay.R;
import com.example.essay.models.Certificate;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class CertificateActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private CertificateAdapter certificateAdapter;
    private List<Certificate> certificateList;
    private FirebaseFirestore db;
    private Button buttonAddCertificate,buttonClose;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_certificates);

        // Initialize Firestore
        db = FirebaseFirestore.getInstance();

        buttonClose = findViewById(R.id.btn_close);
        // Initialize views
        recyclerView = findViewById(R.id.RVCertificate);
        EditText editTextSearch = findViewById(R.id.editTextSearch);
        ImageButton buttonAddCertificate = findViewById(R.id.buttonAddCertificate);

        // Set up RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize the list
        certificateList = new ArrayList<>();
        certificateAdapter = new CertificateAdapter(this, certificateList);
        recyclerView.setAdapter(certificateAdapter);

        // Fetch data from Firebase
        fetchCertificatesFromFirebase();
        editTextSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                // Filter the list based on the search query
                filterCertificates(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });
        // Set up button listener to add certificate
        buttonAddCertificate.setOnClickListener(v -> {
            // Navigate to AddCertificate activity
            Intent intent = new Intent(CertificateActivity.this, AddCertificate.class);
            startActivity(intent);
        });


        buttonClose.setOnClickListener(v -> finish());
    }
    private void filterCertificates(String query) {
        List<Certificate> filteredList = new ArrayList<>();
        for (Certificate certificate : certificateList) {
            if (certificate.getCertificateName().toLowerCase().contains(query.toLowerCase()) ||
                    certificate.getIssuingOrganization().toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(certificate);
            }
        }
        // Update the adapter with the filtered list
        certificateAdapter.updateList(filteredList);
    }
    @Override
    protected void onResume() {
        super.onResume();

        // Fetch the latest certificates from Firebase when the activity is resumed
        fetchCertificatesFromFirebase();
    }

    private void fetchCertificatesFromFirebase() {
        db.collection("certificates")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot querySnapshot = task.getResult();
                        if (querySnapshot != null) {
                            // Clear the existing list before adding new items
                            certificateList.clear();

                            // Loop through the documents and add them to the list
                            for (DocumentSnapshot document : querySnapshot) {
                                String certificateId = document.getString("certificateId");
                                String certificateName = document.getString("certificateName");
                                String issuingOrganization = document.getString("issuingOrganization");

                                // Create a Certificate object and add it to the list
                                Certificate certificate = new Certificate(certificateId, certificateName, issuingOrganization);
                                certificateList.add(certificate);
                            }

                            // Notify the adapter that the data has been updated
                            certificateAdapter.notifyDataSetChanged();
                        }
                    } else {
                        Toast.makeText(CertificateActivity.this, "Error getting certificates", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
