package com.andrius.btle;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class LedBroadcastReceiver extends BroadcastReceiver{
	private final static String TAG = "LedBroadcastReceiver";

	@Override
	public void onReceive(Context cont, Intent intent) {
		Bundle extras = intent.getExtras();
		int led = extras.getInt("LedID");
		int color = extras.getInt("Color");
		
		
		if(led == 0) {
			Log.e(TAG, "LedID must not be 0");
			return;
		} else {
			if(color == 0) {
				Log.e(TAG, "Color must not be 0");
				return;
			} else {
				Intent i = new Intent(cont, Bracelet.class);
				i.putExtra("LedID", led);
				i.putExtra("Color", color);
				// If service is running this will call Service.onStartCommand
				cont.startService(i);				
			}			
		}
	}
}
