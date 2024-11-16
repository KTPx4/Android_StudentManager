package com.example.essay.services;

import com.example.essay.models.CertificateModel;

import java.util.List;

public interface CallbackGetCertificate{
    void onSuccess(List<CertificateModel> listCertificate);

    void onFailure(String mess);

}