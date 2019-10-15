package com.itsdf07.nf877ble;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.itsdf07.nf877ble.bean.BLEChannelSetting;
import com.itsdf07.nf877ble.bean.BLEPublicSetting;
import com.itsdf07.nf877ble.blelib.client.core.OKBLEDevice;
import com.itsdf07.nf877ble.blelib.client.core.OKBLEDeviceImp;
import com.itsdf07.nf877ble.blelib.client.core.OKBLEDeviceListener;
import com.itsdf07.nf877ble.blelib.client.core.OKBLEOperation;
import com.itsdf07.nf877ble.blelib.client.scan.BLEScanResult;

import java.util.Arrays;
import java.util.HashMap;

public class BLEActivity extends AppCompatActivity implements
        View.OnClickListener,
        OKBLEDeviceListener,
        AdapterView.OnItemSelectedListener {
    private static final String TAG = "BLEActivity";
    public static final String EXTRA_BLEDEVICE = BLEActivity.class.getName() + ".EXTRA_BLEDEVICE";
    public static final String UUIDWRITE = "0000ffe3-0000-1000-8000-00805f9b34fb";
    public static final String UUIDNOTIFY = "0000ffe2-0000-1000-8000-00805f9b34fb";

    private BLEScanResult bleScanResult;
    OKBLEDevice okbleDevice;

    private BLEPublicSetting blePublicSetting = new BLEPublicSetting();
    /**
     * 16信道对应的独立信道协议
     */
    private HashMap<Integer, BLEChannelSetting> bleChannelSettingHashMap = new HashMap<>();

    private TextView tvConnectStatus;
    private Button btnMhzWrite;


    //发送的数据包个数
    private int packageDataIndex = 0;


    /**
     * gps:0-OFF,1-Automatic Mode,2-Manual Mode
     */
    private Spinner spGps;
    /**
     * 蓝牙开关:0-OFF,1-ON
     */
    private Spinner spBluetoothStatus;
    /**
     * 静噪1:0~9
     */
    private Spinner spSquelch1;
    /**
     * 声控等级:0-OFF,1~9
     */
    private Spinner spVoiceLevel;
    /**
     * 声控延时[毫秒]:0~5->500,1000,1500,2000,2500,3000
     */
    private Spinner spVoiceDelay;
    /**
     * 扫描模式:0-TO,1-CO
     */
    private Spinner spSscanType;

    /**
     * 显示模式:0-Black and white,1-Colorful
     */
    private Spinner spDisplayModel;

    /**
     * BEEP声:0-OFF,1-ON
     */
    private Spinner spBeep;

    /**
     * 发射提示音:0-OFF,1-ON
     */
    private Spinner spVoice2Send;

    /**
     * TOT超时:0-OFF,1~12->15s-180s,每15s一个选项,共12
     */
    private Spinner spTotTimeOut;

    /**
     * 屏保时间:0-OFF,1~30->5s-150s,每5s一个选项,共30
     */
    private Spinner spDisplayTime;

    /**
     * 省电模式:0-OFF,1-ON
     */
    private Spinner spPowerMode;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (okbleDevice != null) {
            okbleDevice.disConnect(false);
            okbleDevice.remove();
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ble_channel);
        bleScanResult = getIntent().getParcelableExtra(EXTRA_BLEDEVICE);

        initView();

        okbleDevice = new OKBLEDeviceImp(this, bleScanResult);

        okbleDevice.addDeviceListener(this);
        okbleDevice.connect(true);
        initBleChannelSettingHashMap();
    }

    private void initView() {
        ((TextView) findViewById(R.id.tv_ble_info)).setText(getString(R.string.string_public_information) + "　" +
                bleScanResult.getBluetoothDevice().getName() + "->" + bleScanResult.getBluetoothDevice().getAddress());

        tvConnectStatus = findViewById(R.id.tv_connect_status);
        tvConnectStatus.setText("连接中...");

        btnMhzWrite = findViewById(R.id.btn_writeHz);
        btnMhzWrite.setOnClickListener(this);


        /**
         * gps:0-OFF,1-Automatic Mode,2-Manual Mode
         */
        spGps = findViewById(R.id.sp_gps);
        spBluetoothStatus = findViewById(R.id.sp_bluetooth_status);
        spSquelch1 = findViewById(R.id.sp_squelch1);
        spVoiceLevel = findViewById(R.id.sp_voice_level);
        spVoiceDelay = findViewById(R.id.sp_voice_delay);
        spSscanType = findViewById(R.id.sp_scan_type);
        spDisplayModel = findViewById(R.id.sp_display_model);
        spBeep = findViewById(R.id.sp_beep);
        spVoice2Send = findViewById(R.id.sp_voice2send);
        spTotTimeOut = findViewById(R.id.sp_tot_timeout);
        spDisplayTime = findViewById(R.id.sp_display_time);
        spPowerMode = findViewById(R.id.sp_power_model);

        spGps.setOnItemSelectedListener(this);
        spBluetoothStatus.setOnItemSelectedListener(this);
        spSquelch1.setOnItemSelectedListener(this);
        spVoiceLevel.setOnItemSelectedListener(this);
        spVoiceDelay.setOnItemSelectedListener(this);
        spSscanType.setOnItemSelectedListener(this);
        spDisplayModel.setOnItemSelectedListener(this);
        spBeep.setOnItemSelectedListener(this);
        spVoice2Send.setOnItemSelectedListener(this);
        spTotTimeOut.setOnItemSelectedListener(this);
        spDisplayTime.setOnItemSelectedListener(this);
        spPowerMode.setOnItemSelectedListener(this);
    }

    private void initBleChannelSettingHashMap() {
        for (int i = 0; i < 32; i++) {
            BLEChannelSetting bleChannelSetting = new BLEChannelSetting();
            bleChannelSetting.setChannelNum(i + 1);
            bleChannelSetting.setSendHz("400.12500");
            bleChannelSetting.setRecHz("400.12500");
            bleChannelSetting.setStrDecodeCtcDcs("D023N");
            bleChannelSetting.setStrEncodeCtcDcs("67.0");
            bleChannelSetting.setTransmitPower(1);
            bleChannelSetting.setScanAdd(0);
            bleChannelSetting.setBandwidth(1);
            bleChannelSettingHashMap.put(i, bleChannelSetting);
        }
    }

    public void sendData(String uuid, byte data) {
        byte[] datas = new byte[1];
        datas[0] = data;
        sendData(uuid, datas);
    }

    public void sendData(String uuid, byte[] data) {
        okbleDevice.addWriteOperation(uuid, data, new OKBLEOperation.WriteOperationListener() {
            @Override
            public void onWriteValue(byte[] value) {
                Log.e(TAG, "onWriteValue->value:" + Arrays.toString(value));
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {

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
            case R.id.btn_writeHz:
                count = 1;
                sendData(UUIDWRITE, BLEMhzUtils.handshakeProtocolHead());
                Log.e("Protocol-BLE", "Protocol:" + Arrays.toString(BLEMhzUtils.handshakeProtocolHead()));
                break;
            default:
                break;
        }
    }

    @Override
    public void onConnected(String deviceTAG) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tvConnectStatus.setText("已连接");
            }
        });
        Log.e(TAG, "onConnected->deviceTAG:" + deviceTAG);

        final OKBLEOperation.OperationType[] operationType = new OKBLEOperation.OperationType[1];
