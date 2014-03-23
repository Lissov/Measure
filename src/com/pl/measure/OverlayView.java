package com.pl.measure;
import android.view.*;
import android.app.*;
import android.graphics.*;
import java.util.*;

public class OverlayView extends View
{
	private Paint pSelection = new Paint();
	private float startX;
	private float startY;
	private float endX;
	private float endY;
	
	public OverlayView(Activity context){
		super(context);
		
		pSelection.setColor(Color.RED);
	}

	@Override
	protected void onDraw(Canvas canvas)
	{
		canvas.drawLine(startX,startY,endX,endY, pSelection);
		
		Date d = new Date();
		int s = d.getSeconds();
		canvas.drawLine(0,0,800,300 + s, pSelection);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		float x = event.getX();
		float y = event.getY();
		
		switch(event.getAction()){
			case MotionEvent.ACTION_DOWN:
				startX = x;
				startY = y;
				endX = x;
				endY = y;
				this.invalidate();
				return true;
			case MotionEvent.ACTION_MOVE:
				endX = x;
				endY = y;
				this.invalidate();
				return true;
			case MotionEvent.ACTION_UP:
				endX = x;
				endY = y;
				this.invalidate();
				return true;
			default:
				return super.onTouchEvent(event);
		}
	}
}
