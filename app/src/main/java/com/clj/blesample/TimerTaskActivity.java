package com.clj.blesample;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;


import com.agrawalsuneet.dotsloader.loaders.CircularDotsLoader;

import java.util.Timer;
import java.util.TimerTask;

public class TimerTaskActivity extends AppCompatActivity {

    Button startTimer,viewButton;

    Timer timer;
    TimerTask timerTask;

    //we are going to use a handler to be able to run in our TimerTask
    final Handler handler = new Handler();
   /* Handler handler;
    Runnable r;*/



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer_task);
        viewButton=(Button)findViewById(R.id.viewButton);
        startTimer=(Button)findViewById(R.id.startTimer);

        CircularDotsLoader loader = new CircularDotsLoader(this);
        loader.setBigCircleRadius(80);
        loader.setRadius(100);
        loader.setAnimDur(300);
        //loader.setFirstShadowColor(ContextCompat.getColor(this, R.color.blue));
        //loader.setSecondShadowColor(ContextCompat.getColor(this, R.color.yellow));




        startTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //stoptimertask();
                //startTimer();
            }
        });


    }


    private void startTimer() {

        viewButton.setVisibility(View.VISIBLE);
        //set a new Timer
       // timer = new Timer();

        //initialize the TimerTask's job
        //initializeTimerTask();

        //schedule the timer, after the first 5000ms the TimerTask will run every 10000ms
        //timer.schedule(timerTask,3000);
        //timer.schedule(timerTask, 1000, 0); //
    }

    private void initializeTimerTask() {

        timerTask = new TimerTask() {

            @Override
            public void run() {
                viewButton.setVisibility(View.INVISIBLE);
            }
        };

    }

    public void stoptimertask() {
        //stop the timer, if it's not already null
        if (timer != null) {
            timer.cancel();
            viewButton.setVisibility(View.VISIBLE);
            timer = null;
        }
    }


    /*@Override
    public void onUserInteraction() {
        super.onUserInteraction();

        stopHandler();//stop first and then start
        startHandler();
    }

    public void stopHandler() {
        handler.removeCallbacks(r);
    }
    public void startHandler() {
        handler.postDelayed(r, 3000); //for 5 minutes
    }*/

      /*handler = new Handler();
        r = new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                Toast.makeText(TimerTaskActivity.this, "user is inactive from last 3 minutes", Toast.LENGTH_SHORT).show();
            }
        };
        startHandler();*/

}