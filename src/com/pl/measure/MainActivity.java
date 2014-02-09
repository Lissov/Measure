package com.pl.measure;

import android.app.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import android.view.View.*;
import java.text.*;
import android.content.*;
import android.widget.AdapterView.*;
import java.util.*;
import android.preference.*;
import android.content.pm.*;

public class MainActivity extends Activity
{
	private EditText etDiagonal;
	private ScreenSize screenSize;
	private RulerView ruler;
	private Spinner spUnit;
	private Spinner spRulerUnit;
	private float diagonalScale = 1; // inch initially
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
	{
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
		
		screenSize = new ScreenSize();
		
		etDiagonal = (EditText) findViewById(R.id.etDiagonal);

		LinearLayout llRuler = (LinearLayout) findViewById(R.id.llRuler);
		if (llRuler != null){
			ruler = new RulerView(this);
			llRuler.addView(ruler);
		}
		
		spUnit = (Spinner)findViewById(R.id.spDiagUnit);
		spUnit.setOnItemSelectedListener(new OnItemSelectedListener() {
				@Override
				public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id){
					diagonalUnitChanged();
				}
				@Override
				public void onNothingSelected(AdapterView<?> parentView){
					diagonalUnitChanged();
				}
		});
		
		spRulerUnit = (Spinner) findViewById(R.id.spRulerUnit);
		if (spRulerUnit != null){
			spRulerUnit.setOnItemSelectedListener(new OnItemSelectedListener(){
				@Override
				public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id){
					applyRulerUnit();
				}
				@Override
				public void onNothingSelected(AdapterView<?> parentView){
					applyRulerUnit();
				}
			});
			SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
			String unit = sp.getString("ruler_unit", "cm");
			spRulerUnit.setSelection(getItemNumber(R.array.unit_names, unit));
		}
		
		showScreenSize();
	}
	
	private void diagonalUnitChanged(){
		String unitName = (String)spUnit.getSelectedItem();
		float scale = getInchScale(unitName);

		if (scale > 0)
		{
			diagonalScale = scale;
			showScreenSize();
		}
	}
	
	private void applyRulerUnit(){
		String unitName = (String)spRulerUnit.getSelectedItem();
		float scale = getInchScale(unitName);
		
		if (scale > 0)
			ruler.setScale(scale);
			
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
		SharedPreferences.Editor ed = sp.edit();
		ed.putString("ruler_unit", unitName);
		ed.commit();
	}
	
	private void showScreenSize(){
		double diag = screenSize.getExpectedDiagonal(this) / diagonalScale;
		DecimalFormat df = new DecimalFormat("0.0#");
		etDiagonal.setText(df.format(diag));
	}
	
	public void showRuler(View view){
		applyDiagonal();
		Intent i = new Intent(this, RulerActivity.class);
		startActivity(i);
	}
	
	public void setDiagonal(View view){
		applyDiagonal();
	}
	
	public void showSettings(View view) {
		goToPreferences();
	}
	
	private void goToPreferences(){
		startActivity(new Intent(this, PrefsActivity.class));
	}
	
	public void openPhotoMeasure(View view){
		//Toast.makeText(this, "make photo", Toast.LENGTH_LONG).show();
		startActivity(new Intent(this, PhotoMeasureActivity.class));	
	}
	
	public void applyDiagonal(){
		String strDiag = etDiagonal.getText().toString();
		double newD = Helpers.TryParse(strDiag, -1);
		if (newD <= 0) return;
		
		screenSize.setDiagonal(newD * diagonalScale, this);

		showScreenSize();
		
		if (ruler != null){
			ruler.invalidate();
		}
	}
	
	private float getInchScale(String unitName){
		int unitIndex = getItemNumber(R.array.unit_names, unitName);
		float scale = Helpers.TryParse(getResources().getStringArray(R.array.unit_inch)[unitIndex], -1); 
		return scale;
	}
	
	private int getItemNumber(int resId, String item){
		String[] names = this.getResources().getStringArray(resId);
		return Arrays.asList(names).indexOf(item);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu){
		menu.add(Menu.NONE, 0, 0, getResources().getString(R.string.menu_preferences));
		
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item){
		switch (item.getItemId())
		{
			case 0:
				goToPreferences();
				return true;
		}
		
		return false;
	}
}
