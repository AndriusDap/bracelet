package com.andrius.blte;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothAdapter.LeScanCallback;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends Activity {

	private class NamedBluetoothDevice {
		BluetoothDevice device;

		public NamedBluetoothDevice(BluetoothDevice source) {
			device = source;
		}

		@Override
		public String toString() {
			return device.getName();
		}

		public BluetoothDevice getDevice() {
			return device;
		}
	}

	BluetoothManager mBluetoothManager;
	BluetoothAdapter mBluetoothAdapter;
	ListView deviceList;
	Button startScanButton;
	ArrayAdapter<NamedBluetoothDevice> deviceListAdapter;
	LeScanCallback mLeScanCallback;

	BluetoothGatt mBluetoothGatt = null;
	BluetoothDevice connectedDevice = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		// Get default bluetooth manager and adapter
		mBluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
		mBluetoothAdapter = mBluetoothManager.getAdapter();
		// Adapter is null if device does not support bluetooth.
		if (mBluetoothAdapter == null) {
			showMessage("Bluetoth is not available, shutting down");
			this.finish();
		}
		// Enable adapter if not enabled, should use intent to system preferences
		if (!mBluetoothAdapter.isEnabled()) {
			mBluetoothAdapter.enable();
		}

		// Callback object must be saved, because it is used to identify scan that must be canceled before connecting/finishing
		mLeScanCallback = new LeScanCallback() {
			@Override
			public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
				deviceListAdapter.add(new NamedBluetoothDevice(device));
			}
		};

		startScanButton = (Button) findViewById(R.id.scan);
		startScanButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!mBluetoothAdapter.isDiscovering()) {
					startScanButton.setText("Scanning");
					startScan();
				} else {
					startScanButton.setText("Scan");
					stopScan();
				}
			}
		});
		
		// Default list view with strings to show available devices
		deviceListAdapter = new ArrayAdapter<NamedBluetoothDevice>(this, android.R.layout.simple_list_item_1);
		deviceList = (ListView) findViewById(R.id.device_list);
		deviceList.setAdapter(deviceListAdapter);

		
		deviceList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parentView, View childview, int position, long id) {
				NamedBluetoothDevice selectedDevice = (NamedBluetoothDevice) deviceList.getItemAtPosition(position);
				stopScan();
				startScanButton.setText("Scan");
				connect(selectedDevice.getDevice());
			}
		});
	}

	private void showMessage(String message) {
		Toast.makeText(this, message, Toast.LENGTH_LONG).show();
		Log.i("Bt", message);
	}

	private void startScan() {
		if (mBluetoothAdapter != null) {
			if (!mBluetoothAdapter.isDiscovering()) {
				mBluetoothAdapter.startLeScan(mLeScanCallback);
			}
		}
	}

	private void stopScan() {
		if (mBluetoothAdapter.isDiscovering()) {
			mBluetoothAdapter.stopLeScan(mLeScanCallback);
		}
	}

	private void connect(BluetoothDevice device) {
		// Must stop scan before connecting
		if(mBluetoothAdapter.isDiscovering()){
			stopScan();
		}
		// Disconect from current device
		if (connectedDevice != null) {
			disconnect();
		}
		connectedDevice = device;

		mBluetoothGatt = connectedDevice.connectGatt(this, true, new BluetoothGattCallback() {

			@Override
			public void onDescriptorRead(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
				super.onDescriptorRead(gatt, descriptor, status);
				showMessage("onDescriptorRead");
			}

			@Override
			public void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
				super.onDescriptorWrite(gatt, descriptor, status);
				showMessage("onDescriptorWrite");
			}

			@Override
			public void onReadRemoteRssi(BluetoothGatt gatt, int rssi, int status) {
				super.onReadRemoteRssi(gatt, rssi, status);
				showMessage("onReadRemoteRsii");
			}

			@Override
			public void onReliableWriteCompleted(BluetoothGatt gatt, int status) {
				super.onReliableWriteCompleted(gatt, status);
				showMessage("onReliableWriteCompleted");
			}

			@Override
			public void onServicesDiscovered(BluetoothGatt gatt, int status) {
				super.onServicesDiscovered(gatt, status);
				showMessage("onServicesDiscovered");			
			}

			@Override
			public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
				super.onConnectionStateChange(gatt, status, newState);
				showMessage("onConnectionStateChange");
			}

			@Override
			public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
				super.onCharacteristicWrite(gatt, characteristic, status);
				showMessage("onCharacteristicWrite");
			}

			@Override
			public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
				super.onCharacteristicRead(gatt, characteristic, status);
				showMessage("onCharacteristicRead");
			}

			@Override
			public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
				super.onCharacteristicChanged(gatt, characteristic);
				showMessage("onCharacteristicChanged");
			}
		});
	}

	private void disconnect() {
		if (mBluetoothGatt != null) {
			mBluetoothGatt.disconnect();
			mBluetoothGatt.close();
			mBluetoothGatt = null;
		}
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		// Should not leave bluetooth in background while scaning
		stopScan();
	}
}
