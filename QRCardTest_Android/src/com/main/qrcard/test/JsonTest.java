package com.main.qrcard.test;

import java.util.ArrayList;
import java.util.List;

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
		
		List<ContactInfo> contactList = new ArrayList<ContactInfo>();
		contactList.add(initialContact());
		contactList.add(initialContact());
		actual = jsonhandler.toJson(contactList);
		expected = "[{\"emailList\":[{\"value\":\"email\",\"type\":2}],\"name\":{\"type\":\"data2\",\"value\":\"name\"},\"phoneList\":[{\"value\":\"phone\",\"type\":3}]},{\"emailList\":[{\"value\":\"email\",\"type\":2}],\"name\":{\"type\":\"data2\",\"value\":\"name\"},\"phoneList\":[{\"value\":\"phone\",\"type\":3}]}]";
		Assert.assertEquals(expected, actual);
	}
	
	public void testGetContact() throws Exception { 
		String jsonString = "{\"emailList\":[{\"value\":\"email\",\"type\":2}],\"name\":{\"type\":\"data2\",\"value\":\"name\"},\"phoneList\":[{\"value\":\"phone\",\"type\":3}]}";
		ContactInfo actual = jsonhandler.getContact(jsonString);
		Assert.assertNotNull(actual);
		ContactInfo expected = initialContact();
		Assert.assertEquals(expected.getEmailList().size(), actual.getEmailList().size());
		Assert.assertEquals(expected.getName().getType(), actual.getName().getType());
		Assert.assertEquals(expected.getName().getValue(), actual.getName().getValue());
		Assert.assertEquals(expected.getPhoneList().size(), actual.getPhoneList().size());
		
		jsonString = "[{\"emailList\":[{\"value\":\"email\",\"type\":2}],\"name\":{\"type\":\"data2\",\"value\":\"name\"},\"phoneList\":[{\"value\":\"phone\",\"type\":3}]},{\"emailList\":[{\"value\":\"email\",\"type\":2}],\"name\":{\"type\":\"data2\",\"value\":\"name\"},\"phoneList\":[{\"value\":\"phone\",\"type\":3}]}]";
		List<ContactInfo> contactList = jsonhandler.getContactList(jsonString); 
		Assert.assertNotNull(contactList);
		Assert.assertEquals(2, contactList.size());
		ContactInfo contact1 = contactList.get(0);
		Assert.assertEquals(expected.getEmailList().size(), contact1.getEmailList().size());
		Assert.assertEquals(expected.getName().getType(), contact1.getName().getType());
		Assert.assertEquals(expected.getName().getValue(), contact1.getName().getValue());
		Assert.assertEquals(expected.getPhoneList().size(), contact1.getPhoneList().size());
		ContactInfo contact2 = contactList.get(1);
		Assert.assertEquals(expected.getEmailList().size(), contact2.getEmailList().size());
		Assert.assertEquals(expected.getName().getType(), contact2.getName().getType());
		Assert.assertEquals(expected.getName().getValue(), contact2.getName().getValue());
		Assert.assertEquals(expected.getPhoneList().size(), contact2.getPhoneList().size());
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
