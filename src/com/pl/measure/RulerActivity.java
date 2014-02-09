package com.pl.measure;
import android.app.*;
import android.os.*;
import android.annotation.*;

public class RulerActivity extends Activity
{
	@SuppressLint("NewApi")
	@Override
    public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		RulerView view = new RulerView(this);
		setContentView(view);
	}
}
