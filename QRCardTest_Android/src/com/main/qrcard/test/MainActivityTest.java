/**
 * 
 */
package com.main.qrcard.test;

import android.content.Intent;
import android.test.InstrumentationTestCase;
import android.util.Log;

import com.main.qrcard.*;

/**
 * @author Administrator
 *
 */
public class MainActivityTest extends InstrumentationTestCase {
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

	/* (non-Javadoc)
	 * @see android.test.InstrumentationTestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		mainActivity.finish();
		super.tearDown();
	}

	public void testAdd() throws Exception{
		String tag = "testAdd";
		Log.v(tag, "test the method");
		int test = mainActivity.add(1, 1);
		assertEquals(2, test);
	}
}
