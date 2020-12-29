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


        //Set Vessel
        if (notificationDTOList.get(i).getRightVesselStatus() == 0) {

            notificationViewHolder.notifyRightVessel.setVisibility(View.VISIBLE);
            notificationViewHolder.notifyRightVessel.setText(MathUtil.RIGHT_VESSEL_0);


        } else if (notificationDTOList.get(i).getRightVesselStatus() == 1) {
            notificationViewHolder.notifyRightVessel.setVisibility(View.VISIBLE);
            notificationViewHolder.notifyRightVessel.setText(MathUtil.RIGHT_VESSEL_1);

        }else {

        }

        if (notificationDTOList.get(i).getLeftVesselStatus() == 0) {
            notificationViewHolder.notifyLeftVessel.setVisibility(View.VISIBLE);
            notificationViewHolder.notifyLeftVessel.setText(MathUtil.LEFT_VESSEL_0);

        } else if (notificationDTOList.get(i).getLeftVesselStatus() == 1) {
            notificationViewHolder.notifyLeftVessel.setVisibility(View.VISIBLE);
            notificationViewHolder.notifyLeftVessel.setText(MathUtil.LEFT_VESSEL_1);
        }else {

        }

        if (notificationDTOList.get(i).getCenterVesselStatus() == 0) {
            notificationViewHolder.notifyCenterVessel.setVisibility(View.VISIBLE);
            notificationViewHolder.notifyCenterVessel.setText(MathUtil.CENTER_VESSEL_0);
        } else if (notificationDTOList.get(i).getCenterVesselStatus() == 1) {
            notificationViewHolder.notifyCenterVessel.setVisibility(View.VISIBLE);
            notificationViewHolder.notifyCenterVessel.setText(MathUtil.CENTER_VESSEL_1);
        }else {

        }


        //End of Set Vessel


        //Right Whistle and Timer
        if (notificationDTOList.get(i).getRightWhistleStatus() > 0) {
            notificationViewHolder.notifyRightWhistle.setVisibility(View.VISIBLE);
            notificationViewHolder.notifyRightWhistle.setText("" + notificationDTOList.get(i).getRightWhistleStatus() + " " + MathUtil.RIGHT_WHISTLE);
        }/*else {
            notificationViewHolder.notifyRightWhistle.setVisibility(View.GONE);
        }*/
        if (notificationDTOList.get(i).getRightTimerStatus() > 0) {
            notificationViewHolder.notifyRightTimer.setVisibility(View.VISIBLE);
            notificationViewHolder.notifyRightTimer.setText("" + notificationDTOList.get(i).getRightTimerStatus() + " " + MathUtil.RIGHT_TIMER);
        }/*else {
            notificationViewHolder.notifyRightWhistle.setVisibility(View.GONE);
        }*/

        // End of Right Whistle and Timer


        //Left Whistle and Timer
        if (notificationDTOList.get(i).getLeftWhistleStatus() > 0) {
            notificationViewHolder.notifyLeftWhistle.setVisibility(View.VISIBLE);
            notificationViewHolder.notifyLeftWhistle.setText("" + notificationDTOList.get(i).getLeftWhistleStatus() + " " + MathUtil.LEFT_WHISTLE);
        }/*else {
            notificationViewHolder.notifyLeftWhistle.setVisibility(View.GONE);
        }*/
        if (notificationDTOList.get(i).getLeftTimerStatus() > 0) {
            notificationViewHolder.notifyLeftTimer.setVisibility(View.VISIBLE);
            notificationViewHolder.notifyLeftTimer.setText("" + notificationDTOList.get(i).getLeftTimerStatus() + " " + MathUtil.LEFT_TIMER);
        }/*else {
            notificationViewHolder.notifyLeftTimer.setVisibility(View.GONE);
        }*/

        // End of LEFT Whistle and Timer


        //Center Whistle and Timer
        if (notificationDTOList.get(i).getCenterWhistleStatus() > 0) {
            notificationViewHolder.notifyCenterWhistle.setVisibility(View.VISIBLE);
            notificationViewHolder.notifyCenterWhistle.setText("" + notificationDTOList.get(i).getCenterWhistleStatus() + " " + MathUtil.CENTER_WHISTLE);
        }/*else {
            notificationViewHolder.notifyCenterWhistle.setVisibility(View.GONE);
        }*/
        if (notificationDTOList.get(i).getCenterTimerStatus() > 0) {
            notificationViewHolder.notifyCenterTimer.setVisibility(View.VISIBLE);
            notificationViewHolder.notifyCenterTimer.setText("" + notificationDTOList.get(i).getCenterTimerStatus() + " " + MathUtil.CENTER_TIMER);
        }/*else {
            notificationViewHolder.notifyCenterTimer.setVisibility(View.GONE);
        }*/// End of Center Whistle and Timer


        // Picasso.get().load(notificationDTOList.get(i).getNotiImage()).into(notificationViewHolder.notiImg);
        //notificationViewHolder.notiText.setText(notificationDTOList.get(i).getNotiText());

    }

    @Override
    public int getItemCount() {
        return notificationDTOList == null ? 0 : notificationDTOList.size();
    }

    class NotificationViewHolder extends RecyclerView.ViewHolder {

        ImageView notiImg;
        TextView notifyRightVessel, notifyRightWhistle, notifyRightTimer;

        TextView notifyLeftVessel, notifyLeftWhistle, notifyLeftTimer;
        TextView notifyCenterVessel, notifyCenterWhistle, notifyCenterTimer;


        public NotificationViewHolder(@NonNull View itemView) {
            super(itemView);

            //notiImg = (ImageView) itemView.findViewById(R.id.notifImage);
            notifyRightVessel = (TextView) itemView.findViewById(R.id.notifyRightVessel);
            notifyRightWhistle = (TextView) itemView.findViewById(R.id.notifyRightWhistle);
            notifyRightTimer = (TextView) itemView.findViewById(R.id.notifyRightTimer);

            notifyLeftVessel = (TextView) itemView.findViewById(R.id.notifyLeftVessel);
            notifyLeftWhistle = (TextView) itemView.findViewById(R.id.notifyLeftWhistle);
            notifyLeftTimer = (TextView) itemView.findViewById(R.id.notifyLeftTimer);

            notifyCenterVessel = (TextView) itemView.findViewById(R.id.notifyCenterVessel);
            notifyCenterWhistle = (TextView) itemView.findViewById(R.id.notifyCenterWhistle);
            notifyCenterTimer = (TextView) itemView.findViewById(R.id.notifyCenterTimer);

        }
    }

}
