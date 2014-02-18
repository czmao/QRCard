package com.main.contact;

import java.util.ArrayList;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds;
import android.util.Log;

public class ContactManager {
	private static ContactManager instance = null; 
	
	public static ContactManager getInstance() {    
		if (instance == null) {    
		    instance = new ContactManager();  
		}    
		return instance;    
	} 
	
	public String addContact(Context context, ContactInfo contact){
		String conflictName = "";
		int rawContactId = existContactWithPhone(context, contact.getPhoneList());
		if(rawContactId>0){
			conflictName = getNameByRawContactId(context,rawContactId);
			updateContact(context,rawContactId,contact);
		}else{
			addNewContact(context, contact);
		}
		return conflictName;
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
	
	public ContactInfo getContactInfo(Context context,Uri contactData) {  
		ContactInfo contact = new ContactInfo();
		
        Cursor cursor= context.getContentResolver().query(contactData, null, null, null, null);  
        if (cursor.moveToFirst()) {  
        	int idColumn = cursor.getColumnIndex(ContactsContract.Contacts._ID); 
            int displayNameColumn = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);  
  
            do {  
                // 获得联系人的ID号  
                String contactId = cursor.getString(idColumn);  
                // 获得联系人姓名  
                String disPlayName = cursor.getString(displayNameColumn); 
        		ContactName name = new ContactName();
        		name.setType(CommonDataKinds.StructuredName.GIVEN_NAME);
        		name.setValue(disPlayName);
        		contact.setName(name);
                Log.d("username", disPlayName); 
 
                // 查看该联系人有多少个电话号码。 
                int phoneCount = cursor.getInt(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));   
                if (phoneCount > 0) {  
            		ArrayList<ContactPhone> phoneList = new ArrayList<ContactPhone>();
                    // 获得联系人的电话号码  
                    Cursor phones = context.getContentResolver().query(  
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,  
                            null,  
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID+ " = " + contactId, 
                            null, 
                            null);  
                    if (phones.moveToFirst()) {  
                        do {  
                            // 遍历所有的电话号码  
                            int phoneType = phones.getInt(
                    				phones.getColumnIndex(
                    				ContactsContract.CommonDataKinds.Phone.TYPE)); 
                            String phoneNumber = phones.getString(
                            				phones.getColumnIndex(
                            				ContactsContract.CommonDataKinds.Phone.NUMBER)); 
                    		ContactPhone phone = new ContactPhone();
                    		phone.setType(phoneType);
                    		phone.setValue(phoneNumber);
                    		phoneList.add(phone);
                    		contact.setPhoneList(phoneList);
                            Log.i("phoneType", String.valueOf(phoneType));
                            Log.i("phoneNumber", phoneNumber); 
                        } while (phones.moveToNext());  
                    }  
                }  
  
                // 获取该联系人邮箱  
                Cursor emails = context.getContentResolver().query(  
                        ContactsContract.CommonDataKinds.Email.CONTENT_URI,  
                        null,  
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId,
                        null, 
                        null);  
                if (emails.moveToFirst()) {  
                	ArrayList<ContactEmail> emailList = new ArrayList<ContactEmail>();
                    do {  
                        // 遍历所有的电话号码  
                        int emailType = emails.getInt(
                        				emails.getColumnIndex(
                        				ContactsContract.CommonDataKinds.Email.TYPE));  
                        String emailValue = emails.getString(
                        				emails.getColumnIndex(
                        				ContactsContract.CommonDataKinds.Email.DATA));  
                		ContactEmail email = new ContactEmail();
                		email.setType(emailType);
                		email.setValue(emailValue);
                		emailList.add(email);
                		contact.setEmailList(emailList);
                        Log.i("emailType", String.valueOf(emailType));  
                        Log.i("emailValue", emailValue); 
                    } while (emails.moveToNext());  
                }  
  
//                // 获取该联系人IM  
//                Cursor IMs = context.getContentResolver().query(  
//                        Data.CONTENT_URI,  
//                        new String[] { Data._ID, Im.PROTOCOL, Im.DATA },  
//                        Data.CONTACT_ID + "=?" + " AND " + Data.MIMETYPE + "='"  
//                                + Im.CONTENT_ITEM_TYPE + "'",  
//                        new String[] { contactId }, null);  
//                if (IMs.moveToFirst()) {  
//                    do {  
//                        String protocol = IMs.getString(IMs  
//                                .getColumnIndex(Im.PROTOCOL));  
//                        String date = IMs  
//                                .getString(IMs.getColumnIndex(Im.DATA));  
//                        Log.i("protocol", protocol);  
//                        Log.i("date", date);  
//                    } while (IMs.moveToNext());  
//                }  
//  
//                // 获取该联系人地址  
//                Cursor address = getContentResolver()  
//                        .query(  
//                                ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_URI,  
//                                null,  
//                                ContactsContract.CommonDataKinds.Phone.CONTACT_ID  
//                                        + " = " + contactId, null, null);  
//                if (address.moveToFirst()) {  
//                    do {  
//                        // 遍历所有的地址  
//                        String street = address  
//                                .getString(address  
//                                        .getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.STREET));  
//                        String city = address  
//                                .getString(address  
//                                        .getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.CITY));  
//                        String region = address  
//                                .getString(address  
//                                        .getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.REGION));  
//                        String postCode = address  
//                                .getString(address  
//                                        .getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.POSTCODE));  
//                        String formatAddress = address  
//                                .getString(address  
//                                        .getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.FORMATTED_ADDRESS));  
//                        Log.i("street", street);  
//                        Log.i("city", city);  
//                        Log.i("region", region);  
//                        Log.i("postCode", postCode);  
//                        Log.i("formatAddress", formatAddress);  
//                    } while (address.moveToNext());  
//                }  
//  
//                // 获取该联系人组织  
//                Cursor organizations = context.getContentResolver().query(  
//                        Data.CONTENT_URI,  
//                        new String[] { Data._ID, Organization.COMPANY,  
//                                Organization.TITLE },  
//                        Data.CONTACT_ID + "=?" + " AND " + Data.MIMETYPE + "='"  
//                                + Organization.CONTENT_ITEM_TYPE + "'",  
//                        new String[] { contactId }, null);  
//                if (organizations.moveToFirst()) {  
//                    do {  
//                        String company = organizations.getString(organizations  
//                                .getColumnIndex(Organization.COMPANY));  
//                        String title = organizations.getString(organizations  
//                                .getColumnIndex(Organization.TITLE));  
//                        Log.i("company", company);  
//                        Log.i("title", title);  
//                    } while (organizations.moveToNext());  
//                }  
//  
//                // 获取备注信息  
//                Cursor notes = context.getContentResolver().query(  
//                        Data.CONTENT_URI,  
//                        new String[] { Data._ID, Note.NOTE },  
//                        Data.CONTACT_ID + "=?" + " AND " + Data.MIMETYPE + "='"  
//                                + Note.CONTENT_ITEM_TYPE + "'",  
//                        new String[] { contactId }, null);  
//                if (notes.moveToFirst()) {  
//                    do {  
//                        String noteinfo = notes.getString(notes  
//                                .getColumnIndex(Note.NOTE));  
//                        Log.i("noteinfo", noteinfo);  
//                    } while (notes.moveToNext());  
//                }  
//  
//                // 获取nickname信息  
//                Cursor nicknames = context.getContentResolver().query(  
//                        Data.CONTENT_URI,  
//                        new String[] { Data._ID, Nickname.NAME },  
//                        Data.CONTACT_ID + "=?" + " AND " + Data.MIMETYPE + "='"  
//                                + Nickname.CONTENT_ITEM_TYPE + "'",  
//                        new String[] { contactId }, null);  
//                if (nicknames.moveToFirst()) {  
//                    do {  
//                        String nickname_ = nicknames.getString(nicknames  
//                                .getColumnIndex(Nickname.NAME));  
//                        Log.i("nickname_", nickname_);  
//                    } while (nicknames.moveToNext());  
//                }  
  
            } while (cursor.moveToNext());  
        }   
        return contact;
    }
	
	public String getNameByRawContactId(Context context,int rawContactId) {  
		String name = "";
        String[] projection = { ContactsContract.Data.RAW_CONTACT_ID ,  
        						ContactsContract.Contacts.DISPLAY_NAME};  

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
        		int contactIdFieldColumnIndex = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);     
        		name = cursor.getString(contactIdFieldColumnIndex);  
        	}  
        }
        
        return name;
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
