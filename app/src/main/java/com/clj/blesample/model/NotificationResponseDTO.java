package com.clj.blesample.model;

public class NotificationResponseDTO {

    private int rightVesselStatus;
    private String rightWhistleStatus;
    private String rightTimerStatus;

    private int leftVesselStatus;
    private String leftWhistleStatus;
    private String leftTimerStatus;

    private int centerVesselStatus;
    private String centerWhistleStatus;
    private String centerTimerStatus;

    public NotificationResponseDTO(int rightVesselStatus, String rightWhistleStatus, String rightTimerStatus, int leftVesselStatus, String leftWhistleStatus, String leftTimerStatus, int centerVesselStatus, String centerWhistleStatus, String centerTimerStatus) {
        this.rightVesselStatus = rightVesselStatus;
        this.rightWhistleStatus = rightWhistleStatus;
        this.rightTimerStatus = rightTimerStatus;
        this.leftVesselStatus = leftVesselStatus;
        this.leftWhistleStatus = leftWhistleStatus;
        this.leftTimerStatus = leftTimerStatus;
        this.centerVesselStatus = centerVesselStatus;
        this.centerWhistleStatus = centerWhistleStatus;
        this.centerTimerStatus = centerTimerStatus;
    }

    public int getRightVesselStatus() {
        return rightVesselStatus;
    }

    public void setRightVesselStatus(int rightVesselStatus) {
        this.rightVesselStatus = rightVesselStatus;
    }

    public String getRightWhistleStatus() {
        return rightWhistleStatus;
    }

    public void setRightWhistleStatus(String rightWhistleStatus) {
        this.rightWhistleStatus = rightWhistleStatus;
    }

    public String getRightTimerStatus() {
        return rightTimerStatus;
    }

    public void setRightTimerStatus(String rightTimerStatus) {
        this.rightTimerStatus = rightTimerStatus;
    }

    public int getLeftVesselStatus() {
        return leftVesselStatus;
    }

    public void setLeftVesselStatus(int leftVesselStatus) {
        this.leftVesselStatus = leftVesselStatus;
    }

    public String getLeftWhistleStatus() {
        return leftWhistleStatus;
    }

    public void setLeftWhistleStatus(String leftWhistleStatus) {
        this.leftWhistleStatus = leftWhistleStatus;
    }

    public String getLeftTimerStatus() {
        return leftTimerStatus;
    }

    public void setLeftTimerStatus(String leftTimerStatus) {
        this.leftTimerStatus = leftTimerStatus;
    }

    public int getCenterVesselStatus() {
        return centerVesselStatus;
    }

    public void setCenterVesselStatus(int centerVesselStatus) {
        this.centerVesselStatus = centerVesselStatus;
    }

    public String getCenterWhistleStatus() {
        return centerWhistleStatus;
    }

    public void setCenterWhistleStatus(String centerWhistleStatus) {
        this.centerWhistleStatus = centerWhistleStatus;
    }

    public String getCenterTimerStatus() {
        return centerTimerStatus;
    }

    public void setCenterTimerStatus(String centerTimerStatus) {
        this.centerTimerStatus = centerTimerStatus;
    }
}
