package com.clj.blesample.operation;


import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
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
import android.widget.TextView;
import android.widget.Toast;

import com.clj.blesample.R;
import com.clj.blesample.menuoperationactivity.EditActivity;
import com.clj.blesample.menuoperationactivity.MenuActivity;
import com.clj.blesample.menuoperationactivity.NotificationActivity;
import com.clj.blesample.sessionmanager.PreferencesUtil;
import com.clj.blesample.utils.FontUtil;
import com.clj.fastble.BleManager;
import com.clj.fastble.callback.BleNotifyCallback;
import com.clj.fastble.callback.BleWriteCallback;
import com.clj.fastble.data.BleDevice;
import com.clj.fastble.exception.BleException;
import com.skyfishjy.library.RippleBackground;

import java.util.ArrayList;
import java.util.List;

@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
public class CharacteristicListFragment extends Fragment {


    private ResultAdapter mResultAdapter;

    //MyCode
    BluetoothGattCharacteristic characteristic;
    List<Integer> propList = new ArrayList<>();
    List<String> propNameList = new ArrayList<>();


    int SIZE_OF_CHARACTERISTIC = 0;


    int currentApiVersion;

    Button leftBurner, leftBurnerSettings, leftBurnerEdit, centerBurner, centerBurnerSettings, centerBurnerEdit, rightBurner, rightBurnerSettings, rightBurnerEdit;
    ImageView menuIcon, vesselLeft, vesselCenter, vesselRight, timerLeft, timerCenter, timerRight;

    byte[] currentByte, currentByte1;

    String left = "00", center = "01", right = "10";


    TextView selectLeft, selectCenter, selectRight, selectSim, selectHigh, selectOff;
    ImageView notificationIcon;

    ImageView selectedLeftWhistle,selectedRightWhistle,selectedCenterWhistle,selectedLeftTimer,selectedRightTimer,selectedCenterTimer;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

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

        //end of status bar
        View v = inflater.inflate(R.layout.fragment_characteric_list, null);

        initView(v);


