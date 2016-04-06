package com.jhyarrow.scanner;

import com.jhyarrow.scanner.http.HttpClientRegisterThread;
import com.jhyarrow.scanner.http.HttpClientThread;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class RegisterActivity extends Activity{
	private Button submit;
	private Button cancel;
	private Context mContext;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register);
		mContext = this;
		submit = (Button) findViewById(R.id.submit);
		cancel = (Button) findViewById(R.id.cancel);
		
		//提交按钮
		submit.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				EditText username = (EditText)findViewById(R.id.registerUsername);
				EditText password = (EditText)findViewById(R.id.registerPassword);
				EditText email = (EditText)findViewById(R.id.registerEmail);
				EditText phone = (EditText)findViewById(R.id.registerPhone);
				String url = "http://192.168.1.100:8080/webServer/user/register";
				new HttpClientRegisterThread(url,
						username.getText().toString(),
						password.getText().toString(),
						email.getText().toString(),
						phone.getText().toString()).start();
				finish();
			}
		});
		//返回按钮
		cancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
				
			}
		});
	}
}
