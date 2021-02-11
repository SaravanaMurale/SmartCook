package com.clj.blesample.menuoperationactivity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.clj.blesample.R;
import com.clj.blesample.sessionmanager.PreferencesUtil;
import com.clj.blesample.utils.MathUtil;

public class SettingsActivity extends AppCompatActivity {

    RelativeLayout connectivityBlock,deviceBlock,statusBlock,deviceDetailsBlock;

    TextView connected,setDeviceName,setDeviceMacAddress,setRssi;

    Switch notiSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        connectivityBlock=(RelativeLayout)findViewById(R.id.connectivityBlock);
        deviceBlock=(RelativeLayout)findViewById(R.id.addDeviceBlock);
        statusBlock=(RelativeLayout)findViewById(R.id.statusBlock);
        deviceDetailsBlock=(RelativeLayout)findViewById(R.id.deviceDetailsBlock);

        connected=(TextView)findViewById(R.id.connected);
        setDeviceName=(TextView)findViewById(R.id.setDeviceName);
        setDeviceMacAddress=(TextView)findViewById(R.id.setDeviceMacAddress);
        setRssi=(TextView)findViewById(R.id.setRssi);

        notiSwitch = (Switch) findViewById(R.id.notiSwitch);

        int notiPreference= PreferencesUtil.getValueInt(SettingsActivity.this,PreferencesUtil.NOTI_PREFERENCE);

        if(notiPreference==1 || notiPreference==0){
            notiSwitch.setChecked(true);
        }else if(notiPreference==-1){
            notiSwitch.setChecked(false);
        }

        notiSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if(b){

                    setVisibilityGone();

                    PreferencesUtil.setValueSInt(SettingsActivity.this,PreferencesUtil.NOTI_PREFERENCE, MathUtil.NOTI_PREFERENCE_ENABLE);

                    Toast.makeText(SettingsActivity.this,"Notification Enabled",Toast.LENGTH_LONG).show();
                }else {
                    setVisibilityGone();
                    PreferencesUtil.setValueSInt(SettingsActivity.this,PreferencesUtil.NOTI_PREFERENCE,MathUtil.NOTI_PREFERENCE_DISABLE);
                    Toast.makeText(SettingsActivity.this,"Notification Disabled",Toast.LENGTH_LONG).show();
                }

            }
        });



        connectivityBlock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                statusBlock.setVisibility(View.VISIBLE);
                deviceDetailsBlock.setVisibility(View.GONE);
                connected.setText("Connected");
                connected.setTextColor(getResources().getColor(R.color.green));
            }
        });

        deviceBlock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deviceDetailsBlock.setVisibility(View.VISIBLE);
                statusBlock.setVisibility(View.GONE);

                setDeviceName.setText(PreferencesUtil.getValueString(SettingsActivity.this,PreferencesUtil.BLE_NAME));
                setDeviceName.setTextColor(getResources().getColor(R.color.red));
                setDeviceMacAddress.setText(PreferencesUtil.getValueString(SettingsActivity.this,PreferencesUtil.BLE_MAC));
                setDeviceMacAddress.setTextColor(getResources().getColor(R.color.red));
                setRssi.setText(""+PreferencesUtil.getValueInt(SettingsActivity.this,PreferencesUtil.BLE_RSSI));
                setRssi.setTextColor(getResources().getColor(R.color.red));


            }
        });

    }

    private void setVisibilityGone() {
        deviceDetailsBlock.setVisibility(View.GONE);
        statusBlock.setVisibility(View.GONE);
    }
}