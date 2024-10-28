package com.example.essay.models.account;

import com.google.firebase.firestore.FirebaseFirestore;

public class AccountRepository {
    private FirebaseFirestore db;
    private static final String TAG = "AccountRepository";

    public AccountRepository(FirebaseFirestore db) {
        this.db = db;
    }




}
