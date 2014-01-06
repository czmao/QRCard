package com.main.qrcard;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.WriterException;

public class QRCardActivity extends Activity {

	private TextView resultTextView;
	private EditText qrStrEditText;
	private ImageView qrImgImageView;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcard);
        
        resultTextView = (TextView) this.findViewById(R.id.tv_scan_result);
        qrStrEditText = (EditText) this.findViewById(R.id.et_qr_string);
        qrImgImageView = (ImageView) this.findViewById(R.id.iv_qr_image);
        
        Button scanBarCodeButton = (Button) this.findViewById(R.id.btn_scan_barcode);
//        scanBarCodeButton.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				//打开扫描界面扫描条形码或二维码
//				Intent openCameraIntent = new Intent(QRCardActivity.this,CaptureActivity.class);
//				startActivityForResult(openCameraIntent, 0);
//			}
//		});
        
        Button generateQRCodeButton = (Button) this.findViewById(R.id.btn_add_qrcode);
//        generateQRCodeButton.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				try {
//					String contentString = qrStrEditText.getText().toString();
//					if (!contentString.equals("")) {
//						//根据字符串生成二维码图片并显示在界面上，第二个参数为图片的大小（350*350）
//						Bitmap qrCodeBitmap = EncodingHandler.createQRCode(contentString, 350);
//						qrImgImageView.setImageBitmap(qrCodeBitmap);
//					}else {
//						Toast.makeText(QRCardActivity.this, "Text can not be empty", Toast.LENGTH_SHORT).show();
//					}
//					
//				} catch (WriterException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			}
//		});
    }

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		//show the scan result in view
		if (resultCode == RESULT_OK) {
			Bundle bundle = data.getExtras();
			String scanResult = bundle.getString("result");
			resultTextView.setText(scanResult);
		}
	}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
}
