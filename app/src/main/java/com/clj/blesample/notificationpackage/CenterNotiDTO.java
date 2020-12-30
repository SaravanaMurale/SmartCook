package com.clj.blesample.notificationpackage;

public class CenterNotiDTO {

    int centerColumnId;
    int centerVesselStatus;
    String centerBunrer;

    public CenterNotiDTO(int centerColumnId, int centerVesselStatus, String centerBunrer) {
        this.centerColumnId = centerColumnId;
        this.centerVesselStatus = centerVesselStatus;
        this.centerBunrer = centerBunrer;
    }

    public int getCenterColumnId() {
        return centerColumnId;
    }

    public void setCenterColumnId(int centerColumnId) {
        this.centerColumnId = centerColumnId;
    }

    public int getCenterVesselStatus() {
        return centerVesselStatus;
    }

    public void setCenterVesselStatus(int centerVesselStatus) {
        this.centerVesselStatus = centerVesselStatus;
    }

    public String getCenterBunrer() {
        return centerBunrer;
    }

    public void setCenterBunrer(String centerBunrer) {
        this.centerBunrer = centerBunrer;
    }
}
