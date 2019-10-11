package com.itsdf07.nf877ble.blelib.client.scan;

import android.Manifest;
import android.annotation.TargetApi;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Build;
import android.os.Handler;
import android.os.Message;


import com.itsdf07.nf877ble.blelib.permission.PermissionConstants;
import com.itsdf07.nf877ble.blelib.permission.PermissionUtils;

import java.util.List;

public class OKBLEScanManager {
    private String TAG="OKBLEScanManager";
    private static final int DefaultScanDuration = 10 * 1000;
    private static final int DefaultSleepDuration = 2 * 1000;


    private int scanDuration = DefaultScanDuration;

    private int sleepDuration = DefaultSleepDuration;

    private BluetoothAdapter bluetoothAdapter;
    private BluetoothManager bluetoothManager;
    private Context context;
    private DeviceScanCallBack deviceScanCallBack;
    private boolean isScanning = false;


    private static final int MsgWhat_stopScan=0;

    private static final int MsgWhat_startScan=1;

    public boolean isScanning() {
        return isScanning;
    }

    public OKBLEScanManager(Context context) {
        this.context = context;
        init();
    }

    private boolean enableBluetooth=false;
    /**
     *
     * @param context
     * @param enableBluetooth 初始化时候，如果手机蓝牙未打开，则调用打开蓝牙代码
     */
    public OKBLEScanManager(Context context, boolean enableBluetooth) {
        this.context = context;
        this.enableBluetooth=enableBluetooth;
        init();
    }

    /**
     * 关闭手机蓝牙
     */
    public void disableBluetooth(){
        if(bluetoothAdapter!=null){
            bluetoothAdapter.disable();
        }
    }
    /**
     * 打开手机蓝牙
     */
    public void enableBluetooth(){
        if(bluetoothAdapter!=null){
            bluetoothAdapter.enable();
        }
    }

    public void setScanCallBack(DeviceScanCallBack scanCallBack) {
        this.deviceScanCallBack = scanCallBack;
    }
    public boolean isSupportBLE(){
        if(Build.VERSION.SDK_INT< Build.VERSION_CODES.JELLY_BEAN_MR2){
            return false;
        }
        if (!context. getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            return false;
        }
        return true;
    }

    private boolean autoRebootBluetoothWhenScanFailed=false;//开启扫描返回失败的时候 是否自动调用代码重启手机蓝牙


    /**
     *
     * @param value
     */
    public void setAutoRebootBluetoothWhenScanFailed(boolean value){
        autoRebootBluetoothWhenScanFailed=value;
    }

    private void init() {
        bluetoothManager = (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);
        bluetoothAdapter = bluetoothManager.getAdapter();
        if(enableBluetooth){
            if(!bluetoothAdapter.isEnabled()) {
                bluetoothAdapter.enable();
            }
        }

     }

    public void setScanDuration(int scanDuration) {
        this.scanDuration = scanDuration;
    }

    public void setSleepDuration(int sleepDuration) {
        this.sleepDuration = sleepDuration;
    }

    public BluetoothDevice retrieveBluetoothDeviceWithMac(String mac) {
        if (bluetoothAdapter != null) {
            if (BluetoothAdapter.checkBluetoothAddress(mac)) {
                return bluetoothAdapter.getRemoteDevice(mac);
            }
        }
        return null;
    }

