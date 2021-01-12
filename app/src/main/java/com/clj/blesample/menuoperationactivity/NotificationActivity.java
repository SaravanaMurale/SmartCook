package com.clj.blesample.menuoperationactivity;

import android.app.Dialog;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.clj.blesample.R;
import com.clj.blesample.adapter.NotificationAdapter;
import com.clj.blesample.databasemanager.SqliteManager;
import com.clj.blesample.model.NotificationId;
import com.clj.blesample.model.NotificationResponseDTO;
import com.clj.blesample.notificationpackage.NotificationAct;
import com.clj.blesample.utils.MathUtil;

import java.util.ArrayList;
import java.util.List;

public class NotificationActivity extends AppCompatActivity implements NotificationAdapter.DeleteNotiListener {

    RecyclerView notificationRecyclerView;

    NotificationAdapter notificationAdapter;

    List<NotificationResponseDTO> notificationResponseDTOList;
    SqliteManager sqliteManager;

    Dialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);




        notificationRecyclerView = (RecyclerView) findViewById(R.id.notificationRecyclerView);
        notificationRecyclerView.setLayoutManager(new LinearLayoutManager(NotificationActivity.this));
        notificationRecyclerView.setHasFixedSize(true);

        notificationResponseDTOList = new ArrayList<>();
        notificationAdapter = new NotificationAdapter(NotificationActivity.this, notificationResponseDTOList, NotificationActivity.this);
        notificationRecyclerView.setAdapter(notificationAdapter);

        sqliteManager = new SqliteManager(NotificationActivity.this);

        System.out.println("NotificationCalled");



        deleteRecordsMoreThanHundred();


       /* new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

            }
        },1000);
*/

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                getAllNotifications();
            }
        },1000);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                updateReadStatus();
            }
        },500);




    }

    private void deleteRecordsMoreThanHundred() {

        Dialog dialog=new Dialog(NotificationActivity.this);

        dialog=MathUtil.showProgressBar(NotificationActivity.this);

        List<NotificationId> deleteRecord = sqliteManager.getAllNotificationsToDelte();

        System.out.println("DeleteRecordSize"+deleteRecord.size());


         if(deleteRecord.size()>=120 ){
            sqliteManager.deleteMoreThanHundred(50,deleteRecord);
        }

        MathUtil.dismisProgressBar(NotificationActivity.this,dialog);


    }


    private void getAllNotifications() {

        Dialog dialog=new Dialog(NotificationActivity.this);

        dialog=MathUtil.showProgressBar(NotificationActivity.this);

        notificationResponseDTOList = sqliteManager.getAllNotifications();


        if (notificationResponseDTOList == null) {
            Toast.makeText(NotificationActivity.this, "Notification List Is Empty", Toast.LENGTH_LONG).show();
        } else {
            notificationAdapter.dataSet(notificationResponseDTOList);

            Toast.makeText(NotificationActivity.this, "Notification List Has Data", Toast.LENGTH_LONG).show();
        }

        MathUtil.dismisProgressBar(NotificationActivity.this,dialog);

    }

    private void updateReadStatus() {

        Dialog dialog=new Dialog(NotificationActivity.this);

        dialog=MathUtil.showProgressBar(NotificationActivity.this);

        boolean status = sqliteManager.updateReadStatus();
        System.out.println("UpdatedStatus " + status);

        MathUtil.dismisProgressBar(NotificationActivity.this,dialog);
    }

    @Override
    public void deleteNoti(NotificationResponseDTO notificationResponseDTO) {



        Dialog dialog=new Dialog(NotificationActivity.this);

        dialog=MathUtil.showProgressBar(NotificationActivity.this);


        boolean deleteStatus= sqliteManager.deleteNotiById(notificationResponseDTO.getNotiColumnID());

       if(deleteStatus){
           getAllNotifications();
       }

       MathUtil.dismisProgressBar(NotificationActivity.this,dialog);




    }
}