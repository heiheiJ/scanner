package com.jhyarrow.scanner;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity{
	private Button logIn;
	private Button register;
	private Context mContext;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		mContext = this;
		logIn = (Button) findViewById(R.id.logIn);
		register = (Button) findViewById(R.id.register);
		
		//登录按钮点击事件
		logIn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(mContext,LogInActivity.class);
				startActivity(intent);
			}
		});
		//注册按钮点击事件
		register.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(mContext,RegisterActivity.class);
				Log.v("heihei", "lalala");
				startActivity(intent);				
			}
		});
	}
}
