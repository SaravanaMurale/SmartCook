package com.clj.blesample.operation;

import android.annotation.TargetApi;
import android.bluetooth.BluetoothGattCharacteristic;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.clj.blesample.R;
import com.clj.fastble.BleManager;
import com.clj.fastble.callback.BleIndicateCallback;
import com.clj.fastble.callback.BleNotifyCallback;
import com.clj.fastble.callback.BleReadCallback;
import com.clj.fastble.callback.BleWriteCallback;
import com.clj.fastble.data.BleDevice;
import com.clj.fastble.exception.BleException;
import com.clj.fastble.utils.HexUtil;

import java.util.ArrayList;
import java.util.List;

@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
public class CharacteristicOperationFragment extends Fragment {

    public static final int PROPERTY_READ = 1;
    public static final int PROPERTY_CHECK = 6;
    public static final int PROPERTY_WRITE = 2;
    public static final int PROPERTY_WRITE_NO_RESPONSE = 3;
    public static final int PROPERTY_NOTIFY = 4;
    public static final int PROPERTY_INDICATE = 5;

    private LinearLayout layout_container;
    private List<String> childList = new ArrayList<>();

    //MyCode
    EditText enterData;
    BleDevice bleDevice;
    BluetoothGattCharacteristic characteristic;
    int charaProp;
    String child;
    Button enter;
    TextView showData;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_characteric_operation, null);
        initView(v);


        return v;
    }


    private void initView(View v) {
        layout_container = (LinearLayout) v.findViewById(R.id.layout_container);

        enterData = (EditText) v.findViewById(R.id.enterData);
        enter = (Button) v.findViewById(R.id.enter);
        showData = (TextView) v.findViewById(R.id.showData);


    }

    @Override
    public void onResume() {
        super.onResume();

       // callMeNotify();


        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String data = enterData.getText().toString();

                callMe(data);

            }
        });





    }

    public void showData() {
        bleDevice = ((OperationActivity) getActivity()).getBleDevice();
        characteristic = ((OperationActivity) getActivity()).getCharacteristic();
        charaProp = ((OperationActivity) getActivity()).getCharaProp();
        child = characteristic.getUuid().toString() + String.valueOf(charaProp);

        for (int i = 0; i < layout_container.getChildCount(); i++) {
            layout_container.getChildAt(i).setVisibility(View.GONE);
        }
        if (childList.contains(child)) {
            layout_container.findViewWithTag(bleDevice.getKey() + characteristic.getUuid().toString() + charaProp).setVisibility(View.VISIBLE);
        } else {
            childList.add(child);

            View view = LayoutInflater.from(getActivity()).inflate(R.layout.layout_characteric_operation, null);
            view.setTag(bleDevice.getKey() + characteristic.getUuid().toString() + charaProp);
            LinearLayout layout_add = (LinearLayout) view.findViewById(R.id.layout_add);
            final TextView txt_title = (TextView) view.findViewById(R.id.txt_title);
            txt_title.setText(String.valueOf(characteristic.getUuid().toString() + getActivity().getString(R.string.data_changed)));
            final TextView txt = (TextView) view.findViewById(R.id.txt);
            txt.setMovementMethod(ScrollingMovementMethod.getInstance());
            switch (charaProp) {
                case PROPERTY_READ: {
                    View view_add = LayoutInflater.from(getActivity()).inflate(R.layout.layout_characteric_operation_button, null);
                    Button btn = (Button) view_add.findViewById(R.id.btn);

                    System.out.println("IAMSERVICEUUID" + characteristic.getService().getUuid().toString());
                    System.out.println("IAMUUID" + characteristic.getUuid().toString());
                    System.out.println(characteristic.getDescriptors());


                    btn.setText(getActivity().getString(R.string.read));
                    btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            BleManager.getInstance().read(
                                    bleDevice,
                                    characteristic.getService().getUuid().toString(),
                                    characteristic.getUuid().toString(),
                                    new BleReadCallback() {

                                        @Override
                                        public void onReadSuccess(final byte[] data) {
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {

                                                    System.out.println("IAMDATA" + data);


                                                    /*String hex=HexUtil.formatHexString(data, true);
                                                    int decimal=Integer.parseInt(hex,16);
                                                    System.out.println("IAMDECIMALFORMAT"+decimal);
                                                    String hexToDecFormat=String.valueOf(decimal);

                                                    addText(txt,hexToDecFormat);*/

                                                    addText(txt, HexUtil.formatHexString(data, true));
                                                }
                                            });
                                        }

                                        @Override
                                        public void onReadFailure(final BleException exception) {
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    addText(txt, exception.toString());
                                                }
                                            });
                                        }
                                    });
                        }
                    });
                    layout_add.addView(view_add);
                }
                break;
                case PROPERTY_CHECK: {

                    View view_add = LayoutInflater.from(getActivity()).inflate(R.layout.layout_characteric_operation_button, null);
                    Button btn = (Button) view_add.findViewById(R.id.btn);
                    btn.setText(getActivity().getString(R.string.read));

                    btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            //BleManager.getInstance().read();

                        }
                    });


                    break;
                }


                case PROPERTY_WRITE: {
                    View view_add = LayoutInflater.from(getActivity()).inflate(R.layout.layout_characteric_operation_et, null);
                    final EditText et = (EditText) view_add.findViewById(R.id.et);
                    Button btn = (Button) view_add.findViewById(R.id.btn);
                    btn.setText(getActivity().getString(R.string.write));
                    btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String hex = et.getText().toString();
                            if (TextUtils.isEmpty(hex)) {
                                return;
                            }

                            callMe(hex);

                            //Converting String to byte format
                          /*  BleManager.getInstance().write(
                                    bleDevice,
                                    characteristic.getService().getUuid().toString(),
                                    characteristic.getUuid().toString(),
                                    HexUtil.hexStringToBytes(hex),
                                    new BleWriteCallback() {

                                        //Converting byte to String and displaying to user
                                        @Override
                                        public void onWriteSuccess(final int current, final int total, final byte[] justWrite) {
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    addText(txt, "write success, current: " + current
                                                            + " total: " + total
                                                            + " justWrite: " + HexUtil.formatHexString(justWrite, true));
                                                }
                                            });
                                        }

                                        @Override
                                        public void onWriteFailure(final BleException exception) {
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    addText(txt, exception.toString());
                                                }
                                            });
                                        }
                                    });*/


                        }
                    });
                    layout_add.addView(view_add);
                }
                break;

                case PROPERTY_WRITE_NO_RESPONSE: {
                    View view_add = LayoutInflater.from(getActivity()).inflate(R.layout.layout_characteric_operation_et, null);
                    final EditText et = (EditText) view_add.findViewById(R.id.et);
                    Button btn = (Button) view_add.findViewById(R.id.btn);
                    btn.setText(getActivity().getString(R.string.write));
                    btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String hex = et.getText().toString();
                            if (TextUtils.isEmpty(hex)) {
                                return;
                            }
                            BleManager.getInstance().write(
                                    bleDevice,
                                    characteristic.getService().getUuid().toString(),
                                    characteristic.getUuid().toString(),
                                    HexUtil.hexStringToBytes(hex),
                                    new BleWriteCallback() {

                                        @Override
                                        public void onWriteSuccess(final int current, final int total, final byte[] justWrite) {
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    addText(txt, "write success, current: " + current
                                                            + " total: " + total
                                                            + " justWrite: " + HexUtil.formatHexString(justWrite, true));
                                                }
                                            });
                                        }

                                        @Override
                                        public void onWriteFailure(final BleException exception) {
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    addText(txt, exception.toString());
                                                }
                                            });
                                        }
                                    });
                        }
                    });
                    layout_add.addView(view_add);
                }
                break;

                case PROPERTY_NOTIFY: {
                    View view_add = LayoutInflater.from(getActivity()).inflate(R.layout.layout_characteric_operation_button, null);
                    final Button btn = (Button) view_add.findViewById(R.id.btn);
                    btn.setText(getActivity().getString(R.string.open_notification));
                    btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (btn.getText().toString().equals(getActivity().getString(R.string.open_notification))) {
                                btn.setText(getActivity().getString(R.string.close_notification));
                                /*BleManager.getInstance().notify(
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
                                                        System.out.println("IamException"+exception);
                                                    }
                                                });
                                            }

                                            @Override
                                            public void onCharacteristicChanged(byte[] data) {

                                                System.out.println("Iamnotifydata"+data);

                                                runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {


                                                           //Converting hex to string
                                                        addText(txt, HexUtil.formatHexString(characteristic.getValue(),
                                                                true));
                                                        *//*notifyText(txt, HexUtil.formatHexString(characteristic.getValue(),
                                                                true));
*//*

                                                        System.out.println("NOTIFY"+characteristic.getValue());
                                                    }
                                                });
                                            }
                                        });*/

                                callMeNotify();

                            } else {
                                btn.setText(getActivity().getString(R.string.open_notification));
                                BleManager.getInstance().stopNotify(
                                        bleDevice,
                                        characteristic.getService().getUuid().toString(),
                                        characteristic.getUuid().toString());
                            }
                        }
                    });
                    layout_add.addView(view_add);
                }
                break;

                case PROPERTY_INDICATE: {
                    View view_add = LayoutInflater.from(getActivity()).inflate(R.layout.layout_characteric_operation_button, null);
                    final Button btn = (Button) view_add.findViewById(R.id.btn);
                    btn.setText(getActivity().getString(R.string.open_notification));
                    btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (btn.getText().toString().equals(getActivity().getString(R.string.open_notification))) {
                                btn.setText(getActivity().getString(R.string.close_notification));
                                BleManager.getInstance().indicate(
                                        bleDevice,
                                        characteristic.getService().getUuid().toString(),
                                        characteristic.getUuid().toString(),
                                        new BleIndicateCallback() {

                                            @Override
                                            public void onIndicateSuccess() {
                                                runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        addText(txt, "indicate success");
                                                    }
                                                });
                                            }

                                            @Override
                                            public void onIndicateFailure(final BleException exception) {
                                                runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        addText(txt, exception.toString());
                                                    }
                                                });
                                            }

                                            @Override
                                            public void onCharacteristicChanged(byte[] data) {
                                                runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        addText(txt, HexUtil.formatHexString(characteristic.getValue(), true));
                                                    }
                                                });
                                            }
                                        });
                            } else {
                                btn.setText(getActivity().getString(R.string.open_notification));
                                BleManager.getInstance().stopIndicate(
                                        bleDevice,
                                        characteristic.getService().getUuid().toString(),
                                        characteristic.getUuid().toString());
                            }
                        }
                    });
                    layout_add.addView(view_add);
                }
                break;
            }

            layout_container.addView(view);
        }
    }

    public void callMeNotify() {

        System.out.println("IAMCALLED");

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
                    public void onCharacteristicChanged(byte[] data) {

                        System.out.println("Iamnotifydata" + data);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {


                                //Converting hex to string
                                /*addText(txt, HexUtil.formatHexString(characteristic.getValue(),
                                        true));
                                */                        /*notifyText(txt, HexUtil.formatHexString(characteristic.getValue(),
                                                                true));

*/
                                showData.setText(HexUtil.formatHexString(characteristic.getValue()));

                                System.out.println("NOTIFY" + characteristic.getValue());
                            }
                        });
                    }
                });

    }

    private void callMe(String hex) {

        BleManager.getInstance().write(
                bleDevice,
                characteristic.getService().getUuid().toString(),
                characteristic.getUuid().toString(),
                HexUtil.hexStringToBytes(hex),
                new BleWriteCallback() {

                    //Converting byte to String and displaying to user
                    @Override
                    public void onWriteSuccess(final int current, final int total, final byte[] justWrite) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                /*addText(txt, "write success, current: " + current
                                        + " total: " + total
                                        + " justWrite: " + HexUtil.formatHexString(justWrite, true));*/

                                System.out.println("SuccessWritingData");
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


    private void runOnUiThread(Runnable runnable) {
        if (isAdded() && getActivity() != null)
            getActivity().runOnUiThread(runnable);
    }

    //Prints in the same Location
    private void notifyText(TextView txt, String formatHexString) {
        txt.setText(formatHexString);
    }

    //Serial Print
    private void addText(TextView textView, String content) {
        textView.append(content);
        textView.append("\n");
        int offset = textView.getLineCount() * textView.getLineHeight();
        if (offset > textView.getHeight()) {
            textView.scrollTo(0, offset - textView.getHeight());
        }
    }
}
