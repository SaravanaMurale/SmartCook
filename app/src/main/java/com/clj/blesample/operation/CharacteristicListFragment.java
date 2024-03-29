package com.clj.blesample.operation;


import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Dialog;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.clj.blesample.R;
import com.clj.blesample.comm.ObserverManager;
import com.clj.blesample.databasemanager.SqliteManager;
import com.clj.blesample.menuoperationactivity.MenuActivity;
import com.clj.blesample.menuoperationactivity.NotificationActivity;
import com.clj.blesample.menuoperationactivity.ProfileSettingsActivity;
import com.clj.blesample.model.NotificationResponseDTO;
import com.clj.blesample.model.StoreImageDTO;
import com.clj.blesample.model.UserDTO;
import com.clj.blesample.sessionmanager.PreferencesUtil;
import com.clj.blesample.utils.AnimationUtil;
import com.clj.blesample.utils.FontUtil;
import com.clj.blesample.utils.MathUtil;
import com.clj.fastble.BleManager;
import com.clj.fastble.callback.BleNotifyCallback;
import com.clj.fastble.callback.BleWriteCallback;
import com.clj.fastble.data.BleDevice;
import com.clj.fastble.exception.BleException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import de.hdodenhof.circleimageview.CircleImageView;

@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
public class CharacteristicListFragment extends Fragment {


    private ResultAdapter mResultAdapter;

    //MyCode
    BluetoothGattCharacteristic characteristic;
    List<Integer> propList = new ArrayList<>();
    List<String> propNameList = new ArrayList<>();


    int SIZE_OF_CHARACTERISTIC = 0;

    int currentApiVersion;

    CircleImageView seletedUserProfile;
    ImageView selectBluetoothStatus;

    TextView menuIcon;
    ImageView notificationIcon;
    Typeface octinPrisonFont;

    Snackbar snack;

    ImageView selectedLeftWhistle, selectedRightWhistle, selectedCenterWhistle, selectedLeftTimer, selectedRightTimer, selectedCenterTimer;

    ImageView selectedRightVessel, selectedLeftVessel, selectedCenterVessel;

    TextView selectedRightWhistleCount, selectedRightTimerCount, selectedLeftWhistleCount, selectedLeftTimerCount, selectedCenterWhistleCount, selectedCenterTimerCount;

    private SqliteManager sqliteManager;

    ImageView leftBurner, rightBurner, centerBurner;

    TextView leftOff, leftHigh, leftSim, rightOff, rightHigh, rightSim, centerOff, centerHigh, centerSim;

    TextView selectdUserName;

    int setNotification = 0;
    TextView notificationCount;

    List<NotificationResponseDTO> nonReadNotiCount;


    ImageView eStop;

    int leftBurnerStatus, rightBurnerStatus, centerBurnerStatus = 0;
    int leftVesselStatus, rightVesselStatus, centerVesselStatus = 0;

    ImageView selectBatteryStatus;

    Dialog eStopDialog;

    RelativeLayout profileSelectBlock;

    RelativeLayout setTimerBlock, setWhistleBlock;

    int whistleCount = 0;
    int timerCount = 0;
    TextView whistleSub, whistleAdd, setWhistleCount, whistleFont;
    TextView timerSub, timerAdd, setTimerCount, timerFont;
    Button timerStart, timerCancel, whistleStart, whistleCancel;
    int rightTimerToSet, rightWhistleToSet, leftTimerToSet, leftWhistleToSet, centerTimerToSet, centerWhistleToSet = 0;
    Button timerTitle, whistleTitle, timerReset, whistleReset;

    Timer timer;
    TimerTask timerTask;

    TextView batteryValue;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        hideStatusBar();

        View v = inflater.inflate(R.layout.fragment_characteric_list, null);

        initView(v);

        getFont();

        startBlinking(selectBluetoothStatus);

