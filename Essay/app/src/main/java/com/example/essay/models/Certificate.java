package com.example.essay.models;

public class Certificate {
    private String certificateId;
    private String certificateName;
    private String issuingOrganization;
    private String IssueDate;
    private String ExpDate;
    private String StudentId;
    private boolean selected;

    public Certificate() {
    }

    // Constructor
    public Certificate(String certificateId, String certificateName, String issuingOrganization) {
        this.certificateId = certificateId;
        this.certificateName = certificateName;
        this.issuingOrganization = issuingOrganization;
    }

    public Certificate(String certificateId, String certificateName, String issuingOrganization, String issueDate, String expDate, String studentId, boolean selected) {
        this.certificateId = certificateId;
        this.certificateName = certificateName;
        this.issuingOrganization = issuingOrganization;
        IssueDate = issueDate;
        ExpDate = expDate;
        StudentId = studentId;
        this.selected = selected;
    }

    public String getIssueDate() {
        return IssueDate;
    }

    public void setIssueDate(String issueDate) {
        IssueDate = issueDate;
    }

    public String getExpDate() {
        return ExpDate;
    }

    public void setExpDate(String expDate) {
        ExpDate = expDate;
    }

    public String getStudentId() {
        return StudentId;
    }

    public void setStudentId(String studentId) {
        StudentId = studentId;
    }

    // Getters and setters
    public String getCertificateId() {
        return certificateId;
    }

    public void setCertificateId(String certificateId) {
        this.certificateId = certificateId;
    }

    public String getCertificateName() {
        return certificateName;
    }

    public void setCertificateName(String certificateName) {
        this.certificateName = certificateName;
    }

    public String getIssuingOrganization() {
        return issuingOrganization;
    }

    public void setIssuingOrganization(String issuingOrganization) {
        this.issuingOrganization = issuingOrganization;
    }
    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
