package com.main.qrcard;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.WriterException;
import com.main.contact.ContactInfo;
import com.main.contact.ContactManager;
import com.main.gson.JsonHandler;
import com.main.qrImage.ImageManager;
import com.zxing.CaptureActivity;
import com.zxing.ecnoding.EncodingHandler;

public class QRCardActivity extends Activity {
	public final static String TAG = "QRCardActivity";
	public final static int QR_CODE_BITMAP_WIDTH_AND_HIGHT = 500;
	private static final int SCAN_CONTACT = 0;
	private static final int PICK_CONTACT = 3; 

	private ImageView qrImgImageView;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
        setContentView(R.layout.activity_qrcard);

        qrImgImageView = (ImageView) this.findViewById(R.id.iv_qr_image);
        
        Button scanBarCodeButton = (Button) this.findViewById(R.id.btn_scan_barcode);
        scanBarCodeButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//打开扫描界面扫描条形码或二维码
				Intent openCameraIntent = new Intent(QRCardActivity.this,CaptureActivity.class);
				startActivityForResult(openCameraIntent, 0);
			}
		});
        
        Button generateQRCodeButton = (Button) this.findViewById(R.id.btn_add_qrcode);
        generateQRCodeButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//调用系统联系人
				//方式一
//				Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
//				startActivityForResult(intent, PICK_CONTACT);
				//方式二
				Intent intent = new Intent(Intent.ACTION_PICK);        
				intent.setType(ContactsContract.Contacts.CONTENT_TYPE);//vnd.android.cursor.dir/contact
				startActivityForResult(intent, PICK_CONTACT);
			}
		});
    }

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Log.d(TAG, "onActivityResult");
		
		//show the scan result in view
		if (resultCode == Activity.RESULT_OK) {
			ContactInfo contact = null;
	        switch(requestCode){  
	        	case SCAN_CONTACT:
	        		Log.d(TAG, "onActivityResult SCAN_CONTACT");
	    			Bundle bundle = data.getExtras();
	    			String scanResult = bundle.getString("result");
	    			contact = JsonHandler.getInstance().getContact(scanResult);
	    			if(contact!=null&&!contact.getName().getValue().isEmpty()){
	    				ContactManager.getInstance().addContact(this, contact);
	    				Toast.makeText(QRCardActivity.this, "Add " + contact.getName().getValue() + " to contact list.", Toast.LENGTH_SHORT).show();
	    			}else{
	    				Toast.makeText(QRCardActivity.this, "The QR code is invalid.", Toast.LENGTH_SHORT).show();
	    			}
	    			break;
	            case PICK_CONTACT:  
	               	Log.d(TAG, "onActivityResult PICK_CONTACT");  
	                Uri contactData = data.getData();  
	                if(contactData!=null){
	     				try {
	     					contact = ContactManager.getInstance().getContactInfo(this, contactData);	
		 					if (contact!=null&&!contact.getName().getValue().isEmpty()) {
		 						String contentString = JsonHandler.getInstance().toJson(contact);
		 						//Generate QR code bitmap with default width and height
		 						Bitmap qrCodeBitmap = EncodingHandler.createQRCode(contentString, QR_CODE_BITMAP_WIDTH_AND_HIGHT);
		 						qrImgImageView.setImageBitmap(qrCodeBitmap);
		 						ImageManager.getInstance().saveImage(contact.getName().getValue(), qrCodeBitmap);
		 					}else {
		 						Toast.makeText(QRCardActivity.this, "Please choose a contact with a name.", Toast.LENGTH_SHORT).show();
		 					}					 
		                } catch (WriterException e) {
	     					// TODO Auto-generated catch block
	     					e.printStackTrace();
	     				}
	                }
	                break;            	
	        }
		}
	}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
}
