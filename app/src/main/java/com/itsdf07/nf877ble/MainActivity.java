package com.itsdf07.nf877ble;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.itsdf07.nf877ble.blelib.client.scan.BLEScanResult;
import com.itsdf07.nf877ble.blelib.client.scan.DeviceScanCallBack;
import com.itsdf07.nf877ble.blelib.client.scan.OKBLEScanManager;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemLongClickListener {
    private static final String TAG = "MainActivity";

    private Button btnStartScan;
    private Button btnStopScan;
    private ListView lvBles;

    OKBLEScanManager scanManager;
    MyLinkedHashMap<String, BLEScanResult> scanedResults;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnStartScan = findViewById(R.id.btn_start_scan);
        btnStopScan = findViewById(R.id.btn_stop_scan);
        lvBles = findViewById(R.id.lv_bles);

        lvBles.setAdapter(adapter);
        lvBles.setOnItemLongClickListener(this);

        btnStartScan.setOnClickListener(this);
        btnStopScan.setOnClickListener(this);

        scanedResults=new MyLinkedHashMap<>();
        scanManager=new OKBLEScanManager(this);
        scanManager.setScanCallBack(scanCallBack);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_start_scan:
                scanManager.startScan();
                break;
            case R.id.btn_stop_scan:
                scanManager.stopScan();
                break;
            default:
                break;
        }
    }

    private BaseAdapter adapter = new BaseAdapter() {

        @Override
        public int getCount() {
            return scanedResults == null ? 0 : scanedResults.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewholder;
            if (convertView == null) {
                viewholder = new ViewHolder();
                convertView = getLayoutInflater().inflate(R.layout.item_ble_device, null);
                convertView.setTag(viewholder);
                viewholder.tv_ble_name = convertView.findViewById(R.id.tv_ble_name);
                viewholder.tv_ble_mac = convertView.findViewById(R.id.tv_ble_mac);
                viewholder.tv_ble_rssi = convertView.findViewById(R.id.tv_ble_rssi);
            } else {
                viewholder = (ViewHolder) convertView.getTag();
            }
            viewholder.tv_ble_name.setText(scanedResults.get(position).getBluetoothDevice().getName());
            viewholder.tv_ble_mac.setText(scanedResults.get(position).getBluetoothDevice().getAddress());
            viewholder.tv_ble_rssi.setText(scanedResults.get(position).getRssi() + "");
            return convertView;
        }
    };

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        scanManager.stopScan();
        Intent intent = new Intent(this, BLEActivity.class);
        intent.putExtra(BLEActivity.EXTRA_BLEDEVICE, scanedResults.get(position));
        startActivity(intent);
        return true;
    }

    class ViewHolder {
        TextView tv_ble_name;
        TextView tv_ble_mac;
        TextView tv_ble_rssi;
    }

    public void updatePosition(int posi) {
        int visibleFirstPosi = lvBles.getFirstVisiblePosition();
        int visibleLastPosi = lvBles.getLastVisiblePosition();
        if (posi >= visibleFirstPosi && posi <= visibleLastPosi) {
            View view = lvBles.getChildAt(posi - visibleFirstPosi);
            ViewHolder holder = (ViewHolder) view.getTag();
        }
    }

    DeviceScanCallBack scanCallBack = new DeviceScanCallBack() {
        @Override
        public void onBLEDeviceScan(BLEScanResult device, int rssi) {
            Log.d(TAG, "onBLEDeviceScan->device:" + device.toString());
            int value[] = scanedResults.put(device.getMacAddress(), device);

            if (value[1] == 1) {
                //这是新增数据,
                adapter.notifyDataSetChanged();
            } else {
                //这是重复数据,刷新rssi
                int index = value[0];
                updatePosition(index);
            }
        }

        @Override
        public void onFailed(int code) {
            Log.e(TAG, "onFailed->code:" + code);
            switch (code) {
                case DeviceScanCallBack.SCAN_FAILED_BLE_NOT_SUPPORT:
                    Toast.makeText(MainActivity.this, "该设备不支持BLE", Toast.LENGTH_SHORT).show();
//                    refreshLayout.finishRefresh(false);
                    break;
                case DeviceScanCallBack.SCAN_FAILED_BLUETOOTH_DISABLE:
                    Toast.makeText(MainActivity.this, "请打开手机蓝牙", Toast.LENGTH_SHORT).show();
//                    refreshLayout.finishRefresh(false);
                    break;
                case DeviceScanCallBack.SCAN_FAILED_LOCATION_PERMISSION_DISABLE:
                    Toast.makeText(MainActivity.this, "请授予位置权限以扫描周围的蓝牙设备", Toast.LENGTH_SHORT).show();
//                    refreshLayout.finishRefresh(false);
                    break;
                case DeviceScanCallBack.SCAN_FAILED_LOCATION_PERMISSION_DISABLE_FOREVER:
//                    refreshLayout.finishRefresh(false);
                    Toast.makeText(MainActivity.this, "位置权限被您永久拒绝,请在设置里授予位置权限以扫描周围的蓝牙设备", Toast.LENGTH_SHORT).show();
                    break;
            }
        }

        @Override
        public void onStartSuccess() {
            Log.e(TAG, "onStartSuccess->...");
//            refreshLayout.finishRefresh();
        }
    };
}
