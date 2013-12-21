package com.main.contact;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.CommonDataKinds.SipAddress;
import android.provider.ContactsContract.CommonDataKinds;
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
	
	public void addContact(Context context, ContactInfo contact){
		//首先插入空值，再得到rawContactsId ，用于下面插值 
		ContentValues values = new ContentValues (); 
		Uri rawContactUri = context.getContentResolver().insert(ContactsContract.RawContacts.CONTENT_URI,values); 
		long rawContactId = ContentUris.parseId(rawContactUri); 
		insertName(context,values,rawContactId, contact.getName());
		for(ContactPhone phone: contact.getPhoneList()){
			insertPhone(context,values,rawContactId, phone);
		}
		for(ContactEmail email: contact.getEmailList()){
			insertEmail(context,values,rawContactId, email);
		}
//		for(ContactAddress address: contact.getAddressList()){
//			insertAddress(context,values,rawContactId, address);
//		}
	}
	
	private void insertName(Context context, ContentValues values, long rawContactId, ContactName name){
		//往空记录中插入姓名 
		values.clear(); 
		values.put(ContactsContract.Data.RAW_CONTACT_ID,rawContactId); 
		//设置内容类型 
		values.put(ContactsContract.Data.MIMETYPE,CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE); 
		//设置联系人姓名
		values.put(name.getType(),name.getValue()); 
		//向联系人URI添加联系人姓名
		context.getContentResolver().insert(ContactsContract.Data.CONTENT_URI,values); 
	}
	
	private void insertPhone(Context context, ContentValues values, long rawContactId, ContactPhone phone){
		//插入电话 
		values.clear(); 
		values.put(ContactsContract.Data.RAW_CONTACT_ID,rawContactId); 
		values.put(ContactsContract.Data.MIMETYPE,CommonDataKinds.Phone.CONTENT_ITEM_TYPE); 
		//设置联系人电话号码 
		values.put(CommonDataKinds.Phone.NUMBER, phone.getValue()); 
		//设置电话类型
		values.put(CommonDataKinds.Phone.TYPE, phone.getType());
		context.getContentResolver().insert(ContactsContract.Data.CONTENT_URI,values);
	}
	
	private void insertEmail(Context context, ContentValues values, long rawContactId, ContactEmail email){
		values.clear();     
	    values.put(ContactsContract.Data.RAW_CONTACT_ID, rawContactId);       
	    values.put(ContactsContract.Data.MIMETYPE, CommonDataKinds.Email.CONTENT_ITEM_TYPE);     
	    values.put(CommonDataKinds.Email.DATA, email.getValue());     
	    values.put(CommonDataKinds.Email.TYPE, email.getType());     
	    context.getContentResolver().insert(ContactsContract.Data.CONTENT_URI, values);
	}
	
//	private void insertAddress(Context context, ContentValues values, long rawContactId, ContactAddress address){
//		values.clear();     
//	    values.put(ContactsContract.Data.RAW_CONTACT_ID, rawContactId);       
//	    values.put(ContactsContract.Data.MIMETYPE, CommonDataKinds.SipAddress.CONTENT_ITEM_TYPE);     
//	    values.put(CommonDataKinds.SipAddress.DATA, address.getValue());     
//	    values.put(CommonDataKinds.SipAddress.TYPE, address.getType());     
//	    context.getContentResolver().insert(ContactsContract.Data.CONTENT_URI, values);
//	}
	
	public String getContactIdByPhone(Context context,String phone) {  
		String contactId = "";
        String[] projection = { ContactsContract.RawContacts.CONTACT_ID ,  
                                ContactsContract.CommonDataKinds.Phone.NUMBER};  

        Cursor cursor = context.getContentResolver().query(  
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,  
                projection,    // Which columns to return.  
                ContactsContract.CommonDataKinds.Phone.NUMBER + " = '" + phone + "'", // WHERE clause.  
                null,          // WHERE clause value substitution  
                null);   // Sort order.  
  
        if( cursor != null ) {  
        	for( int i = 0; i < cursor.getCount(); i++ )  
        	{  
        		cursor.moveToPosition(i);    
        		int contactIdFieldColumnIndex = cursor.getColumnIndex(ContactsContract.RawContacts.CONTACT_ID);     
        		contactId = cursor.getString(contactIdFieldColumnIndex);  
        	}  
        }
        
        return contactId;
    } 
	
	//RawContacts.CONTENT_URI = Uri.parse("content://com.android.contacts/raw_contacts"); 
	//ContactsContract.Data = Uri.parse("content://com.android.contacts/data")
	public void deleteByPhone(Context context,String phone){ 
	    String contactId = getContactIdByPhone(context,phone);
	    if(contactId.length()>0){
	    	try{
	    		context.getContentResolver().delete(ContactsContract.Data.CONTENT_URI, "contact_id=?", new String[]{contactId});
	    		context.getContentResolver().delete(ContactsContract.RawContacts.CONTENT_URI, "contact_id=?", new String[]{contactId});
	    	}catch(Exception e){
	    		
	    	}
	    }
	} 
}
