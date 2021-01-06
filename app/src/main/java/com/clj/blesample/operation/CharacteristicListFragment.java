package com.clj.blesample.operation;


import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.clj.blesample.R;
import com.clj.blesample.databasemanager.SqliteManager;
import com.clj.blesample.menuoperationactivity.EditActivity;
import com.clj.blesample.menuoperationactivity.MenuActivity;
import com.clj.blesample.menuoperationactivity.NotificationActivity;
import com.clj.blesample.menuoperationactivity.ProfileSettingsActivity;
import com.clj.blesample.model.NotificationResponseDTO;
import com.clj.blesample.model.StoreImageDTO;
import com.clj.blesample.sessionmanager.PreferencesUtil;
import com.clj.blesample.utils.FontUtil;
import com.clj.blesample.utils.MathUtil;
import com.clj.fastble.BleManager;
import com.clj.fastble.callback.BleNotifyCallback;
import com.clj.fastble.callback.BleWriteCallback;
import com.clj.fastble.data.BleDevice;
import com.clj.fastble.exception.BleException;
import com.sdsmdg.harjot.crollerTest.Croller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

    TextView  menuIcon;
    ImageView notificationIcon;
    Typeface octinPrisonFont;

    ImageView selectedLeftWhistle, selectedRightWhistle, selectedCenterWhistle, selectedLeftTimer, selectedRightTimer, selectedCenterTimer;

    ImageView selectedRightVessel, selectedLeftVessel, selectedCenterVessel;

    TextView selectedRightWhistleCount, selectedRightTimerCount, selectedLeftWhistleCount, selectedLeftTimerCount, selectedCenterWhistleCount, selectedCenterTimerCount;

    private SqliteManager sqliteManager;

    Croller leftCroller,rightCroller,centerCroller;

    String selectedBurner;

    int setNotification = 0;
    TextView notificationCount;

    List<NotificationResponseDTO> nonReadNotiCount;


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

        getImageFromSqliteDB();

        //Calls Notify
        if (SIZE_OF_CHARACTERISTIC == 2 && mResultAdapter != null) {

            Toast.makeText(getActivity(), "NotifyCalled", Toast.LENGTH_LONG).show();

            callMe(0, null, 0, 0, 0, 0);

        }


        setNonReadNotificationCount();


        String selectedBurner = PreferencesUtil.getValueString(getActivity(), PreferencesUtil.BURNER);
        int selectedTimer = PreferencesUtil.getValueInt(getActivity(), PreferencesUtil.TIMER_IN_MINUTE);
        int selectedWhistle = PreferencesUtil.getValueInt(getActivity(), PreferencesUtil.WHISTLE_IN_COUNT);
        //int selectedFlameModde = PreferencesUtil.getValueInt(getActivity(), PreferencesUtil.FLAME_MODE);

        if (selectedBurner.equals("no_value") && selectedTimer <= 0 && selectedWhistle <= 0) {
            Toast.makeText(getActivity(), "Empty Write Data", Toast.LENGTH_LONG).show();
        } else {

            Toast.makeText(getActivity(), "WriteCalled", Toast.LENGTH_LONG).show();

            System.out.println("ReceivedStoredPreferenceValue" + selectedBurner + " " + selectedTimer + " " + selectedWhistle);

            //Calls Write
            if (SIZE_OF_CHARACTERISTIC == 2 && mResultAdapter != null) {

                callMe(1, selectedBurner, selectedTimer, selectedWhistle, 0, MathUtil.EDIT_FORMET);

                PreferencesUtil.remove(getActivity(), PreferencesUtil.BURNER);
                PreferencesUtil.remove(getActivity(), PreferencesUtil.TIMER_IN_MINUTE);
                PreferencesUtil.remove(getActivity(), PreferencesUtil.WHISTLE_IN_COUNT);
                PreferencesUtil.remove(getActivity(), PreferencesUtil.FLAME_MODE);


            }

        }


    }


    private void initView(View v) {
        mResultAdapter = new ResultAdapter(getActivity());
        ListView listView_device = (ListView) v.findViewById(R.id.list_service_character);

        sqliteManager = new SqliteManager(getActivity());

        seletedUserProfile = (CircleImageView) v.findViewById(R.id.seletedUserProfile);
        selectBluetoothStatus=(ImageView)v.findViewById(R.id.selectBluetoothStatus);


        leftCroller=(Croller)v.findViewById(R.id.leftBurner);
        rightCroller=(Croller)v.findViewById(R.id.rightBurner);
        centerCroller=(Croller)v.findViewById(R.id.centerBurner);

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
        notificationIcon = (ImageView) v.findViewById(R.id.notificationIcon);

        menuIcon.setTypeface(octinPrisonFont);

        seletedUserProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ProfileSettingsActivity.class);
                startActivity(intent);
            }
        });


        selectedLeftWhistle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                callEditActivity(MathUtil.LEFT_BURNER);

            }
        });

        selectedLeftTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callEditActivity(MathUtil.LEFT_BURNER);
            }
        });


        selectedRightWhistle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callEditActivity(MathUtil.RIGHT_BURNER);
            }
        });

        selectedRightTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callEditActivity(MathUtil.RIGHT_BURNER);
            }
        });

        selectedCenterWhistle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callEditActivity(MathUtil.CENTER_BURNER);
            }
        });


        selectedCenterTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callEditActivity(MathUtil.CENTER_BURNER);
            }
        });








        notificationIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), NotificationActivity.class);
                startActivity(intent);

            }
        });

        menuIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callMenuItems();
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
    }

    private void callMe(int position, final String burner, final int timerInMin, final int whistleInCount, final int flameMode, final int frameFormet) {

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
        if (propList.size() > 0 && position == 1 && frameFormet == 2) {
            ((OperationActivity) getActivity()).setCharacteristic(characteristic);
            ((OperationActivity) getActivity()).setCharaProp(propList.get(0));
            //((OperationActivity) getActivity()).changePage(2);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    wrietUserData(burner, timerInMin, whistleInCount, flameMode, frameFormet);
                }
            }, 2000);


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
                                    Toast.makeText(getActivity(), "Write Success", Toast.LENGTH_LONG).show();
                                }
                            });
                        }

                        @Override
                        public void onWriteFailure(final BleException exception) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                    Toast.makeText(getActivity(), "Write Failed", Toast.LENGTH_LONG).show();

                                    System.out.println("Exception" + exception.toString());
                                }
                            });
                        }
                    });


        } else if (frameFormet == 2) {

            String selectedBurner = PreferencesUtil.getValueString(getActivity(), PreferencesUtil.BURNER);
            int selectedTimer = PreferencesUtil.getValueInt(getActivity(), PreferencesUtil.TIMER_IN_MINUTE);
            int selectedWhistle = PreferencesUtil.getValueInt(getActivity(), PreferencesUtil.WHISTLE_IN_COUNT);

            if (selectedBurner.equals("00")) {
                if (selectedWhistle > 0) {

                    sqliteManager.setNotification(selectedWhistle + " Whistle is set for Right Burner");
                    //sqliteManager.getNonReadNotifications();
                }
                if (selectedTimer > 0) {
                    sqliteManager.setNotification(selectedTimer + " Min Timer is set for Right Burner");
                    //sqliteManager.getNonReadNotifications();
                }
            } else if (selectedBurner.equals("01")) {

                if (selectedWhistle > 0) {
                    sqliteManager.setNotification(selectedWhistle + " Whistle is set for Left Burner");
                    //sqliteManager.getNonReadNotifications();
                }
                if (selectedTimer > 0) {
                    sqliteManager.setNotification(selectedTimer + " Min Timer is set for Left Burner");
                    //sqliteManager.getNonReadNotifications();
                }

            } else if (selectedBurner.equals("10")) {
                if (selectedWhistle > 0) {
                    sqliteManager.setNotification(selectedWhistle + " Whistle is set for Center Burner");
                    //sqliteManager.getNonReadNotifications();
                }
                if (selectedTimer > 0) {
                    sqliteManager.setNotification(selectedTimer + " Min Timer is set for Center Burner");
                    //sqliteManager.getNonReadNotifications();
                }
            }

            setNonReadNotificationCount();


            //Whistle and Timer

            byte[] timerOrWhistle = new byte[9];

            int rightTimer = 0, rightWhistle = 0;
            int leftTimer = 0, leftWhistle = 0;
            int centerTimer = 0, centerWhistle = 0;


            if (burner.equals(MathUtil.RIGHT_BURNER)) {

                rightTimer = timerInMin;
                rightWhistle = whistleInCount;


                leftTimer = 0xff;
                leftWhistle = 0xff;
                centerTimer = 0xff;
                centerWhistle = 0xff;


            } else if (burner.equals(MathUtil.LEFT_BURNER)) {

                leftTimer = timerInMin;
                leftWhistle = whistleInCount;


                rightTimer = 0xff;
                rightWhistle = 0xff;
                centerTimer = 0xff;
                centerWhistle = 0xff;


            } else if (burner.equals(MathUtil.CENTER_BURNER)) {

                centerTimer = timerInMin;
                centerWhistle = whistleInCount;

                rightTimer = 0xff;
                rightWhistle = 0xff;
                leftTimer = 0xff;
                leftWhistle = 0xff;


            }


            timerOrWhistle[0] = (byte) ('*');
            timerOrWhistle[1] = (byte) (0xC0);
            timerOrWhistle[2] = (byte) (rightWhistle);
            timerOrWhistle[3] = (byte) (rightTimer);
            timerOrWhistle[4] = (byte) (leftWhistle);
            timerOrWhistle[5] = (byte) (leftTimer);
            timerOrWhistle[6] = (byte) (centerWhistle);
            timerOrWhistle[7] = (byte) (centerTimer);
            timerOrWhistle[8] = (byte) ('#');


            BleDevice bleDevice = ((OperationActivity) getActivity()).getBleDevice();
            BluetoothGattCharacteristic characteristic = ((OperationActivity) getActivity()).getCharacteristic();


            Toast.makeText(getActivity(), "New Data Write", Toast.LENGTH_LONG).show();

            BleManager.getInstance().write(
                    bleDevice,
                    characteristic.getService().getUuid().toString(),
                    characteristic.getUuid().toString(),
                    timerOrWhistle,
                    new BleWriteCallback() {

                        //Converting byte to String and displaying to user
                        @Override
                        public void onWriteSuccess(final int current, final int total, final byte[] justWrite) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                    Toast.makeText(getActivity(), "Whistle and Timer Is Set", Toast.LENGTH_LONG).show();

                                }
                            });
                        }

                        @Override
                        public void onWriteFailure(final BleException exception) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                    Toast.makeText(getActivity(), "Whistle and Timer Is Not Set", Toast.LENGTH_LONG).show();

                                    System.out.println("TimerException" + exception.toString());
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


        //Timer and Whistle
        if (data.length == 9) {
            System.out.println("Length9Recevied");
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

                System.out.println("WhistleAndTimerData" + rightWhistle + " " + rightTimer + " " + leftWhistle + " " + leftTimer + " " + centerWhistle + " " + centerTimer);

                setWhistleAndTimerNotification(rightWhistle, rightTimer, leftWhistle, leftTimer, centerWhistle, centerTimer);


            }

        }

        //Burner
        if (data.length == 7) {
            System.out.println("Length7Recevied");
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

                if (batteryPercentage <= 30) {
                    sqliteManager.setNotification("Your Battery is Criticaly Low");
                }


                System.out.println("VesselAndFlameMode" + rightVessel + " " + rightFlameMode + " " + leftVessel + " " + leftFlameMode + " " + centerVessel + " " + centerFlameMode + " " + batteryPercentage);


                setValueInUI(rightVessel, rightFlameMode, leftVessel, leftFlameMode, centerVessel, centerFlameMode);

                setNotification = 1;


            }

        }

        //Gas Consumption Pattern
        if (data.length == 17) {

            if (data[0] == 42 && data[16] == 35) {

                int date = data[2];
                int month = data[3];

                String dateFormation = date + "/" + month + "/" + "2020";

                Date dateFormet = null;
                try {
                    dateFormet = new SimpleDateFormat("dd/MM/yyyy").parse(dateFormation);

                } catch (ParseException e) {
                    e.printStackTrace();
                }

                int rightBurner = (data[4] & 0x0FF) << 0 | (data[5] & 0x0FF) << 8 | (data[6] & 0x0FF) << 16 | (data[7] & 0x0FF) << 24;
                int leftBurner = (data[8] & 0x0FF) << 0 | (data[9] & 0x0FF) << 8 | (data[10] & 0x0FF) << 16 | (data[11] & 0x0FF) << 24;
                int centerBurner = (data[12] & 0x0FF) << 0 | (data[13] & 0x0FF) << 8 | (data[14] & 0x0FF) << 16 | (data[15] & 0x0FF) << 24;

                float rightBurGasUsage = rightBurner / 4096;

                System.out.println("DateFormet " + dateFormet + " " + rightBurGasUsage);

                sqliteManager.addGasConsumptionPattern(dateFormet, rightBurGasUsage, "00");

                float leftBurGasUsage = leftBurner / 4096;
                sqliteManager.addGasConsumptionPattern(dateFormet, leftBurGasUsage, "01");

                float centerBurGasUsage = centerBurner / 4096;
                sqliteManager.addGasConsumptionPattern(dateFormet, centerBurGasUsage, "10");


                /*System.out.println("GasUsage" +date+" "+month+" " + right + " " + left + " " + center);

                System.out.println("ReceivedGCP" + date + " " + " " + month + " " + rightBurner + " " + leftBurner + " " + centerBurner);*/


            }

        }


    }

    private void setWhistleAndTimerNotification(int rightWhistle, int rightTimer, int leftWhistle, int leftTimer, int centerWhistle, int centerTimer) {

        if (rightWhistle == 0) {
            sqliteManager.setNotification("Whistle is completed for Right Burner");

        }

        if (rightTimer == 0) {
            sqliteManager.setNotification("Timer is completed for Right Burner");
        }

        if (leftWhistle == 0) {
            sqliteManager.setNotification("Whistle is completed for Left Burner");
        }

        if (leftTimer == 0) {
            sqliteManager.setNotification("Timer is completed for Left Burner");
        }

        if (centerWhistle == 0) {
            sqliteManager.setNotification("Whistle is completed for Center Burner");
        }

        if (centerTimer == 0) {
            sqliteManager.setNotification("Timer is completed for Center Burner");
        }

        setNonReadNotificationCount();


    }

    private void setNonReadNotificationCount() {

        nonReadNotiCount = sqliteManager.getNonReadNotifications();

        int count = nonReadNotiCount.size();

        if (count > 0) {
            notificationCount.setText("" + count);
        } else {

        }


    }


    private void setWhistleAndTimerValueInUI(int rightWhistle, int rightTimer, int leftWhistle, int leftTimer, int centerWhistle, int centerTimer) {

        selectedRightWhistleCount.setText("" + rightWhistle);
        selectedRightWhistleCount.setTypeface(octinPrisonFont);
        selectedRightTimerCount.setText("" + rightTimer + "min");
        selectedRightTimerCount.setTypeface(octinPrisonFont);

        selectedLeftWhistleCount.setText("" + leftWhistle);
        selectedLeftWhistleCount.setTypeface(octinPrisonFont);
        selectedLeftTimerCount.setText("" + leftTimer + "min");
        selectedLeftTimerCount.setTypeface(octinPrisonFont);


        selectedCenterWhistleCount.setText("" + centerWhistle);
        selectedCenterWhistleCount.setTypeface(octinPrisonFont);
        selectedCenterTimerCount.setText("" + centerTimer + "min");
        selectedCenterTimerCount.setTypeface(octinPrisonFont);


    }


    private void setValueInUI(int rightVessel, int rightFlameMode, int leftVessel, int leftFlameMode, int centerVessel, int centerFlameMode) {

        //Right
        if (rightVessel == 0) {

            selectedRightVessel.setVisibility(View.INVISIBLE);


        } else if (rightVessel == 1) {
            selectedRightVessel.setVisibility(View.VISIBLE);


        }

        if (rightFlameMode == 1) {



        } else if (rightFlameMode == 2) {



        } else if (rightFlameMode == 3) {



        }

        //Left
        if (leftVessel == 0) {
            selectedLeftVessel.setVisibility(View.INVISIBLE);
        } else if (leftVessel == 1) {
            selectedLeftVessel.setVisibility(View.VISIBLE);


        }

        if (leftFlameMode == 1) {



        } else if (leftFlameMode == 2) {


        } else if (leftFlameMode == 3) {

        }

        //Center
        //Left
        if (centerVessel == 0) {
            selectedCenterVessel.setVisibility(View.INVISIBLE);
        } else if (centerVessel == 1) {
            selectedCenterVessel.setVisibility(View.VISIBLE);


        }

        if (centerFlameMode == 1) {


        } else if (centerFlameMode == 2) {


        } else if (centerFlameMode == 3) {


        }


    }

   /* private void CallEditActivity(String burner) {
        currentByte = new byte[4];
        currentByte1 = new byte[5];

        currentByte[0] = 0;
        currentByte[1] = 1;
        currentByte[2] = 0;
        currentByte[3] = 1;

        currentByte1[0] = 1;
        currentByte1[1] = 1;
        currentByte1[2] = 1;
        currentByte1[3] = 1;
        currentByte1[4] = 1;

        System.out.println("MyLength" + currentByte.length);

        ArrayList<byte[]> arrayList = new ArrayList<>();
        arrayList.add(currentByte);
        arrayList.add(currentByte1);

        Intent intent = new Intent(getActivity(), EditActivity.class);
        //intent.putExtra("currentByte_size", arrayList.size());
        intent.putExtra("BURNER", burner);
        intent.putExtra("currentByteArrayList", arrayList);
        *//*for (int i = 0; i < arrayList.size(); i++) {
            intent.putExtra("currentByte" + i, arrayList.get(i));
        }
*//*
        startActivity(intent);


    }*/


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

    private void callAlertDialog() {

        final AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
        // alert.setTitle("Vessel is not detected");
        alert.setMessage("Please Place Vessel");
        alert.setIcon(R.drawable.preethi_logo);

        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();

            }
        });

        alert.show();

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

    private String intToString(int data) {
        String val = String.valueOf(data);

        return val;

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


    private void callEditActivity(String burner) {

        Intent intent = new Intent(getActivity(), EditActivity.class);
        intent.putExtra("BURNER", burner);
        startActivity(intent);

    }

    private void getImageFromSqliteDB() {

        StoreImageDTO storeImageDTO = sqliteManager.getImage();

        if (storeImageDTO.getImage() == null) {
            seletedUserProfile.setImageDrawable(getResources().getDrawable(R.drawable.person));
            System.out.println("ImageIsNotAvaliable");
        } else if (storeImageDTO.getImage() != null) {
            seletedUserProfile.setImageBitmap(storeImageDTO.getImage());

        }


    }

    private void stopBlinking(ImageView bluetoothIcon) {
        bluetoothIcon.clearAnimation();
    }

    private void startBlinking(ImageView bluetoothIcon) {

        Animation animation = new AlphaAnimation(1, 0); //to change visibility from visible to invisible
        animation.setDuration(1000); //1 second duration for each animation cycle
        animation.setInterpolator(new LinearInterpolator());
        animation.setRepeatCount(Animation.INFINITE); //repeating indefinitely
        animation.setRepeatMode(Animation.REVERSE); //animation will start from end point once ended.
        bluetoothIcon.startAnimation(animation);

    }

    private void getFont() {

        octinPrisonFont = FontUtil.getOctinPrisonFont(getActivity());

    }


}
