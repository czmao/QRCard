package com.main.contact;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.provider.ContactsContract.Data;
import android.provider.ContactsContract.RawContacts;
import android.provider.ContactsContract.CommonDataKinds.StructuredName;

public class ContactManager {
	private static ContactManager instance = null; 
	
	public static ContactManager getInstance() {    
		if (instance == null) {    
		    instance = new ContactManager();  
		}    
		return instance;    
	} 
	
	public void addContact(ContactInfo contact, Context context){
		//首先插入空值，再得到rawContactsId ，用于下面插值 
		ContentValues values = new ContentValues (); 
		Uri rawContactUri = context.getContentResolver().insert(RawContacts.CONTENT_URI,values); 
		long rawContactsId = ContentUris.parseId(rawContactUri); 

		//往刚才的空记录中插入姓名 
		values.clear(); 
		values.put(Data.RAW_CONTACT_ID,rawContactsId); 
		//设置内容类型 
		values.put(Data.MIMETYPE,StructuredName.CONTENT_ITEM_TYPE); 
		//设置联系人姓名
		values.put(StructuredName.GIVEN_NAME,"Demo"); 
		//向联系人URI添加联系人姓名
		context.getContentResolver().insert(Data.CONTENT_URI,values); 

//		//插入电话 
//		values.clear(); 
//		values.put(Data.RAW_CONTACT_ID,rawContactsId); 
//		values.put(Data.MIMETYPE,Phone.CONTENT_ITEM_TYPE); 
//		//设置联系人电话号码 
//		values.put(Phone.NUMBER,"123567"); 
//		//设置电话类型
//		values.put(Phone.TYPE, Phone.TYPE_MOBILE); 
//		context.getContentResolver().insert(Data.CONTENT_URI,values);
	}
}
