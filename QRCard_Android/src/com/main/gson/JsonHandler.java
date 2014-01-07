package com.main.gson;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.main.contact.ContactInfo;

public class JsonHandler {
	static final String TAG = "JsonHandler";
	
	private static JsonHandler ref;
	private Gson gson;
	
	public static JsonHandler getInstance(){
		if (ref == null){
			synchronized (JsonHandler.class){
				if (ref == null){
					ref = new JsonHandler();
				}
			}
		}
		return ref;
	}
	
	private JsonHandler(){
		gson = new Gson();
	}
	
	public String toJson(Object value){
		String result = gson.toJson(value);
		return result;
	}
	
	public <T> T getObject(String jsonString, Class<T> tclass){  
        T t = null;  
        try {  
            t = gson.fromJson(jsonString, tclass);  
        } catch (Exception e) {  
            // TODO: handle exception  
        }  
        return t;  
    } 
	
	//TODO: cannot get correct object list
	public <T> List<T> getObjects(String jsonString, Class<T> tclass){  
        List<T> list = new ArrayList<T>();  
        try {  
            list = gson.fromJson(jsonString,   
                    new TypeToken<List<T>>(){}.getType());  
        } catch (Exception e) {  
            // TODO: handle exception  
        }  
        return list;  
    } 
	
	public ContactInfo getContact(String jsonString){  
		ContactInfo contact = null;  
        try {  
        	contact = gson.fromJson(jsonString, ContactInfo.class);  
        } catch (Exception e) {  
            // TODO: handle exception  
        }  
        return contact;  
    } 
	
	public List<ContactInfo> getContactList(String jsonString){  
        List<ContactInfo> contactList = new ArrayList<ContactInfo>();  
        try {  
        	contactList = gson.fromJson(jsonString,   
                    new TypeToken<List<ContactInfo>>(){}.getType());  
        } catch (Exception e) {  
            // TODO: handle exception  
        }  
        return contactList;  
    } 
}
