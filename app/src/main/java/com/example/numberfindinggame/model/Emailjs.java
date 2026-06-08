package com.example.numberfindinggame.model;

public class Emailjs {
    private String serviceID, templateID, publicKey, email;
    private long suDung; //ngay su dung

    public Emailjs() {
    }

    @Override
    public String toString() {
        return "Emailjs{" +
                "serviceID='" + serviceID + '\'' +
                ", templateID='" + templateID + '\'' +
                ", publicKey='" + publicKey + '\'' +
                ", email='" + email + '\'' +
                ", suDung=" + suDung +
                '}';
    }

    public String getServiceID() {
        return serviceID;
    }

    public void setServiceID(String serviceID) {
        this.serviceID = serviceID;
    }

    public String getTemplateID() {
        return templateID;
    }

    public void setTemplateID(String templateID) {
        this.templateID = templateID;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public long getSuDung() {
        return suDung;
    }

    public void setSuDung(long suDung) {
        this.suDung = suDung;
    }

    public Emailjs(String serviceID, String templateID, String publicKey, String email, long suDung) {
        this.serviceID = serviceID;
        this.templateID = templateID;
        this.publicKey = publicKey;
        this.email = email;
        this.suDung = suDung;
    }
}
