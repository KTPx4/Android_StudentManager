package com.example.essay.models;

public class Certificate {
    private String certificateId;
    private String certificateName;
    private String issuingOrganization;
    private boolean selected;

    // Constructor
    public Certificate(String certificateId, String certificateName, String issuingOrganization) {
        this.certificateId = certificateId;
        this.certificateName = certificateName;
        this.issuingOrganization = issuingOrganization;
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
