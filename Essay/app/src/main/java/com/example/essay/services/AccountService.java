package com.example.essay.services;

import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.essay.HomeActivity;
import com.example.essay.LoginActivity;
import com.example.essay.models.account.AccountModel;
import com.example.essay.models.history.HistoryModel;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class AccountService {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    public void saveHistory(String user)
    {
        // Định dạng ngày giờ theo yêu cầu
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss", Locale.getDefault());
        String currentDate = dateFormat.format(new Date());
        Log.d("HISTORy", "saveLoginHistory: ");
        // Tạo một bản ghi lịch sử
        HistoryModel historyRecord = new HistoryModel(user, currentDate);

        // Lưu vào Firestore
        db.collection("histories")
                .add(historyRecord)
                .addOnSuccessListener(documentReference -> Log.d("History", "Login history saved successfully"))
                .addOnFailureListener(e -> Log.w("History", "Error saving login history", e));
    }
    public void createUser(String user, String pass, String name, String phone, String birthDay, String role, String email, ServiceCallback callback) {
        checkUserExists(user, new ServiceCallback() {
            @Override
            public void onSuccess(DocumentReference documentReference) {

            }

            @Override
            public void onSuccess() {

            }

            @Override
            public void onFailure(@NonNull Exception e) {

            }

            @Override
            public void onResult(boolean exists) {
                if (exists) {
                    // User already exists, return failure
                    callback.onFailure(new Exception("User already exists"));
                }
                else
                {
                    // User does not exist, proceed to create
                    AccountModel newUser = new AccountModel(user, pass, name, phone, birthDay, role, email);

                    db.collection("accounts")
                            .add(newUser)
                            .addOnSuccessListener(documentReference -> {
                                // Success
                                callback.onSuccess(documentReference);
                            })
                            .addOnFailureListener(e -> {
                                // Failure
                                callback.onFailure(e);
                            });
                }
            }
        });
    }
    public void fastCreate(String user, String name, String role, ServiceCallback callback) {
        checkUserExists(user, new ServiceCallback() {
            @Override
            public void onSuccess(DocumentReference documentReference) {

            }

            @Override
            public void onSuccess() {

            }

            @Override
            public void onFailure(@NonNull Exception e) {

            }

            @Override
            public void onResult(boolean exists) {
                if (exists) {
                    // User already exists, return failure
                    callback.onFailure(new Exception("User already exists"));
                }
                else
                {
                    // User does not exist, proceed to create
                    AccountModel newUser = new AccountModel(user, user, name, "", "1/1/2003", role, "");

                    db.collection("accounts")
                            .add(newUser)
                            .addOnSuccessListener(documentReference -> {
                                // Success
                                callback.onSuccess(documentReference);
                            })
                            .addOnFailureListener(e -> {
                                // Failure
                                callback.onFailure(e);
                            });
                }
            }
        });
    }


    public void updateUser(String user, String newPass, String newName, String newPhone, String newBirthDay, String newRole, String email, ServiceCallback callback) {
        // Tìm tài liệu của người dùng bằng user (username)
        db.collection("accounts")
                .whereEqualTo("user", user) // Tìm kiếm theo username
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && !task.getResult().isEmpty()) {
                        // Người dùng tồn tại, cập nhật thông tin
                        DocumentSnapshot document = task.getResult().getDocuments().get(0); // Giả sử chỉ có 1 tài liệu
                        DocumentReference docRef = document.getReference();

                        // Tạo bản cập nhật dữ liệu
                        Map<String, Object> updates = new HashMap<>();
                        updates.put("pass", newPass);
                        updates.put("name", newName);
                        updates.put("phone", newPhone);
                        updates.put("birthDay", newBirthDay);
                        updates.put("role", newRole);
                        updates.put("email", email);
                        // Cập nhật tài liệu
                        docRef.update(updates)
                                .addOnSuccessListener(aVoid -> {
                                    // Cập nhật thành công
                                    callback.onSuccess(docRef);
                                })
                                .addOnFailureListener(e -> {
                                    // Xảy ra lỗi khi cập nhật
                                    callback.onFailure(e);
                                });
                    } else {
                        // Người dùng không tồn tại, trả về lỗi
                        callback.onFailure(new Exception("User does not exist"));
                    }
                })
                .addOnFailureListener(e -> {
                    // Xảy ra lỗi khi tìm kiếm người dùng
                    callback.onFailure(e);
                });
    }

    public void changePass(String user, String oldPass, String newPass, ServiceCallback callback)
    {
        db.collection("accounts")
                .whereEqualTo("user", user)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        if (!task.getResult().isEmpty()) {
                            // Lấy tài liệu đầu tiên (vì user là duy nhất)
                            DocumentSnapshot document = task.getResult().getDocuments().get(0);

                            // Lấy mật khẩu và role từ Firestore
                            String storedPassword = document.getString("pass");
                            String userRole = document.getString("role");
                            String name = document.getString("name");
                            String phone = document.getString("phone");
                            String birth = document.getString("birthDay");
                            String email = document.getString("email");

                            // So sánh password
                            if (oldPass.equals(storedPassword))
                            {
                                // Nếu mật khẩu cũ khớp, cập nhật mật khẩu mới
                                document.getReference()
                                        .update("pass", newPass)
                                        .addOnSuccessListener(aVoid -> {
                                            // Gọi lại thành công
                                            callback.onSuccess();
                                        })
                                        .addOnFailureListener(e -> {
                                            // Gọi lại khi có lỗi trong quá trình cập nhật
                                            callback.onFailure(new Exception("Failed to update password"));
                                        });
                            }
                            else
                            {
                               callback.onFailure(new Exception("Password not match"));
                            }
                        }
                        else
                        {
                            callback.onFailure(new Exception("User not found"));

                        }
                    } else {
                        callback.onFailure(new Exception("Application error. Try again!"));

                        Log.w("Changepass", "Error getting documents.", task.getException());
                    }
                });
    }
    public void deleteAccount(String user, ServiceCallback callback) {
        // Truy vấn để tìm tài khoản với tên người dùng đã nhập
        db.collection("accounts")
                .whereEqualTo("user", user)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && !task.getResult().isEmpty()) {
                        // Lấy tài liệu đầu tiên có tên người dùng khớp
                        DocumentSnapshot document = task.getResult().getDocuments().get(0);

                        // Xóa tài liệu đó khỏi Firestore
                        document.getReference().delete()
                                .addOnSuccessListener(documentReference -> {
                                    // Thành công
                                    callback.onSuccess();
                                })
                                .addOnFailureListener(e -> {
                                    // Thất bại
                                    callback.onFailure(e);
                                });
                    } else if (task.isSuccessful() && task.getResult().isEmpty()) {
                        // Không tìm thấy tài khoản
                        callback.onFailure(new Exception("User not found"));
                    } else {
                        // Lỗi trong quá trình truy vấn
                        callback.onFailure(task.getException());
                    }
                });
    }


    private void checkUserExists(String username, ServiceCallback callback) {
        db.collection("accounts")
                .whereEqualTo("user", username)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot documents = task.getResult();
                        callback.onResult(documents != null && !documents.isEmpty());
                    } else {
                        callback.onResult(false);
                    }
                });
    }



}