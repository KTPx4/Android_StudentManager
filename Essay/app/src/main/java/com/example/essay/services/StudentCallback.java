package com.example.essay.services;

import com.example.essay.models.CertificateModel;

import java.util.List;

public interface StudentCallback {
    void onSuccess();

    void onFailure(String mess);

}
