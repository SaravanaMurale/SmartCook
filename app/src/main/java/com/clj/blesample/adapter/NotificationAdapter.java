package com.clj.blesample.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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


        if(notificationDTOList.get(i).getNotiReadStatus().equals("0")){
            notificationViewHolder.notiBlock.setBackgroundColor(Color.WHITE);
            notificationViewHolder.notiAlertText.setText(notificationDTOList.get(i).getNotiText());
        }else if(notificationDTOList.get(i).getNotiReadStatus().equals("1")){
            notificationViewHolder.notiBlock.setBackgroundColor(Color.GRAY);
            notificationViewHolder.notiAlertText.setText(notificationDTOList.get(i).getNotiText());
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
        TextView notiAlertText;
        RelativeLayout notiBlock;


        public NotificationViewHolder(@NonNull View itemView) {
            super(itemView);

            //notiImg = (ImageView) itemView.findViewById(R.id.notifImage);
            notiBlock=(RelativeLayout)itemView.findViewById(R.id.notiBlock);
            notiAlertText = (TextView) itemView.findViewById(R.id.notiAlertText);


        }
    }

}
