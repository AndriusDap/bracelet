package com.andrius.btle;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class Resources {
	private final static String TABLE_NAME = "Callbacks";
	private SharedPreferences mSharedPreferences;
	
	/***
	 * 
	 * @param cont Service context
	 */
	public Resources(Context cont) {
		mSharedPreferences = cont.getSharedPreferences(TABLE_NAME, Context.MODE_MULTI_PROCESS);
	}
	
	/***
	 * Registers callback for a button
	 * @param callbackTarget Class name that will be used as a target for intent
	 * @param button button id from {@link Bracelet}
	 * @param click click id from {@link Bracelet}
	 * @throws ResourceUnavailableException Exception will be thrown if resource is being used by any application
	 */
	public void registerButton(String callbackTarget, int button, int click) throws ResourceUnavailableException {

		if(mSharedPreferences.contains(getKey(button, click))) {
			throw new ResourceUnavailableException();
		} else {
			Editor editor = mSharedPreferences.edit();
			editor.putString(getKey(button, click), callbackTarget);
			editor.apply();
		}		
	}
	
	/***
	 * Release callback from button
	 * @param button button id from {@link Bracelet}
	 * @param click click id from {@link Bracelet}
	 */
	public void releaseButton(int button, int click) {
		Editor editor = mSharedPreferences.edit();
		editor.remove(getKey(button, click));
		editor.apply();
	}
	/***
	 * Gets requested callback
	 * @param button button id from {@link Bracelet}
	 * @param click click id from {@link Bracelet}
	 * @return Intent target that must be called on click
	 * @throws ResourceNotUsedException will be thrown if resource is not registered for any application
	 */
	public String getCallback(int button, int click) throws ResourceNotUsedException {
		if(mSharedPreferences.contains(getKey(button, click))) {
			
			String result = mSharedPreferences.getString(getKey(button, click), "DeletedInRacing");
			if(result.equals("DeletedInRacing")) {
				throw new ResourceNotUsedException();
			}
			
			return result;
		} else {
			throw new ResourceNotUsedException();
		}
	}
	
	/***
	 * Gets intent that should be launched as a callback, or throws exception if nothing should be done
	 * @param button button id from {@link Bracelet}
	 * @param click click id from {@link Bracelet}
	 * @return Intent that must be launched as a callback
	 * @throws ResourceNotUsedException if nothing is using this button and click combo
	 */
	public Intent getCallbackIntent(int button, int click) throws ResourceNotUsedException {
		String target = mSharedPreferences.getString(getKey(button, click), "DeletedInRacing");
		if(target.equals("DeletedInRacing")) {
			throw new ResourceNotUsedException();
		}
		
		Intent i = new Intent(target);
		i.putExtra("Button", button);
		i.putExtra("Click", click);
		
		return i;		
	}
	
	// for testing purposes only
	public void drop(){
		mSharedPreferences.edit().clear().commit();
	}
	/***
	 * Returns key used to store callback for this button and click set
	 * @param button
	 * @param click
	 * @return sharedPreferences key
	 */
	private static String getKey(int button, int click) {
		return button + " " + click;
	}	
}
