package com.clj.blesample.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.clj.blesample.R;
import com.clj.blesample.model.NotificationDTO;
import com.clj.blesample.model.NotificationResponseDTO;
import com.clj.blesample.utils.MathUtil;
import com.squareup.picasso.Picasso;

import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder> {


    private Context mCtx;
    private List<NotificationResponseDTO> notificationDTOList;


    public NotificationAdapter(Context mCtx, List<NotificationResponseDTO> notificationDTOList) {
        this.mCtx = mCtx;
        this.notificationDTOList = notificationDTOList;
    }

    public void dataSet(List<NotificationResponseDTO> notificationDTOList) {
        this.notificationDTOList = notificationDTOList;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        LayoutInflater layoutInflater = LayoutInflater.from(mCtx);
        View view = layoutInflater.inflate(R.layout.layout_notification_adapter, viewGroup, false);
        return new NotificationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder notificationViewHolder, int i) {


        if(notificationDTOList.get(i).getRightVesselStatus()==0){

            notificationViewHolder.notifyRight.setText(MathUtil.RIGHT_VESSEL_0);

        }else {
            notificationViewHolder.notifyRight.setText(MathUtil.RIGHT_VESSEL_1);
        }

        if(notificationDTOList.get(i).getLeftVesselStatus()==0){
            notificationViewHolder.notifyLeft.setText(MathUtil.LEFT_VESSEL_0);
        }else {
            notificationViewHolder.notifyLeft.setText(MathUtil.LEFT_VESSEL_1);
        }

        if(notificationDTOList.get(i).getCenterVesselStatus()==0){
            notificationViewHolder.notifyCenter.setText(MathUtil.CENTER_VESSEL_0);
        }else {
            notificationViewHolder.notifyCenter.setText(MathUtil.CENTER_VESSEL_1);
        }



       // Picasso.get().load(notificationDTOList.get(i).getNotiImage()).into(notificationViewHolder.notiImg);
        //notificationViewHolder.notiText.setText(notificationDTOList.get(i).getNotiText());

    }

    @Override
    public int getItemCount() {
        return notificationDTOList == null ? 0 : notificationDTOList.size();
    }

    class NotificationViewHolder extends RecyclerView.ViewHolder {

        ImageView notiImg;
        TextView notifyRight,notifyLeft,notifyCenter;


        public NotificationViewHolder(@NonNull View itemView) {
            super(itemView);

            //notiImg = (ImageView) itemView.findViewById(R.id.notifImage);
            notifyRight = (TextView) itemView.findViewById(R.id.notifyRight);
            notifyLeft = (TextView) itemView.findViewById(R.id.notifyLeft);
            notifyCenter = (TextView) itemView.findViewById(R.id.notifyCenter);

        }
    }

}
