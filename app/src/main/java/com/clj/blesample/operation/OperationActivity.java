package com.clj.blesample.operation;


import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import com.clj.blesample.R;
import com.clj.blesample.comm.Observer;
import com.clj.blesample.comm.ObserverManager;
import com.clj.blesample.sessionmanager.PreferencesUtil;
import com.clj.fastble.BleManager;
import com.clj.fastble.data.BleDevice;

import java.util.ArrayList;
import java.util.List;

public class OperationActivity extends AppCompatActivity implements Observer {

    public static final String KEY_DATA = "key_data";

    private BleDevice bleDevice;
    private BluetoothGattService bluetoothGattService;
    private BluetoothGattCharacteristic characteristic;
    private int charaProp;

    private Toolbar toolbar;
    private List<Fragment> fragments = new ArrayList<>();
    private int currentPage = 0;
    private String[] titles = new String[3];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_operation);


        initData();
        initView();
        //Setting up current Page
        initPage();

        ObserverManager.getInstance().addObserver(this);
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            //If keyCode==4 onBack Button is pressed, so directly calling Main Activity from characteristicListFragment
            //My Code
            if (keyCode==4 && currentPage==1){

                currentPage=0;
            } //end of My Code

            System.out.println("KEYCODE" + keyCode);
            System.out.println("KEYCODE_currentPage" + currentPage);

            if (currentPage != 0) {
                currentPage--;
                changePage(currentPage);
                return true;
            } else {
                finish();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    private void initView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(titles[0]);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentPage != 0) {
                    currentPage--;
                    changePage(currentPage);
                } else {
                    finish();
                }
            }
        });
    }

    private void initData() {
        //Getting Ble Device data
        bleDevice = getIntent().getParcelableExtra(KEY_DATA);

        if(bleDevice==null){
            finish();
        }else if(bleDevice!=null){
            PreferencesUtil.setValueString(OperationActivity.this,PreferencesUtil.BLE_NAME,bleDevice.getName());
            PreferencesUtil.setValueString(OperationActivity.this,PreferencesUtil.BLE_MAC,bleDevice.getMac());
            PreferencesUtil.setValueSInt(OperationActivity.this,PreferencesUtil.BLE_RSSI,bleDevice.getRssi());
        }

        System.out.println("DeviceDetails"+bleDevice.getName()+" "+bleDevice.getMac()+" "+bleDevice.getRssi()+" "+bleDevice.getKey()+" "+bleDevice.getDevice()+" "+bleDevice.describeContents()+" "+bleDevice.getScanRecord()+" "+bleDevice.getTimestampNanos());

        if (bleDevice == null)
            finish();

        titles = new String[]{
                getString(R.string.service_list),
                getString(R.string.characteristic_list),
                getString(R.string.console)};
    }

    private void initPage() {
        prepareFragment();
        changePage(0);

    }

    public void changePage(int page) {
        currentPage = page;
        toolbar.setTitle(titles[page]);
        //Shows current page and hides another page
        updateFragment(page);
        if (currentPage == 1) {
            ((CharacteristicListFragment) fragments.get(1)).showData();
        } else if (currentPage == 2) {
            ((CharacteristicOperationFragment) fragments.get(2)).showData();
        }
    }

    private void prepareFragment() {
        fragments.add(new ServiceListFragment());
        fragments.add(new CharacteristicListFragment());
        fragments.add(new CharacteristicOperationFragment());
        for (Fragment fragment : fragments) {
            getSupportFragmentManager().beginTransaction().add(R.id.fragment, fragment).hide(fragment).commit();
        }
    }

    private void updateFragment(int position) {



        if (position > fragments.size() - 1) {
            return;
        }
        for (int i = 0; i < fragments.size(); i++) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            Fragment fragment = fragments.get(i);
            if (i == position) {
                transaction.show(fragment);
            } else {
                transaction.hide(fragment);
            }
            transaction.commit();
        }


    }

    public BleDevice getBleDevice() {
        return bleDevice;
    }

    public BluetoothGattService getBluetoothGattService() {
        return bluetoothGattService;
    }

    public void setBluetoothGattService(BluetoothGattService bluetoothGattService) {
        this.bluetoothGattService = bluetoothGattService;
    }

    public BluetoothGattCharacteristic getCharacteristic() {
        return characteristic;
    }

    public void setCharacteristic(BluetoothGattCharacteristic characteristic) {
        this.characteristic = characteristic;
    }

    public int getCharaProp() {
        return charaProp;
    }

    public void setCharaProp(int charaProp) {
        this.charaProp = charaProp;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        Toast.makeText(OperationActivity.this,"Bluetooth Connection Terminated",Toast.LENGTH_SHORT).show();
        BleManager.getInstance().clearCharacterCallback(bleDevice);
        ObserverManager.getInstance().deleteObserver(this);
    }

    @Override
    public void disConnected(BleDevice device) {
        Toast.makeText(OperationActivity.this,"Bluetooth Connection Terminated",Toast.LENGTH_SHORT).show();
        if (device != null && bleDevice != null && device.getKey().equals(bleDevice.getKey())) {
            finish();
        }
    }


}
