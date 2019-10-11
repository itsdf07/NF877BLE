package com.itsdf07.nf877ble;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.itsdf07.nf877ble.blelib.client.core.OKBLEDevice;
import com.itsdf07.nf877ble.blelib.client.core.OKBLEDeviceImp;
import com.itsdf07.nf877ble.blelib.client.core.OKBLEDeviceListener;
import com.itsdf07.nf877ble.blelib.client.core.OKBLEOperation;
import com.itsdf07.nf877ble.blelib.client.scan.BLEScanResult;

public class BLEActivity extends AppCompatActivity implements View.OnClickListener, OKBLEDeviceListener {
    private static final String TAG = "BLEActivity";
    public static final String EXTRA_BLEDEVICE = BLEActivity.class.getName() + ".EXTRA_BLEDEVICE";
    String uuid = "0000ffe3-0000-1000-8000-00805f9b34fb";
    String uuidr = "0000ffe2-0000-1000-8000-00805f9b34fb";

    private BLEScanResult bleScanResult;
    OKBLEDevice okbleDevice;
    private TextView tvBleInfo;
    private Button btnConnectBLE;
    private Button btnOpenNotify;
    private EditText etData;
    private Button btnSendData;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ble);

        bleScanResult = getIntent().getParcelableExtra(EXTRA_BLEDEVICE);


        tvBleInfo = findViewById(R.id.tv_ble_info);
        tvBleInfo.setText(bleScanResult.getBluetoothDevice().getName() + ":" + bleScanResult.getBluetoothDevice().getAddress());
        btnConnectBLE = findViewById(R.id.btn_connect);
        btnConnectBLE.setOnClickListener(this);

        btnOpenNotify = findViewById(R.id.btn_opennotify);
        btnOpenNotify.setOnClickListener(this);

        etData = findViewById(R.id.et_data);

        btnSendData = findViewById(R.id.btn_send);
        btnSendData.setOnClickListener(this);

        okbleDevice = new OKBLEDeviceImp(this, bleScanResult);
    }

    public void sendData(String uuid, byte[] data) {
        btnSendData.setEnabled(false);
        okbleDevice.addWriteOperation(uuid, data, new OKBLEOperation.WriteOperationListener() {
            @Override
            public void onWriteValue(byte[] value) {
                Log.e(TAG, "onWriteValue->value:" + new String(value));
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        btnSendData.setEnabled(true);

                    }
                });

            }

            @Override
            public void onFail(int code, String errMsg) {
                Log.e(TAG, "onFail->code:" + code + ",errMsg:" + errMsg);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                    }
                });

            }

            @Override
            public void onExecuteSuccess(OKBLEOperation.OperationType type) {
                Log.e(TAG, "onExecuteSuccess->type:" + type.name());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                    }
                });
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_connect:
                if ("连接".equals(btnConnectBLE.getText().toString())) {
                    btnConnectBLE.setText("已连接");
                    okbleDevice.addDeviceListener(this);
                    okbleDevice.connect(true);

                } else {
                    btnConnectBLE.setText("连接");
                    okbleDevice.disConnect(false);
                    okbleDevice.remove();
                }
                break;
            case R.id.btn_opennotify:
                if ("打开通知".equals(btnOpenNotify.getText().toString())) {
                    final OKBLEOperation.OperationType[] operationType = new OKBLEOperation.OperationType[1];
                    okbleDevice.addNotifyOrIndicateOperation(uuidr, true, new OKBLEOperation.NotifyOrIndicateOperationListener() {
                        @Override
                        public void onNotifyOrIndicateComplete() {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    btnOpenNotify.setText("打开成功");
                                    //addLog("onNotifyOrIndicateComplete");
                                    if (operationType[0] == OKBLEOperation.OperationType.OperationType_Enable_Indicate) {
                                        //  btn_indicate.setText("Indication enabled");
                                        //  btn_indicate.setEnabled(false);
                                    } else if (operationType[0] == OKBLEOperation.OperationType.OperationType_Enable_Notify) {
                                        // btn_notify.setText("Notification enabled");
                                        // btn_notify.setEnabled(false);
                                    }
                                }
                            });
                        }

                        @Override
                        public void onFail(int code, final String errMsg) {
                            Log.e(TAG, "onFail->code:" + code + ",errMsg:" + errMsg);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    // addLog("NotifyOrIndicate onFail:"+ errMsg);
                                }
                            });
                        }

                        @Override
                        public void onExecuteSuccess(OKBLEOperation.OperationType type) {
                            Log.e(TAG, "onExecuteSuccess->type:" + type.name());
                            operationType[0] = type;
                        }
                    });
                } else {
                    btnOpenNotify.setText("打开通知");
                }

                break;
            case R.id.btn_send:
                String content = etData.getText().toString();
                sendData(uuid, content.getBytes());
                Toast.makeText(this, "value:" + content, Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }

    @Override
    public void onConnected(String deviceTAG) {
        Log.e(TAG, "onConnected->deviceTAG:" + deviceTAG);
    }

    @Override
    public void onDisconnected(String deviceTAG) {
        Log.e(TAG, "onDisconnected->deviceTAG:" + deviceTAG);
    }

    @Override
    public void onReadBattery(String deviceTAG, int battery) {
        Log.e(TAG, "onReadBattery->deviceTAG:" + deviceTAG + ",battery:" + battery);
    }

    @Override
    public void onReceivedValue(String deviceTAG, String uuid, byte[] value) {
        Log.e(TAG, "onReceivedValue->deviceTAG:" + deviceTAG + ",uuid:" + uuid + ",value:" + new String(value));
    }

    @Override
    public void onWriteValue(String deviceTAG, String uuid, byte[] value, boolean success) {
        Log.e(TAG, "onWriteValue->deviceTAG:" + deviceTAG + ",uuid:" + uuid + ",value:" + new String(value));
    }

    @Override
    public void onReadValue(String deviceTAG, String uuid, byte[] value, boolean success) {
        Log.e(TAG, "onReadValue->deviceTAG:" + deviceTAG + ",uuid:" + uuid + ",success:" + success + ",value:" + new String(value));
    }

    @Override
    public void onNotifyOrIndicateComplete(String deviceTAG, String uuid, boolean enable, boolean success) {
        Log.e(TAG, "onNotifyOrIndicateComplete->deviceTAG:" + deviceTAG + ",uuid:" + uuid + ",success:" + success + ",enable:" + enable);
    }
}
