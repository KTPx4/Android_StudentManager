package com.example.essay.services;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.essay.models.CertificateModel;
import com.google.firebase.database.*;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StudentService {

    private final DatabaseReference database;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    public StudentService() {
        this.database = FirebaseDatabase.getInstance().getReference("certificates");
    }

    public void getAllCertificate(String studentId, CallbackGetCertificate CallBack)
    {

        db.collection("certificates")
                .whereEqualTo("studentId", studentId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<CertificateModel> certificates = new ArrayList<>();
                        QuerySnapshot querySnapshot = task.getResult();
                        if (querySnapshot != null) {
                            for (QueryDocumentSnapshot document : querySnapshot) {
                                // Lấy từng trường thủ công từ tài liệu

                                String id = document.getString("id");
                                String studentIdFromDoc = document.getString("studentId");
                                String certificateName = document.getString("certificateName");
                                String issueDate = document.getString("issueDate");
                                String expDate = document.getString("expDate");

                                // Tạo một đối tượng CertificateModel mới

                                CertificateModel certificate = new CertificateModel(id, studentId, certificateName, issueDate, expDate);

                                // Thêm vào danh sách
                                certificates.add(certificate);
                            }
                        }
                        CallBack.onSuccess(certificates);
                    } else {
                        CallBack.onFailure("Failed");
                    }
                });


    }
    // Add a certificate
    public void addCertificate(String studentId,String id, String certificateName, String issueDate, String expDate, StudentCallback studentCallback) {
        CertificateModel certificateModel  = new CertificateModel(id, studentId, certificateName, issueDate, expDate);

        // Kiểm tra xem ID đã tồn tại chưa
        db.collection("certificates")
                .whereEqualTo("id", id) // Kiểm tra với trường "id" trong Firestore
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null && !task.getResult().isEmpty()) {
                        // ID đã tồn tại
                        studentCallback.onFailure("Certificate ID already exists.");
                    } else {
                        // ID chưa tồn tại, thêm tài liệu mới
                        db.collection("certificates")
                                .add(certificateModel)
                                .addOnSuccessListener(documentReference -> {
                                    // Success
                                    studentCallback.onSuccess();
                                })
                                .addOnFailureListener(e -> {
                                    // Failure
                                    studentCallback.onFailure(e.getMessage());
                                });
                    }
                })
                .addOnFailureListener(e -> {
                    // Truy vấn thất bại
                    studentCallback.onFailure("Error checking certificate ID: " + e.getMessage());
                });
    }

    // Get certificates by studentId
    public void getCertificatesByStudentId(String studentId, ValueEventListener listener) {
        database.orderByChild("studentId").equalTo(studentId).addListenerForSingleValueEvent(listener);
    }

    // Edit a certificate
    public void editCertificate(String id,  String certificateName, String issuedDate, String expDate, StudentCallback callback) {
        // Tham chiếu tới collection và document cần chỉnh sửa
        DocumentReference certificateRef = db.collection("certificates").document(id);

        // Tạo bản đồ chứa các giá trị cần cập nhật
        Map<String, Object> updates = new HashMap<>();
        updates.put("certificateName", certificateName);
        updates.put("issueDate", issuedDate);
        updates.put("expDate", expDate);

        // Thực hiện cập nhật
        certificateRef.update(updates)
                .addOnSuccessListener(aVoid -> {
                    // Gọi lại callback khi thành công
                    callback.onSuccess();
                })
                .addOnFailureListener(e -> {
                    // Gọi lại callback khi có lỗi
                    callback.onFailure(e.getMessage());
                });
    }

    // Delete a certificate
    public void deleteCertificate(String certificateId, StudentCallback callback) {
        // Tham chiếu tới document cần xóa
        DocumentReference certificateRef = db.collection("certificates").document(certificateId);

        // Thực hiện xóa
        certificateRef.delete()
                .addOnSuccessListener(aVoid -> {
                    // Gọi lại callback khi thành công
                    callback.onSuccess();
                })
                .addOnFailureListener(e -> {
                    // Gọi lại callback khi có lỗi
                    callback.onFailure("Error deleting certificate: " + e.getMessage());
                });
    }


}
