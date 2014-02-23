package com.andrius.btle;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends Activity {
	private static final String TAG = "MainActivity";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Log.v(TAG, "MainActivity launched");
		if(!isBraceletRunning()) {
			Log.v(TAG, "Starting bracelet service");
			Intent i = new Intent(this, Bracelet.class);
			startService(i);
		}		
	}
	
	private boolean isBraceletRunning() {
		ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
		for(RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
			if(Bracelet.class.getName().equals(service.service.getClassName())) {
				return true;
			}
		}
		return false;
	}
}