//        Toast.makeText(BLEActivity.this, "通知打开中...", Toast.LENGTH_SHORT).show();
        okbleDevice.addNotifyOrIndicateOperation(UUIDNOTIFY, true, new OKBLEOperation.NotifyOrIndicateOperationListener() {
            @Override
            public void onNotifyOrIndicateComplete() {
                Log.e(TAG, "onNotifyOrIndicateComplete->通知已打开");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
//                        Toast.makeText(BLEActivity.this, "通知已打开", Toast.LENGTH_SHORT).show();
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
                    }
                });
            }

            @Override
            public void onExecuteSuccess(OKBLEOperation.OperationType type) {
                Log.e(TAG, "onExecuteSuccess->type:" + type.name());
                operationType[0] = type;
            }
        });
    }

    @Override
    public void onDisconnected(String deviceTAG) {
        Log.e(TAG, "onDisconnected->deviceTAG:" + deviceTAG);
        if (okbleDevice != null) {
            okbleDevice.disConnect(false);
            okbleDevice.remove();
        }
    }

    @Override
    public void onReadBattery(String deviceTAG, int battery) {
        Log.e(TAG, "onReadBattery->deviceTAG:" + deviceTAG + ",battery:" + battery);
    }

    private int count = 0;

    @Override
    public void onReceivedValue(String deviceTAG, String uuid, final byte[] value) {
        Log.e(TAG, "onReceivedValue->deviceTAG:" + deviceTAG + ",uuid:" + uuid + ",value:" + Arrays.toString(value));
        if (count == 1) {
            if (value[0] == (byte) 0x06) {
                count++;
                //TODO 发送 (byte) 0x02
                sendData(UUIDWRITE, (byte) 0x02);
            }
        } else if (count == 2) {
            if (value.length == BLEMhzUtils.acceptHandshakeProtocol().length) {
                count++;
                boolean isMatch = true;
                for (int i = 0; i < value.length; i++) {
                    if (value[i] != BLEMhzUtils.acceptHandshakeProtocol()[i]) {
                        isMatch = false;
                        break;
                    }
                }
                if (isMatch) {
                    // TODO 发送 (byte) 0x06
                    sendData(UUIDWRITE, (byte) 0x06);
                }

            }
        } else if (count == 3) {
            if (value[0] == (byte) 0x06) {
                count = 0;
                //TODO 开始发送第一个数据包:设置数据
                Log.e(TAG, "onReceivedValue->count == 3:握手成功，可以开始发送公共协议数据了");
                sendData(UUIDWRITE, getBLEPublicDataPackage(blePublicSetting));
            }
        } else {
            if (value[0] == (byte) 0x06) {

                //TODO 开始发送第N+1个数据包:设置数据
                Log.e(TAG, "onReceivedValue->count == 3:握手成功，可以开始发送第" + packageDataIndex + "个信道数据了");
                if (packageDataIndex >= 32) {
                    sendData(UUIDWRITE, (byte) 0x45);
                    packageDataIndex = 0;
                } else {
                    sendData(UUIDWRITE, getChannelDataPackage(bleChannelSettingHashMap.get(packageDataIndex)));
                    packageDataIndex++;
                }
            }
        }
    }

    @Override
    public void onWriteValue(String deviceTAG, String uuid, byte[] value, boolean success) {
        Log.e(TAG, "onWriteValue->deviceTAG:" + deviceTAG + ",uuid:" + uuid + ",value:" + Arrays.toString(value));
    }

    @Override
    public void onReadValue(String deviceTAG, String uuid, byte[] value, boolean success) {
        Log.e(TAG, "onReadValue->deviceTAG:" + deviceTAG + ",uuid:" + uuid + ",success:" + success + ",value:" + Arrays.toString(value));
    }

    @Override
    public void onNotifyOrIndicateComplete(String deviceTAG, String uuid, boolean enable, boolean success) {
        Log.e(TAG, "onNotifyOrIndicateComplete->deviceTAG:" + deviceTAG + ",uuid:" + uuid + ",success:" + success + ",enable:" + enable);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (null == blePublicSetting) {
            Log.w(TAG, "blePublicSetting is null");
            return;
        }
        switch (parent.getId()) {
            case R.id.sp_gps:
                blePublicSetting.setGps(spGps.getSelectedItemPosition());
                break;
            case R.id.sp_bluetooth_status:
//                Log.d("SPPPP", "position:" + position + ",value:" + spBluetoothStatus.getSelectedItem().toString());
//                spBluetoothStatus.setSelection(position,true);
                blePublicSetting.setBluetoothStatus(spBluetoothStatus.getSelectedItemPosition());
                break;
            case R.id.sp_squelch1:
                blePublicSetting.setSquelch1(spSquelch1.getSelectedItemPosition());
                break;
            case R.id.sp_voice_level:
                blePublicSetting.setVoiceLevel(spVoiceLevel.getSelectedItemPosition());
                break;
            case R.id.sp_voice_delay:
                blePublicSetting.setVoiceDelay((spVoiceDelay.getSelectedItemPosition()));
                break;
            case R.id.sp_scan_type:
                blePublicSetting.setScanType(spSscanType.getSelectedItemPosition());
                break;
            case R.id.sp_display_model:
                blePublicSetting.setDisplayModel(spDisplayModel.getSelectedItemPosition());
                break;
            case R.id.sp_beep:
                blePublicSetting.setBeep(spBeep.getSelectedItemPosition());
                break;
            case R.id.sp_voice2send:
                blePublicSetting.setVoice2Send(spVoice2Send.getSelectedItemPosition());
                break;
            case R.id.sp_tot_timeout:
                blePublicSetting.setTotTimeOut(spTotTimeOut.getSelectedItemPosition());
                break;
            case R.id.sp_display_time:
                blePublicSetting.setDisplayTime(spDisplayTime.getSelectedItemPosition());
                break;
            case R.id.sp_power_model:
                blePublicSetting.setPowerMode(spPowerMode.getSelectedItemPosition());
                break;
            default:
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private byte[] getBLEPublicDataPackage(BLEPublicSetting blePublicSetting) {
        byte[] datas = new byte[20];
        datas[0] = 0x57;
        datas[1] = 0x0A;
        datas[2] = 0x00;
        datas[3] = 0x10;

        datas[4] = (byte) blePublicSetting.getGps();
        datas[5] = (byte) blePublicSetting.getBluetoothStatus();
        datas[6] = (byte) blePublicSetting.getSquelch1();
        datas[7] = (byte) 0xFF;
        datas[8] = (byte) blePublicSetting.getVoiceLevel();
        datas[9] = (byte) blePublicSetting.getVoiceDelay();
        datas[10] = (byte) blePublicSetting.getScanType();
        datas[11] = (byte) blePublicSetting.getDisplayModel();
        datas[12] = (byte) blePublicSetting.getBeep();
        datas[13] = (byte) blePublicSetting.getVoice2Send();
        datas[14] = (byte) blePublicSetting.getTotTimeOut();
        datas[15] = (byte) blePublicSetting.getDisplayTime();
        datas[16] = (byte) blePublicSetting.getPowerMode();
        datas[17] = (byte) 0xFF;
        datas[18] = (byte) 0xFF;
        datas[19] = (byte) 0xFF;
        return datas;
    }

    private byte[] getChannelDataPackage(BLEChannelSetting bleChannelSetting) {
        byte[] datas = new byte[20];
        short address = (short) ((bleChannelSetting.getChannelNum() - 1) * 16);
        datas[0] = 0x57;
        datas[1] = (byte) (address >> 8);
        datas[2] = (byte) address;
        datas[3] = 0x10;
        datas[4] = 0x00;
        datas[5] = 0x25;
        datas[6] = 0x12;
        datas[7] = 0x40;
        datas[8] = 0x00;
        datas[9] = 0x25;
        datas[10] = 0x12;
        datas[11] = 0x40;
        datas[12] = (byte) 0x9E;
        datas[13] = 0x02;
        datas[14] = (byte) 0x9E;
        datas[15] = 0x02;
        datas[16] = 0x03;
        datas[17] = 0x02;
        datas[18] = (byte) 0xFF;
        datas[19] = (byte) 0xFF;
        return datas;
    }
}
