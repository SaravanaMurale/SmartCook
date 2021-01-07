package com.clj.blesample.menuoperationactivity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.clj.blesample.R;
import com.clj.blesample.databasemanager.SqliteManager;
import com.clj.blesample.model.StoreImageDTO;
import com.clj.blesample.model.UserDTO;
import com.clj.blesample.utils.MathUtil;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileSettingsActivity extends AppCompatActivity {

    RelativeLayout profileBlock, emailBlock, mobileBlock, changePasswordBlock;

    CircleImageView profilePic;
    Uri imageFilePath;
    Bitmap imageToStore;

    SqliteManager sqliteManager;

    TextView userName, email, mobile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_settings);

        sqliteManager = new SqliteManager(this);

        profilePic = (CircleImageView) findViewById(R.id.profilePic);

        userName = (TextView) findViewById(R.id.userName);
        email = (TextView) findViewById(R.id.email);
        mobile = (TextView) findViewById(R.id.mobile);

        profileBlock = (RelativeLayout) findViewById(R.id.profileBlock);
        emailBlock = (RelativeLayout) findViewById(R.id.emailBlock);
        mobileBlock = (RelativeLayout) findViewById(R.id.mobileBlock);
        changePasswordBlock = (RelativeLayout) findViewById(R.id.changePasswordBlock);

        getImageFromSqliteDB();

        getUserDetails();

        profileBlock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openDialog("Enter Your Name", 1);

            }
        });

        emailBlock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openDialog("Enter Email", 2);

            }
        });

        mobileBlock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog("Mobile Number", 3);
            }
        });

        changePasswordBlock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openDialog("Enter Password", 4);

            }
        });

        profilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                        String[] permission = {Manifest.permission.READ_EXTERNAL_STORAGE};

                        requestPermissions(permission, MathUtil.PERMISSION_CODE);
                    } else {
                        pickImageFromGallery();
                    }
                } else {
                    pickImageFromGallery();
                }

            }
        });


    }

    private void getUserDetails() {

        List<UserDTO> userDTOList = sqliteManager.getUserDetails();

        if (userDTOList != null) {
            userName.setText(userDTOList.get(0).getUserName());
            email.setText(userDTOList.get(0).getUserEmail());
            mobile.setText(userDTOList.get(0).getUserMobile());

        } else {
            Toast.makeText(ProfileSettingsActivity.this, "Please Signup", Toast.LENGTH_LONG).show();
        }


    }

    private void pickImageFromGallery() {

        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, MathUtil.IMAGE_PICK_CODE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK && requestCode == MathUtil.IMAGE_PICK_CODE) {
            // profilePic.setImageURI(data.getData());

            imageFilePath = data.getData();
            try {
                imageToStore = MediaStore.Images.Media.getBitmap(getContentResolver(), imageFilePath);
                profilePic.setImageBitmap(imageToStore);
                sqliteManager.storeImage(new StoreImageDTO("Murali", imageToStore));


            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void getImageFromSqliteDB() {

        StoreImageDTO storeImageDTO = sqliteManager.getImage();

        if (storeImageDTO.getImage() == null) {
            profilePic.setImageDrawable(getResources().getDrawable(R.drawable.person));
            System.out.println("ImageIsNotAvaliable");
        } else if (storeImageDTO.getImage() != null) {
            profilePic.setImageBitmap(storeImageDTO.getImage());

        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MathUtil.PERMISSION_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_DENIED) {
                    pickImageFromGallery();
                } else {
                    //Toast.makeText(ProfileSettingsActivity.this, "Permission Denied", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    private void openDialog(String hintData, final int i) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater layoutInflater = this.getLayoutInflater();
        final View view = layoutInflater.inflate(R.layout.layout_dialog_userprofile, null);

        final EditText update = (EditText) view.findViewById(R.id.updateName);
        update.setHint(hintData);

        builder.setView(view);

        builder.setPositiveButton("UPDATE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if (i == 1) {
                    //update name

                    boolean status = sqliteManager.updateUserName(update.getText().toString());
                    if (status) {
                        Toast.makeText(ProfileSettingsActivity.this, "Updated UserName", Toast.LENGTH_LONG).show();

                        getUserDetails();
                    }

                } else if (i == 2) {
                    //update Email
                    boolean status = sqliteManager.updateEmail(update.getText().toString());
                    if (status) {
                        Toast.makeText(ProfileSettingsActivity.this, "Updated Email", Toast.LENGTH_LONG).show();

                        getUserDetails();
                    }

                } else if (i == 3) {

                    //update mobile

                    boolean status = sqliteManager.updateMobile(update.getText().toString());
                    if (status) {
                        Toast.makeText(ProfileSettingsActivity.this, "Updated Mobile Number", Toast.LENGTH_LONG).show();

                        getUserDetails();
                    }


                } else if (i == 4) {
                    //update password

                    boolean status = sqliteManager.updatePassword(update.getText().toString());
                    if (status) {
                        Toast.makeText(ProfileSettingsActivity.this, "Updated Password", Toast.LENGTH_LONG).show();

                        getUserDetails();
                    }
                }


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