package com.example.essay.models;

public class CertificateModel {
    private String id;
    private String studentId;
    private String certificateName;
    private String expDate;
    private String issueDate;

    public CertificateModel() {
        // Default constructor required for calls to DataSnapshot.getValue(CertificateModel.class)
    }

    public CertificateModel(String id, String studentId, String certificateName, String issueDate, String expDate) {
        this.id = id;
        this.studentId = studentId;
        this.certificateName = certificateName;
        this.expDate = expDate;
        this.issueDate = issueDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getCertificateName() {
        return certificateName;
    }

    public void setCertificateName(String certificateName) {
        this.certificateName = certificateName;
    }

    public String getExpDate() {
        return expDate;
    }

    public void setExpDate(String expDate) {
        this.expDate = expDate;
    }

    public String getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(String issueDate) {
        this.issueDate = issueDate;
    }
}