        return v;
    }


    @Override
    public void onResume() {
        super.onResume();

        //Calls Notify
        if (SIZE_OF_CHARACTERISTIC == 2 && mResultAdapter != null) {

            Toast.makeText(getActivity(), "NotifyCalled", Toast.LENGTH_LONG).show();

            callMe(0, null, 0, 0, 0);

        }


        String selectedBurner = PreferencesUtil.getValueString(getActivity(), PreferencesUtil.BURNER);
        int selectedTimer = PreferencesUtil.getValueInt(getActivity(), PreferencesUtil.TIMER_IN_MINUTE);
        int selectedWhistle = PreferencesUtil.getValueInt(getActivity(), PreferencesUtil.WHISTLE_IN_COUNT);
        int selectedFlameModde = PreferencesUtil.getValueInt(getActivity(), PreferencesUtil.FLAME_MODE);

        if (selectedBurner.equals("no_value") && selectedTimer <= 0 && selectedWhistle <= 0 && selectedFlameModde <= 0) {
            Toast.makeText(getActivity(), "Empty Write Data", Toast.LENGTH_LONG).show();
        } else {

            if (selectedBurner.equals("00")) {
                leftBurnerSettings.setVisibility(View.INVISIBLE);
                leftBurnerEdit.setVisibility(View.VISIBLE);
            } else if (selectedBurner.equals("01")) {
                centerBurnerSettings.setVisibility(View.INVISIBLE);
                centerBurnerEdit.setVisibility(View.VISIBLE);
            } else if (selectedBurner.equals("10")) {
                rightBurnerSettings.setVisibility(View.INVISIBLE);
                rightBurnerEdit.setVisibility(View.VISIBLE);
            }

            Toast.makeText(getActivity(), "WriteCalled", Toast.LENGTH_LONG).show();

            System.out.println("ReceivedStoredPreferenceValue" + selectedBurner + " " + selectedTimer + " " + selectedWhistle + " " + selectedFlameModde);

            //Calls Write
            if (SIZE_OF_CHARACTERISTIC == 2 && mResultAdapter != null) {

                //callMe(1, selectedBurner, selectedTimer, selectedWhistle, selectedFlameModde);

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

        selectLeft = (TextView) v.findViewById(R.id.selectLeft);
        selectCenter = (TextView) v.findViewById(R.id.selectCenter);
        selectRight = (TextView) v.findViewById(R.id.selectRight);

        selectSim = (TextView) v.findViewById(R.id.selectSim);
        selectHigh = (TextView) v.findViewById(R.id.selectHigh);
        selectOff = (TextView) v.findViewById(R.id.selectOff);

        selectedLeftWhistle=(ImageView)v.findViewById(R.id.selectedLeftWhistle);
        selectedRightWhistle=(ImageView)v.findViewById(R.id.selectedRightWhistle);
        selectedCenterWhistle=(ImageView)v.findViewById(R.id.selectedCenterWhistle);


        selectedLeftTimer=(ImageView)v.findViewById(R.id.selectedLeftTimer);
        selectedRightTimer=(ImageView)v.findViewById(R.id.selectedRightTimer);
        selectedCenterTimer=(ImageView)v.findViewById(R.id.selectedCenterTimer);



        notificationIcon = (ImageView) v.findViewById(R.id.notificationIcon);


        selectedLeftWhistle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                callEditActivity();

            }
        });

        selectedRightWhistle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callEditActivity();
            }
        });

        selectedCenterWhistle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callEditActivity();
            }
        });

        selectedLeftTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callEditActivity();
            }
        });

        selectedRightTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callEditActivity();
            }
        });

        selectedCenterTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callEditActivity();
            }
        });



        selectLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                selectLeft.setBackground(getResources().getDrawable(R.drawable.edit_button_border_on));
                selectCenter.setBackground(getResources().getDrawable(R.drawable.edit_button_border_off));
                selectRight.setBackground(getResources().getDrawable(R.drawable.edit_button_border_off));

            }
        });

        selectCenter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                selectCenter.setBackground(getResources().getDrawable(R.drawable.edit_button_border_on));
                selectLeft.setBackground(getResources().getDrawable(R.drawable.edit_button_border_off));
                selectRight.setBackground(getResources().getDrawable(R.drawable.edit_button_border_off));

            }
        });

        selectRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                selectRight.setBackground(getResources().getDrawable(R.drawable.edit_button_border_on));
                selectCenter.setBackground(getResources().getDrawable(R.drawable.edit_button_border_off));
                selectLeft.setBackground(getResources().getDrawable(R.drawable.edit_button_border_off));


            }
        });

        selectSim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                selectSim.setBackgroundColor(getResources().getColor(R.color.burner_on_green));
                selectHigh.setBackground(getResources().getDrawable(R.drawable.rounded_border));
                selectOff.setBackground(getResources().getDrawable(R.drawable.rounded_border));
            }
        });

        selectHigh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                selectHigh.setBackgroundColor(getResources().getColor(R.color.burner_on_green));
                selectSim.setBackground(getResources().getDrawable(R.drawable.rounded_border));
                selectOff.setBackground(getResources().getDrawable(R.drawable.rounded_border));

            }
        });

        selectOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                selectOff.setBackgroundColor(getResources().getColor(R.color.burner_on_green));
                selectHigh.setBackground(getResources().getDrawable(R.drawable.rounded_border));
                selectSim.setBackground(getResources().getDrawable(R.drawable.rounded_border));


            }
        });

        notificationIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), NotificationActivity.class);
                startActivity(intent);

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

    private void callEditActivity() {

        Intent intent=new Intent(getActivity(),EditActivity.class);
        startActivity(intent);

    }


    private void callMe(int position, String burner, int timerInMin, int whistleInCount, int flameMode) {

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
        if (propList.size() > 0 && position == 1) {
            ((OperationActivity) getActivity()).setCharacteristic(characteristic);
            ((OperationActivity) getActivity()).setCharaProp(propList.get(0));
            //((OperationActivity) getActivity()).changePage(2);
            wrietUserData(burner, timerInMin, whistleInCount, flameMode);
        }


    }


    private void wrietUserData(String burner, int timerInMin, int whistleInCount, int flameMode) {


        byte[] startBurner = new byte[9];


        startBurner[0] = (byte) ('*');
        startBurner[1] = (byte) (0xC0);
                /*secondFrame[2] = (byte) (burner);
                secondFrame[3] = (byte) ();
                secondFrame[4] = (byte) ();
                secondFrame[5] = (byte) (leftBurnerTimer);
                secondFrame[6] = (byte) (rightBurnerWhistle);
                secondFrame[7] = (byte) (rightBurnerTimer);*/
        startBurner[8] = (byte) ('#');


        BleDevice bleDevice = ((OperationActivity) getActivity()).getBleDevice();
        BluetoothGattCharacteristic characteristic = ((OperationActivity) getActivity()).getCharacteristic();


        BleManager.getInstance().write(
                bleDevice,
                characteristic.getService().getUuid().toString(),
                characteristic.getUuid().toString(),
                startBurner,
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


        byte[] gcp=new byte[17];
        gcp=data;

        //Gas Consumption Pattern
        if (data.length == 17) {

            if (data[0] == 42 && data[16] == 35) {

                int date = data[2];
                int month = data[3];
                int rightBurner = (data[4]&0x0FF) << 0 | (data[5]&0x0FF) << 8 | (data[6]&0x0FF) << 16 | (data[7]&0x0FF) << 24;
                int leftBurner = (data[8]&0x0FF) << 0 | (data[9]&0x0FF) << 8 | (data[10]&0x0FF) << 16 | (data[11]&0x0FF) << 24;
                int centerBurner = (data[12]&0x0FF) << 0 | (data[13]&0x0FF) << 8 | (data[14]&0x0FF) << 16 | (data[15]&0x0FF) << 24;

                float right = rightBurner / 4096;
                float left = leftBurner / 4096;
                long center = centerBurner / 4096;



                System.out.println("GasUsage" + right + " " + left + " " + center);

                System.out.println("ReceivedGCP" + date + " " + " " + month + " " + rightBurner + " " + leftBurner + " " + centerBurner);


            }

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


}
