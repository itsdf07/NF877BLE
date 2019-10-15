package com.itsdf07.nf877ble.bean;

/**
 * @Description ：模拟对讲机信道公共协议
 * @Author itsdf07
 * @Time 2019/10/12
 */

public class BLEPublicSetting {
    /**
     * gps:0-OFF,1-Automatic Mode,2-Manual Mode
     */
    private int gps = 0;
    /**
     * 蓝牙开关:0-OFF,1-ON
     */
    private int bluetoothStatus = 0;
    /**
     * 静噪1:0~9
     */
    private int squelch1 = 0;
    /**
     * 静噪2:保留
     */
    private int squelch2 = 0;
    /**
     * 声控等级:0-OFF,1~9
     */
    private int voiceLevel = 0;
    /**
     * 声控延时[毫秒]:0~5->500,1000,1500,2000,2500,3000
     */
    private int voiceDelay = 0;
    /**
     * 扫描模式:0-TO,1-CO
     */
    private int scanType = 0;

    /**
     * 显示模式:0-Black and white,1-Colorful
     */
    private int displayModel = 0;

    /**
     * BEEP声:0-OFF,1-ON
     */
    private int beep = 0;

    /**
     * 发射提示音:0-OFF,1-ON
     */
    private int voice2Send = 0;

    /**
     * TOT超时:0-OFF,1~12->15s-180s,每15s一个选项,共12
     */
    private int totTimeOut = 0;

    /**
     * 屏保时间:0-OFF,1~30->5s-150s,每5s一个选项,共30
     */
    private int displayTime = 0;

    /**
     * 省电模式:0-OFF,1-ON
     */
    private int powerMode = 0;


    public int getGps() {
        return gps;
    }

    public void setGps(int gps) {
        this.gps = gps;
    }

    public int getBluetoothStatus() {
        return bluetoothStatus;
    }

    public void setBluetoothStatus(int bluetoothStatus) {
        this.bluetoothStatus = bluetoothStatus;
    }

    public int getSquelch1() {
        return squelch1;
    }

    public void setSquelch1(int squelch1) {
        this.squelch1 = squelch1;
    }

    public int getSquelch2() {
        return squelch2;
    }

    public void setSquelch2(int squelch2) {
        this.squelch2 = squelch2;
    }

    public int getVoiceLevel() {
        return voiceLevel;
    }

    public void setVoiceLevel(int voiceLevel) {
        this.voiceLevel = voiceLevel;
    }

    public int getVoiceDelay() {
        return voiceDelay;
    }

    public void setVoiceDelay(int voiceDelay) {
        this.voiceDelay = voiceDelay;
    }

    public int getScanType() {
        return scanType;
    }

    public void setScanType(int scanType) {
        this.scanType = scanType;
    }

    public int getDisplayModel() {
        return displayModel;
    }

    public void setDisplayModel(int displayModel) {
        this.displayModel = displayModel;
    }

    public int getBeep() {
        return beep;
    }

    public void setBeep(int beep) {
        this.beep = beep;
    }

    public int getVoice2Send() {
        return voice2Send;
    }

    public void setVoice2Send(int voice2Send) {
        this.voice2Send = voice2Send;
    }

    public int getTotTimeOut() {
        return totTimeOut;
    }

    public void setTotTimeOut(int totTimeOut) {
        this.totTimeOut = totTimeOut;
    }

    public int getDisplayTime() {
        return displayTime;
    }

    public void setDisplayTime(int displayTime) {
        this.displayTime = displayTime;
    }

    public int getPowerMode() {
        return powerMode;
    }

    public void setPowerMode(int powerMode) {
        this.powerMode = powerMode;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
