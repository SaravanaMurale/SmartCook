package com.clj.blesample.notificationpackage;

public class LeftNotiDTO {

    int leftColumnId;
    int leftVesselStatus;
    String leftBunrer;

    public LeftNotiDTO(int leftColumnId, int leftVesselStatus, String leftBunrer) {
        this.leftColumnId = leftColumnId;
        this.leftVesselStatus = leftVesselStatus;
        this.leftBunrer = leftBunrer;
    }

    public int getLeftColumnId() {
        return leftColumnId;
    }

    public void setLeftColumnId(int leftColumnId) {
        this.leftColumnId = leftColumnId;
    }

    public int getLeftVesselStatus() {
        return leftVesselStatus;
    }

    public void setLeftVesselStatus(int leftVesselStatus) {
        this.leftVesselStatus = leftVesselStatus;
    }

    public String getLeftBunrer() {
        return leftBunrer;
    }

    public void setLeftBunrer(String leftBunrer) {
        this.leftBunrer = leftBunrer;
    }
}