    private Handler handle = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            // LogUtils.e(" msg.what:"+msg.what);
            if(msg.what==MsgWhat_stopScan){
                doStopScan();
                handle.removeMessages(MsgWhat_startScan);
                handle.sendEmptyMessageDelayed(MsgWhat_startScan, sleepDuration);
            }else if(msg.what==MsgWhat_startScan){
                doScan();
            }
        }

    };

    public boolean bluetoothIsEnable() {
        return bluetoothAdapter.isEnabled();
    }

    public void startScan() {
        if(!isSupportBLE()){
            if(callback!=null){
                deviceScanCallBack.onFailed(DeviceScanCallBack.SCAN_FAILED_BLE_NOT_SUPPORT);
            }
            return ;
        }
        if (!bluetoothIsEnable()) {
            if(callback!=null){
                deviceScanCallBack.onFailed(DeviceScanCallBack.SCAN_FAILED_BLUETOOTH_DISABLE);
            }
            return ;
        }

        boolean isGranted=  PermissionUtils.isGranted(Manifest.permission.ACCESS_FINE_LOCATION)||PermissionUtils.isGranted(Manifest.permission.ACCESS_COARSE_LOCATION);
        if(isGranted){
            if(deviceScanCallBack!=null){
                deviceScanCallBack.onStartSuccess();
            }
            doScan();
        }else{
            PermissionUtils.permission(PermissionConstants.LOCATION).callback(new PermissionUtils.FullCallback() {
                @Override
                public void onGranted(List<String> permissionsGranted) {
                    if(deviceScanCallBack!=null){
                        deviceScanCallBack.onStartSuccess();
                    }
                    //权限授权成功
                    doScan();
                }

                @Override
                public void onDenied(List<String> permissionsDeniedForever, List<String> permissionsDenied) {
                    //权限被禁止
                    if(!permissionsDeniedForever.isEmpty()){
                        if(deviceScanCallBack!=null){
                            deviceScanCallBack.onFailed(DeviceScanCallBack.SCAN_FAILED_LOCATION_PERMISSION_DISABLE_FOREVER);
                        }
                    }else{
                        if(deviceScanCallBack!=null){
                            deviceScanCallBack.onFailed(DeviceScanCallBack.SCAN_FAILED_LOCATION_PERMISSION_DISABLE);
                        }
                    }
                }
            }).request();
        }
    }

    private void doScan(){
        if(!bluetoothIsEnable()){
            return;
        }
        isScanning = true;
        if (sleepDuration > 0) {
            handle.removeMessages(MsgWhat_stopScan);
            handle.sendEmptyMessageDelayed(MsgWhat_stopScan, scanDuration);
        }
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP&& isSpecialPhone()) {
            if(bleScanner!=null&&bleScannerCallback!=null){
                ((BluetoothLeScanner)bleScanner).stopScan((ScanCallback) bleScannerCallback);
            }
            if(bleScannerCallback==null){
                bleScannerCallback=new ScanCallback() {
                    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
                    @Override
                    public void onScanResult(int callbackType, ScanResult result) {
                        super.onScanResult(callbackType, result);
                        if(!isScanning) return;
                        BLEScanResult bleScanResult = new BLEScanResult(result.getDevice(), result.getScanRecord().getBytes(),result.getRssi());
                        if (deviceScanCallBack != null) {
                            deviceScanCallBack.onBLEDeviceScan(bleScanResult, result.getRssi());
                        }
                    }

                    @Override
                    public void onScanFailed(int errorCode) {
                        super.onScanFailed(errorCode);
                        if(deviceScanCallBack!=null){
                            deviceScanCallBack.onFailed(DeviceScanCallBack.SCAN_FAILED_SYSTEM);
                        }
                        if(autoRebootBluetoothWhenScanFailed){
                            if(bluetoothAdapter!=null){
                                bluetoothAdapter.disable();
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        while(true) {
                                            try {
                                                Thread.sleep(500);
                                            } catch (InterruptedException e) {
                                                e.printStackTrace();
                                            }
                                            //要等待蓝牙彻底关闭，然后再打开，才能实现重启效果
                                            if(bluetoothAdapter.getState() == BluetoothAdapter.STATE_OFF) {
                                                bluetoothAdapter.enable();
                                                break;
                                            }
                                        }
                                    }

                                }).start();
                            }
                        }
                    }
                };
            }
            if(bleScanner==null){
                bleScanner=bluetoothAdapter.getBluetoothLeScanner();
            }
            ((BluetoothLeScanner)bleScanner).startScan(null,new ScanSettings.Builder().setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY).build(), (ScanCallback) bleScannerCallback);

        }else{
            bluetoothAdapter.stopLeScan(callback);
            bluetoothAdapter.startLeScan(callback);
        }

    }




    public void stopScan() {
        isScanning = false;
        doStopScan();
        handle.removeMessages(MsgWhat_stopScan);
    }
    private void doStopScan(){
        handle.removeMessages(MsgWhat_startScan);
        if(!bluetoothIsEnable()){
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP&&isSpecialPhone()) {
            if(bleScanner!=null&&bleScannerCallback!=null){
                ((BluetoothLeScanner)bleScanner).stopScan((ScanCallback) bleScannerCallback);
            }
        }else{
            bluetoothAdapter.stopLeScan(callback);

        }
    }


    private BluetoothAdapter.LeScanCallback callback = new BluetoothAdapter.LeScanCallback() {
        @Override
        synchronized public void onLeScan(BluetoothDevice device, final int rssi, byte[] scanRecord) {
            if(!isScanning) return;
            BLEScanResult bleScanResult = new BLEScanResult(device, scanRecord,rssi);
            if (deviceScanCallBack != null) {
                deviceScanCallBack.onBLEDeviceScan(bleScanResult, rssi);
            }
        }
    };
    //***************************************************************************************//
    private Object bleScannerCallback;
    private Object bleScanner;


    /**
     * 判断是不是需要特殊适配的机型，比如一加手机，在android8.0系统上使用4.3API扫描方法无法扫描到BLE设备，但是使用5.0API可以扫描到
     * @return
     */
    private boolean isSpecialPhone(){


        return true;
    }
    public void requestLocationPermission(){
        boolean isGranted=  PermissionUtils.isGranted(Manifest.permission.ACCESS_FINE_LOCATION)||PermissionUtils.isGranted(Manifest.permission.ACCESS_COARSE_LOCATION);
        if(isGranted) return;
        PermissionUtils.permission(PermissionConstants.LOCATION).callback(new PermissionUtils.FullCallback() {
            @Override
            public void onGranted(List<String> permissionsGranted) {
            }

            @Override
            public void onDenied(List<String> permissionsDeniedForever, List<String> permissionsDenied) {

            }
        }).request();
    }


    /**
     * 手机是否开启位置服务
     */
    public boolean isLocationServiceEnable() {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        boolean gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean network = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        if (gps || network) {
            return true;
        }
        return false;
    }
}
