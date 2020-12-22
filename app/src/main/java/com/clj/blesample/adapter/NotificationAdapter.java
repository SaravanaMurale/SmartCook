package com.clj.blesample.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder> {

    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder notificationViewHolder, int i) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    class NotificationViewHolder extends RecyclerView.ViewHolder {

        public NotificationViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

}
