package com.clj.blesample.model;

public class NotificationResponseDTO {

    private int rightVesselStatus;
    private int rightWhistleStatus;
    private int rightTimerStatus;

    private int leftVesselStatus;
    private int leftWhistleStatus;
    private int leftTimerStatus;

    private int centerVesselStatus;
    private int centerWhistleStatus;
    private int centerTimerStatus;

    public NotificationResponseDTO(int rightVesselStatus, int rightWhistleStatus, int rightTimerStatus, int leftVesselStatus, int leftWhistleStatus, int leftTimerStatus, int centerVesselStatus, int centerWhistleStatus, int centerTimerStatus) {
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

    public int getRightWhistleStatus() {
        return rightWhistleStatus;
    }

    public void setRightWhistleStatus(int rightWhistleStatus) {
        this.rightWhistleStatus = rightWhistleStatus;
    }

    public int getRightTimerStatus() {
        return rightTimerStatus;
    }

    public void setRightTimerStatus(int rightTimerStatus) {
        this.rightTimerStatus = rightTimerStatus;
    }

    public int getLeftVesselStatus() {
        return leftVesselStatus;
    }

    public void setLeftVesselStatus(int leftVesselStatus) {
        this.leftVesselStatus = leftVesselStatus;
    }

    public int getLeftWhistleStatus() {
        return leftWhistleStatus;
    }

    public void setLeftWhistleStatus(int leftWhistleStatus) {
        this.leftWhistleStatus = leftWhistleStatus;
    }

    public int getLeftTimerStatus() {
        return leftTimerStatus;
    }

    public void setLeftTimerStatus(int leftTimerStatus) {
        this.leftTimerStatus = leftTimerStatus;
    }

    public int getCenterVesselStatus() {
        return centerVesselStatus;
    }

    public void setCenterVesselStatus(int centerVesselStatus) {
        this.centerVesselStatus = centerVesselStatus;
    }

    public int getCenterWhistleStatus() {
        return centerWhistleStatus;
    }

    public void setCenterWhistleStatus(int centerWhistleStatus) {
        this.centerWhistleStatus = centerWhistleStatus;
    }

    public int getCenterTimerStatus() {
        return centerTimerStatus;
    }

    public void setCenterTimerStatus(int centerTimerStatus) {
        this.centerTimerStatus = centerTimerStatus;
    }
}
