package com.andrius.btle;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class DeviceBootReceiver extends BroadcastReceiver {
	private final static String TAG = "DeviceBootReceiver";

	@Override
	public void onReceive(Context context, Intent intent) {
		Log.v(TAG, "Starting bracelet service");
		Intent i = new Intent(context, Bracelet.class);
		context.startService(i);
	}
}
