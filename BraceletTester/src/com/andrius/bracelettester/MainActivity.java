package com.andrius.bracelettester;

import com.andrius.btle.IRegistrationService;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

public class MainActivity extends Activity {
	public String TAG = "BraceletTester";
	private IRegistrationService service = null;
	private ServiceConnection svConnection = new ServiceConnection() {
		public void onServiceConnected(ComponentName className, IBinder binder) {
			service = IRegistrationService.Stub.asInterface(binder);
			Log.v(TAG, "Registering service");
			try {
				service.RegisterClick("com.andrius.bracelettester", 0, 0);
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		public void onServiceDisconnected(ComponentName clasName) {
			service = null;
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);		
		bindService(new Intent("com.andrius.btle.IRegistrationService"), svConnection, Context.BIND_AUTO_CREATE);
		
	}
	
	@Override
	protected void onDestroy() {
		this.unbindService(svConnection);
		super.onDestroy();
	}
	
}
