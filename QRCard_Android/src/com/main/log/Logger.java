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
	private static String filename = Environment.getExternalStorageDirectory() + "/QRCard/QRCard.log";

	private FileOutputStream fout;

	private static Logger instance;
	
	private static FileWriter writer;


	private Logger() {
		File file = new File(filename);
		if (file.exists()) {
			file.delete();
		}
		try {
			file.createNewFile();
		} catch (IOException e) {
			return;
		}
		try {
			fout = new FileOutputStream(file);
		} catch (FileNotFoundException e) {
			return;
		}
	}

	private void writeToFile(String msg) {
		try {
			fout.write(msg.getBytes());
			fout.write('\r');
			fout.write('\n');
			fout.write('\n');
			fout.flush();
		} catch (Exception e) {
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

	public static synchronized void write(String msg) {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		Date now = Calendar.getInstance().getTime();
		getInstance().writeToFile(String.format("%s--%s", df.format(now),msg));
	}
	
	
	public static synchronized void openWriter(String filepath, boolean overwrite){
		String filenameLog=null;
		if(filepath.startsWith("/"))
			filenameLog = Environment.getExternalStorageDirectory()+filepath;
		else
			filenameLog = Environment.getExternalStorageDirectory()+"/"+filepath;
	
		try {
			if(writer!=null){
				writer.close();
			}
			writer = new FileWriter(filenameLog,!overwrite);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static synchronized void writeToLog(String msg) {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		Date now = Calendar.getInstance().getTime();

		try {
			if(writer!=null)
				writer.write(String.format("%s--%s\r\n", df.format(now),msg));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static synchronized void closeWriter(){
		if(writer!=null){
			try {
				writer.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			writer = null;
		}
	}
}
