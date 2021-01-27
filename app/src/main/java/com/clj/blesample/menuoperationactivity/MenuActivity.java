package com.clj.blesample.menuoperationactivity;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.clj.blesample.R;
import com.clj.blesample.sessionmanager.PreferencesUtil;

public class MenuActivity extends AppCompatActivity {

    RelativeLayout profileSettingBlock, gasConsumptionBlock, recipeMenuBlock, productServiceBlock, settingsBlock, contactUsBlock, signOutBlock, preSetMenuBlock,getInTocuBlock;

    public static final int REQUEST_PHONE_CALL = 121;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);


        initView();
        profileSettingBlock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MenuActivity.this, ProfileSettingsActivity.class);
                startActivity(intent);


            }
        });

        gasConsumptionBlock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this, GCPDateSelectActivity.class);
                startActivity(intent);
            }
        });

        productServiceBlock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MenuActivity.this, GetInTouchActivity.class);
                startActivity(intent);

            }
        });

        getInTocuBlock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(MenuActivity.this,GetInTouchActivity.class);
                startActivity(intent);

            }
        });



        signOutBlock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(MenuActivity.this);

                builder.setTitle("Signout");

                builder.setMessage("Are you sure want to signout?");

                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        signoutFromDevice();
                    }
                });

                builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();

                    }
                });

                AlertDialog diag = builder.create();
                diag.show();

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

                        String phone = "9940000005";
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

                        smsIntent.putExtra("address", "9940000005");
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

    private void signoutFromDevice() {

        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        if (adapter.isEnabled()){
            adapter.disable();
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                PreferencesUtil.clearAll(MenuActivity.this);
            }
        },500);


        Intent intent = new Intent(MenuActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();

    }

    private void callPreSetMenuDialog() {


    }

    private void initView() {

        profileSettingBlock = (RelativeLayout) findViewById(R.id.profileSettingBlock);
        gasConsumptionBlock = (RelativeLayout) findViewById(R.id.gasConsumptionBlock);
        recipeMenuBlock = (RelativeLayout) findViewById(R.id.recipeMenuBlock);
        productServiceBlock = (RelativeLayout) findViewById(R.id.productServiceBlock);
        settingsBlock = (RelativeLayout) findViewById(R.id.settingsBlock);
        contactUsBlock = (RelativeLayout) findViewById(R.id.contactUsBlock);
        getInTocuBlock=(RelativeLayout)findViewById(R.id.aboutUsBlock);
        signOutBlock = (RelativeLayout) findViewById(R.id.signOutBlock);


    }
}
