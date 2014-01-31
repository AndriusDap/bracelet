package com.andrius.pov;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class PovViewer extends Activity {
	SurfaceView surfaceView;
	SurfaceHolder holder;
	Runnable task;
	Thread thread;
	PovText text;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_pov);

		Intent i = this.getIntent();
		String message = i.getExtras().getString("Message");

		text = new PovText(message);
		surfaceView = (SurfaceView) findViewById(R.id.surface_view);
		holder = surfaceView.getHolder();
		task = new Runnable() {
			@Override
			public void run() {
				long time = System.currentTimeMillis();
				long oldTime = time;
				while (!Thread.interrupted()) {
					Canvas c = holder.lockCanvas();
					if (c != null) {
						text.draw(c, time - oldTime);
						holder.unlockCanvasAndPost(c);
					}

					oldTime = time;
					time = System.currentTimeMillis();
				}
			}
		};
		holder.addCallback(new SurfaceHolder.Callback() {

			@Override
			public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
			}

			@Override
			public void surfaceCreated(SurfaceHolder holder) {
				thread = new Thread(task);
				thread.start();
			}

			@Override
			public void surfaceDestroyed(SurfaceHolder holder) {
				thread.interrupt();
			}
		});

		SensorManager sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		Sensor sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

		sensorManager.registerListener(new SensorEventListener() {

			double avg_x;
			long sample_count;

			@Override
			public void onSensorChanged(SensorEvent event) {

				float x = event.values[0];
				if (x > 3 * (avg_x / sample_count)) {
					text.setDirection(1);
				} else {
					if (Math.abs(x) > 3 * (avg_x / sample_count)) {
						text.setDirection(-1);
					}
				}

				if (sample_count < Long.MAX_VALUE) {
					avg_x += (double) x;
					sample_count++;
				}
			}

			@Override
			public void onAccuracyChanged(Sensor sensor, int accuracy) {
			}

		}, sensor, SensorManager.SENSOR_DELAY_FASTEST);
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (thread != null) {
			thread.interrupt();
		}
	}
}
