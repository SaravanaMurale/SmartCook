package com.clj.blesample.menuoperationactivity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.clj.blesample.R;
import com.clj.blesample.utils.MathUtil;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileSettingsActivity extends AppCompatActivity {

    RelativeLayout profileBlock,emailBlock,mobileBlock,changePasswordBlock;

    CircleImageView profilePic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_settings);

        profilePic=(CircleImageView)findViewById(R.id.profilePic);

        profileBlock=(RelativeLayout)findViewById(R.id.profileBlock);
        emailBlock=(RelativeLayout)findViewById(R.id.emailBlock);
        mobileBlock=(RelativeLayout)findViewById(R.id.mobileBlock);
        changePasswordBlock=(RelativeLayout)findViewById(R.id.changePasswordBlock);

        profileBlock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openDialog("Enter Your Name", 1);

            }
        });

        emailBlock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openDialog("Enter Email", 1);

            }
        });

        mobileBlock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog("Enter Your Name", 1);
            }
        });

        changePasswordBlock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openDialog("Enter Password", 1);

            }
        });

        profilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
                    if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_DENIED){
                        String[] permission={Manifest.permission.READ_EXTERNAL_STORAGE};

                        requestPermissions(permission, MathUtil.PERMISSION_CODE);
                    }
                    else {
                        pickImageFromGallery();
                    }
                }
                else {
                    pickImageFromGallery();
                }

            }
        });


    }

    private void pickImageFromGallery() {

        Intent intent=new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, MathUtil.IMAGE_PICK_CODE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(resultCode==RESULT_OK && requestCode==MathUtil.IMAGE_PICK_CODE){
            profilePic.setImageURI(data.getData());
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case MathUtil.PERMISSION_CODE:{
                if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_DENIED){
                    pickImageFromGallery();
                }else {
                    Toast.makeText(ProfileSettingsActivity.this,"Permission Denied",Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    private void openDialog(String hintData, int i) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater layoutInflater = this.getLayoutInflater();
        final View view = layoutInflater.inflate(R.layout.layout_dialog_userprofile, null);

        final EditText update = (EditText) view.findViewById(R.id.updateName);
        update.setHint(hintData);

        builder.setView(view);

        builder.setPositiveButton("UPDATE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Toast.makeText(ProfileSettingsActivity.this, "Updated", Toast.LENGTH_LONG).show();



            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();

            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();



    }
}