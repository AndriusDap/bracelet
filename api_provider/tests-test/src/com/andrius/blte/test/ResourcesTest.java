package com.andrius.blte.test;

import android.content.Intent;
import android.test.AndroidTestCase;

import com.andrius.btle.Bracelet;
import com.andrius.btle.ResourceNotUsedException;
import com.andrius.btle.ResourceUnavailableException;
import com.andrius.btle.Resources;

public class ResourcesTest extends AndroidTestCase {
	private Resources resources;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		resources = new Resources(mContext);
	}

	public void testCorrectness() {
		try {
			resources.registerButton("Callback", Bracelet.BUTTON1, Bracelet.CLICK);
		} catch (ResourceUnavailableException e) {
			e.printStackTrace();
		}
		try {
			assertEquals("Callback", resources.getCallback(Bracelet.BUTTON1, Bracelet.CLICK));
		} catch (ResourceNotUsedException e) {
			e.printStackTrace();
		}
	}

	public void testRequestCollision() {
		Throwable throwable = null;

		try {
			resources.registerButton("Callback", Bracelet.BUTTON1, Bracelet.CLICK);
		} catch (Exception e) {
		}

		try {
			resources.registerButton("Callback2", Bracelet.BUTTON1, Bracelet.CLICK);
		} catch (Exception e) {
			throwable = e;
		}

		assertTrue(throwable instanceof ResourceUnavailableException);

		try {
			assertEquals("Callback", resources.getCallback(Bracelet.BUTTON1, Bracelet.CLICK));
		} catch (ResourceNotUsedException e) {
			e.printStackTrace();
		}
	}

	volatile int failCounter = 0;
	volatile int successCounter = 0;

	private class Racecar implements Runnable {
		private String name = "Name";
		private Resources mResources;

		public Racecar(int id, Resources r) {
			name += id;
			mResources = r;
		}

		@Override
		public void run() {
			for (int i = 0; i < 100; i++) {
				try {
					mResources.registerButton(name, Bracelet.BUTTON0, Bracelet.CLICK);
					successCounter++;
				} catch (ResourceUnavailableException e) {
					failCounter++;
				}
			}
		}
	}

	public void testRaceCondition() {
		Thread[] threads = new Thread[100];
		for (int i = 0; i < 100; i++) {
			threads[i] = new Thread(new Racecar(i, resources));
		}
		for (Thread t : threads) {
			t.start();
		}
		for (Thread t : threads) {
			try {
				t.join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		assertEquals(1, successCounter);
		assertEquals(100 * 100 - 1, failCounter);
	}

	public void testRelease() throws IllegalArgumentException, ResourceUnavailableException {
		resources.registerButton("callback", Bracelet.BUTTON0, Bracelet.CLICK);
		resources.releaseButton(Bracelet.BUTTON0, Bracelet.CLICK);
		resources.registerButton("callback", Bracelet.BUTTON0, Bracelet.CLICK);
	}

	
	public void testCallback() throws ResourceUnavailableException, ResourceNotUsedException {
		resources.registerButton("com.andrius.btle.test.TestService", Bracelet.BUTTON0, Bracelet.CLICK);
		Intent i = resources.getCallbackIntent(Bracelet.BUTTON0, Bracelet.CLICK);
		mContext.startService(i);
	}
	
	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
		resources.drop();
	}
}
