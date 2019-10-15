package com.itsdf07.nf877ble.blelib.client.core;


public interface OKBLEDeviceListener {

    void onConnected(String deviceTAG);

    void onDisconnected(String deviceTAG);

    void onReadBattery(String deviceTAG, int battery);

    void onReceivedValue(String deviceTAG, String uuid, byte[] value);

    void onWriteValue(String deviceTAG, String uuid, byte[] value, boolean success);

    void onReadValue(String deviceTAG, String uuid, byte[] value, boolean success);

    void onNotifyOrIndicateComplete(String deviceTAG, String uuid, boolean enable, boolean success);
}
