package com.example.essay.services;

import androidx.annotation.NonNull;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class AccountService {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    public void createUser(String user, String pass, String name, String phone, String birthDay, String role, UserCreationCallback callback) {
        checkUserExists(user, exists -> {
            if (exists) {
                // User already exists, return failure
                callback.onFailure(new Exception("User already exists"));
            }
            else
            {
                // User does not exist, proceed to create
                UserModel newUser = new UserModel(user, pass, name, phone, birthDay, role);

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
        });
    }

    private void checkUserExists(String username, UserExistsCallback callback) {
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

    public interface UserCreationCallback {
        void onSuccess(DocumentReference documentReference);

        void onFailure(@NonNull Exception e);
    }

    public interface UserExistsCallback {
        void onResult(boolean exists);
    }

    public static class UserModel {
        private String user;
        private String pass;
        private String name;
        private String phone;
        private String birthDay;
        private String role;
        private String linkAvt;
        private String status;

        public UserModel() {
            // Default constructor required for Firestore
        }

        public UserModel(String username, String password, String name, String phone, String birthDay, String role) {
            this.user = username;
            this.pass = password;
            this.name = name;
            this.phone = phone;
            this.birthDay = birthDay;
            this.role = role;
            this.linkAvt = "/";
            this.status = "normal";
        }

        // Getters and setters
        public String getUser() {
            return user;
        }

        public void setUser(String user) {
            this.user = user;
        }

        public String getPassword() {
            return pass;
        }

        public void setPassword(String password) {
            this.pass = password;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getBirthDay() {
            return birthDay;
        }

        public void setBirthDay(String birthDay) {
            this.birthDay = birthDay;
        }

        public String getRole() {
            return role;
        }

        public void setRole(String role) {
            this.role = role;
        }
    }
}