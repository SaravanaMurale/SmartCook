package com.clj.blesample.notificationpackage;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.clj.blesample.R;
import com.clj.blesample.adapter.NotificationAdapter;

public class NotiAdapter extends RecyclerView.Adapter<NotiAdapter.NotiViewHolder> {

    Context mCtx;

    @NonNull
    @Override
    public NotiViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(mCtx);
        View view = layoutInflater.inflate(R.layout.layout_noti_adapter, viewGroup, false);
        return new NotiViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotiViewHolder notiViewHolder, int i) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    class NotiViewHolder extends RecyclerView.ViewHolder{

        TextView notiText;

        public NotiViewHolder(@NonNull View itemView) {
            super(itemView);

            notiText=(TextView)itemView.findViewById(R.id.notiText);

        }
    }

}
