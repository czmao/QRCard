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
	}

	protected void tearDown() throws Exception {
		deleteContact();
		super.tearDown();
	}

	public void testAddContact() throws Exception { 
		addContact();
		String contactId = ContactManager.getInstance().getContactIdByPhone( mainActivity, testPhone);
		Assert.assertTrue(contactId.length()>0);
	}
	
	public void testGetContactIdByPhone() throws Exception { 
		addContact();
		String contactId = ContactManager.getInstance().getContactIdByPhone( mainActivity, testPhone);
		Assert.assertTrue(contactId.length()>0);

		deleteContact();
		contactId = ContactManager.getInstance().getContactIdByPhone( mainActivity, testPhone);
		Assert.assertTrue(contactId.length()==0);
	}
	
	private void deleteContact()
	{
		ContactManager.getInstance().deleteByPhone(mainActivity, testPhone);
	}
	
	private void addContact()
	{
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
		
		ContactManager.getInstance().addContact( mainActivity, contact);
	}
	
	private void listColumnNames()
	{
		Uri contactUri =ContactsContract.Contacts.CONTENT_URI;
		ContentResolver resolver = mainActivity.getContentResolver();
		Cursor cursor =resolver.query(contactUri, null,null, null,null);
		Logger.writeForTest("listColumnNames///////////////");
		int columnNumber = cursor.getColumnCount();
		for(int i = 0; i <columnNumber; i++) {
			String temp =cursor.getColumnName(i);
			Logger.writeForTest(i + "\t" + temp);
		}
		Logger.closeWriter();
		cursor.close();
	}
}
