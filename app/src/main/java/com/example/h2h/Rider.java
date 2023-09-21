package com.example.h2h;

public class Rider {
    String name;
    String phone;
    String email;
    String status;
    String address;
    String password;

    String cnic;

    String id;

    boolean verificationStatus;

    String CnicUrl;

    public Rider() {

    }

    public Rider(String name, String phone, String email, String address, String password, String cnic,String cnicUrl, boolean verificationStatus, String id, String status) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.password = password;
        this.cnic = cnic;
        this.verificationStatus = verificationStatus;
        this.CnicUrl = cnicUrl;
        this.id = id;
        this.status=status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public String getCnic() {
        return cnic;
    }

    public void setCnic(String cnic) {
        this.cnic = cnic;
    }

    public boolean isVerificationStatus() {
        return verificationStatus;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setVerificationStatus(boolean verificationStatus) {
        this.verificationStatus = verificationStatus;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }



    public String getCnicUrl() {
        return CnicUrl;
    }

    public void setCnicUrl(String cnicUrl) {
        CnicUrl = cnicUrl;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
