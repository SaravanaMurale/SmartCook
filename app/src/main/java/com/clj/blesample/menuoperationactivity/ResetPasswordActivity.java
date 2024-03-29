package com.clj.blesample.menuoperationactivity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.clj.blesample.R;
import com.clj.blesample.databasemanager.SqliteManager;

import static com.clj.blesample.utils.MathUtil.validateEmail;
import static com.clj.blesample.utils.MathUtil.validateLoginEmailOrPassword;
import static com.clj.blesample.utils.MathUtil.validatePassword;

public class ResetPasswordActivity extends AppCompatActivity {

    private EditText resetUserName, resetPassword,reset_confirm_password;
    private Button btnReset;

    private SqliteManager sqliteManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        sqliteManager = new SqliteManager(ResetPasswordActivity.this);

        initView();
    }

    private void initView() {

        resetUserName = (EditText) findViewById(R.id.reset_email);
        resetPassword = (EditText) findViewById(R.id.reset_password);
        reset_confirm_password=(EditText)findViewById(R.id.reset_confirm_password);
        btnReset = (Button) findViewById(R.id.resetBtn);

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String resetEmail = resetUserName.getText().toString().trim();
                String setPassword = resetPassword.getText().toString().trim();
                String resetConfirmPassword=reset_confirm_password.getText().toString().trim();

                if (resetEmail.isEmpty() || resetEmail.equals("") || resetEmail.equals(null)) {
                    Toast.makeText(ResetPasswordActivity.this, "Please Enter Email/Mobile Number", Toast.LENGTH_LONG).show();
                    resetUserName.findFocus();
                    return;
                }

                if (setPassword.isEmpty() || setPassword.equals("") || setPassword.equals(null)) {
                    Toast.makeText(ResetPasswordActivity.this, "Please Enter Password", Toast.LENGTH_LONG).show();
                    resetPassword.findFocus();
                    return;
                }

                if (resetConfirmPassword.isEmpty() || resetConfirmPassword.equals("") || resetConfirmPassword.equals(null)) {
                    Toast.makeText(ResetPasswordActivity.this, "Please Enter Confirm Password", Toast.LENGTH_LONG).show();
                    resetPassword.findFocus();
                    return;
                }

                if (!validatePassword(setPassword)) {
                    Toast.makeText(ResetPasswordActivity.this, "Password should have minimum 6 characters", Toast.LENGTH_LONG).show();
                    resetPassword.findFocus();
                    return;
                }

                if(!setPassword.equals(resetConfirmPassword)){
                    Toast.makeText(ResetPasswordActivity.this, "Password and Confirm Password Mismatch", Toast.LENGTH_LONG).show();
                    return;
                }


                String mobileNumberFromDB=sqliteManager.checkMobileNumber(resetEmail);
                String emailFromDB=sqliteManager.checkEmail(resetEmail);

                if(mobileNumberFromDB.equals("NULL") &&emailFromDB.equals("NULL")){
                    Toast.makeText(ResetPasswordActivity.this,"You are not registered,  Please Signup",Toast.LENGTH_LONG).show();
                    return;
                }

                if (!mobileNumberFromDB.equals("NULL") || !emailFromDB.equals("NULL") && setPassword.equals(resetConfirmPassword)  ) {

                    boolean status = sqliteManager.resetPassword(resetEmail, setPassword);

                    if (status) {
                        Toast.makeText(ResetPasswordActivity.this, "Password Updated Successfully", Toast.LENGTH_LONG).show();

                        Intent intent=new Intent(ResetPasswordActivity.this,LoginActivity.class);
                        startActivity(intent);
                        finish();

                    } else {
                        Toast.makeText(ResetPasswordActivity.this, "You are not registered,  Please Signup", Toast.LENGTH_LONG).show();
                    }

                }


            }
        });


    }
}