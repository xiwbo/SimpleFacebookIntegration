package com.bowwi;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;
import java.io.InputStream;

public class MyTask extends AsyncTask<String, Void, Bitmap> {
	private ImageView myImage;

	public MyTask(ImageView image) {
		myImage = image;
	}

	protected Bitmap doInBackground(String... url) {
		String urldisplay = url[0];
		Bitmap img = null;
		try {
			InputStream in = new java.net.URL(urldisplay).openStream();
			img = BitmapFactory.decodeStream(in);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return(img);
	}

	protected void onPostExecute(Bitmap result) {
		myImage.setImageBitmap(result);
	}
}
