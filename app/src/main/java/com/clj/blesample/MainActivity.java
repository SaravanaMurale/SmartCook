package com.clj.blesample;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothGatt;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.clj.blesample.adapter.DeviceAdapter;
import com.clj.blesample.comm.ObserverManager;
import com.clj.blesample.menuoperationactivity.MenuActivity;
import com.clj.blesample.operation.OperationActivity;
import com.clj.blesample.sessionmanager.PreferencesUtil;
import com.clj.blesample.utils.FontUtil;
import com.clj.blesample.utils.MathUtil;
import com.clj.fastble.BleManager;
import com.clj.fastble.callback.BleGattCallback;
import com.clj.fastble.callback.BleMtuChangedCallback;
import com.clj.fastble.callback.BleRssiCallback;
import com.clj.fastble.callback.BleScanCallback;
import com.clj.fastble.data.BleDevice;
import com.clj.fastble.exception.BleException;
import com.clj.fastble.scan.BleScanRuleConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int REQUEST_CODE_OPEN_GPS = 1;
    private static final int REQUEST_CODE_PERMISSION_LOCATION = 2;

    private static final int REQ_CODE_TO_ENABLE = 1;

    private LinearLayout layout_setting;
    private TextView txt_setting;
    private Button btn_scan;
    private EditText et_name, et_mac, et_uuid;
    private Switch sw_auto;
    private ImageView img_loading;

    private Animation operatingAnim;
    private DeviceAdapter mDeviceAdapter;
    private ProgressDialog progressDialog;

    private Switch switchShow;
    private TextView switchStatus, bleConnectStatus;

    //Dialog dialog;

    ListView listView_device;

    Typeface octinPrisonFont;

    LinearLayout mainLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);


        getFont();

        initView();

        String connectedDevice = PreferencesUtil.getValueString(MainActivity.this, PreferencesUtil.BLE_MAC_ADDRESS);

        if (connectedDevice.equals("no_value")) {

            progressDialog.dismiss();
            mainLayout.setVisibility(View.VISIBLE);

        } else {
            //dialog=MathUtil.showProgressBar(MainActivity.this);

            progressDialog.setMessage("Connecting Please Wait");
            progressDialog.show();
            mainLayout.setVisibility(View.INVISIBLE);
        }

        //System.out.println("ConnectedDeviceDetail"+connectedDevice);


        BleManager.getInstance().init(getApplication());
        BleManager.getInstance()
                .enableLog(true)
                .setReConnectCount(1, 2000)
                .setConnectOverTime(10000)
                .setOperateTimeout(3000);

        /*BleManager.getInstance().init(getApplication());
        BleManager.getInstance()
                .enableLog(true)
                .setReConnectCount(1, 5000)
                .setConnectOverTime(20000)
                .setOperateTimeout(5000);*/

        //startScan();
        checkPermissions();


        switchShow.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                System.out.println("SwitchClickedStatus" + isChecked);

                if (isChecked) {

                    checkPermissions();

                    listView_device.setVisibility(View.VISIBLE);


                    //Toast.makeText(MainActivity.this, "Switch On", Toast.LENGTH_LONG).show();
                    switchStatus.setText("ON");


                } else {

                    BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                    if (mBluetoothAdapter.isEnabled()) {
                        mBluetoothAdapter.disable();
                    }

                    PreferencesUtil.remove(MainActivity.this, PreferencesUtil.BLE_MAC_ADDRESS);

                    listView_device.setVisibility(View.INVISIBLE);
                    mDeviceAdapter.clear();
                    mDeviceAdapter.notifyDataSetChanged();
                    switchStatus.setText("OFF");
                    bleConnectStatus.setText("Please Turn On Bluetooth");
                    BleManager.getInstance().cancelScan();


                }

            }
        });


    }


    @Override
    protected void onResume() {
        super.onResume();

        showConnectedDevice();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        BleManager.getInstance().disconnectAllDevice();
        BleManager.getInstance().destroy();

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_scan:
                if (btn_scan.getText().equals(getString(R.string.start_scan))) {
                    checkPermissions();
                } else if (btn_scan.getText().equals(getString(R.string.stop_scan))) {
                    BleManager.getInstance().cancelScan();
                }
                break;


            case R.id.txt_setting:
                if (layout_setting.getVisibility() == View.VISIBLE) {
                    layout_setting.setVisibility(View.GONE);
                    txt_setting.setText(getString(R.string.expand_search_settings));
                } else {
                    layout_setting.setVisibility(View.VISIBLE);
                    txt_setting.setText(getString(R.string.retrieve_search_settings));
                }
                break;
        }

    }

    private void initView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //dialog = new Dialog(getApplicationContext());

        btn_scan = (Button) findViewById(R.id.btn_scan);
        btn_scan.setText(getString(R.string.start_scan));
        btn_scan.setOnClickListener(this);

        switchShow = (Switch) findViewById(R.id.switchShow);
        switchStatus = (TextView) findViewById(R.id.switchStatus);
        bleConnectStatus = (TextView) findViewById(R.id.bleConnectStatus);


        et_name = (EditText) findViewById(R.id.et_name);
        et_mac = (EditText) findViewById(R.id.et_mac);
        et_uuid = (EditText) findViewById(R.id.et_uuid);
        sw_auto = (Switch) findViewById(R.id.sw_auto);

        layout_setting = (LinearLayout) findViewById(R.id.layout_setting);
        txt_setting = (TextView) findViewById(R.id.txt_setting);
        txt_setting.setOnClickListener(this);
        layout_setting.setVisibility(View.GONE);
        txt_setting.setText(getString(R.string.expand_search_settings));

        img_loading = (ImageView) findViewById(R.id.img_loading);
        operatingAnim = AnimationUtils.loadAnimation(this, R.anim.rotate);
        operatingAnim.setInterpolator(new LinearInterpolator());

        progressDialog = new ProgressDialog(this);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setIcon(R.drawable.preethi_logo);
        progressDialog.setCancelable(false);

        //progressDialog.setTitle("Please Wait");


        mainLayout = (LinearLayout) findViewById(R.id.mainLayout);


        setFont();


        mDeviceAdapter = new DeviceAdapter(this);

        mDeviceAdapter.getFont(this);


        mDeviceAdapter.setOnDeviceClickListener(new DeviceAdapter.OnDeviceClickListener() {
            @Override
            public void onConnect(BleDevice bleDevice) {


                if (!BleManager.getInstance().isConnected(bleDevice)) {
                    BleManager.getInstance().cancelScan();
                    connect(bleDevice);

                }


            }

            @Override
            public void onDisConnect(final BleDevice bleDevice) {
                if (BleManager.getInstance().isConnected(bleDevice)) {

                    PreferencesUtil.remove(MainActivity.this, PreferencesUtil.BLE_MAC_ADDRESS);
                    bleConnectStatus.setText("Please Connect");
                    bleConnectStatus.setTypeface(octinPrisonFont);
                    BleManager.getInstance().disconnect(bleDevice);

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            startScan();
                        }
                    }, 500);


                }
            }

            @Override
            public void onDetail(BleDevice bleDevice) {
                if (BleManager.getInstance().isConnected(bleDevice)) {

                    if (bleDevice.getName().equals("Preethi")) {
                        Intent intent = new Intent(MainActivity.this, OperationActivity.class);
                        intent.putExtra(OperationActivity.KEY_DATA, bleDevice);
                        startActivity(intent);
                    } else {
                        //Connecting with other device
                        Toast.makeText(MainActivity.this, "Please Pair With Preethi Stove", Toast.LENGTH_SHORT).show();
                    }


                }
            }
        });
        //ListView listView_device = (ListView) findViewById(R.id.list_device);
        listView_device = (ListView) findViewById(R.id.list_device);
        listView_device.setAdapter(mDeviceAdapter);
    }


    private void showConnectedDevice() {
        List<BleDevice> deviceList = BleManager.getInstance().getAllConnectedDevice();
        // deviceList = BleManager.getInstance().getAllConnectedDevice();
        mDeviceAdapter.clearConnectedDevice();
        for (BleDevice bleDevice : deviceList) {
            mDeviceAdapter.addDevice(bleDevice);
        }
        mDeviceAdapter.notifyDataSetChanged();
    }

    private void setScanRule() {
        String[] uuids;
        String str_uuid = et_uuid.getText().toString();
        if (TextUtils.isEmpty(str_uuid)) {
            uuids = null;
        } else {
            uuids = str_uuid.split(",");
        }
        UUID[] serviceUuids = null;
        if (uuids != null && uuids.length > 0) {
            serviceUuids = new UUID[uuids.length];
            for (int i = 0; i < uuids.length; i++) {
                String name = uuids[i];
                String[] components = name.split("-");
                if (components.length != 5) {
                    serviceUuids[i] = null;
                } else {
                    serviceUuids[i] = UUID.fromString(uuids[i]);
                }
            }
        }

        String[] names;
        String str_name = et_name.getText().toString();
        if (TextUtils.isEmpty(str_name)) {
            names = null;
        } else {
            names = str_name.split(",");
        }

        String mac = et_mac.getText().toString();

        boolean isAutoConnect = sw_auto.isChecked();

        BleScanRuleConfig scanRuleConfig = new BleScanRuleConfig.Builder()
                .setServiceUuids(serviceUuids)      // 只扫描指定的服务的设备，可选
                .setDeviceName(true, names)   // 只扫描指定广播名的设备，可选
                .setDeviceMac(mac)                  // 只扫描指定mac的设备，可选
                .setAutoConnect(isAutoConnect)      // 连接时的autoConnect参数，可选，默认false
                //.setScanTimeOut(10000)              // 扫描超时时间，可选，默认10秒
                .setScanTimeOut(2000)              // 扫描超时时间，可选，默认10秒
                .build();
        BleManager.getInstance().initScanRule(scanRuleConfig);
    }

    private void startScan() {

        System.out.println("StartScanStarted");

        BleManager.getInstance().scan(new BleScanCallback() {
            @Override
            public void onScanStarted(boolean success) {

                mDeviceAdapter.clearScanDevice();
                mDeviceAdapter.notifyDataSetChanged();
                img_loading.startAnimation(operatingAnim);
                img_loading.setVisibility(View.VISIBLE);
                btn_scan.setText(getString(R.string.stop_scan));


                switchShow.setChecked(true);
                switchStatus.setText("ON");
                bleConnectStatus.setText("Bluetooth On...");


            }

            @Override
            public void onLeScan(BleDevice bleDevice) {
                super.onLeScan(bleDevice);
            }

            @Override
            public void onScanning(BleDevice bleDevice) {

                //Only Adds if Bluetooth Name is Preethi

                //if (PreferencesUtil.getValueString(MainActivity.this, PreferencesUtil.BLE_MAC_ADDRESS).equals("no_value") &&)

                if (bleDevice.getName() == null) {

                } else if (bleDevice.getName().equals("Preethi")) {
                    mDeviceAdapter.addDevice(bleDevice);
                    mDeviceAdapter.notifyDataSetChanged();
                }

                bleConnectStatus.setText("Searching...");
                //bleConnectStatus.setTypeface(octinPrisonFont);

            }

            @Override
            public void onScanFinished(List<BleDevice> scanResultList) {
                img_loading.clearAnimation();
                img_loading.setVisibility(View.INVISIBLE);
                btn_scan.setText(getString(R.string.start_scan));
                bleConnectStatus.setTextColor(Color.BLACK);
                bleConnectStatus.setText("Please Connect");
                // MathUtil.dismisProgressBar(MainActivity.this, dialog);

                //System.out.println("CalledOnScanFinished");

                for (int i = 0; i < scanResultList.size(); i++) {
                    System.out.println("ListOFBLE" + scanResultList.get(i).getMac());

                    if (PreferencesUtil.getValueString(MainActivity.this, PreferencesUtil.BLE_MAC_ADDRESS).equals(scanResultList.get(i).getMac())) {

                        BleDevice bleDevice = scanResultList.get(i);
                        //Toast.makeText(MainActivity.this, "Device already connected", Toast.LENGTH_SHORT).show();
                        if (bleDevice != null) {
                            connect(bleDevice);
                        } else {

                        }
                        break;

                    } else {

                        //If already paired device is not matching with current device
                        progressDialog.dismiss();
                        PreferencesUtil.remove(MainActivity.this,PreferencesUtil.BLE_MAC_ADDRESS);
                        mainLayout.setVisibility(View.VISIBLE);

                        Toast.makeText(MainActivity.this, "Please Connect", Toast.LENGTH_SHORT).show();
                    }

                }

                //Toast.makeText(MainActivity.this, "MoveToNextActivity", Toast.LENGTH_LONG).show();


            }
        });
    }

    private void connect(final BleDevice bleDevice) {
        BleManager.getInstance().connect(bleDevice, new BleGattCallback() {
            @Override
            public void onStartConnect() {
                progressDialog.show();

                //progressDialog.setMessage("Connecting Please Wait");
                //dialog=MathUtil.showProgressBar(MainActivity.this);


            }

            @Override
            public void onConnectFail(BleDevice bleDevice, BleException exception) {
                img_loading.clearAnimation();
                img_loading.setVisibility(View.INVISIBLE);
                btn_scan.setText(getString(R.string.start_scan));
                //MathUtil.dismisProgressBar(MainActivity.this, dialog);
                progressDialog.dismiss();
                Toast.makeText(MainActivity.this, getString(R.string.connect_fail), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onConnectSuccess(BleDevice bleDevice, BluetoothGatt gatt, int status) {
                //progressDialog.dismiss();

                mDeviceAdapter.addDevice(bleDevice);
                mDeviceAdapter.notifyDataSetChanged();
                bleConnectStatus.setText("Connected");

                progressDialog.dismiss();
                //MathUtil.dismisProgressBar(MainActivity.this, dialog);
                //Mycode
                if (BleManager.getInstance().isConnected(bleDevice)) {

                    if (bleDevice.getName() == null || bleDevice.getName().isEmpty() || bleDevice.getName().equals("") || bleDevice.getName().length() == 0 || !bleDevice.getName().equals("Preethi")) {
                        Toast.makeText(MainActivity.this, "Please Pair With Preethi Stove", Toast.LENGTH_SHORT).show();
                        mDeviceAdapter.removeDevice(bleDevice);
                        mDeviceAdapter.clearConnectedDevice();
                        mDeviceAdapter.notifyDataSetChanged();
                        return;
                    } else if (bleDevice.getName().equals("Preethi")) {
                        PreferencesUtil.setValueString(MainActivity.this, PreferencesUtil.BLE_MAC_ADDRESS, bleDevice.getMac());
                        Intent intent = new Intent(MainActivity.this, OperationActivity.class);
                        intent.putExtra(OperationActivity.KEY_DATA, bleDevice);
                        startActivity(intent);

                    } else {
                        //Connecting with other device
                        Toast.makeText(MainActivity.this, "Please Pair with Preethi Stove", Toast.LENGTH_SHORT).show();
                    }

                } else {

                    Toast.makeText(MainActivity.this, "Please Pair With Preethi Stove", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onDisConnected(boolean isActiveDisConnected, BleDevice bleDevice, BluetoothGatt gatt, int status) {
                progressDialog.dismiss();
                //MathUtil.dismisProgressBar(MainActivity.this, dialog);

                mDeviceAdapter.removeDevice(bleDevice);
                mDeviceAdapter.notifyDataSetChanged();

                if (isActiveDisConnected) {
                    //Toast.makeText(MainActivity.this, getString(R.string.active_disconnected), Toast.LENGTH_LONG).show();
                } else {
                    //Toast.makeText(MainActivity.this, getString(R.string.disconnected), Toast.LENGTH_LONG).show();
                    ObserverManager.getInstance().notifyObserver(bleDevice);
                }

            }
        });
    }


    private void readRssi(BleDevice bleDevice) {
        BleManager.getInstance().readRssi(bleDevice, new BleRssiCallback() {
            @Override
            public void onRssiFailure(BleException exception) {
                Log.i(TAG, "onRssiFailure" + exception.toString());
            }

            @Override
            public void onRssiSuccess(int rssi) {
                Log.i(TAG, "onRssiSuccess: " + rssi);
            }
        });
    }

    private void setMtu(BleDevice bleDevice, int mtu) {
        BleManager.getInstance().setMtu(bleDevice, mtu, new BleMtuChangedCallback() {
            @Override
            public void onSetMTUFailure(BleException exception) {
                Log.i(TAG, "onsetMTUFailure" + exception.toString());
            }

            @Override
            public void onMtuChanged(int mtu) {
                Log.i(TAG, "onMtuChanged: " + mtu);
            }
        });
    }

    @Override
    public final void onRequestPermissionsResult(int requestCode,
                                                 @NonNull String[] permissions,
                                                 @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_CODE_PERMISSION_LOCATION:
                if (grantResults.length > 0) {
                    for (int i = 0; i < grantResults.length; i++) {
                        if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                            onPermissionGranted(permissions[i]);
                        }
                    }
                }
                break;
        }
    }

    private void checkPermissions() {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (!bluetoothAdapter.isEnabled()) {

            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP_MR1) {

                Toast.makeText(this, getString(R.string.please_open_blue), Toast.LENGTH_SHORT).show();
                return;

            } else {

                requestUserToEnableBluetooth();
            }

        }

        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION};
        List<String> permissionDeniedList = new ArrayList<>();
        for (String permission : permissions) {
            int permissionCheck = ContextCompat.checkSelfPermission(this, permission);
            if (permissionCheck == PackageManager.PERMISSION_GRANTED) {

                onPermissionGranted(permission);
            } else {

                permissionDeniedList.add(permission);
            }
        }
        if (!permissionDeniedList.isEmpty()) {
            String[] deniedPermissions = permissionDeniedList.toArray(new String[permissionDeniedList.size()]);
            ActivityCompat.requestPermissions(this, deniedPermissions, REQUEST_CODE_PERMISSION_LOCATION);
        }
    }

    private void requestUserToEnableBluetooth() {
        Intent actionToReqEnableBluetooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        startActivityForResult(actionToReqEnableBluetooth, REQ_CODE_TO_ENABLE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_OPEN_GPS) {
            if (checkGPSIsOpen()) {
                setScanRule();
                startScan();
            }
        } else if (requestCode == REQ_CODE_TO_ENABLE) {

            if (resultCode == RESULT_OK) {

                Toast.makeText(MainActivity.this, "Your Bluetooth is  on", Toast.LENGTH_SHORT).show();

            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(MainActivity.this, "Your Bluetooth Enabling  is cancelled", Toast.LENGTH_SHORT).show();
            }

        }
    }


    private void onPermissionGranted(String permission) {
        switch (permission) {
            case Manifest.permission.ACCESS_FINE_LOCATION:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !checkGPSIsOpen()) {
                    new AlertDialog.Builder(this)
                            .setTitle(R.string.notifyTitle)
                            .setMessage(R.string.gpsNotifyMsg)
                            .setNegativeButton(R.string.cancel,
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            finish();
                                        }
                                    })
                            .setPositiveButton(R.string.setting,
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                            startActivityForResult(intent, REQUEST_CODE_OPEN_GPS);
                                        }
                                    })

                            .setCancelable(false)
                            .show();
                } else {
                    setScanRule();
                    startScan();
                }
                break;
        }
    }

    private boolean checkGPSIsOpen() {
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        if (locationManager == null)
            return false;
        return locationManager.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER);
    }


    private void getFont() {

        octinPrisonFont = FontUtil.getOctinPrisonFont(MainActivity.this);

    }

    private void setFont() {

        bleConnectStatus.setTypeface(octinPrisonFont);
        switchStatus.setTypeface(octinPrisonFont);

    }

}