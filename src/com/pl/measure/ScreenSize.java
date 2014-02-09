package com.pl.measure;
import android.app.*;
import android.content.*;
import android.preference.*;
import android.util.*;
import android.view.*;
import java.lang.reflect.*;
import android.widget.*;

public class ScreenSize
{
	public static float xDpi = 0;
	public static float yDpi = 0;
		
	private void loadDpi(Activity activity, Display disp){

		//clearDpiPrefs(activity);
		
		try {		
			DisplayMetrics dm = new DisplayMetrics();
			disp.getMetrics(dm);
			
			boolean isHorz = disp.getWidth() > disp.getHeight();

			float xD = dm.xdpi * dm.density;
			float yD = dm.ydpi * dm.density;
			
			SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(activity);
			String lds = sp.getString("long_dpi", "0");
			String sds = sp.getString("short_dpi", "0");
			float ld = Helpers.TryParse(lds, 0);
			float sd = Helpers.TryParse(sds, 0);
			
			if (ld != 0 && sd != 0){
				if (isHorz){
					xDpi = ld;
					yDpi = sd;
				} else{
					xDpi = sd;
					yDpi = ld;
				}
			} else{
				xDpi = xD;
				yDpi = yD;
				saveDpiToPrefs(activity);
			}
		} catch (Exception ex){
			Toast.makeText(activity, "Exception: " + ex.getMessage(), Toast.LENGTH_LONG).show();
		}	
	}
	
	private void saveDpiToPrefs(Activity activity){
		WindowManager wm = (WindowManager) activity.getSystemService(Context.WINDOW_SERVICE);
		Display disp = wm.getDefaultDisplay();
		DisplayMetrics dm = new DisplayMetrics();
		disp.getMetrics(dm);

		boolean isHorz = disp.getWidth() > disp.getHeight();

		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(activity);
		
		SharedPreferences.Editor e = sp.edit();
		e.putString("long_dpi", "" + (isHorz ? xDpi : yDpi));
		e.putString("short_dpi", "" + (isHorz ? yDpi : xDpi));
		e.commit();
	}
	
	private void clearDpiPrefs(Activity activity){
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(activity);
		SharedPreferences.Editor e = sp.edit();
		e.putString("long_dpi", "0");
		e.putString("short_dpi", "0");
		e.commit();	
	}
	
	public double getExpectedDiagonal(Activity activity){
		
		WindowManager wm = (WindowManager) activity.getSystemService(Context.WINDOW_SERVICE);
		Display disp = wm.getDefaultDisplay();
		
		if (xDpi == 0 || yDpi == 0)
		{
			loadDpi(activity, disp);
		}
		
		int width = 0;
		int height = 0;
		try{
		
			Method mGetRawW = Display.class.getMethod("getRawWidth");
			Method mGetRawH = Display.class.getMethod("getRawHeight");
			width = (Integer)mGetRawW.invoke(disp);
			height = (Integer)mGetRawH.invoke(disp);
			
			//Toast.makeText(activity, "Raw: " + width + "x" + height, 2000).show();
		} catch(Exception e){
			width = disp.getWidth();
			height = disp.getHeight();
			
			//Toast.makeText(activity, "WH: " + width + "x" + height, 2000).show();
		}		
		
		double ws = width / xDpi;
		double hs = height / yDpi;
		
		double diag = Math.sqrt(ws*ws + hs*hs);
		
		return diag;
	}
	
	public void setDiagonal(double diagonal, Activity activity){
		double d = getExpectedDiagonal(activity);
		
		xDpi = (float)(xDpi * d / diagonal);
		yDpi = (float)(yDpi * d / diagonal);
		
		saveDpiToPrefs(activity);
	}
}
