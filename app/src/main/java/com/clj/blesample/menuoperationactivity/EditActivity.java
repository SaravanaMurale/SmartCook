package com.clj.blesample.menuoperationactivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.clj.blesample.R;
import com.clj.blesample.sessionmanager.PreferencesUtil;
import com.clj.blesample.utils.FontUtil;
import com.skyfishjy.library.RippleBackground;

import java.util.ArrayList;

public class EditActivity extends AppCompatActivity {

    Button operateLeftBurner, operateCenterBurner, operateRightBurner, start, cancel;
    TextView operateTimer, operateWhistleCount, operateSub, operateAdd, minute, whistle, burnerSettingsText, flamModeText;

    int timerInMin = 0;
    int whistleInCount = 0;

    TextView miniMinute, miniWhistle;


    byte[] bytes1, byte2;

    boolean timerFlag, whistleFlag;

    String burner = "";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        initView();

        ArrayList<byte[]> byteArrayList = new ArrayList<>();

        Intent intent = getIntent();
        burner = intent.getStringExtra("BURNER");
        byteArrayList = (ArrayList<byte[]>) intent.getSerializableExtra("currentByteArrayList");

       /* int byteSize = intent.getIntExtra("currentByte_size", 0);

        for (int i = 0; i < byteSize; i++) {
            barray.add(intent.getByteArrayExtra("currentByte" + i));
            System.out.println("TotalArraySize" + barray.get(i));
        }

        System.out.println("ArrayListSize" + barray.size());*/

        /*for (int i = 0; i < byteArrayList.size(); i++) {
            System.out.println("DATA" + byteArrayList.get(i));

            bytes1 = byteArrayList.get(0);
            byte2 = byteArrayList.get(1);

        }*/

        //setBurner();

        /*System.out.println("ByteAtZero" + bytes1[0]);
        System.out.println("ByteAtOne" + bytes1[1]);
        System.out.println("ByteAtTwo" + bytes1[2]);
        System.out.println("ByteAtThree" + bytes1[3]);

        System.out.println("ByteAtZero" + byte2[0]);
        System.out.println("ByteAtOne" + byte2[1]);
        System.out.println("ByteAtTwo" + byte2[2]);
        System.out.println("ByteAtThree" + byte2[3]);
        System.out.println("ByteAtFour" + byte2[4]);*/


        backgroundChangeOperation();
/*
        operateTimer.setBackground(getResources().getDrawable(R.drawable.edit_button_border_on));
        miniWhistle.setVisibility(View.INVISIBLE);
        miniMinute.setVisibility(View.VISIBLE);
        operateWhistleCount.setBackground(getResources().getDrawable(R.drawable.edit_button_border_off));

        timerFlag = true;
        whistleFlag = false;
        if (timerInMin > 0) {
            minute.setText("" + timerInMin);
            whistle.setVisibility(View.INVISIBLE);
        }*/

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (timerInMin > 0 && whistleInCount > 0 ) {
                    System.out.println("ReceivedData" + burner + " " + timerInMin + " " + whistleInCount );

                    PreferencesUtil.setValueString(EditActivity.this, PreferencesUtil.BURNER, burner);
                    PreferencesUtil.setValueSInt(EditActivity.this, PreferencesUtil.TIMER_IN_MINUTE, timerInMin);
                    PreferencesUtil.setValueSInt(EditActivity.this, PreferencesUtil.WHISTLE_IN_COUNT, whistleInCount);
                    //PreferencesUtil.setValueSInt(EditActivity.this, PreferencesUtil.FLAME_MODE, flameMode);

                    onBackPressed();

                } else {
                    Toast.makeText(EditActivity.this, "Please Select Time or Whistle", Toast.LENGTH_LONG).show();
                }


                // startBurnerClickListener.onStartClick(burner, timerInMin, whistleInCount, flameMode);

                /*Bundle bundle = new Bundle();
                bundle.putString("edttext", "From Activity");

                CharacteristicListFragment fragobj = new CharacteristicListFragment();
                fragobj.setArguments(bundle);*/


            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    private void setBurner() {

        if (burner.equals("00")) {
            /*rippleEditLeft.startRippleAnimation();
            rippleEditCenter.stopRippleAnimation();
            rippleEditRight.stopRippleAnimation();*/

            System.out.println("LeftBurnerSettings");
            burnerSettingsText.setText("Left Burner Settings");

        } else if (burner.equals("01")) {
           /* rippleEditLeft.stopRippleAnimation();
            rippleEditCenter.startRippleAnimation();
            rippleEditRight.stopRippleAnimation();*/
            System.out.println("CenterBurnerSettings");
            burnerSettingsText.setText("Center Burner Settings");

        } else if (burner.equals("10")) {
            /*rippleEditLeft.stopRippleAnimation();
            rippleEditCenter.stopRippleAnimation();
            rippleEditRight.startRippleAnimation();*/
            System.out.println("RightBurnerSettings");
            burnerSettingsText.setText("Right Burner Settings");
        }
    }


