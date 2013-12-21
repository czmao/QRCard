package com.main.log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.os.Environment;

public class Logger {
	private static String DRI_NAME = Environment.getExternalStorageDirectory() + "/QRCard";
	private static String FILE_FOR_TEST_NAME = DRI_NAME + "/QRCardTest.txt";
	private static String FILE_NAME = DRI_NAME + "/QRCard.log";
	private static int MAX_FILE_SIZE = 1024*100;
	private static DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	private static Logger instance;
	private FileOutputStream fout;
	private static FileWriter writer;


	private Logger() {
		boolean ret = false;
		File destDir = new File(DRI_NAME);
		if (!destDir.exists()) {
			ret = destDir.mkdirs();
		}
		
		File file = new File(FILE_NAME);	
		if (file.exists()&&file.length()>MAX_FILE_SIZE) {
			file.delete();
		}	
		if(!file.exists()){
			try {
				file.createNewFile();
			} catch (IOException e) {
				return;
			}
		}

		file = new File(FILE_FOR_TEST_NAME);
		long length = file.length();
		if (file.exists()&&file.length()>0) {
			file.delete();
		}	
		if(!file.exists()){
			try {
				file.createNewFile();
			} catch (IOException e) {
				return;
			}
		}
	}

	private void closeFout() {
		try {
			fout.close();
		} catch (Exception e) {
		}
	}

	public static synchronized Logger getInstance() {
		if (instance == null) {
			instance = new Logger();
		}
		return instance;
	}

	public static synchronized void close() {
		if (instance != null) {
			instance.closeFout();
			instance = null;
		}
	}

	public static synchronized void writeForTest(String msg) {
		Date now = Calendar.getInstance().getTime();
		getInstance().writeToLog(FILE_FOR_TEST_NAME, String.format("%s--%s\r\n", df.format(now),msg));
	}
	
	
	public static synchronized void write(String msg) {
		Date now = Calendar.getInstance().getTime();
		getInstance().writeToLog(FILE_NAME, String.format("%s--%s\r\n", df.format(now),msg));
	}
	
	private void writeToLog(String fileName, String msg) {		
		try {
			if(writer==null){
				writer = new FileWriter(fileName,true);
			}		
			if(writer!=null)
				writer.write(msg);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static synchronized void closeWriter(){
		if(writer!=null){
			try {
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			writer = null;
		}
	}
}
