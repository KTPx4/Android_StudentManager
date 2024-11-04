package com.example.essay.services;

import androidx.annotation.NonNull;

import com.google.firebase.firestore.DocumentReference;

public interface ServiceCallback {
    void onSuccess(DocumentReference documentReference);
    void onSuccess();
    void onFailure(@NonNull Exception e);
    void onResult(boolean exists);
}
