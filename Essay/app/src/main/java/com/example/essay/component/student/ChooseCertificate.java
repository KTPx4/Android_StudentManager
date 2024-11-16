package com.example.essay.component.student;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.essay.Adapter.ChooseCertificateAdapter;
import com.example.essay.R;
import com.example.essay.models.Certificate;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ChooseCertificate extends AppCompatActivity {

    private RecyclerView recyclerViewCertificates;
    private ChooseCertificateAdapter certificateAdapter;
    private List<Certificate> certificateList;

    private Button BtnClose, BtnSaveCertificates;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.component_choose_certificate); // Layout của Activity

        recyclerViewCertificates = findViewById(R.id.recyclerViewCertificates);
        recyclerViewCertificates.setLayoutManager(new LinearLayoutManager(this));

        // Khởi tạo danh sách chứng chỉ
        certificateList = new ArrayList<>();
        certificateAdapter = new ChooseCertificateAdapter(certificateList);
        recyclerViewCertificates.setAdapter(certificateAdapter);

        // Lấy dữ liệu chứng chỉ từ Firebase Firestore
        loadCertificatesFromFirebase();

        BtnClose = findViewById(R.id.btn_close);
        BtnSaveCertificates = findViewById(R.id.buttonSaveCertificates);

        BtnSaveCertificates.setOnClickListener(v -> {
            ArrayList<String> selectedCertificates = new ArrayList<>();
            for (Certificate certificate : certificateList) {
                if (certificate.isSelected()) {
                    selectedCertificates.add(certificate.getCertificateName());
                }
            }

            Intent resultIntent = new Intent();
            resultIntent.putStringArrayListExtra("selectedCertificates", selectedCertificates);
            setResult(RESULT_OK, resultIntent);
            finish();
        });

    }

    private void loadCertificatesFromFirebase() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference certificatesRef = db.collection("certificates");

        certificatesRef.get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot querySnapshot = task.getResult();
                        if (querySnapshot != null) {
                            for (var document : querySnapshot) {
                                String certificateId = document.getId();
                                String certificateName = document.getString("certificateName");
                                String issuingOrganization = document.getString("issuingOrganization");

                                Certificate certificate = new Certificate(certificateId, certificateName, issuingOrganization);
                                certificateList.add(certificate);
                            }

                            // Thông báo cho adapter để cập nhật dữ liệu
                            certificateAdapter.notifyDataSetChanged();
                        }
                    } else {
                        // Xử lý khi tải dữ liệu không thành công
                    }
                });
    }
}
