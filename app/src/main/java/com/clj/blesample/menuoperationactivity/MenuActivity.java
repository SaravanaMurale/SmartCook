package com.clj.blesample.menuoperationactivity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.clj.blesample.R;

public class MenuActivity extends AppCompatActivity {

    RelativeLayout profileSettingBlock, recipeMenuBlock, productServiceBlock, settingsBlock, contactUsBlock, signOutBlock, preSetMenuBlock;

    public static final int REQUEST_PHONE_CALL = 121;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);


        initView();
        profileSettingBlock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MenuActivity.this, ProfileSettingsActivity.class);
                startActivity(intent);


            }
        });

        productServiceBlock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(MenuActivity.this,ProductServiceActivity.class);
                startActivity(intent);

            }
        });

        signOutBlock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        settingsBlock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent settingsActivity = new Intent(MenuActivity.this, SettingsActivity.class);
                startActivity(settingsActivity);

            }
        });



        //Customercare Service call and message
        contactUsBlock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(MenuActivity.this);

                builder.setTitle("Customer Service Assistance");

                builder.setMessage("Do you want to?");

                builder.setPositiveButton("Call", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        String phone = "9445903393";
                        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
                        if (ContextCompat.checkSelfPermission(MenuActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(MenuActivity.this, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_PHONE_CALL);
                        } else {
                            startActivity(intent);
                        }
                    }
                });


                builder.setNegativeButton("Message", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Intent smsIntent = new Intent(Intent.ACTION_VIEW);
                        smsIntent.setType("vnd.android-dir/mms-sms");
                        //Key both address and sms_body should not change

                        smsIntent.putExtra("address", "9123521374");
                        smsIntent.putExtra("sms_body", "");
                        startActivity(smsIntent);
                    }
                });


                builder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.cancel();
                    }
                });

                AlertDialog diag = builder.create();
                diag.show();


            }
        });




        /*preSetMenuBlock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callPreSetMenuDialog();
            }
        });*/


    }

    private void callPreSetMenuDialog() {


    }

    private void initView() {

        profileSettingBlock = (RelativeLayout) findViewById(R.id.profileSettingBlock);
        recipeMenuBlock = (RelativeLayout) findViewById(R.id.recipeMenuBlock);
        productServiceBlock = (RelativeLayout) findViewById(R.id.productServiceBlock);
        settingsBlock = (RelativeLayout) findViewById(R.id.settingsBlock);
        contactUsBlock = (RelativeLayout) findViewById(R.id.contactUsBlock);
        signOutBlock = (RelativeLayout) findViewById(R.id.signOutBlock);


    }
}
