package com.clj.blesample.notificationpackage;

public class RightNotiDTO {

    int rightColumnId;
    int rightVesselStatus;
    String rightBunrer;

    public RightNotiDTO(int rightColumnId, int rightVesselStatus, String rightBunrer) {
        this.rightColumnId = rightColumnId;
        this.rightVesselStatus = rightVesselStatus;
        this.rightBunrer = rightBunrer;
    }

    public int getRightColumnId() {
        return rightColumnId;
    }

    public void setRightColumnId(int rightColumnId) {
        this.rightColumnId = rightColumnId;
    }

    public int getRightVesselStatus() {
        return rightVesselStatus;
    }

    public void setRightVesselStatus(int rightVesselStatus) {
        this.rightVesselStatus = rightVesselStatus;
    }

    public String getRightBunrer() {
        return rightBunrer;
    }

    public void setRightBunrer(String rightBunrer) {
        this.rightBunrer = rightBunrer;
    }
}
