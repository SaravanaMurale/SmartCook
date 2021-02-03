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
import com.clj.blesample.model.NotificationResponseDTO;

import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder> {


    DeleteNotiListener deleteNotiListener;

    public interface DeleteNotiListener{
        public void deleteNoti(NotificationResponseDTO notificationResponseDTO);
    }


    private Context mCtx;
    private List<NotificationResponseDTO> notificationDTOList;


    public NotificationAdapter(Context mCtx, List<NotificationResponseDTO> notificationDTOList,DeleteNotiListener deleteNotiListener) {
        this.mCtx = mCtx;
        this.notificationDTOList = notificationDTOList;
        this.deleteNotiListener=deleteNotiListener;
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


        if (notificationDTOList.get(i).getNotiReadStatus().equals("0")) {
            notificationViewHolder.notiBlock.setBackgroundColor(Color.WHITE);
            notificationViewHolder.notiAlertText.setText(notificationDTOList.get(i).getNotiText());
        } else if (notificationDTOList.get(i).getNotiReadStatus().equals("1")) {
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

        ImageView deleteNoti;
        TextView notiAlertText;
        RelativeLayout notiBlock;


        public NotificationViewHolder(@NonNull View itemView) {
            super(itemView);

            deleteNoti = (ImageView) itemView.findViewById(R.id.deleteNoti);
            notiBlock = (RelativeLayout) itemView.findViewById(R.id.notiBlock);
            notiAlertText = (TextView) itemView.findViewById(R.id.notiAlertText);

            deleteNoti.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    NotificationResponseDTO notificationResponseDTO = notificationDTOList.get(getAdapterPosition());

                    deleteNotiListener.deleteNoti(notificationResponseDTO);


                }
            });


        }
    }

}