        return v;
    }


    @Override
    public void onResume() {
        super.onResume();

        List<UserDTO> userDTOList = sqliteManager.getUserDetails();
        selectdUserName.setText(userDTOList.get(0).getUserName());
        getImageFromSqliteDB();

        //Calls Notify
        if (SIZE_OF_CHARACTERISTIC == 2 && mResultAdapter != null) {
            //Getting Stove Data
            callMe(0, null, 0, 0, 0, 0);
        }

        setNonReadNotificationCount();

    }


    private void initView(View v) {
        mResultAdapter = new ResultAdapter(getActivity());
        ListView listView_device = (ListView) v.findViewById(R.id.list_service);

        sqliteManager = new SqliteManager(getActivity());

        setTimerBlock = (RelativeLayout) v.findViewById(R.id.setTimerBlock);
        setWhistleBlock = (RelativeLayout) v.findViewById(R.id.setWhistleCountBlock);

        batteryValue=(TextView)v.findViewById(R.id.batteryValue);

        whistleSub = (TextView) v.findViewById(R.id.whistleSub);
        whistleAdd = (TextView) v.findViewById(R.id.whistleAdd);
        setWhistleCount = (TextView) v.findViewById(R.id.setWhistleCount);
        whistleFont = (TextView) v.findViewById(R.id.whistleFont);


        timerSub = (TextView) v.findViewById(R.id.timerSub);
        timerAdd = (TextView) v.findViewById(R.id.timerAdd);
        setTimerCount = (TextView) v.findViewById(R.id.setTimerCount);
        timerFont = (TextView) v.findViewById(R.id.minuteFont);
        timerTitle = (Button) v.findViewById(R.id.timerTitle);

        timerStart = (Button) v.findViewById(R.id.timerStart);
        timerCancel = (Button) v.findViewById(R.id.timerCancel);
        whistleStart = (Button) v.findViewById(R.id.whistleStart);
        whistleCancel = (Button) v.findViewById(R.id.whistleCancel);
        whistleTitle = (Button) v.findViewById(R.id.whistleTitle);

        timerReset = (Button) v.findViewById(R.id.timerReset);
        whistleReset = (Button) v.findViewById(R.id.whistleReset);


        profileSelectBlock = (RelativeLayout) v.findViewById(R.id.profileSelectBlock);
        selectdUserName = (TextView) v.findViewById(R.id.selectdUserName);
        seletedUserProfile = (CircleImageView) v.findViewById(R.id.seletedUserProfile);
        selectBluetoothStatus = (ImageView) v.findViewById(R.id.selectBluetoothStatus);

        selectBatteryStatus = (ImageView) v.findViewById(R.id.selectBatteryStatus);


        leftBurner = (ImageView) v.findViewById(R.id.leftBurner);
        rightBurner = (ImageView) v.findViewById(R.id.rightBurner);
        centerBurner = (ImageView) v.findViewById(R.id.centerBurner);


        eStop = (ImageView) v.findViewById(R.id.eStop);
        eStopDialog = new Dialog(getActivity());

        selectedLeftWhistle = (ImageView) v.findViewById(R.id.leftWhistle);
        selectedRightWhistle = (ImageView) v.findViewById(R.id.rightWhistle);
        selectedCenterWhistle = (ImageView) v.findViewById(R.id.centerWhistle);


        selectedLeftTimer = (ImageView) v.findViewById(R.id.leftTimer);
        selectedRightTimer = (ImageView) v.findViewById(R.id.rightTimer);
        selectedCenterTimer = (ImageView) v.findViewById(R.id.centerTimer);

        selectedRightVessel = (ImageView) v.findViewById(R.id.rightVessel);
        selectedLeftVessel = (ImageView) v.findViewById(R.id.LeftVessel);
        selectedCenterVessel = (ImageView) v.findViewById(R.id.centerVessel);


        //Whistle and Timer
        selectedRightWhistleCount = (TextView) v.findViewById(R.id.rightWhistleCount);
        selectedRightWhistleCount.setTypeface(octinPrisonFont);
        selectedRightTimerCount = (TextView) v.findViewById(R.id.rightTimerCount);
        selectedRightTimerCount.setTypeface(octinPrisonFont);


        selectedLeftWhistleCount = (TextView) v.findViewById(R.id.leftWhistleCount);
        selectedLeftWhistleCount.setTypeface(octinPrisonFont);
        selectedLeftTimerCount = (TextView) v.findViewById(R.id.leftTimerCount);
        selectedLeftTimerCount.setTypeface(octinPrisonFont);


        selectedCenterWhistleCount = (TextView) v.findViewById(R.id.centerWhistleCount);
        selectedCenterWhistleCount.setTypeface(octinPrisonFont);
        selectedCenterTimerCount = (TextView) v.findViewById(R.id.centerTimerCount);
        selectedCenterTimerCount.setTypeface(octinPrisonFont);

        notificationCount = (TextView) v.findViewById(R.id.notificationCount);

        //End of Whistle and Timer

        menuIcon = (TextView) v.findViewById(R.id.menuIcon);
        menuIcon.setTypeface(octinPrisonFont);
        notificationIcon = (ImageView) v.findViewById(R.id.notificationIcon);


        leftOff = (TextView) v.findViewById(R.id.leftOff);
        leftHigh = (TextView) v.findViewById(R.id.leftHigh);
        leftSim = (TextView) v.findViewById(R.id.leftSim);

        rightOff = (TextView) v.findViewById(R.id.rightOff);
        rightHigh = (TextView) v.findViewById(R.id.rightHigh);
        rightSim = (TextView) v.findViewById(R.id.rightSim);

        centerOff = (TextView) v.findViewById(R.id.centerOff);
        centerHigh = (TextView) v.findViewById(R.id.centerHigh);
        centerSim = (TextView) v.findViewById(R.id.centerSim);


        leftOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (leftBurnerStatus > 0) {
                    leftOff.setTextColor(Color.RED);
                    leftHigh.setTextColor(Color.WHITE);
                    leftSim.setTextColor(Color.WHITE);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            leftOff.setTextColor(Color.WHITE);
                        }
                    }, 500);

                    callMe(1, MathUtil.LEFT_BURNER, 0, 0, MathUtil.OFF, 1);
                } else {
                    callSnackBar(MathUtil.LBISA, v);
                }


            }
        });

        leftHigh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (leftBurnerStatus > 0) {
                    leftHigh.setTextColor(Color.RED);
                    leftSim.setTextColor(Color.WHITE);
                    leftOff.setTextColor(Color.WHITE);
                    callMe(1, MathUtil.LEFT_BURNER, 0, 0, MathUtil.HIGH, 1);
                } else {
                    callSnackBar(MathUtil.LBISA, v);
                }


            }
        });

        leftSim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (leftBurnerStatus > 0) {
                    leftSim.setTextColor(Color.RED);
                    leftHigh.setTextColor(Color.WHITE);
                    leftOff.setTextColor(Color.WHITE);
                    callMe(1, MathUtil.LEFT_BURNER, 0, 0, MathUtil.SIM, 1);
                } else {
                    callSnackBar(MathUtil.LBISA, v);
                }


            }
        });

        rightOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (rightBurnerStatus > 0) {
                    rightOff.setTextColor(Color.RED);
                    rightHigh.setTextColor(Color.WHITE);
                    rightSim.setTextColor(Color.WHITE);

                    callMe(1, MathUtil.RIGHT_BURNER, 0, 0, MathUtil.OFF, 1);

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            rightOff.setTextColor(Color.WHITE);
                        }
                    }, 500);
                } else {
                    callSnackBar(MathUtil.RBINA, v);
                }


            }
        });

        rightHigh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (rightBurnerStatus > 0) {
                    rightOff.setTextColor(Color.WHITE);
                    rightHigh.setTextColor(Color.RED);
                    rightSim.setTextColor(Color.WHITE);
                    callMe(1, MathUtil.RIGHT_BURNER, 0, 0, MathUtil.HIGH, 1);
                } else {
                    callSnackBar(MathUtil.RBINA, v);
                }


            }
        });
        rightSim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (rightBurnerStatus > 0) {
                    rightOff.setTextColor(Color.WHITE);
                    rightHigh.setTextColor(Color.WHITE);
                    rightSim.setTextColor(Color.RED);
                    callMe(1, MathUtil.RIGHT_BURNER, 0, 0, MathUtil.SIM, 1);
                } else {

                    callSnackBar(MathUtil.RBINA, v);

                }


            }
        });


        centerOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (centerBurnerStatus > 0) {
                    centerOff.setTextColor(Color.RED);
                    centerHigh.setTextColor(Color.WHITE);
                    centerSim.setTextColor(Color.WHITE);

                    callMe(1, MathUtil.CENTER_BURNER, 0, 0, MathUtil.OFF, 1);

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            centerOff.setTextColor(Color.WHITE);
                        }
                    }, 500);
                } else {
                    callSnackBar(MathUtil.CBISA, v);
                }


            }
        });

        centerHigh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (centerBurnerStatus > 0) {
                    centerOff.setTextColor(Color.WHITE);
                    centerHigh.setTextColor(Color.RED);
                    centerSim.setTextColor(Color.WHITE);

                    callMe(1, MathUtil.CENTER_BURNER, 0, 0, MathUtil.HIGH, 1);
                } else {
                    callSnackBar(MathUtil.CBISA, v);
                }


            }
        });

        centerSim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (centerBurnerStatus > 0) {
                    centerOff.setTextColor(Color.WHITE);
                    centerHigh.setTextColor(Color.WHITE);
                    centerSim.setTextColor(Color.RED);

                    callMe(1, MathUtil.CENTER_BURNER, 0, 0, MathUtil.SIM, 1);
                } else {
                    callSnackBar(MathUtil.CBISA, v);
                }


            }
        });

        selectedLeftWhistle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Burner Status
                if (leftBurnerStatus > 0) {

                    //Vessel Status
                    if (leftVesselStatus > 0) {

                        //Other Burner Whistle Status
                        if (centerWhistleToSet <= 0 && rightWhistleToSet <= 0) {

                            /*if(snack!=null){

                                snack.dismiss();
                            }*/
                            stopBGTimerTask(MathUtil.SET_WHISTLE_VISIBLE);
                            startBGTimerTast(MathUtil.SET_WHISTLE_VISIBLE);

                            //setWhistleVisible();
                            setWhistle(MathUtil.LEFT_BURNER);

                        } else {

                            if (centerWhistleToSet > 0) {
                                setInvisibleTimerAndWhistle();
                                callSnackBar(MathUtil.WASC, v);
                            } else if (rightWhistleToSet > 0) {
                                setInvisibleTimerAndWhistle();
                                callSnackBar(MathUtil.WASR, v);
                            }
                            //System.out.println("WhistleIsAlreadySetToAnotherBurnerPleaseResetToSetLeftBurner");
                        }


                    } else {
                        //System.out.println("VesselIsNotPlacedOnLeftBurnerToSetWhistle");
                        setInvisibleTimerAndWhistle();
                        callSnackBar(MathUtil.VNPL, v);

                    }
                } else {
                    //System.out.println("LeftBurnerIsNotActivated");
                    setInvisibleTimerAndWhistle();
                    callSnackBar(MathUtil.LBISA, v);


                }


            }
        });

        selectedLeftTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (leftBurnerStatus > 0) {

                    if (leftVesselStatus > 0) {

                        //snack.dismiss();
                        stopBGTimerTask(MathUtil.SET_TIMER_VISIBLE);
                        startBGTimerTast(MathUtil.SET_TIMER_VISIBLE);
                        //setTimerVisible();
                        setTimer(MathUtil.LEFT_BURNER);
                    } else {
                        setInvisibleTimerAndWhistle();
                        callSnackBar(MathUtil.VNPLT, v);
                        //System.out.println("VesselIsNotPlacedOnLeftBurnerToSetTimer");
                    }
                } else {
                    //System.out.println("LeftBurnerIsNotActivated");
                    setInvisibleTimerAndWhistle();
                    callSnackBar(MathUtil.LBISA, v);
                }

            }
        });


        selectedRightWhistle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rightBurnerStatus > 0) {

                    if (rightVesselStatus > 0) {

                        if (leftWhistleToSet <= 0 && centerWhistleToSet <= 0) {

                            // snack.dismiss();
                            stopBGTimerTask(MathUtil.SET_WHISTLE_VISIBLE);
                            startBGTimerTast(MathUtil.SET_WHISTLE_VISIBLE);
                            //setWhistleVisible();
                            setWhistle(MathUtil.RIGHT_BURNER);
                        } else {

                            if (leftWhistleToSet > 0) {
                                setInvisibleTimerAndWhistle();
                                callSnackBar(MathUtil.WASL, v);
                            } else if (centerWhistleToSet > 0) {
                                setInvisibleTimerAndWhistle();
                                callSnackBar(MathUtil.WASC, v);
                            }

                            //System.out.println("WhistleIsAlreadySetPleaseResetToSetRightBurner");
                        }


                    } else {
                        //System.out.println("VesselIsNotPlacedOnRightBurnerToSetWhistle");
                        setInvisibleTimerAndWhistle();
                        callSnackBar(MathUtil.VNPR, v);

                    }

                } else {
                    //System.out.println("RightBurnerIsNotActivated");
                    setInvisibleTimerAndWhistle();
                    callSnackBar(MathUtil.RBINA, v);

                }

            }
        });

        selectedRightTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (rightBurnerStatus > 0) {

                    if (rightVesselStatus > 0) {

                        //snack.dismiss();
                        stopBGTimerTask(MathUtil.SET_TIMER_VISIBLE);
                        startBGTimerTast(MathUtil.SET_TIMER_VISIBLE);
                        //setTimerVisible();
                        setTimer(MathUtil.RIGHT_BURNER);
                    } else {
                        setInvisibleTimerAndWhistle();
                        callSnackBar(MathUtil.VNPRT, v);
                        //System.out.println("VesselIsNotPlacedOnRightBurnerToSetTimer");
                    }
                } else {
                    //System.out.println("RightBurnerIsNotActivated");
                    setInvisibleTimerAndWhistle();
                    callSnackBar(MathUtil.RBINA, v);

                }

            }
        });

        selectedCenterWhistle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (centerBurnerStatus > 0) {

                    //System.out.println("WhistleSetToCheck" + centerBurnerStatus + " " + centerVesselStatus + " " + leftWhistleToSet + " " + rightWhistleToSet);

                    if (centerVesselStatus > 0) {

                        if (leftWhistleToSet <= 0 && rightWhistleToSet <= 0) {

                            //snack.dismiss();
                            stopBGTimerTask(MathUtil.SET_WHISTLE_VISIBLE);
                            startBGTimerTast(MathUtil.SET_WHISTLE_VISIBLE);
                            //setWhistleVisible();
                            setWhistle(MathUtil.CENTER_BURNER);
                        } else {

                            if (leftWhistleToSet > 0) {
                                setInvisibleTimerAndWhistle();
                                callSnackBar(MathUtil.WASL, v);
                            } else if (rightWhistleToSet > 0) {
                                setInvisibleTimerAndWhistle();
                                callSnackBar(MathUtil.WASR, v);
                            }

                        }

                    } else {
                        // System.out.println("VesselIsNotPlacedOnCenterBurnerToSetWhistle");
                        setInvisibleTimerAndWhistle();
                        callSnackBar(MathUtil.VNPC, v);
                    }
                } else {
                    //System.out.println("CenterBurnerIsNotActivated");
                    setInvisibleTimerAndWhistle();
                    callSnackBar(MathUtil.CBISA, v);
                }


            }
        });


        selectedCenterTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (centerBurnerStatus > 0) {

                    if (centerVesselStatus > 0) {

                        // snack.dismiss();
                        stopBGTimerTask(MathUtil.SET_TIMER_VISIBLE);
                        startBGTimerTast(MathUtil.SET_TIMER_VISIBLE);
                        //setTimerVisible();
                        setTimer(MathUtil.CENTER_BURNER);
                    } else {
                        setInvisibleTimerAndWhistle();
                        callSnackBar(MathUtil.VNPCT, v);
                        // System.out.println("VesselIsNotPlacedOnCenterBurnerToSetTimer");
                    }
                } else {
                    // System.out.println("CenterBurnerIsNotActivated");
                    setInvisibleTimerAndWhistle();
                    callSnackBar(MathUtil.CBISA, v);
                }


            }
        });


        eStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TextView eStopYes, eStopNo, eStopText;

                eStopDialog.setContentView(R.layout.layout_estop);

                eStopYes = eStopDialog.findViewById(R.id.eStopYes);
                eStopNo = eStopDialog.findViewById(R.id.eStopNo);
                eStopText = eStopDialog.findViewById(R.id.e1);

                eStopText.setTypeface(octinPrisonFont);
                eStopYes.setTypeface(octinPrisonFont);
                eStopNo.setTypeface(octinPrisonFont);

                eStopYes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (SIZE_OF_CHARACTERISTIC == 2 && mResultAdapter != null) {


                            callMe(1, null, 0, 0, 0, 4);
                            eStopDialog.dismiss();
                        }

                    }
                });

                eStopNo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        eStopDialog.dismiss();
                    }
                });

                eStopDialog.setCanceledOnTouchOutside(false);
                eStopDialog.show();


            }
        });


        listView_device.setAdapter(mResultAdapter);
        listView_device.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                /*final BluetoothGattCharacteristic characteristic = mResultAdapter.getItem(position);
                final List<Integer> propList = new ArrayList<>();
                List<String> propNameList = new ArrayList<>();
                int charaProp = characteristic.getProperties();
                if ((charaProp & BluetoothGattCharacteristic.PROPERTY_READ) > 0) {
                    propList.add(CharacteristicOperationFragment.PROPERTY_READ);
                    propNameList.add("Read");
                }
                if ((charaProp & BluetoothGattCharacteristic.PROPERTY_WRITE) > 0) {
                    propList.add(CharacteristicOperationFragment.PROPERTY_WRITE);
                    propNameList.add("Write");
                }
                if ((charaProp & BluetoothGattCharacteristic.PROPERTY_WRITE_NO_RESPONSE) > 0) {
                    propList.add(CharacteristicOperationFragment.PROPERTY_WRITE_NO_RESPONSE);
                    propNameList.add("Write No Response");
                }
                if ((charaProp & BluetoothGattCharacteristic.PROPERTY_NOTIFY) > 0) {
                    propList.add(CharacteristicOperationFragment.PROPERTY_NOTIFY);
                    propNameList.add("Notify");
                }
                if ((charaProp & BluetoothGattCharacteristic.PROPERTY_INDICATE) > 0) {
                    propList.add(CharacteristicOperationFragment.PROPERTY_INDICATE);
                    propNameList.add("Indicate");
                }

                if (propList.size() > 1) {
                    new AlertDialog.Builder(getActivity())
                            .setTitle(getActivity().getString(R.string.select_operation_type))
                            .setItems(propNameList.toArray(new String[propNameList.size()]), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    ((OperationActivity) getActivity()).setCharacteristic(characteristic);
                                    ((OperationActivity) getActivity()).setCharaProp(propList.get(which));
                                    ((OperationActivity) getActivity()).changePage(2);
                                }
                            })
                            .show();
                } else if (propList.size() > 0) {
                    ((OperationActivity) getActivity()).setCharacteristic(characteristic);
                    ((OperationActivity) getActivity()).setCharaProp(propList.get(0));
                    ((OperationActivity) getActivity()).changePage(2);
                }*/

                //callMe(position);


            }
        });

        profileSelectBlock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ProfileSettingsActivity.class);
                startActivity(intent);
            }
        });

        notificationIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int notiPreference=PreferencesUtil.getValueInt(getActivity(),PreferencesUtil.NOTI_PREFERENCE);

                if(notiPreference==1 || notiPreference==0){
                    Intent intent = new Intent(getActivity(), NotificationActivity.class);
                    startActivity(intent);
                }else if(notiPreference==-1){
                    Toast.makeText(getActivity(),"You disabled notification preference",Toast.LENGTH_LONG).show();
                }



            }
        });

        menuIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callMenuItems();
            }
        });
    }

    private void setTimer(final String burner) {

        int resetStatus = 0;


        if (burner.equals("00")) {

            if (rightTimerToSet >= 0) {
                setTimerCount.setText("" + rightTimerToSet);
                timerCount = rightTimerToSet;
                if (rightTimerToSet > 0) {
                    timerCancel.setVisibility(View.INVISIBLE);
                    timerReset.setVisibility(View.VISIBLE);
                    resetStatus = 1;
                } else {
                    timerCancel.setVisibility(View.VISIBLE);
                    timerReset.setVisibility(View.INVISIBLE);
                }
            } else if (rightTimerToSet < 0) {
                timerCancel.setVisibility(View.VISIBLE);
                timerReset.setVisibility(View.INVISIBLE);
                setTimerCount.setText("" + rightTimerToSet);
            }


        } else if (burner.equals("01")) {

            if (leftTimerToSet >= 0) {
                setTimerCount.setText("" + leftTimerToSet);
                timerCount = leftTimerToSet;
                if (leftTimerToSet > 0) {
                    timerCancel.setVisibility(View.INVISIBLE);
                    timerReset.setVisibility(View.VISIBLE);
                    resetStatus = 2;
                } else {
                    timerCancel.setVisibility(View.VISIBLE);
                    timerReset.setVisibility(View.INVISIBLE);
                }
            } else if (leftTimerToSet < 0) {
                timerCancel.setVisibility(View.VISIBLE);
                timerReset.setVisibility(View.INVISIBLE);
                setTimerCount.setText("" + leftTimerToSet);
            }


        } else if (burner.equals("10")) {
            if (centerTimerToSet >= 0) {
                setTimerCount.setText("" + centerTimerToSet);
                timerCount = centerTimerToSet;
                if (centerTimerToSet > 0) {
                    timerCancel.setVisibility(View.INVISIBLE);
                    timerReset.setVisibility(View.VISIBLE);
                    resetStatus = 3;
                } else {
                    timerCancel.setVisibility(View.VISIBLE);
                    timerReset.setVisibility(View.INVISIBLE);
                }

            } else if (centerTimerToSet < 0) {
                timerCancel.setVisibility(View.VISIBLE);
                timerReset.setVisibility(View.INVISIBLE);
                setTimerCount.setText("" + centerTimerToSet);
            }

        }

        setTimerCount.setTypeface(octinPrisonFont);
        timerFont.setTypeface(octinPrisonFont);
        timerStart.setTypeface(octinPrisonFont);
        timerCancel.setTypeface(octinPrisonFont);
        timerTitle.setTypeface(octinPrisonFont);
        timerReset.setTypeface(octinPrisonFont);
        batteryValue.setTypeface(octinPrisonFont);


        //mHandler.postDelayed(mRunnable,50000);

        timerAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopBGTimerTask(1);
                startBGTimerTast(1);
                timerSub.setEnabled(true);
                timerCount = timerCount + 1;
                setTimerCount.setText("" + timerCount);
            }
        });

        timerSub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopBGTimerTask(1);
                startBGTimerTast(1);
                if (timerCount > 0) {
                    timerSub.setEnabled(true);
                    timerCount = timerCount - 1;
                    setTimerCount.setText("" + timerCount);
                } else if (timerCount == 0) {
                    timerSub.setEnabled(false);
                    setTimerCount.setText("" + timerCount);
                }
            }
        });


        timerStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int burnerTimerCount = Integer.parseInt(setTimerCount.getText().toString());

                if (burnerTimerCount == 0) {

                    Toast.makeText(getActivity(), "Please Set Timer", Toast.LENGTH_LONG).show();

                } else {

                    if (SIZE_OF_CHARACTERISTIC == 2 && mResultAdapter != null) {


                        callMe(1, burner, burnerTimerCount, 0, 0, 3);
                        timerCount = 0;

                    }

                }


            }
        });


        timerCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                timerCount = 0;
                setTimerCount.setText("0");
                setTimerBlock.setVisibility(View.INVISIBLE);
            }
        });

        final int finalResetStatus = resetStatus;
        timerReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                timerCount = 0;
                setTimerCount.setText("0");


                if (finalResetStatus == 1) {
                    //System.out.println("RightBurnerTimerIsReset");
                    if (SIZE_OF_CHARACTERISTIC == 2 && mResultAdapter != null) {


                        callMe(1, "00", 0, 0, 0, 3);

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                timerCount = 0;
                                rightTimerToSet = 0;
                            }
                        }, 500);


                    }

                } else if (finalResetStatus == 2) {
                    //System.out.println("LeftBurnerTimerIsReset");

                    if (SIZE_OF_CHARACTERISTIC == 2 && mResultAdapter != null) {


                        callMe(1, "01", 0, 0, 0, 3);

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                timerCount = 0;
                                leftTimerToSet = 0;
                            }
                        }, 500);

                    }

                } else if (finalResetStatus == 3) {
                    //System.out.println("CenterBurnerTimerIsReset");
                    if (SIZE_OF_CHARACTERISTIC == 2 && mResultAdapter != null) {


                        callMe(1, "10", 0, 0, 0, 3);

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                timerCount = 0;
                                centerTimerToSet = 0;
                            }
                        }, 500);


                    }
                }
            }
        });


    }


    private void setWhistle(final String burner) {

        int resetWhistleStatus = 0;

        if (burner.equals("00")) {


            if (rightWhistleToSet >= 0) {
                setWhistleCount.setText("" + rightWhistleToSet);
                whistleCount = rightWhistleToSet;

                if (rightWhistleToSet > 0) {
                    whistleCancel.setVisibility(View.INVISIBLE);
                    whistleReset.setVisibility(View.VISIBLE);
                    resetWhistleStatus = 1;
                } else {
                    whistleCancel.setVisibility(View.VISIBLE);
                    whistleReset.setVisibility(View.INVISIBLE);
                }

            }

        } else if (burner.equals("01")) {


            if (leftWhistleToSet >= 0) {
                setWhistleCount.setText("" + leftWhistleToSet);
                whistleCount = leftWhistleToSet;

                if (leftWhistleToSet > 0) {
                    whistleCancel.setVisibility(View.INVISIBLE);
                    whistleReset.setVisibility(View.VISIBLE);
                    resetWhistleStatus = 2;
                } else {
                    whistleCancel.setVisibility(View.VISIBLE);
                    whistleReset.setVisibility(View.INVISIBLE);
                }

            }

        } else if (burner.equals("10")) {

            if (centerWhistleToSet >= 0) {
                setWhistleCount.setText("" + centerWhistleToSet);
                whistleCount = centerWhistleToSet;

                if (centerWhistleToSet > 0) {
                    whistleCancel.setVisibility(View.INVISIBLE);
                    whistleReset.setVisibility(View.VISIBLE);
                    resetWhistleStatus = 3;
                } else {
                    whistleCancel.setVisibility(View.VISIBLE);
                    whistleReset.setVisibility(View.INVISIBLE);
                }

            }
        }

        whistleFont.setTypeface(octinPrisonFont);
        setWhistleCount.setTypeface(octinPrisonFont);
        whistleStart.setTypeface(octinPrisonFont);
        whistleCancel.setTypeface(octinPrisonFont);
        whistleTitle.setTypeface(octinPrisonFont);
        whistleReset.setTypeface(octinPrisonFont);

        whistleAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopBGTimerTask(2);
                startBGTimerTast(2);
                whistleSub.setEnabled(true);
                whistleCount = whistleCount + 1;
                setWhistleCount.setText("" + whistleCount);

            }
        });

        // mHandler.postDelayed(mRunnable,50000);

        whistleSub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopBGTimerTask(2);
                startBGTimerTast(2);
                if (whistleCount > 0) {
                    whistleSub.setEnabled(true);
                    whistleCount = whistleCount - 1;
                    setWhistleCount.setText("" + whistleCount);
                } else if (whistleCount == 0) {
                    whistleSub.setEnabled(false);
                    setWhistleCount.setText("" + whistleCount);
                }
            }
        });


        final int finalResetWhistleStatus = resetWhistleStatus;

        whistleReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                whistleCount = 0;
                //setWhistleCount.setText("0");

                if (finalResetWhistleStatus == 1) {
                    if (SIZE_OF_CHARACTERISTIC == 2 && mResultAdapter != null) {
                        callMe(1, "00", 0, 0, 0, 2);

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                whistleCount = 0;
                                rightWhistleToSet = 0;
                                leftWhistleToSet = 0;
                                centerWhistleToSet = 0;
                            }
                        }, 500);
                    }
                } else if (finalResetWhistleStatus == 2) {

                    if (SIZE_OF_CHARACTERISTIC == 2 && mResultAdapter != null) {
                        callMe(1, "01", 0, 0, 0, 2);

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                whistleCount = 0;
                                leftWhistleToSet = 0;
                                rightWhistleToSet = 0;
                                centerWhistleToSet = 0;
                            }
                        }, 500);
                    }


                } else if (finalResetWhistleStatus == 3) {
                    if (SIZE_OF_CHARACTERISTIC == 2 && mResultAdapter != null) {
                        callMe(1, "10", 0, 0, 0, 2);

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                whistleCount = 0;
                                centerWhistleToSet = 0;
                                leftWhistleToSet = 0;
                                rightWhistleToSet = 0;
                            }
                        }, 500);
                    }
                }

            }
        });


        whistleStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int burnerWhistleCount = Integer.parseInt(setWhistleCount.getText().toString());

                if (burnerWhistleCount == 0) {

                    Toast.makeText(getActivity(), "Please Set Whistle Count", Toast.LENGTH_LONG).show();

                } else {

                    if (SIZE_OF_CHARACTERISTIC == 2 && mResultAdapter != null) {

                        whistleCount = 0;
                        setWhistleCount.setText("0");
                        callMe(1, burner, 0, burnerWhistleCount, 0, 2);


                    }

                }


            }
        });

        whistleCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                whistleCount = 0;
                setWhistleCount.setText("0");
                setWhistleBlock.setVisibility(View.INVISIBLE);
            }
        });

    }


    private void callMe(int position, String burner, int timerInMin, int whistleInCount, int flameMode, int frameFormet) {

        //Position 0 -->Notify
        //Position 1 -->Write

        final BluetoothGattCharacteristic characteristic = mResultAdapter.getItem(position);
        final List<Integer> propList = new ArrayList<>();
        List<String> propNameList = new ArrayList<>();
        int charaProp = characteristic.getProperties();


        if ((charaProp & BluetoothGattCharacteristic.PROPERTY_WRITE) > 0) {
            propList.add(CharacteristicOperationFragment.PROPERTY_WRITE);
            propNameList.add("Write");
        }
        if ((charaProp & BluetoothGattCharacteristic.PROPERTY_NOTIFY) > 0) {
            propList.add(CharacteristicOperationFragment.PROPERTY_NOTIFY);
            propNameList.add("Notify");
        }

       /* if (propList.size() > 0 ) {
            ((OperationActivity) getActivity()).setCharacteristic(characteristic);
            ((OperationActivity) getActivity()).setCharaProp(propList.get(0));
            ((OperationActivity) getActivity()).changePage(2);

        }*/

        if (propList.size() > 0 && position == 0) {
            ((OperationActivity) getActivity()).setCharacteristic(characteristic);
            ((OperationActivity) getActivity()).setCharaProp(propList.get(0));
            //((OperationActivity) getActivity()).changePage(2);

            //Notify
            gettingStoveData();


        }
        if (propList.size() > 0 && position == 1 && frameFormet == 1) {
            ((OperationActivity) getActivity()).setCharacteristic(characteristic);
            ((OperationActivity) getActivity()).setCharaProp(propList.get(0));
            //((OperationActivity) getActivity()).changePage(2);
            wrietUserData(burner, timerInMin, whistleInCount, flameMode, frameFormet);
        }
        //whistle
        if (propList.size() > 0 && position == 1 && frameFormet == 2) {
            ((OperationActivity) getActivity()).setCharacteristic(characteristic);
            ((OperationActivity) getActivity()).setCharaProp(propList.get(0));
            //((OperationActivity) getActivity()).changePage(2);

            wrietUserData(burner, timerInMin, whistleInCount, flameMode, frameFormet);

            /*new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                }
            }, 2000);*/


        }

        //Timer
        if (propList.size() > 0 && position == 1 && frameFormet == 3) {
            ((OperationActivity) getActivity()).setCharacteristic(characteristic);
            ((OperationActivity) getActivity()).setCharaProp(propList.get(0));
            //((OperationActivity) getActivity()).changePage(2);

            wrietUserData(burner, timerInMin, whistleInCount, flameMode, frameFormet);

            /*new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                }
            }, 2000);*/


        }

        //Emergency Off
        if (propList.size() > 0 && position == 1 && frameFormet == 4) {
            ((OperationActivity) getActivity()).setCharacteristic(characteristic);
            ((OperationActivity) getActivity()).setCharaProp(propList.get(0));
            //((OperationActivity) getActivity()).changePage(2);
            wrietUserData("ss", 0, 0, 0, 4);
        }


    }


    private void wrietUserData(String burner, int timerInMin, int whistleInCount, int flameMode, int frameFormet) {


        //Burner
        if (frameFormet == 1) {

            byte[] flame = new byte[6];

            int rightBurnerFlame = 0, leftBurnerFlame = 0, centerBurnerFlame = 0;

            if (burner.equals(MathUtil.RIGHT_BURNER)) {
                rightBurnerFlame = flameMode;
                leftBurnerFlame = 0xff;
                centerBurnerFlame = 0xff;
            } else if (burner.equals(MathUtil.LEFT_BURNER)) {
                leftBurnerFlame = flameMode;
                rightBurnerFlame = 0xff;
                centerBurnerFlame = 0xff;

            } else if (burner.equals(MathUtil.CENTER_BURNER)) {
                centerBurnerFlame = flameMode;
                rightBurnerFlame = 0xff;
                leftBurnerFlame = 0xff;

            }


            flame[0] = (byte) ('*');
            flame[1] = (byte) (0xD0);
            flame[2] = (byte) (rightBurnerFlame);
            flame[3] = (byte) (leftBurnerFlame);
            flame[4] = (byte) (centerBurnerFlame);
            flame[5] = (byte) ('#');


            BleDevice bleDevice = ((OperationActivity) getActivity()).getBleDevice();
            BluetoothGattCharacteristic characteristic = ((OperationActivity) getActivity()).getCharacteristic();


            BleManager.getInstance().write(
                    bleDevice,
                    characteristic.getService().getUuid().toString(),
                    characteristic.getUuid().toString(),
                    flame,
                    new BleWriteCallback() {

                        //Converting byte to String and displaying to user
                        @Override
                        public void onWriteSuccess(final int current, final int total, final byte[] justWrite) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    // Toast.makeText(getActivity(), "Write Success", Toast.LENGTH_LONG).show();
                                }
                            });
                        }

                        @Override
                        public void onWriteFailure(final BleException exception) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                    //Toast.makeText(getActivity(), "Write Failed", Toast.LENGTH_LONG).show();

                                    System.out.println("Exception" + exception.toString());
                                }
                            });
                        }
                    });


        } else if (frameFormet == 2) {

            //Whistle

            /*String selectedBurner = PreferencesUtil.getValueString(getActivity(), PreferencesUtil.BURNER);
            int selectedTimer = PreferencesUtil.getValueInt(getActivity(), PreferencesUtil.TIMER_IN_MINUTE);
            int selectedWhistle = PreferencesUtil.getValueInt(getActivity(), PreferencesUtil.WHISTLE_IN_COUNT);*/

            if (burner.equals(MathUtil.RIGHT_BURNER)) {
                if (whistleInCount > 0) {

                    sqliteManager.setNotification(whistleInCount + " Whistle is set for Right Burner");
                    //sqliteManager.getNonReadNotifications();
                }
                /*if (selectedTimer > 0) {
                    sqliteManager.setNotification(selectedTimer + " Min Timer is set for Right Burner");
                    //sqliteManager.getNonReadNotifications();
                }*/
            } else if (burner.equals(MathUtil.LEFT_BURNER)) {

                if (whistleInCount > 0) {
                    sqliteManager.setNotification(whistleInCount + " Whistle is set for Left Burner");
                    //sqliteManager.getNonReadNotifications();
                }
                /*if (selectedTimer > 0) {
                    sqliteManager.setNotification(selectedTimer + " Min Timer is set for Left Burner");
                    //sqliteManager.getNonReadNotifications();
                }*/

            } else if (burner.equals(MathUtil.CENTER_BURNER)) {
                if (whistleInCount > 0) {
                    sqliteManager.setNotification(whistleInCount + " Whistle is set for Center Burner");
                    //sqliteManager.getNonReadNotifications();
                }
                /*if (selectedTimer > 0) {
                    sqliteManager.setNotification(selectedTimer + " Min Timer is set for Center Burner");
                    //sqliteManager.getNonReadNotifications();
                }*/
            }

            setNonReadNotificationCount();


            //Whistle

            byte[] whistle = new byte[9];

            int rightTimer = 0, rightWhistle = 0;
            int leftTimer = 0, leftWhistle = 0;
            int centerTimer = 0, centerWhistle = 0;


            if (burner.equals(MathUtil.RIGHT_BURNER)) {

                rightWhistle = whistleInCount;

                rightTimer = 0xff;
                leftTimer = 0xff;
                leftWhistle = 0xff;
                centerTimer = 0xff;
                centerWhistle = 0xff;


            } else if (burner.equals(MathUtil.LEFT_BURNER)) {


                leftWhistle = whistleInCount;

                leftTimer = 0xff;
                rightTimer = 0xff;
                rightWhistle = 0xff;
                centerTimer = 0xff;
                centerWhistle = 0xff;


            } else if (burner.equals(MathUtil.CENTER_BURNER)) {

                centerWhistle = whistleInCount;

                centerTimer = 0xff;
                rightTimer = 0xff;
                rightWhistle = 0xff;
                leftTimer = 0xff;
                leftWhistle = 0xff;


            }


            whistle[0] = (byte) ('*');
            whistle[1] = (byte) (0xC0);
            whistle[2] = (byte) (rightWhistle);
            whistle[3] = (byte) (rightTimer);
            whistle[4] = (byte) (leftWhistle);
            whistle[5] = (byte) (leftTimer);
            whistle[6] = (byte) (centerWhistle);
            whistle[7] = (byte) (centerTimer);
            whistle[8] = (byte) ('#');


            BleDevice bleDevice = ((OperationActivity) getActivity()).getBleDevice();
            BluetoothGattCharacteristic characteristic = ((OperationActivity) getActivity()).getCharacteristic();


            // Toast.makeText(getActivity(), "New Data Write", Toast.LENGTH_LONG).show();

            BleManager.getInstance().write(
                    bleDevice,
                    characteristic.getService().getUuid().toString(),
                    characteristic.getUuid().toString(),
                    whistle,
                    new BleWriteCallback() {

                        //Converting byte to String and displaying to user
                        @Override
                        public void onWriteSuccess(final int current, final int total, final byte[] justWrite) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                    setWhistleBlock.setVisibility(View.INVISIBLE);

                                    // Toast.makeText(getActivity(), "Whistle Is Set", Toast.LENGTH_LONG).show();

                                }
                            });
                        }

                        @Override
                        public void onWriteFailure(final BleException exception) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                    // Toast.makeText(getActivity(), "Whistle  Is Not Set", Toast.LENGTH_LONG).show();

                                    System.out.println("TimerException" + exception.toString());
                                }
                            });
                        }
                    });


        } else if (frameFormet == 3) {

            //Timer

            /*String selectedBurner = PreferencesUtil.getValueString(getActivity(), PreferencesUtil.BURNER);
            int selectedTimer = PreferencesUtil.getValueInt(getActivity(), PreferencesUtil.TIMER_IN_MINUTE);
            int selectedWhistle = PreferencesUtil.getValueInt(getActivity(), PreferencesUtil.WHISTLE_IN_COUNT);*/

            if (burner.equals("00")) {
               /* if (whistleInCount > 0) {

                    sqliteManager.setNotification(whistleInCount + " Whistle is set for Right Burner");
                    //sqliteManager.getNonReadNotifications();
                }*/
                if (timerInMin > 0) {
                    sqliteManager.setNotification(timerInMin + " Min Timer is set for Right Burner");
                    //sqliteManager.getNonReadNotifications();
                }
            } else if (burner.equals("01")) {

                /*if (whistleInCount > 0) {
                    sqliteManager.setNotification(whistleInCount + " Whistle is set for Left Burner");
                    //sqliteManager.getNonReadNotifications();
                }*/
                if (timerInMin > 0) {
                    sqliteManager.setNotification(timerInMin + " Min Timer is set for Left Burner");
                    //sqliteManager.getNonReadNotifications();
                }

            } else if (burner.equals("10")) {
                /*if (whistleInCount > 0) {
                    sqliteManager.setNotification(whistleInCount + " Whistle is set for Center Burner");
                    //sqliteManager.getNonReadNotifications();
                }*/
                if (timerInMin > 0) {
                    sqliteManager.setNotification(timerInMin + " Min Timer is set for Center Burner");
                    //sqliteManager.getNonReadNotifications();
                }
            }

            setNonReadNotificationCount();


            //Whistle and Timer

            byte[] timer = new byte[9];

            int rightTimer = 0, rightWhistle = 0;
            int leftTimer = 0, leftWhistle = 0;
            int centerTimer = 0, centerWhistle = 0;


            if (burner.equals(MathUtil.RIGHT_BURNER)) {

                rightTimer = timerInMin;

                rightWhistle = 0xff;
                leftTimer = 0xff;
                leftWhistle = 0xff;
                centerTimer = 0xff;
                centerWhistle = 0xff;


            } else if (burner.equals(MathUtil.LEFT_BURNER)) {

                leftTimer = timerInMin;

                leftWhistle = 0xff;
                rightTimer = 0xff;
                rightWhistle = 0xff;
                centerTimer = 0xff;
                centerWhistle = 0xff;


            } else if (burner.equals(MathUtil.CENTER_BURNER)) {

                centerTimer = timerInMin;

                centerWhistle = 0xff;
                rightTimer = 0xff;
                rightWhistle = 0xff;
                leftTimer = 0xff;
                leftWhistle = 0xff;


            }


            timer[0] = (byte) ('*');
            timer[1] = (byte) (0xC0);
            timer[2] = (byte) (rightWhistle);
            timer[3] = (byte) (rightTimer);
            timer[4] = (byte) (leftWhistle);
            timer[5] = (byte) (leftTimer);
            timer[6] = (byte) (centerWhistle);
            timer[7] = (byte) (centerTimer);
            timer[8] = (byte) ('#');


            BleDevice bleDevice = ((OperationActivity) getActivity()).getBleDevice();
            BluetoothGattCharacteristic characteristic = ((OperationActivity) getActivity()).getCharacteristic();


            //Toast.makeText(getActivity(), "New Data Write", Toast.LENGTH_LONG).show();

            BleManager.getInstance().write(
                    bleDevice,
                    characteristic.getService().getUuid().toString(),
                    characteristic.getUuid().toString(),
                    timer,
                    new BleWriteCallback() {

                        //Converting byte to String and displaying to user
                        @Override
                        public void onWriteSuccess(final int current, final int total, final byte[] justWrite) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                    setTimerBlock.setVisibility(View.INVISIBLE);

                                    // Toast.makeText(getActivity(), "Timer Is Set", Toast.LENGTH_LONG).show();

                                }
                            });
                        }

                        @Override
                        public void onWriteFailure(final BleException exception) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                    //Toast.makeText(getActivity(), "Is Not Set", Toast.LENGTH_LONG).show();

                                    System.out.println("TimerException" + exception.toString());
                                }
                            });
                        }
                    });


        } else if (frameFormet == 4) {
//Estop
            byte[] eStop = new byte[6];
            eStop[0] = (byte) ('*');
            eStop[1] = (byte) (0XD1);
            eStop[2] = (byte) (0);
            eStop[3] = (byte) (0);
            eStop[4] = (byte) (0);
            eStop[5] = (byte) ('#');


            BleDevice bleDevice = ((OperationActivity) getActivity()).getBleDevice();
            BluetoothGattCharacteristic characteristic = ((OperationActivity) getActivity()).getCharacteristic();

            BleManager.getInstance().write(
                    bleDevice,
                    characteristic.getService().getUuid().toString(),
                    characteristic.getUuid().toString(),
                    eStop,
                    new BleWriteCallback() {

                        //Converting byte to String and displaying to user
                        @Override
                        public void onWriteSuccess(final int current, final int total, final byte[] justWrite) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                }
                            });
                        }

                        @Override
                        public void onWriteFailure(final BleException exception) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    System.out.println("Exception" + exception.toString());
                                }
                            });
                        }
                    });


        }


    }


    private void gettingStoveData() {

        BleDevice bleDevice = ((OperationActivity) getActivity()).getBleDevice();
        final BluetoothGattCharacteristic characteristic = ((OperationActivity) getActivity()).getCharacteristic();

        BleManager.getInstance().notify(
                bleDevice,
                characteristic.getService().getUuid().toString(),
                characteristic.getUuid().toString(),
                new BleNotifyCallback() {

                    @Override
                    public void onNotifySuccess() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //addText(txt, "notify success");
                            }
                        });
                    }

                    @Override
                    public void onNotifyFailure(final BleException exception) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //addText(txt, exception.toString());
                                System.out.println("IamException" + exception);
                            }
                        });
                    }

                    @Override
                    public void onCharacteristicChanged(final byte[] data) {


                        // System.out.println("Iamnotifydata" + data);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {


                                splitEachBurnerDataFromReceivedByte(data);


                            }
                        });
                    }
                });

    }

    @SuppressLint("NewApi")
    private void splitEachBurnerDataFromReceivedByte(byte[] data) {

        //Validation Block
        if (data[0] != 42) {
            getActivity().finish();
        }//End of Validation Block


        //Timer and Whistle
        if (data.length == 9) {
            // System.out.println("Length9Recevied");
            //C1

            if (data[0] == 42 && data[1] == -63) {

                int rightWhistle = data[2];
                int rightTimer = data[3];

                int leftWhistle = data[4];
                int leftTimer = data[5];

                int centerWhistle = data[6];
                int centerTimer = data[7];

                setWhistleAndTimerValueInUI(rightWhistle, rightTimer, leftWhistle, leftTimer, centerWhistle, centerTimer);

                // setNotificationForWhistleAndTimer(rightVesselForNoti,  rightWhistle, rightTimer,leftVesselForNoti ,leftWhistle, leftTimer, centerVesselForNoti,centerWhistle, centerTimer);


                //System.out.println("WhistleAndTimerData" + rightWhistle + " " + rightTimer + " " + leftWhistle + " " + leftTimer + " " + centerWhistle + " " + centerTimer);

                setWhistleAndTimerNotification(rightWhistle, rightTimer, leftWhistle, leftTimer, centerWhistle, centerTimer);


            }

        }

        //Burner
        if (data.length == 7) {
            //System.out.println("Length7Recevied");
            //D1

            if (data[0] == 42 && data[1] == -47) {

                byte[] rightVesselFlame = new byte[1];
                byte[] leftVesselFlame = new byte[1];
                byte[] centerVesselFlame = new byte[1];


                rightVesselFlame[0] = data[2];
                int rightVessel = (rightVesselFlame[0] & 0x80) >> 7;
                int rightFlameMode = (rightVesselFlame[0] & 0x7C) >> 2;

                /*sqliteManager.addRight(rightVessel,"00");
                List<RightNotiDTO> rightNotiDTOList =sqliteManager.getRightNoti();*/


                leftVesselFlame[0] = data[3];
                int leftVessel = (leftVesselFlame[0] & 0x80) >> 7;
                int leftFlameMode = (leftVesselFlame[0] & 0x7C) >> 2;

                /*sqliteManager.addLeft(leftVessel,"01");
                List<LeftNotiDTO> leftNotiDTOList=sqliteManager.getLeftNoti();*/


                centerVesselFlame[0] = data[4];
                int centerVessel = (centerVesselFlame[0] & 0x80) >> 7;
                int centerFlameMode = (centerVesselFlame[0] & 0x7C) >> 2;

                /*sqliteManager.addCenter(centerVessel,"10");
               List<CenterNotiDTO> centerNotiDTOList= sqliteManager.getCenterNoti();*/

               /*int notiSize=rightNotiDTOList.size()+leftNotiDTOList.size()+centerNotiDTOList.size();
               notificationCount.setText(""+notiSize);*/


                int batteryPercentage = data[5];

                System.out.println("BatteryPercentage" + batteryPercentage);

                setBatteryStatus(batteryPercentage);


                //System.out.println("VesselAndFlameMode" + rightVessel + " " + rightFlameMode + " " + leftVessel + " " + leftFlameMode + " " + centerVessel + " " + centerFlameMode + " " + batteryPercentage);


                setValueInUI(rightVessel, rightFlameMode, leftVessel, leftFlameMode, centerVessel, centerFlameMode);

                setNotification = 1;


            }

        }

        //Gas Consumption Pattern
        if (data.length == 17) {

            if (data[0] == 42 && data[16] == 35) {

                int date = data[2];
                int month = data[3];

                Date d = new Date();
                int year = d.getYear();
                String yearString = String.valueOf(year);

                String dateFormation = date + "/" + month + "/" + "2021";

                Date dateFormet = null;
                try {
                    dateFormet = new SimpleDateFormat("dd/MM/yyyy").parse(dateFormation);

                } catch (ParseException e) {
                    e.printStackTrace();
                }

                int rightBurner = (data[4] & 0x0FF) << 0 | (data[5] & 0x0FF) << 8 | (data[6] & 0x0FF) << 16 | (data[7] & 0x0FF) << 24;
                int leftBurner = (data[8] & 0x0FF) << 0 | (data[9] & 0x0FF) << 8 | (data[10] & 0x0FF) << 16 | (data[11] & 0x0FF) << 24;
                int centerBurner = (data[12] & 0x0FF) << 0 | (data[13] & 0x0FF) << 8 | (data[14] & 0x0FF) << 16 | (data[15] & 0x0FF) << 24;

                float rightBurGasUsage = rightBurner;// / 4096;

                //System.out.println("DateFormet " + dateFormet + " " + rightBurGasUsage);

                sqliteManager.addGasConsumptionPattern(dateFormet, rightBurGasUsage, MathUtil.RIGHT_BURNER);

                float leftBurGasUsage = leftBurner;// / 4096;
                sqliteManager.addGasConsumptionPattern(dateFormet, leftBurGasUsage, MathUtil.LEFT_BURNER);

                float centerBurGasUsage = centerBurner;// / 4096;
                sqliteManager.addGasConsumptionPattern(dateFormet, centerBurGasUsage, MathUtil.CENTER_BURNER);


                /*System.out.println("GasUsage" +date+" "+month+" " + right + " " + left + " " + center);

                System.out.println("ReceivedGCP" + date + " " + " " + month + " " + rightBurner + " " + leftBurner + " " + centerBurner);*/


            }

        }


    }

    private void setBatteryStatus(int batteryPercentage) {


        batteryValue.setText(""+batteryPercentage+"%");

        if (batteryPercentage >= 80) {
            selectBatteryStatus.setImageDrawable(getResources().getDrawable(R.drawable.battery_full));
            stopBlinking(selectBatteryStatus);
        } else if (batteryPercentage >= 60) {
            selectBatteryStatus.setImageDrawable(getResources().getDrawable(R.drawable.battery_9));
            stopBlinking(selectBatteryStatus);
        } else if (batteryPercentage >= 40) {
            selectBatteryStatus.setImageDrawable(getResources().getDrawable(R.drawable.battery_7));
            stopBlinking(selectBatteryStatus);
        } else if (batteryPercentage >= 30) {
            selectBatteryStatus.setImageDrawable(getResources().getDrawable(R.drawable.battery_30));
            stopBlinking(selectBatteryStatus);
        } else if (batteryPercentage >= 20) {
            selectBatteryStatus.setImageDrawable(getResources().getDrawable(R.drawable.battery_low_10));
            stopBlinking(selectBatteryStatus);
        } else if (batteryPercentage <= 15) {
            selectBatteryStatus.setImageDrawable(getResources().getDrawable(R.drawable.battery_criticaly_low));
            startBlinking(selectBatteryStatus);
        }


        if (batteryPercentage <= 30) {
            sqliteManager.setNotification(MathUtil.CRITICALLY_LOW);
        }

        setNonReadNotificationCount();

    }

    private void setWhistleAndTimerNotification(int rightWhistle, int rightTimer, int leftWhistle, int leftTimer, int centerWhistle, int centerTimer) {

        if (rightWhistle == 0) {
            sqliteManager.setNotification(MathUtil.WCR);

        }

        if (rightTimer == 0) {
            sqliteManager.setNotification(MathUtil.TCR);
        }

        if (leftWhistle == 0) {
            sqliteManager.setNotification(MathUtil.WCL);
        }

        if (leftTimer == 0) {
            sqliteManager.setNotification(MathUtil.TCL);
        }

        if (centerWhistle == 0) {
            sqliteManager.setNotification(MathUtil.WCC);
        }

        if (centerTimer == 0) {
            sqliteManager.setNotification(MathUtil.TCC);
        }

        setNonReadNotificationCount();


    }


    private void setWhistleAndTimerValueInUI(int rightWhistle, int rightTimer, int leftWhistle, int leftTimer, int centerWhistle, int centerTimer) {


        if (rightWhistle <= 0) {
            selectedRightWhistleCount.setText("");
            rightWhistleToSet = 0;


        } else {

            selectedRightWhistleCount.setText("" + rightWhistle);
            selectedRightWhistleCount.setTypeface(octinPrisonFont);
            rightWhistleToSet = rightWhistle;

        }

        if (rightTimer <= 0) {
            rightTimerToSet = 0;
            stopBlinking(selectedRightTimer);
            selectedRightTimerCount.setText("");
        } else {
            startBlinking(selectedRightTimer);
            selectedRightTimerCount.setText("" + rightTimer + "min");
            selectedRightTimerCount.setTypeface(octinPrisonFont);
            rightTimerToSet = rightTimer;
        }

        if (leftWhistle <= 0) {
            selectedLeftWhistleCount.setText("");
            leftWhistleToSet = 0;
        } else {
            selectedLeftWhistleCount.setText("" + leftWhistle);
            selectedLeftWhistleCount.setTypeface(octinPrisonFont);
            leftWhistleToSet = leftWhistle;

        }

        if (leftTimer <= 0) {
            leftTimerToSet = 0;
            stopBlinking(selectedLeftTimer);
            selectedLeftTimerCount.setText("");
        } else {
            startBlinking(selectedLeftTimer);
            selectedLeftTimerCount.setText("" + leftTimer + "min");
            selectedLeftTimerCount.setTypeface(octinPrisonFont);
            leftTimerToSet = leftTimer;

        }


        if (centerWhistle <= 0) {
            selectedCenterWhistleCount.setText("");
            centerWhistleToSet = 0;
        } else {
            selectedCenterWhistleCount.setText("" + centerWhistle);
            selectedCenterWhistleCount.setTypeface(octinPrisonFont);
            centerWhistleToSet = centerWhistle;
        }

        if (centerTimer <= 0) {
            centerTimerToSet = 0;
            stopBlinking(selectedCenterTimer);
            selectedCenterTimerCount.setText("");
        } else {
            startBlinking(selectedCenterTimer);
            selectedCenterTimerCount.setText("" + centerTimer + "min");
            selectedCenterTimerCount.setTypeface(octinPrisonFont);
            centerTimerToSet = centerTimer;
        }


    }


    private void setValueInUI(int rightVessel, int rightFlameMode, int leftVessel, int leftFlameMode, int centerVessel, int centerFlameMode) {

        //Right
        if (rightVessel == 0) {

            selectedRightVessel.setVisibility(View.INVISIBLE);


        } else if (rightVessel == 1) {
            selectedRightVessel.setVisibility(View.VISIBLE);


        }

        rightBurnerStatus = rightFlameMode;
        leftBurnerStatus = leftFlameMode;
        centerBurnerStatus = centerFlameMode;

        rightVesselStatus = rightVessel;
        leftVesselStatus = leftVessel;
        centerVesselStatus = centerVessel;

        if (rightFlameMode == 0) {
            rightOff.setTextColor(Color.RED);
            rightHigh.setTextColor(Color.WHITE);
            rightSim.setTextColor(Color.WHITE);

        } else if (rightFlameMode == 1) {
            rightHigh.setTextColor(Color.RED);
            rightOff.setTextColor(Color.WHITE);
            rightSim.setTextColor(Color.WHITE);

        } else if (rightFlameMode == 2) {

            rightSim.setTextColor(Color.RED);
            rightHigh.setTextColor(Color.WHITE);
            rightOff.setTextColor(Color.WHITE);
        }

        rightOff.setTypeface(octinPrisonFont);
        rightHigh.setTypeface(octinPrisonFont);
        rightSim.setTypeface(octinPrisonFont);

        //Left
        if (leftVessel == 0) {
            selectedLeftVessel.setVisibility(View.INVISIBLE);
        } else if (leftVessel == 1) {
            selectedLeftVessel.setVisibility(View.VISIBLE);


        }

        if (leftFlameMode == 0) {
            leftHigh.setTextColor(Color.WHITE);
            leftOff.setTextColor(Color.RED);
            leftSim.setTextColor(Color.WHITE);

        } else if (leftFlameMode == 1) {
            leftHigh.setTextColor(Color.RED);
            leftOff.setTextColor(Color.WHITE);
            leftSim.setTextColor(Color.WHITE);

        } else if (leftFlameMode == 2) {
            leftHigh.setTextColor(Color.WHITE);
            leftOff.setTextColor(Color.WHITE);
            leftSim.setTextColor(Color.RED);
        }

        leftOff.setTypeface(octinPrisonFont);
        leftSim.setTypeface(octinPrisonFont);
        leftHigh.setTypeface(octinPrisonFont);

        //Center
        //Left
        if (centerVessel == 0) {
            selectedCenterVessel.setVisibility(View.INVISIBLE);
        } else if (centerVessel == 1) {
            selectedCenterVessel.setVisibility(View.VISIBLE);


        }

        if (centerFlameMode == 0) {
            centerOff.setTextColor(Color.RED);
            centerHigh.setTextColor(Color.WHITE);
            centerSim.setTextColor(Color.WHITE);

        } else if (centerFlameMode == 1) {
            centerOff.setTextColor(Color.WHITE);
            centerHigh.setTextColor(Color.RED);
            centerSim.setTextColor(Color.WHITE);

        } else if (centerFlameMode == 2) {
            centerOff.setTextColor(Color.WHITE);
            centerHigh.setTextColor(Color.WHITE);
            centerSim.setTextColor(Color.RED);

        }

        centerOff.setTypeface(octinPrisonFont);
        centerSim.setTypeface(octinPrisonFont);
        centerHigh.setTypeface(octinPrisonFont);


    }

    private void runOnUiThread(Runnable runnable) {
        if (isAdded() && getActivity() != null)
            getActivity().runOnUiThread(runnable);
    }


    public void showData() {
        BluetoothGattService service = ((OperationActivity) getActivity()).getBluetoothGattService();
        System.out.println("CharacteristicListFragment" + service.getCharacteristics());
        mResultAdapter.clear();
        for (BluetoothGattCharacteristic characteristic : service.getCharacteristics()) {
            mResultAdapter.addResult(characteristic);
        }
        mResultAdapter.notifyDataSetChanged();
    }


    private class ResultAdapter extends BaseAdapter {

        private Context context;
        private List<BluetoothGattCharacteristic> characteristicList;

        ResultAdapter(Context context) {
            this.context = context;
            characteristicList = new ArrayList<>();
        }

        void addResult(BluetoothGattCharacteristic characteristic) {
            characteristicList.add(characteristic);
        }

        void clear() {
            characteristicList.clear();
        }

        @Override
        public int getCount() {
            SIZE_OF_CHARACTERISTIC = characteristicList.size();
            return characteristicList.size();
        }

        @Override
        public BluetoothGattCharacteristic getItem(int position) {
            if (position > characteristicList.size())
                return null;
            return characteristicList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView != null) {
                holder = (ViewHolder) convertView.getTag();
            } else {
                convertView = View.inflate(context, R.layout.adapter_service, null);
                holder = new ViewHolder();
                convertView.setTag(holder);
                holder.txt_title = (TextView) convertView.findViewById(R.id.txt_title);
                holder.txt_uuid = (TextView) convertView.findViewById(R.id.txt_uuid);
                holder.txt_type = (TextView) convertView.findViewById(R.id.txt_type);
                holder.img_next = (ImageView) convertView.findViewById(R.id.img_next);
            }

            BluetoothGattCharacteristic characteristic = characteristicList.get(position);
            String uuid = characteristic.getUuid().toString();

            holder.txt_title.setText(String.valueOf(getActivity().getString(R.string.characteristic) + "（" + position + ")"));
            holder.txt_uuid.setText(uuid);

            StringBuilder property = new StringBuilder();
            int charaProp = characteristic.getProperties();
            if ((charaProp & BluetoothGattCharacteristic.PROPERTY_READ) > 0) {
                property.append("Read");
                property.append(" , ");
            }
            if ((charaProp & BluetoothGattCharacteristic.PROPERTY_WRITE) > 0) {
                property.append("Write");
                property.append(" , ");
            }
            if ((charaProp & BluetoothGattCharacteristic.PROPERTY_WRITE_NO_RESPONSE) > 0) {
                property.append("Write No Response");
                property.append(" , ");
            }
            if ((charaProp & BluetoothGattCharacteristic.PROPERTY_NOTIFY) > 0) {
                property.append("Notify");
                property.append(" , ");
            }
            if ((charaProp & BluetoothGattCharacteristic.PROPERTY_INDICATE) > 0) {
                property.append("Indicate");
                property.append(" , ");
            }
            if (property.length() > 1) {
                property.delete(property.length() - 2, property.length() - 1);
            }
            if (property.length() > 0) {
                holder.txt_type.setText(String.valueOf(getActivity().getString(R.string.characteristic) + "( " + property.toString() + ")"));
                holder.img_next.setVisibility(View.VISIBLE);
            } else {
                holder.img_next.setVisibility(View.INVISIBLE);
            }

            return convertView;
        }

        class ViewHolder {
            TextView txt_title;
            TextView txt_uuid;
            TextView txt_type;
            ImageView img_next;
        }


    }


    private void callMenuItems() {

        Intent menuIntent = new Intent(this.getActivity(), MenuActivity.class);
        startActivity(menuIntent);

    }

    private void hideStatusBar() {

        //Hides status bar
        getActivity().getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        currentApiVersion = android.os.Build.VERSION.SDK_INT;
        final int flags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        if (currentApiVersion >= Build.VERSION_CODES.KITKAT) {
            getActivity().getWindow().getDecorView().setSystemUiVisibility(flags);
            final View decorView = getActivity().getWindow().getDecorView();
            decorView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
                @Override
                public void onSystemUiVisibilityChange(int visibility) {
                    if ((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0) {
                        decorView.setSystemUiVisibility(flags);
                    }
                }
            });
        }

    }


    private void getImageFromSqliteDB() {

        StoreImageDTO storeImageDTO = sqliteManager.getImage();

        if (storeImageDTO.getImage() == null) {
            seletedUserProfile.setImageDrawable(getResources().getDrawable(R.drawable.person));
            //System.out.println("ImageIsNotAvaliable");
        } else if (storeImageDTO.getImage() != null) {
            seletedUserProfile.setImageBitmap(storeImageDTO.getImage());

        }


    }

    private void stopBlinking(ImageView bluetoothIcon) {

        AnimationUtil.stopBlinking(getActivity(),bluetoothIcon);

    }

    private void startBlinking(ImageView bluetoothIcon) {

        AnimationUtil.startBlinking(getActivity(),bluetoothIcon);

    }

    private void getFont() {

        octinPrisonFont = FontUtil.getOctinPrisonFont(getActivity());
    }

    private void callSnackBar(String burnerStatus, View v) {

        snack = Snackbar.make(v, burnerStatus, Snackbar.LENGTH_SHORT);
        View view = snack.getView();
        TextView tv = (TextView) view.findViewById(android.support.design.R.id.snackbar_text);
        tv.setTypeface(octinPrisonFont);
        tv.setTextColor(Color.RED);
        snack.show();
    }

    private void setTimerVisible() {
        setTimerBlock.setVisibility(View.VISIBLE);
        setWhistleBlock.setVisibility(View.INVISIBLE);
    }

    private void setWhistleVisible() {
        setWhistleBlock.setVisibility(View.VISIBLE);
        setTimerBlock.setVisibility(View.INVISIBLE);
    }

    private void setInvisibleTimerAndWhistle() {
        setTimerBlock.setVisibility(View.INVISIBLE);
        setWhistleBlock.setVisibility(View.INVISIBLE);
    }

    private void setNonReadNotificationCount() {

        nonReadNotiCount = sqliteManager.getNonReadNotifications();

        int count = nonReadNotiCount.size();

        if (count > 0) {
            if (count < 100) {
                notificationCount.setText("" + count);
            } else {
                notificationCount.setText("99");
                notificationCount.setTypeface(octinPrisonFont);
            }

        } else {

        }


    }

    private void startBGTimerTast(int i) {
        //viewButton.setVisibility(View.VISIBLE);
        //set a new Timer
        timer = new Timer();

        //initialize the TimerTask's job
        initializeTimerTask();

        //schedule the timer, after the first 5000ms the TimerTask will run every 10000ms
        timer.schedule(timerTask, 3000);
        //timer.schedule(timerTask, 1000, 0); //
    }

    private void initializeTimerTask() {
        timerTask = new TimerTask() {

            @Override
            public void run() {

                setInvisibleTimerAndWhistle();


            }
        };
    }

    private void stopBGTimerTask(int i) {
               //stop the timer, if it's not already null
        if (timer != null) {
            timer.cancel();
            if (i == 1) {
                setTimerVisible();
            } else if (i == 2) {
                setWhistleVisible();
            }

            timer = null;
        }


    }

}
