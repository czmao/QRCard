package com.main.qrcard.test;

import java.util.ArrayList;

import android.content.Intent;
import android.provider.ContactsContract.CommonDataKinds;
import android.test.InstrumentationTestCase;

import com.main.contact.ContactEmail;
import com.main.contact.ContactInfo;
import com.main.contact.ContactManager;
import com.main.contact.ContactName;
import com.main.contact.ContactPhone;
import com.main.gson.JsonHandler;
import com.main.log.Logger;
import com.main.qrcard.QRCardActivity;

import junit.framework.Assert;
import junit.framework.TestCase;

public class JsonTest extends InstrumentationTestCase {
	private static final String TAG = "JsonTest";
	private String testName = "name";
	private String testPhone = "phone";
	private String testEmail = "email";
	private QRCardActivity mainActivity = null;
	private JsonHandler jsonhandler;

	protected void setUp() throws Exception {
		super.setUp();
		Intent intent = new Intent();
		intent.setClassName("com.main.qrcard", QRCardActivity.class.getName());
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		mainActivity = (QRCardActivity) getInstrumentation().startActivitySync(intent);
		jsonhandler = JsonHandler.getInstance(mainActivity);
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	public void testToJson() throws Exception { 
		String actual = jsonhandler.toJson(initialContact());
		String expected = "{\"emailList\":[{\"value\":\"email\",\"type\":2}],\"name\":{\"type\":\"data2\",\"value\":\"name\"},\"phoneList\":[{\"value\":\"phone\",\"type\":3}]}";
		Assert.assertEquals(expected, actual);
	}
	
	private ContactInfo initialContact(){
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
}
