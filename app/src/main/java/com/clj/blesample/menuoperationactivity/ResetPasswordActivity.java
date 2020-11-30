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

public class ResetPasswordActivity extends AppCompatActivity {

    private EditText resetUserName, resetPassword;
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
        btnReset = (Button) findViewById(R.id.resetBtn);

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String resetEmail = resetUserName.getText().toString().trim();
                String setPassword = resetPassword.getText().toString().trim();

                if (resetEmail.isEmpty() || resetEmail.equals("") || resetEmail.equals(null)) {
                    Toast.makeText(ResetPasswordActivity.this, "Please enter valid email", Toast.LENGTH_LONG).show();
                    resetUserName.findFocus();
                }

                if (setPassword.isEmpty() || setPassword.equals("") || setPassword.equals(null)) {
                    Toast.makeText(ResetPasswordActivity.this, "Please enter valid Password", Toast.LENGTH_LONG).show();
                    resetPassword.findFocus();
                }

                if (validateEmail(resetEmail) && validateEmail(setPassword)) {

                    boolean status = sqliteManager.resetPassword(resetEmail, setPassword);

                    if (status) {
                        Toast.makeText(ResetPasswordActivity.this, "Password Updated Successfully", Toast.LENGTH_LONG).show();

                        Intent intent=new Intent(ResetPasswordActivity.this,LoginActivity.class);
                        startActivity(intent);
                        finish();

                    } else {
                        Toast.makeText(ResetPasswordActivity.this, "Entered email is not avaliable in our database", Toast.LENGTH_LONG).show();
                    }

                }


            }
        });


    }
}