package com.andrius.btle;

import com.andrius.blte.R;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

public class Bracelet extends Service {

	private static final String TAG = "Bracelet";
	public static int LED0 = 101;
	public static int LED1 = 102;
	public static int LED2 = 103;
	public static int LED3 = 104;

	public static int BUTTON0 = 201;
	public static int BUTTON1 = 202;
	public static int BUTTON2 = 203;
	public static int BUTTON3 = 204;

	public static int CLICK = 301;
	public static int DOUBLE_CLICK = 302;
	public static int TRIPPLE_CLICK = 303;

	private Context mContext = null;
	private Resources mResources = null;

	@Override
	public void onCreate() {
		mContext = getBaseContext();
		mResources = new Resources(mContext);
		
		Notification processNotification = new Notification.Builder(mContext).setSmallIcon(R.drawable.ic_launcher).setContentText("Bracelet service notification").build();
		processNotification.number = 5;
		startForeground(42, processNotification);
	};

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Bundle extras = intent.getExtras();
		if(extras != null) {
			int led = extras.getInt("LedID");
			int color = extras.getInt("Color");
			if (led != 0 && color != 0) {
				setLed(led, color);
			}
		}
		return Service.START_STICKY;
	}

	private void setLed(int led, int color) {
		Log.v(TAG, "Led " + led + " is glowing");
	}


	@SuppressWarnings("unused")
	private void onClick(int buttonId, int clickType) {
		if (mResources != null && mContext != null) {
			try {
				String target = mResources.getCallback(buttonId, clickType);
				Intent i = new Intent();
				i.setAction("com.andrius.btle.BUTTON_CLICK");
				i.setPackage(target);
				i.putExtra("ButtonID", buttonId);
				i.putExtra("ClickType", clickType);
				mContext.sendBroadcast(i);
			} catch (ResourceNotUsedException e) {
			}
		}
	}

}
