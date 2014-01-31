package com.andrius.pov;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Rect;

public class PovText {
	private static Paint paint;
	private static Bitmap prerendered;

	private Rect source;
	private static Rect destination;
	private int stepCount;
	private int direction = 1;
	public PovText(String text) {
		prerendered = textToBitmap(text, 15, Color.GREEN);
		int height = prerendered.getHeight();
		int width = prerendered.getWidth();
		source = new Rect();

		source.top = 0;
		source.left = 0;
		source.bottom = height - 1;
		source.right = 1;

		destination = new Rect(source);
		stepCount = width;
	}

	private Bitmap textToBitmap(String text, float textSize, int textColor) {
		Paint paint = new Paint();
		paint.setTextSize(textSize);
		paint.setColor(textColor);
		paint.setTextAlign(Align.LEFT);

		int width = (int) (paint.measureText(text) + 0.5f);
		float baseline = (int) (-paint.ascent() + 0.5f);
		int height = (int) (baseline + paint.descent() + 0.5f);

		Bitmap image = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
		Canvas canvas = new Canvas(image);
		canvas.drawText(text, 0, baseline, paint);
		return image;
	}

	public void draw(Canvas canvas, long time) {
		moveSource();
		
		canvas.setMatrix(null);
		canvas.scale(20, 20);
		canvas.drawBitmap(prerendered, source, destination, paint);
	}

	private void moveSource() {
		source.left += direction;
		source.right += direction;
		if(source.left < 0) {
			source.left = stepCount - 1;
			source.right = stepCount;
		}
		
		if(source.right > stepCount) {
			source.left = 0;
			source.right = 1;
		}
	}
	
	
	public void setDirection(int direction) {
		this.direction = direction;
	}
}
