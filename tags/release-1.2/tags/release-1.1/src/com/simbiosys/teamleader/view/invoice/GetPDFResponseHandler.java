package com.simbiosys.teamleader.view.invoice;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;

import com.simbiosys.teamleader.Globals;
import com.simbiosys.teamleader.view.ErrorActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Environment;

public class GetPDFResponseHandler implements ResponseHandler<File> {

	public static final int EOF = -1;
	Activity activity;
	String filename;

	public GetPDFResponseHandler(Activity activity, String filename) {
		this.activity = activity;
		if (filename == null) {
			this.filename ="tempFilename.pdf";  
		} else this.filename = filename;
	}

	/* Checks if external storage is available for read and write */
	public boolean isExternalStorageWritable() {
		String state = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(state)) {
			return true;
		}
		return false;
	}

	@Override
	public File handleResponse(HttpResponse response)
			throws ClientProtocolException, IOException {
		HttpEntity httpEntity = response.getEntity();
		InputStream inputStream = httpEntity.getContent();

		//		LineNumberReader lnr = new LineNumberReader(new InputStreamReader(inputStream));
		//		String currentString;
		//		while((currentString = lnr.readLine()) != null) {
		//			Log.v("TeamLeader", "httpResponse:" + currentString);
		//			// parse the response
		//			Log.v("TeamLeader", currentString);
		//		}

		DataInputStream dataInputStream = new DataInputStream(new BufferedInputStream(inputStream));
		int data;
		//		fileWriter = activity.openFileOutput(filename, activity.MODE_PRIVATE);
		if (isExternalStorageWritable()) {
			File file = new File(
					Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
					filename);
			FileOutputStream fileWriter = new FileOutputStream(file);
			while( (data = dataInputStream.read()) != EOF ) {
				fileWriter.write(data);
				//			Log.v("TeamLeader", Integer.valueOf(data).toString());
			}
			//		Log.v("TeamLeader", "Last char: " + Integer.valueOf(data));
			fileWriter.close();
			activity.finish();
			return file;
		} else {
			Intent i = new Intent(activity, ErrorActivity.class);
			Globals.error = "External Storage not writeable";
			activity.startActivity(i);
			return(null);
		}


	}

}
