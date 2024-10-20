package com.example.essay.account;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.auth.FirebaseAuth;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

public class AccountRepository {
    private FirebaseFirestore db;
    private static final String TAG = "AccountRepository";

    public AccountRepository(FirebaseFirestore db) {
        this.db = db;
    }




}
