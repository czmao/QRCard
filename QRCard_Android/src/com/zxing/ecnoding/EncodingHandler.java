package com.zxing.ecnoding;

import java.util.Hashtable;
import java.util.Map;

import android.graphics.Bitmap;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

public final class EncodingHandler {
	private static final int BLACK = 0xff000000;
	private static final int WHITE = 0xffffffff;
	
	public static Bitmap createQRCode(String str,int widthAndHeight) throws WriterException {
		Bitmap bitmap = null;
		Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>();  
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8"); 
        BitMatrix matrix = null;
        try{
			matrix = new MultiFormatWriter().encode(str, BarcodeFormat.QR_CODE, widthAndHeight, widthAndHeight, hints);
        } catch (WriterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
        if(matrix!=null){
			int width = matrix.getWidth();
			int height = matrix.getHeight();
			int[] pixels = new int[width * height];
			
			for (int y = 0; y < height; y++) {
				for (int x = 0; x < width; x++) {
					pixels[y * width + x] = WHITE;
				}
			}
			
			for (int y = 0; y < height; y++) {
				for (int x = 0; x < width; x++) {
					if (matrix.get(x, y)) {
						pixels[y * width + x] = BLACK;
					}
				}
			}
			bitmap = Bitmap.createBitmap(width, height,
					Bitmap.Config.ARGB_8888);
			bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        }
		return bitmap;
	}
}
