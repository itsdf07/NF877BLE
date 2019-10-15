package com.itsdf07.nf877ble.bean;

/**
 * @Description ：信道协议
 * @Author itsdf07
 * @Time 2017/12/8
 */

public class BLEChannelSetting {

    /**
     * 信道的值+1
     */
    private long id = 0L;
    /**
     * 信道
     */
    private int channelNum = 1;
    /**
     * 发射频率
     */
    private String sendHz = "";

    /**
     * 接收频率
     */
    private String recHz = "";//462.0125
    /**
     * CTC/DCS解码
     */
    private int decodeCtcDcs = 0;
    /**
     * CTC/DCS解码
     */
    private String strDecodeCtcDcs = "";
    /**
     * CTC/DCS编码
     */
    private int encodeCtcDcs = 0;
    /**
     * CTC/DCS编码
     */
    private String strEncodeCtcDcs = "";

    /**
     * 添加扫描
     */
    private int scanAdd = 0;
    /**
     * 带宽
     */
    private int bandwidth = 0;
    /**
     * 发射功率
     */
    private int transmitPower = 0;
    /**
     * 繁忙锁定
     */
    private int busyLock = 0;
    /**
     * 扰频
     */
    private int scramble = 0;
    /**
     * 压扩
     */
    private int companding = 0;
    /**
     * 特殊信令
     */
    private int specialSignaling = 0;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getChannelNum() {
        return channelNum;
    }

    public void setChannelNum(int channelNum) {
        this.channelNum = channelNum;
    }

    public String getSendHz() {
        return sendHz;
    }

    public void setSendHz(String sendHz) {
        if (sendHz.toUpperCase().equals("FFF.FFFF")) {
            sendHz = "";
        }
        this.sendHz = sendHz;
    }

    public String getRecHz() {
        return recHz;
    }

    public void setRecHz(String recHz) {
        if (recHz.toUpperCase().equals("FFF.FFFF")) {
            recHz = "";
        }
        this.recHz = recHz;
    }

    public int getDecodeCtcDcs() {
        return decodeCtcDcs;
    }

    public void setDecodeCtcDcs(int decodeCtcDcs) {
        this.decodeCtcDcs = decodeCtcDcs;
    }

    public String getStrDecodeCtcDcs() {
        return strDecodeCtcDcs;
    }

    public void setStrDecodeCtcDcs(String strDecodeCtcDcs) {
        this.strDecodeCtcDcs = strDecodeCtcDcs;
    }

    public int getEncodeCtcDcs() {
        return encodeCtcDcs;
    }

    public void setEncodeCtcDcs(int encodeCtcDcs) {
        this.encodeCtcDcs = encodeCtcDcs;
    }

    public String getStrEncodeCtcDcs() {
        return strEncodeCtcDcs;
    }

    public void setStrEncodeCtcDcs(String strEncodeCtcDcs) {
        this.strEncodeCtcDcs = strEncodeCtcDcs;
    }

    public int getScanAdd() {
        return scanAdd;
    }

    public void setScanAdd(int scanAdd) {
        this.scanAdd = scanAdd;
    }

    public int getBandwidth() {
        return bandwidth;
    }

    public void setBandwidth(int bandwidth) {
        this.bandwidth = bandwidth;
    }

    public int getTransmitPower() {
        return transmitPower;
    }

    public void setTransmitPower(int transmitPower) {
        this.transmitPower = transmitPower;
    }

    public int getBusyLock() {
        return busyLock;
    }

    public void setBusyLock(int busyLock) {
        this.busyLock = busyLock;
    }

    public int getScramble() {
        return scramble;
    }

    public void setScramble(int scramble) {
        this.scramble = scramble;
    }

    public int getCompanding() {
        return companding;
    }

    public void setCompanding(int companding) {
        this.companding = companding;
    }

    public int getSpecialSignaling() {
        return specialSignaling;
    }

    public void setSpecialSignaling(int specialSignaling) {
        this.specialSignaling = specialSignaling;
    }

    @Override
    public String toString() {
        return "信道:" + channelNum
                + ",发射频率:" + sendHz + ",接收频率:" + recHz
                + ",CTC/DCS解码:" + decodeCtcDcs + "-" + strDecodeCtcDcs + ",CTC/DCS编码:" + encodeCtcDcs + "-" + strEncodeCtcDcs
                + ",是否添加扫描:" + scanAdd + ",带宽:" + bandwidth + ",发射功率:" + transmitPower + ",是否繁忙锁定:" + busyLock
                + ",是否扰频:" + scramble + ",是否压扩:" + companding + ",是否特殊信令:" + specialSignaling;
    }
}
