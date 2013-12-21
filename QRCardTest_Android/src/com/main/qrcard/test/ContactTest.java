package com.main.qrcard.test;

import java.util.ArrayList;

import junit.framework.Assert;

import com.main.contact.ContactAddress;
import com.main.contact.ContactEmail;
import com.main.contact.ContactInfo;
import com.main.contact.ContactManager;
import com.main.contact.ContactName;
import com.main.contact.ContactPhone;
import com.main.log.Logger;
import com.main.qrcard.MainActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds;
import android.provider.ContactsContract.RawContacts;
import android.test.InstrumentationTestCase;

public class ContactTest extends InstrumentationTestCase {
	private static final String TAG = "ContactTest";
	private MainActivity mainActivity = null;
	private String testName = "name";
	private String testPhone = "phone";
	private String testEmail = "email";

	/* (non-Javadoc)
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
		Intent intent = new Intent();
		intent.setClassName("com.main.qrcard", MainActivity.class.getName());
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		mainActivity = (MainActivity) getInstrumentation().startActivitySync(intent);
		deleteContact();
	}

	protected void tearDown() throws Exception {
		//deleteContact();
		super.tearDown();
	}

	public void testAddContact() throws Exception { 
		addContact();
		addContact();
		String contactId = ContactManager.getInstance().getContactIdByPhone( mainActivity, testPhone);
		Assert.assertTrue(contactId.length()>0);
	}
	
	public void testUpdateContact() throws Exception { 
		addContact();
		updateContact();
		int rawContactId = ContactManager.getInstance().getRawContactIdByPhone( mainActivity, testPhone);
		ArrayList<String> phoneList = ContactManager.getInstance().getPhoneListByRawContactId( mainActivity, rawContactId);
		Assert.assertTrue(phoneList.size()== 2);
		ArrayList<String> emailList = ContactManager.getInstance().getEmailListByRawContactId( mainActivity, rawContactId);
		Assert.assertTrue(emailList.size()== 2);
	}
	
	public void testGetContactIdByPhone() throws Exception { 
		addContact();
		String contactId = ContactManager.getInstance().getContactIdByPhone( mainActivity, testPhone);
		Assert.assertTrue(contactId.length()>0);

		deleteContact();
		contactId = ContactManager.getInstance().getContactIdByPhone( mainActivity, testPhone);
		Assert.assertTrue(contactId.length()==0);
	}
	
	//RawContacts.CONTENT_URI = Uri.parse("content://com.android.contacts/raw_contacts"); 
	//ContactsContract.Data = Uri.parse("content://com.android.contacts/data")
	public void testColumns() throws Exception { 
		//listColumnNames(ContactsContract.RawContacts.CONTENT_URI);
		//listColumnNames(ContactsContract.Data.CONTENT_URI);
	}
	
	private void deleteContact()
	{
		ContactManager.getInstance().deleteByPhone(mainActivity, testPhone);
	}
	
	private void updateContact()
	{
		int rawContactId = ContactManager.getInstance().getRawContactIdByPhone( mainActivity, testPhone);
		ContactInfo contact = initialContact();
		contact.getPhoneList().get(0).setValue("test");
		contact.getEmailList().get(0).setValue("test");
		ContactManager.getInstance().updateContact( mainActivity, rawContactId, contact);
	}
	
	private void addContact()
	{
		ContactInfo contact = initialContact();
		ContactManager.getInstance().addContact( mainActivity, contact);
	}
	
	ContactInfo initialContact(){
		ContactInfo contact = new ContactInfo();
		ContactName name = new ContactName();
		name.setType(CommonDataKinds.StructuredName.GIVEN_NAME);
		name.setValue(testName);
		contact.setName(name);
		
		ArrayList<ContactPhone> phoneList = new ArrayList<ContactPhone>();
		ContactPhone phone = new ContactPhone();
		phone.setType(CommonDataKinds.Phone.TYPE_WORK);
		phone.setValue(testPhone);
		phoneList.add(phone);
		contact.setPhoneList(phoneList);
		
		ArrayList<ContactEmail> emailList = new ArrayList<ContactEmail>();
		ContactEmail email = new ContactEmail();
		email.setType(CommonDataKinds.Email.TYPE_WORK);
		email.setValue(testEmail);
		emailList.add(email);
		contact.setEmailList(emailList);
		return contact;
	}
	
	private void listColumnNames(Uri url)
	{
		ContentResolver resolver = mainActivity.getContentResolver();
		Cursor cursor =resolver.query(url, null,null, null,null);
		Logger.writeForTest(url.toString());
		int columnNumber = cursor.getColumnCount();
		for(int i = 0; i <columnNumber; i++) {
			String temp =cursor.getColumnName(i);
			Logger.writeForTest(i + "\t" + temp);
		}
		Logger.closeWriter();
		cursor.close();
	}
}
