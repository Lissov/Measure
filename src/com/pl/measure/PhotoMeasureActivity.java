package com.pl.measure;
import android.os.*;
import android.app.*;
import android.view.*;
import android.widget.*;
import android.content.pm.*;
import android.content.*;
import android.provider.*;

public class PhotoMeasureActivity extends Activity
{
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.photomeasure);
		
		Boolean hasCamera = getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA);
		Button btnMakePhoto = (Button)findViewById(R.id.btnMakePhoto);
		btnMakePhoto.setVisibility(hasCamera ? View.VISIBLE : View.INVISIBLE);
	}
	
	public void makePhoto(View view){
		makePhoto();
	}
	
	static final int REQUEST_PHOTO = 1;
	
	private void makePhoto(){
		Intent photoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		if (photoIntent.resolveActivity(getPackageManager()) != null){
			startActivityForResult(photoIntent, REQUEST_PHOTO);
		}
	}
}
