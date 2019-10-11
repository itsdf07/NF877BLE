package com.itsdf07.nf877ble.blelib.server.core;


import com.itsdf07.nf877ble.blelib.common.OKBLECharacteristicModel;
import com.itsdf07.nf877ble.blelib.common.OKBLEServiceModel;

import java.util.List;

/**
 * Created by a1anwang.com on 2018/5/30.
 */

public interface OKBLEServerDevice {


    void addCharacteristic(List<OKBLECharacteristicModel> okbleCharacteristicModels, OKBLEServiceModel okbleServiceModel, OKBLEServerOperation.BLEServerOperationListener operationListener);

    void reSet();
}
