package com.clj.blesample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.sdsmdg.harjot.crollerTest.Croller;
import com.sdsmdg.harjot.crollerTest.OnCrollerChangeListener;
import com.triggertrap.seekarc.SeekArc;

public class CircularActivity extends AppCompatActivity {


    Croller croller;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_circular);

        croller=(Croller)findViewById(R.id.crollerLeft);

        croller.setOnCrollerChangeListener(new OnCrollerChangeListener() {
            @Override
            public void onProgressChanged(Croller croller, int progress) {

                System.out.println("CrollerChangeListener"+progress);

            }

            @Override
            public void onStartTrackingTouch(Croller croller) {

            }

            @Override
            public void onStopTrackingTouch(Croller croller) {

            }
        });


    }
}