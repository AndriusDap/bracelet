package com.andrius.blte.test;

import com.andrius.blte.Bracelet;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;

public class TestService extends Service {
	@Override public void onStart(Intent intent, int startId) {
		Bundle extras = intent.getExtras();
		int button = extras.getInt("Button");
		int click = extras.getInt("Click");
		if(button != Bracelet.BUTTON0) {
			throw new RuntimeException();
		}
		
		if(click != Bracelet.CLICK) {
			throw new RuntimeException();
		}
		this.stopSelf();				
	};
	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}
}
