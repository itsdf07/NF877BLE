package com.itsdf07.nf877ble.blelib.client.scan;


public interface DeviceScanCallBack {
    public static final int SCAN_FAILED_BLUETOOTH_DISABLE=1;
    public static final int SCAN_FAILED_BLE_NOT_SUPPORT=2;
    public static final int SCAN_FAILED_LOCATION_PERMISSION_DISABLE=3;
    public static final int SCAN_FAILED_LOCATION_PERMISSION_DISABLE_FOREVER=4;
    public static final int SCAN_FAILED_SYSTEM=5;

    public void onBLEDeviceScan(BLEScanResult device, final int rssi);
    public void onFailed(int code);
    public void onStartSuccess();
}
