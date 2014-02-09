package com.pl.measure;

import android.view.*;
import android.content.*;
import android.graphics.*;

public class RulerView extends View
{
	private Paint rulerPaint = new Paint();
	private ScreenSize screenSize;
	private Paint textPaint = new Paint();
	
	private float scale = 1.0f;
	public float getScale(){
		return scale;
	}
	public void setScale(float value){
		scale = value;
		this.invalidate();
	}
	
	public RulerView(Context context){
		super(context);
		
		rulerPaint.setColor(Color.WHITE);
		textPaint.setColor(Color.LTGRAY);
		textPaint.setTextSize(20);
		screenSize = new ScreenSize();
	}
	
	@Override
	public void onDraw(Canvas canvas){
		for (int n = 0; n<=1000; n+=1){
			float x = 10 + (float)n/10 * screenSize.xDpi * scale;
			float lineH = (n % 10 == 0) ? 100 : (n % 5 == 0 ? 70 : 50); 

			canvas.drawLine(x, 10, x, 10 + lineH, rulerPaint);
			
			if (n % 10 == 0) {
	        	canvas.drawText("" + n/10, x, 10 + 100 + 10, textPaint); 
			}
		}
	}
}
