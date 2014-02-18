package com.main.qrImage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.graphics.Bitmap;
import android.os.Environment;

public class ImageManager {
	public final static String TAG = "ImageManager";
	private static String DRI_NAME = Environment.getExternalStorageDirectory() + "/QRCard/";
	private static String FORMAT = "png";
	private static ImageManager instance = null; 
	
	public static ImageManager getInstance() {    
		if (instance == null) {    
		    instance = new ImageManager();  
		}    
		return instance;    
	} 
	
	private ImageManager() {
		boolean ret = false;
		File destDir = new File(DRI_NAME);
		if (!destDir.exists()) {
			ret = destDir.mkdirs();
		}
	}
	
	public void saveImage(String name, Bitmap qrCodeBitmap){
		File file = new File(DRI_NAME + name + "." + FORMAT);
		if (file.exists()) {
			file.delete();
		}
		try {
			file.createNewFile();
		} catch (IOException e) {
			return;
		}	
		try {   
			FileOutputStream out = new FileOutputStream(file);
			qrCodeBitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
			out.flush();
			out.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