    private void backgroundChangeOperation() {

        operateTimer.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                /*operateTimer.setBackgroundColor(getResources().getColor(R.color.timer_bg));
                operateTimer.setTextColor(getResources().getColor(R.color.black));
                operateWhistleCount.setBackgroundColor(getResources().getColor(R.color.whistle_bg));
                operateWhistleCount.setTextColor(getResources().getColor(R.color.black));*/

                operateTimer.setBackground(getResources().getDrawable(R.drawable.edit_button_border_on));
                miniWhistle.setVisibility(View.INVISIBLE);
                miniMinute.setVisibility(View.VISIBLE);
                operateWhistleCount.setBackground(getResources().getDrawable(R.drawable.edit_button_border_off));
                //minuteOrWhistleCount.setText("5 minute");

                timerFlag = true;
                whistleFlag = false;

                minute.setVisibility(View.VISIBLE);
                whistle.setVisibility(View.INVISIBLE);

                if (timerInMin > 0) {

                    minute.setText("" + timerInMin);
                } else {

                    minute.setText("" + timerInMin);
                }

            }
        });

        operateWhistleCount.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                /*operateWhistleCount.setBackgroundColor(getResources().getColor(R.color.timer_bg));
                operateWhistleCount.setTextColor(getResources().getColor(R.color.black));
                operateTimer.setBackgroundColor(getResources().getColor(R.color.whistle_bg));
                operateTimer.setTextColor(getResources().getColor(R.color.black));*/
                //minuteOrWhistleCount.setText("1");

                operateWhistleCount.setBackground(getResources().getDrawable(R.drawable.edit_button_border_on));
                miniWhistle.setVisibility(View.VISIBLE);
                miniMinute.setVisibility(View.INVISIBLE);
                operateTimer.setBackground(getResources().getDrawable(R.drawable.edit_button_border_off));


                timerFlag = false;
                whistleFlag = true;

                minute.setVisibility(View.INVISIBLE);
                whistle.setVisibility(View.VISIBLE);

                if (whistleInCount > 0) {
                    whistle.setText("" + whistleInCount);
                } else {
                    minute.setText("" + whistleInCount);
                }

            }
        });


        operateAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                operateAdd.setBackgroundColor(getResources().getColor(R.color.burner_on_green));
                operateAdd.setTextColor(getResources().getColor(R.color.black));
                operateSub.setBackground(getResources().getDrawable(R.drawable.rounded_border));
                operateSub.setTextColor(getResources().getColor(R.color.black));


                operateSub.setEnabled(true);

                if (timerFlag) {

                    timerInMin = timerInMin + 1;
                    minute.setText("" + timerInMin);

                } else if (whistleFlag) {
                    whistleInCount = whistleInCount + 1;
                    whistle.setText("" + whistleInCount);
                }

                //b1TimerClickListener.onB1TimerClick();
            }
        });

        operateSub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                operateSub.setBackgroundColor(getResources().getColor(R.color.burner_on_green));
                operateSub.setTextColor(getResources().getColor(R.color.black));
                operateAdd.setBackground(getResources().getDrawable(R.drawable.rounded_border));
                operateAdd.setTextColor(getResources().getColor(R.color.black));


                if (timerFlag) {

                    if (timerInMin > 0) {
                        operateSub.setEnabled(true);
                        timerInMin = timerInMin - 1;

                        if (timerInMin > 0) {
                            minute.setText("" + timerInMin);
                        } else if (timerInMin == 0) {
                            whistle.setText("" + timerInMin);

                            if (whistleInCount == 0 && timerInMin == 0) {
                                operateSub.setEnabled(false);
                                operateSub.setBackgroundColor(getResources().getColor(R.color.white));
                            }

                        }
                    } else if (timerInMin == 0) {
                        minute.setText("" + timerInMin);
                        if (whistleInCount == 0 && timerInMin == 0) {
                            operateSub.setEnabled(false);
                            operateSub.setBackgroundColor(getResources().getColor(R.color.white));
                        } else {
                            operateSub.setEnabled(true);
                        }

                    }

                } else if (whistleFlag) {
                    if (whistleInCount > 0) {
                        whistleInCount = whistleInCount - 1;

                        if (whistleInCount > 0) {
                            whistle.setText("" + whistleInCount);
                        } else if (whistleInCount == 0) {
                            whistle.setText("" + whistleInCount);

                            if (timerInMin == 0 && whistleInCount == 0) {
                                operateSub.setEnabled(false);
                                operateSub.setBackgroundColor(getResources().getColor(R.color.white));
                            }

                        }
                    } else if (whistleInCount == 0) {
                        whistle.setText("" + whistleInCount);
                        if (timerInMin == 0 && whistleInCount == 0) {
                            operateSub.setEnabled(false);
                            operateSub.setBackgroundColor(getResources().getColor(R.color.white));
                        } else {
                            operateSub.setEnabled(true);
                        }


                    }
                }

            }
        });



    }

    private void initView() {


        operateTimer = (TextView) findViewById(R.id.timerText);
        //operateTimer.setTypeface(FontUtil.getOctinPrisonFont(EditActivity.this));
        operateWhistleCount = (TextView) findViewById(R.id.whistleText);
        //operateWhistleCount.setTypeface(FontUtil.getOctinPrisonFont(EditActivity.this));
        minute = (TextView) findViewById(R.id.minute);
        whistle = (TextView) findViewById(R.id.whistle);

        operateSub = (TextView) findViewById(R.id.sub);
        operateAdd = (TextView) findViewById(R.id.add);



        start = (Button) findViewById(R.id.start);
        cancel = (Button) findViewById(R.id.cancel);

        miniMinute = (TextView) findViewById(R.id.miniMinute);
        miniWhistle = (TextView) findViewById(R.id.miniwhistle);

        //start.setTypeface(FontUtil.getOctinPrisonFont(EditActivity.this));
        //cancel.setTypeface(FontUtil.getOctinPrisonFont(EditActivity.this));


        burnerSettingsText = (TextView) findViewById(R.id.burnerSettingsText);
        //burnerSettingsText.setTypeface(FontUtil.getOctinPrisonFont(EditActivity.this));
        flamModeText = (TextView) findViewById(R.id.flamMode);
        //flamModeText.setTypeface(FontUtil.getOctinPrisonFont(EditActivity.this));

        //minute.setTypeface(FontUtil.getOctinPrisonFont(EditActivity.this));
        //whistle.setTypeface(FontUtil.getOctinPrisonFont(EditActivity.this));


    }
}