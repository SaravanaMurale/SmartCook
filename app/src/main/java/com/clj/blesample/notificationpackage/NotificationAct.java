package com.clj.blesample.notificationpackage;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.clj.blesample.R;
import com.clj.blesample.databasemanager.SqliteManager;
import com.clj.blesample.menuoperationactivity.NotificationActivity;

import java.util.ArrayList;
import java.util.List;

public class NotificationAct extends AppCompatActivity {

    private RecyclerView notiRecyclerView;
    NotiAdapter notiAdapter;

    List<GetAllNotificationListDTO> getAllNotificationListDTOList;

    SqliteManager sqliteManager;

    List<RightNotiDTO> rightNotiDTOList;
    List<LeftNotiDTO> leftNotiDTOList;
    List<CenterNotiDTO> centerNotiDTOList;

    GetAllNotificationListDTO getAllNotificationListDTO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification2);

        notiRecyclerView=(RecyclerView)findViewById(R.id.notiRecyclerView);
        notiRecyclerView.setLayoutManager(new LinearLayoutManager(NotificationAct.this));
        notiRecyclerView.setHasFixedSize(true);

        getAllNotificationListDTOList=new ArrayList<>();
        notiAdapter=new NotiAdapter(NotificationAct.this,getAllNotificationListDTOList);
        notiRecyclerView.setAdapter(notiAdapter);

        sqliteManager=new SqliteManager(NotificationAct.this);




    }




}