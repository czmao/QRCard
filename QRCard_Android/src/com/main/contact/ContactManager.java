package com.main.contact;

import java.util.ArrayList;

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
		int rawContactId = existContactWithPhone(context, contact.getPhoneList());
		if(rawContactId>0){
			updateContact(context,rawContactId,contact);
		}else{
			addNewContact(context, contact);
		}

	}
	
	public void updateContact(Context context,int rawContactId, ContactInfo contact){
		updatePhone(context,rawContactId, contact);
		updateEmail(context,rawContactId, contact);
	}
	
	private void updateEmail(Context context, int rawContactId, ContactInfo contact){
		ContentValues values = new ContentValues (); 
		ArrayList<String> emailList = getEmailListByRawContactId(context,rawContactId);
		if(rawContactId>0){
			for(ContactEmail email: contact.getEmailList()){
				if(!emailList.contains(email.getValue())){
					insertEmail(context,values, rawContactId, email);
				}
			}
		}
	}
	
	private void updatePhone(Context context, int rawContactId, ContactInfo contact){
		ContentValues values = new ContentValues (); 
		ArrayList<String> phoneList = getPhoneListByRawContactId(context,rawContactId);
		if(rawContactId>0){
			for(ContactPhone phone: contact.getPhoneList()){
				if(!phoneList.contains(phone.getValue())){
					insertPhone(context,values, rawContactId, phone);
				}
			}
		}
	}
	
	private void addNewContact(Context context, ContactInfo contact){
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
	
	public int existContactWithPhone(Context context,ArrayList<ContactPhone> phoneList){
		int rawContactId = -1;
		for(ContactPhone phone:phoneList){
			rawContactId = getRawContactIdByPhone(context, phone.getValue());
			if(rawContactId>0){
				break;
			}
		}
		return rawContactId;
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
	
	public int getRawContactIdByPhone(Context context,String phone) {  
		int rawContactId = -1;
        String[] projection = { ContactsContract.Data.RAW_CONTACT_ID,
        						ContactsContract.RawContacts.CONTACT_ID ,  
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
        		int contactIdFieldColumnIndex = cursor.getColumnIndex(ContactsContract.Data.RAW_CONTACT_ID);     
        		rawContactId = cursor.getInt(contactIdFieldColumnIndex);  
        	}  
        }
        
        return rawContactId;
    } 
	
	public String getContactIdByPhone(Context context,String phone) {  
		String contactId = "";
        String[] projection = { ContactsContract.Data.RAW_CONTACT_ID,
        						ContactsContract.RawContacts.CONTACT_ID ,  
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
	
	public ArrayList<String> getPhoneListByRawContactId(Context context,int rawContactId) {  
		ArrayList<String> phoneList = new ArrayList<String>();
        String[] projection = { ContactsContract.Data.RAW_CONTACT_ID ,  
                                ContactsContract.CommonDataKinds.Phone.NUMBER};  

        Cursor cursor = context.getContentResolver().query(  
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,  
                projection,    // Which columns to return.  
                ContactsContract.Data.RAW_CONTACT_ID + " = '" + rawContactId + "'", // WHERE clause.  
                null,          // WHERE clause value substitution  
                null);   // Sort order.  
  
        if( cursor != null ) {  
        	for( int i = 0; i < cursor.getCount(); i++ )  
        	{  
        		cursor.moveToPosition(i);    
        		int contactIdFieldColumnIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);     
        		phoneList.add(cursor.getString(contactIdFieldColumnIndex));  
        	}  
        }
        
        return phoneList;
    } 
	
	public ArrayList<String> getEmailListByRawContactId(Context context,int rawContactId) {  
		ArrayList<String> emailList = new ArrayList<String>();
        String[] projection = { ContactsContract.Data.RAW_CONTACT_ID ,  
                                ContactsContract.CommonDataKinds.Email.ADDRESS};  

        Cursor cursor = context.getContentResolver().query(  
                ContactsContract.CommonDataKinds.Email.CONTENT_URI,  
                projection,    // Which columns to return.  
                ContactsContract.Data.RAW_CONTACT_ID + " = '" + rawContactId + "'", // WHERE clause.  
                null,          // WHERE clause value substitution  
                null);   // Sort order.  
  
        if( cursor != null ) {  
        	for( int i = 0; i < cursor.getCount(); i++ )  
        	{  
        		cursor.moveToPosition(i);    
        		int contactIdFieldColumnIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.ADDRESS);     
        		emailList.add(cursor.getString(contactIdFieldColumnIndex));  
        	}  
        }
        
        return emailList;
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
	
	public void deleteByContactID(Context context,String contactId){ 
	    if(contactId.length()>0){
	    	try{
	    		context.getContentResolver().delete(ContactsContract.Data.CONTENT_URI, "contact_id=?", new String[]{contactId});
	    		context.getContentResolver().delete(ContactsContract.RawContacts.CONTENT_URI, "contact_id=?", new String[]{contactId});
	    	}catch(Exception e){
	    		
	    	}
	    }
	} 
}
