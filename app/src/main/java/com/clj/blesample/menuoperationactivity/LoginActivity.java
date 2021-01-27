package com.clj.blesample.menuoperationactivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.clj.blesample.MainActivity;
import com.clj.blesample.R;
import com.clj.blesample.databasemanager.SqliteManager;
import com.clj.blesample.sessionmanager.PreferencesUtil;
import com.clj.blesample.utils.LogFile;
import com.clj.blesample.utils.PermissionUtils;

import static com.clj.blesample.utils.MathUtil.LOCATION_PERMISSION_REQUEST_CODE;
import static com.clj.blesample.utils.MathUtil.validateEmail;
import static com.clj.blesample.utils.MathUtil.validateLoginEmailOrPassword;
import static com.clj.blesample.utils.MathUtil.validateMobileNumber;
import static com.clj.blesample.utils.MathUtil.validatePassword;

public class LoginActivity extends AppCompatActivity {

    private EditText loginUserName, loginPassword;
    private Button btnLogin;
    private TextView signUp, btn_Reset;

    SqliteManager sqliteManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sqliteManager = new SqliteManager(LoginActivity.this);

        initView();

        loginUserName.findFocus();
    }

    private void initView() {

        loginUserName = (EditText) findViewById(R.id.login_email);
        loginPassword = (EditText) findViewById(R.id.login_password);
        signUp = (TextView) findViewById(R.id.signUp);
        btn_Reset = (TextView) findViewById(R.id.btn_Reset);

        btnLogin = (Button) findViewById(R.id.loginBtn);

        //loginUserName.addTextChangedListener(new MyTextWatcher(loginUserName));
        //loginPassword.addTextChangedListener(new MyTextWatcher(loginPassword));

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);


            }
        });

        btn_Reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(LoginActivity.this, ResetPasswordActivity.class);
                startActivity(intent);
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //validate user from SQLITE

                String email = loginUserName.getText().toString().trim();
                String password = loginPassword.getText().toString().trim();

                if (email.isEmpty() || email.equals("") || email.equals(null) ) {
                    Toast.makeText(LoginActivity.this, "Please Enter Email/Mobile Number", Toast.LENGTH_LONG).show();
                    loginUserName.findFocus();
                    return;
                }

                if (password.isEmpty() || password.equals("") || password.equals(null) ) {
                    Toast.makeText(LoginActivity.this, "Please Enter Password", Toast.LENGTH_LONG).show();
                    loginPassword.findFocus();
                    return;
                }

                if(!validatePassword(password)){
                    Toast.makeText(LoginActivity.this, "Password should have minimum 6 characters", Toast.LENGTH_LONG).show();
                    loginPassword.findFocus();
                    return;
                }

                String mobileNumberFromDB=sqliteManager.checkMobileNumber(email);
                String emailFromDB=sqliteManager.checkEmail(email);

                if(mobileNumberFromDB.equals("NULL") &&emailFromDB.equals("NULL")){
                   Toast.makeText(LoginActivity.this,"You are not registered,  Please Signup",Toast.LENGTH_LONG).show();
                    return;
                }

                /*if(!mobileNumberFromDB.equals("NULL") || !emailFromDB.equals("NULL")){
                    System.out.println("CheckInDB");
                    return;
                }*/


                if (!mobileNumberFromDB.equals("NULL") || !emailFromDB.equals("NULL")) {
                    //Email or Mobile
                    String userName = sqliteManager.validateLoginUser(email, password);

                    //checkWriteStoreAgePermission();

                    PreferencesUtil.setValueString(LoginActivity.this, PreferencesUtil.USER_NAME, userName);

                    if (!userName.equals("empty")) {
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        killActivity();


                    } else {
                        Toast.makeText(LoginActivity.this, "Incorrect Password", Toast.LENGTH_LONG).show();
                    }

                }


            }
        });

    }

    private void killActivity() {

        finish();
    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void checkWriteStoreAgePermission() {

        if (!PermissionUtils.hasPermission(LoginActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

            PermissionUtils.requestPermissions(LoginActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, LOCATION_PERMISSION_REQUEST_CODE);


        } else {

            if (PermissionUtils.hasPermission(LoginActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                LogFile.addLogInFile("MainActivity ");
                System.out.println("Logfilecreatedsuccessfully");
            }
        }


    }

    private class MyTextWatcher implements TextWatcher {

        private View view;

        private MyTextWatcher(View view) {
            this.view = view;
        }

        @SuppressLint("ResourceAsColor")
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {


        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

            String email = loginUserName.getText().toString().trim();
            String password = loginPassword.getText().toString().trim();

            btnLogin.setEnabled(validatePassword(email) && validatePassword(password));

            if (btnLogin.isEnabled()) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    btnLogin.setBackground(getDrawable(R.drawable.burner_vessel_on));

                }
            } else if (!btnLogin.isEnabled()) {
                btnLogin.setEnabled(false);

            }


        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }


}