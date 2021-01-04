package com.clj.blesample.menuoperationactivity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.clj.blesample.R;
import com.clj.blesample.adapter.NotificationAdapter;
import com.clj.blesample.databasemanager.SqliteManager;
import com.clj.blesample.model.NotificationResponseDTO;

import java.util.ArrayList;
import java.util.List;

public class NotificationActivity extends AppCompatActivity {

    RecyclerView notificationRecyclerView;

    NotificationAdapter notificationAdapter;

    List<NotificationResponseDTO> notificationResponseDTOList;
    SqliteManager sqliteManager;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);



        notificationRecyclerView=(RecyclerView)findViewById(R.id.notificationRecyclerView);
        notificationRecyclerView.setLayoutManager(new LinearLayoutManager(NotificationActivity.this));
        notificationRecyclerView.setHasFixedSize(true);

        notificationResponseDTOList=new ArrayList<>();
        notificationAdapter=new NotificationAdapter(NotificationActivity.this,notificationResponseDTOList);
        notificationRecyclerView.setAdapter(notificationAdapter);

        sqliteManager=new SqliteManager(NotificationActivity.this);

        System.out.println("NotificationCalled");

        getAllNotifications();

        updateReadStatus();



    }


    private void getAllNotifications() {

        notificationResponseDTOList = sqliteManager.getAllNotifications();


        if (notificationResponseDTOList == null) {
            Toast.makeText(NotificationActivity.this, "Notification List Is Empty", Toast.LENGTH_LONG).show();
        } else {
            notificationAdapter.dataSet(notificationResponseDTOList);
            Toast.makeText(NotificationActivity.this, "Notification List Has Data", Toast.LENGTH_LONG).show();
        }

    }

    private void updateReadStatus() {
        sqliteManager.updateReadStatus();
    }

}