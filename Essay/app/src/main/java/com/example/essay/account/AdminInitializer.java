package com.example.essay.account;

import android.util.Log;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class AdminInitializer {

    private static final String TAG = "AdminInitializer";
    private FirebaseFirestore db;

    public AdminInitializer(FirebaseFirestore db) {
        this.db = db;
    }

    public void initAdminAccount() {
        // Kiểm tra xem có tài khoản admin chưa
        db.collection("accounts")
                .whereEqualTo("user", "admin2")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot querySnapshot = task.getResult();
                        if (querySnapshot.isEmpty()) {
                            // Nếu chưa có admin, tạo tài khoản admin
                            createAdminAccount();
                        } else {
                            Log.d(TAG, "Admin account already exists.");
                        }
                    } else {
                        Log.e(TAG, "Error checking admin account: ", task.getException());
                    }
                });
    }

    private void createAdminAccount(){
        // Thông tin tài khoản admin
        String name = "Admin";
        String user = "admin2";
        String pass = "admin"; //
        String phone = "123456789";
        String birthDay = "01/01/2003";
        String status = "Normal";
        String linkAvt = "link_to_avatar";

        // Tạo map chứa thông tin admin
        Map<String, Object> adminData = new HashMap<>();
        adminData.put("name", name);
        adminData.put("user", user);
        adminData.put("pass", pass); //
        adminData.put("phone", phone);
        adminData.put("birthDay", birthDay);
        adminData.put("status", status);
        adminData.put("linkAvt", linkAvt);
        adminData.put("role", "admin");

        // Thêm admin vào Firestore
        db.collection("accounts").add(adminData)
                .addOnSuccessListener(documentReference -> Log.d(TAG, "Admin account created successfully."))
                .addOnFailureListener(e -> Log.e(TAG, "Error creating admin account: ", e));
    }
}
