package com.andrius.btle;

import android.app.PendingIntent;

interface IRegistrationService {
	boolean RegisterClick(in String callback, in int ButtonId, in int ClickType);
	boolean UnregisterClick(in String callback, in int ButtonId, in int ClickType);
}