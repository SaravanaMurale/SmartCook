package com.clj.blesample.model;

public class NotificationResponseDTO {

    private int notiColumnID;
    private String notiText;
    private String notiReadStatus;

    public NotificationResponseDTO(int notiColumnID) {
        this.notiColumnID = notiColumnID;
    }

    public NotificationResponseDTO(int notiColumnID, String notiText, String notiReadStatus) {
        this.notiColumnID = notiColumnID;
        this.notiText = notiText;
        this.notiReadStatus = notiReadStatus;
    }

    public int getNotiColumnID() {
        return notiColumnID;
    }

    public void setNotiColumnID(int notiColumnID) {
        this.notiColumnID = notiColumnID;
    }

    public String getNotiText() {
        return notiText;
    }

    public void setNotiText(String notiText) {
        this.notiText = notiText;
    }

    public String getNotiReadStatus() {
        return notiReadStatus;
    }

    public void setNotiReadStatus(String notiReadStatus) {
        this.notiReadStatus = notiReadStatus;
    }
}
