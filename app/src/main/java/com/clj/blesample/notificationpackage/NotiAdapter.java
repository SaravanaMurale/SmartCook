package com.clj.blesample.notificationpackage;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.clj.blesample.R;
import com.clj.blesample.utils.MathUtil;

import java.util.List;

public class NotiAdapter extends RecyclerView.Adapter<NotiAdapter.NotiViewHolder> {

    Context mCtx;
    List<GetAllNotificationListDTO> getAllNotificationListDTOList;

    public NotiAdapter(Context mCtx, List<GetAllNotificationListDTO> getAllNotificationListDTOList) {
        this.mCtx = mCtx;
        this.getAllNotificationListDTOList = getAllNotificationListDTOList;
    }

    public void setDate(List<GetAllNotificationListDTO> getAllNotificationListDTOList) {
        this.getAllNotificationListDTOList = getAllNotificationListDTOList;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public NotiViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(mCtx);
        View view = layoutInflater.inflate(R.layout.layout_noti_adapter, viewGroup, false);
        return new NotiViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotiViewHolder notiViewHolder, int i) {


        if (getAllNotificationListDTOList.get(i).getRightNotiDTOListt().get(i).getRightBunrer().equals("00")) {

            for (int j = 0; j < getAllNotificationListDTOList.get(i).getRightNotiDTOListt().size(); j++) {


                if (getAllNotificationListDTOList.get(i).getRightNotiDTOListt().get(i).getRightVesselStatus() == 0) {
                    notiViewHolder.notiText.setText(MathUtil.RIGHT_VESSEL_0);
                } else {
                    notiViewHolder.notiText.setText(MathUtil.RIGHT_VESSEL_1);
                }

            }
        }

        if (getAllNotificationListDTOList.get(i).getLeftNotiDTOListt().get(i).getLeftBunrer().equals("01")) {

            for (int j = 0; j < getAllNotificationListDTOList.get(i).getLeftNotiDTOListt().size(); j++) {

                if (getAllNotificationListDTOList.get(i).getLeftNotiDTOListt().get(i).getLeftVesselStatus() == 0) {
                    notiViewHolder.notiText.setText(MathUtil.LEFT_VESSEL_0);
                } else {
                    notiViewHolder.notiText.setText(MathUtil.LEFT_VESSEL_1);
                }

            }

        }

        if (getAllNotificationListDTOList.get(i).getCenterNotiDTOList().get(i).getCenterBunrer().equals("10")) {

            for (int j = 0; j < getAllNotificationListDTOList.get(i).getCenterNotiDTOList().size(); j++) {


                if (getAllNotificationListDTOList.get(i).getCenterNotiDTOList().get(i).getCenterVesselStatus() == 0) {
                    notiViewHolder.notiText.setText(MathUtil.CENTER_VESSEL_0);
                } else {
                    notiViewHolder.notiText.setText(MathUtil.CENTER_VESSEL_1);
                }

            }
        }


    }

    @Override
    public int getItemCount() {

        System.out.println("NotificationListSize" + getAllNotificationListDTOList.size());

        return getAllNotificationListDTOList == null ? 0 : getAllNotificationListDTOList.size();
    }

    class NotiViewHolder extends RecyclerView.ViewHolder {

        TextView notiText;

        public NotiViewHolder(@NonNull View itemView) {
            super(itemView);

            notiText = (TextView) itemView.findViewById(R.id.notiText);

        }
    }

}
