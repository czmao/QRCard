package com.main.qrcard.test;

import java.util.ArrayList;

import com.main.contact.ContactInfo;
import com.main.contact.ContactManager;
import com.main.qrcard.MainActivity;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.ContactsContract.Data;
import android.provider.ContactsContract.RawContacts;
import android.provider.ContactsContract.CommonDataKinds.StructuredName;
import android.test.InstrumentationTestCase;

import junit.framework.TestCase;

public class ContactTest extends InstrumentationTestCase {
	private static final String TAG = "ContactTest";
	private MainActivity mainActivity = null;

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
		super.tearDown();
	}

	public void testAddContact() throws Exception { 
		ContactInfo contact = new ContactInfo();
		contact.setName("Demo");
		ContactManager.getInstance().addContact(contact, mainActivity);
	}
}