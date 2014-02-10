package com.pl.measure;
import android.os.*;
import android.app.*;
import android.view.*;
import android.widget.*;
import android.content.pm.*;
import android.content.*;
import android.provider.*;
import android.graphics.*;
import java.io.*;
import java.text.*;
import java.util.*;
import android.net.*;

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
	private static String photoFileName;
	
	private void makePhoto(){
		Intent photoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		if (photoIntent.resolveActivity(getPackageManager()) != null){
			File photoFile = null;
			try{
				photoFile = createPhotoFile();
			} catch (IOException exc){
				Toast.makeText(this, exc.getMessage(), Toast.LENGTH_LONG).show();
			}
			if (photoFile != null){
				photoFileName = photoFile.getAbsolutePath();
				photoIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
				startActivityForResult(photoIntent, REQUEST_PHOTO);	
			}
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		if (requestCode == REQUEST_PHOTO && resultCode == RESULT_OK){
			ImageView image = (ImageView)findViewById(R.id.ivPhoto);
			Bundle extras = data.getExtras();
			Bitmap imageBitmap = (Bitmap)extras.get("data");
			image.setImageBitmap(imageBitmap);
		}
	}
	
	private void setPic() {
		ImageView image = (ImageView)findViewById(R.id.ivPhoto);
		
		// Get the dimensions of the View
		int targetW = image.getWidth();
		int targetH = image.getHeight();

		// Get the dimensions of the bitmap
		BitmapFactory.Options bmOptions = new BitmapFactory.Options();
		bmOptions.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(photoFileName, bmOptions);
		int photoW = bmOptions.outWidth;
		int photoH = bmOptions.outHeight;

		// Determine how much to scale down the image
		int scaleFactor = Math.min(photoW/targetW, photoH/targetH);

		// Decode the image file into a Bitmap sized to fill the View
		bmOptions.inJustDecodeBounds = false;
		bmOptions.inSampleSize = scaleFactor;
		bmOptions.inPurgeable = true;

		Bitmap bitmap = BitmapFactory.decodeFile(photoFileName, bmOptions);
		image.setImageBitmap(bitmap);
	}
	
	private File createPhotoFile() throws IOException
	{
		String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
		String filename = "jpg_" + timestamp;
		File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
		File image= File.createTempFile(filename, ".jpg", storageDir);
		
		return image;
	}
}
