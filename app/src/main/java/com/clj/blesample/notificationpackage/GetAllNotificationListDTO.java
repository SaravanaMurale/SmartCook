package com.clj.blesample.notificationpackage;

import java.util.List;

public class GetAllNotificationListDTO {

    List<RightNotiDTO> rightNotiDTOListt;
    List<LeftNotiDTO> leftNotiDTOListt;
    List<CenterNotiDTO> centerNotiDTOList;

    public GetAllNotificationListDTO(List<RightNotiDTO> rightNotiDTOListt, List<LeftNotiDTO> leftNotiDTOListt, List<CenterNotiDTO> centerNotiDTOList) {
        this.rightNotiDTOListt = rightNotiDTOListt;
        this.leftNotiDTOListt = leftNotiDTOListt;
        this.centerNotiDTOList = centerNotiDTOList;
    }

    public List<RightNotiDTO> getRightNotiDTOListt() {
        return rightNotiDTOListt;
    }

    public void setRightNotiDTOListt(List<RightNotiDTO> rightNotiDTOListt) {
        this.rightNotiDTOListt = rightNotiDTOListt;
    }

    public List<LeftNotiDTO> getLeftNotiDTOListt() {
        return leftNotiDTOListt;
    }

    public void setLeftNotiDTOListt(List<LeftNotiDTO> leftNotiDTOListt) {
        this.leftNotiDTOListt = leftNotiDTOListt;
    }

    public List<CenterNotiDTO> getCenterNotiDTOList() {
        return centerNotiDTOList;
    }

    public void setCenterNotiDTOList(List<CenterNotiDTO> centerNotiDTOList) {
        this.centerNotiDTOList = centerNotiDTOList;
    }
}
