package com.itsdf07.nf877ble;

/**
 * @Description: 针对NF877的BLE蓝牙通讯协议
 * @Author itsdf07
 * @Date 2019/10/12
 */
public class BLEMhzUtils {

    /**
     * 握手协议:协议头
     *
     * @return
     */
    public static byte[] handshakeProtocolHead() {
        byte[] protocol = new byte[8];
        protocol[0] = (byte) 0x49;
        protocol[1] = (byte) 0x69;
        protocol[2] = (byte) 0x4E;
        protocol[3] = (byte) 0x48;
        protocol[4] = (byte) 0x53;
        protocol[5] = (byte) 0x47;
        protocol[6] = (byte) 0x30;
        protocol[7] = (byte) 0x4E;
        return protocol;
    }


    /**
     * 接受握手协议
     *
     * @return
     */
    public static byte[] acceptHandshakeProtocol() {
        byte[] protocol = new byte[8];
        protocol[0] = (byte) 0x50;
        protocol[1] = (byte) 0x33;
        protocol[2] = (byte) 0x31;
        protocol[3] = (byte) 0x30;
        protocol[4] = (byte) 0x37;
        protocol[5] = (byte) 0x00;
        protocol[6] = (byte) 0x00;
        protocol[7] = (byte) 0x00;
        return protocol;
    }

}
