package com.clj.blesample.menuoperationactivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.clj.blesample.MainActivity;
import com.clj.blesample.R;
import com.clj.blesample.sessionmanager.PreferencesUtil;
import com.clj.blesample.utils.LogFile;
import com.clj.blesample.utils.MathUtil;

public class SplashScreenActivity extends AppCompatActivity {

    int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);


        new SplashDownCountDown(1500, 1000).start();

    }

    private void appOpenCount() {



        int appOpenCount=PreferencesUtil.getValueInt(SplashScreenActivity.this,PreferencesUtil.APP_OPEN_COUNT);

        System.out.println("Appcount"+appOpenCount);

        if(appOpenCount>=50){
            //System.exit(1);
            finish();
        }else {


            appOpenCount=appOpenCount+1;
            PreferencesUtil.setValueSInt(SplashScreenActivity.this,PreferencesUtil.APP_OPEN_COUNT,appOpenCount);
            actuallyComesInOnFinish();


        }

    }


    private class SplashDownCountDown extends CountDownTimer {

        SplashDownCountDown(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);


        }

        @Override
        public void onTick(long milliSecond) {

        }

        @Override
        public void onFinish() {

            //appOpenCount();



            Intent intent;

            userId = PreferencesUtil.getValueInt(SplashScreenActivity.this, PreferencesUtil.USER_ID);

            if (userId > 0) {
                intent = new Intent(SplashScreenActivity.this, MainActivity.class);
                startActivity(intent);
                finish();

            } else {
                intent = new Intent(SplashScreenActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();

            }
        }
    }

    private void actuallyComesInOnFinish() {

        Intent intent;

        userId = PreferencesUtil.getValueInt(SplashScreenActivity.this, PreferencesUtil.USER_ID);

        if (userId > 0) {
            intent = new Intent(SplashScreenActivity.this, MainActivity.class);
            startActivity(intent);
            finish();

        } else {
            intent = new Intent(SplashScreenActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();

        }

    }


}