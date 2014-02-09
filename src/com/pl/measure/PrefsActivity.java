//// ,hvvvö9  vfa<c   bbbbbbbbbbbbbbbbbbbbbbbbbbbbbb44444444444444444444444444444444444444444444444444444>qffffffffffffffff                       q><<<<C<<<<<<<<<<<<<<ewwwwwwwwwwwwwwwff  
package com.pl.measure;
import android.app.*;
import android.os.*;
import android.preference.*;
import android.widget.*;

public class PrefsActivity extends PreferenceActivity
{
	private Activity a;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferences);
		
		EditTextPreference ldpi_pref = (EditTextPreference)getPreferenceScreen().findPreference("long_dpi");
		ldpi_pref.setOnPreferenceChangeListener(numberListener);
		ldpi_pref.setSummary(ldpi_pref.getText());

		EditTextPreference sdpi_pref = (EditTextPreference)getPreferenceScreen().findPreference("short_dpi");
		sdpi_pref.setOnPreferenceChangeListener(numberListener);
		sdpi_pref.setSummary(sdpi_pref.getText());
		
		a = this;
		ListPreference rulerunit_pref = (ListPreference)getPreferenceScreen().findPreference("ruler_unit");
		rulerunit_pref.setOnPreferenceChangeListener(rulerUnitListener);
		rulerunit_pref.setSummary(rulerunit_pref.getValue());
	}
	
	Preference.OnPreferenceChangeListener numberListener = new Preference.OnPreferenceChangeListener(){
		@Override
		public boolean onPreferenceChange(Preference preference, Object newValue) {
			boolean correct = isNumber(newValue);
			
			if (correct){
				preference.setSummary(newValue.toString());
			}
			
			return correct;
		}
	};
	
	Preference.OnPreferenceChangeListener rulerUnitListener = new Preference.OnPreferenceChangeListener(){
		@Override
		public boolean onPreferenceChange(Preference preference, Object newValue) {
			ListPreference pref = (ListPreference)preference;
			pref.setSummary(newValue.toString());
			
			return true;
		}
	};
	
	private boolean isNumber(Object obj){
		if(obj != null 
			&& !obj.toString().equals("")
			&& obj.toString().matches("\\f*") )
		{
			return true;
		}
		else {
			Toast.makeText(PrefsActivity.this, obj+" "+getResources().getString(R.string.is_an_invalid_number), Toast.LENGTH_SHORT).show();
			return false;
		}
	}
}
