package com.andrius.btle;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

public class RegistrationService extends Service {
	private Resources mResources;
	private static String TAG = "RegistrationService";

	@Override
	public void onCreate() {
		Context cont = getBaseContext();
		mResources = new Resources(cont);
	};

	@Override
	public IBinder onBind(Intent arg0) {
		return mBinder;
	}

	private final IRegistrationService.Stub mBinder = new IRegistrationService.Stub() {

		@Override
		public boolean RegisterClick(String Package, int ButtonId, int ClickType) throws RemoteException {
			try {
				Log.v(TAG, "RegisterClick called");
				mResources.registerButton(Package, ButtonId, ClickType);
			} catch (ResourceUnavailableException e) {
				return false;
			}
			return true;
		}

		@Override
		public boolean UnregisterClick(String Package, int ButtonId, int ClickType) throws RemoteException {
			try {
				if (mResources.getCallback(ButtonId, ClickType).equals(Package)) {
					mResources.releaseButton(ButtonId, ClickType);
					return true;
				} else {
					return false;
				}
			} catch (ResourceNotUsedException e) {
				return false;
			}
		}

	};

}
