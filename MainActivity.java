package com.example.sm9temphumi;

import android.app.Activity;
import android.os.Bundle;
import android.os.Message;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jnidriver.JNIDriver;

public class MainActivity extends Activity {
	
	JNIDriver mDriver = new JNIDriver();
	
	ReceiveThread mSegThread;
	boolean mThreadRun = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		Button btn = (Button) findViewById(R.id.button1);
		btn.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				mSegThread = new ReceiveThread();
				mSegThread.start();
			}
		});
		
	}
	
	private class ReceiveThread extends Thread {
		@Override
		public void run() {
			super.run();while (mThreadRun) {
				
				Message text = Message.obtain();
				handler.sendMessage(text);
				
				try{
					Thread.sleep(1000);
					
				}catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public Handler handler = new Handler()  {
	
	public void handleMessage(Message msg) {
		TextView tv;
		tv = (TextView) findViewById(R.id.textView1);
		tv.setText("Temp:" + mDriver.getTemp());
		tv = (TextView) findViewById(R.id.textView2);
		tv.setText("Humi:" + mDriver.getHumi());
		}
	
	};
	
	@Override
	protected void onPause() {
		mDriver.close();
		super.onPause();
	}
	
	@Override
	protected void onResume() {
		if(mDriver.open("/dev/sm9s5422_sht20") < 0 ) {
			Toast.makeText(MainActivity.this, "Driver Open Failed", Toast.LENGTH_SHORT).show();
		}
		super.onResume();
	}
	

	
}
