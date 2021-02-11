package com.clj.blesample.model;

public class NotificationResponseDTO {

    private int notiColumnID;
    private String notiText;
    private String notiReadStatus;
    private String notiTimeStamp;

    public NotificationResponseDTO(int notiColumnID) {
        this.notiColumnID = notiColumnID;
    }

    public NotificationResponseDTO(int notiColumnID, String notiText, String notiReadStatus) {
        this.notiColumnID = notiColumnID;
        this.notiText = notiText;
        this.notiReadStatus = notiReadStatus;
    }

    public NotificationResponseDTO(int notiColumnID, String notiText, String notiReadStatus,String notiTimeStamp) {
        this.notiColumnID = notiColumnID;
        this.notiText = notiText;
        this.notiReadStatus = notiReadStatus;
        this.notiTimeStamp=notiTimeStamp;
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

    public String getNotiTimeStamp() {
        return notiTimeStamp;
    }

    public void setNotiTimeStamp(String notiTimeStamp) {
        this.notiTimeStamp = notiTimeStamp;
    }
}
