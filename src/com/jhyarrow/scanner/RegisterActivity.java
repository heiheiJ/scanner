package com.jhyarrow.scanner;

import com.jhyarrow.scanner.http.HttpClientRegisterThread;
import com.jhyarrow.scanner.util.Code;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class RegisterActivity extends Activity{
	private Button submit;
	private Button cancel;
	private Button validate;
	private ImageView validatePic;
	private String code;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register);
		submit = (Button) findViewById(R.id.registerSubmit);
		cancel = (Button) findViewById(R.id.registerCancel);
		validate = (Button) findViewById(R.id.registerValidate);
		validatePic = (ImageView) findViewById(R.id.registerValidatePic);
		validatePic.setImageBitmap(Code.getInstance().createBitmap());
		LayoutParams para = validatePic.getLayoutParams();
		para.height = 200;
		para.width = 500;
		validatePic.setLayoutParams(para);
		code = Code.getInstance().getCode();

		
		//提交按钮
		submit.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				EditText username = (EditText)findViewById(R.id.registerUsername);
				EditText password = (EditText)findViewById(R.id.registerPassword);
				EditText email = (EditText)findViewById(R.id.registerEmail);
				EditText phone = (EditText)findViewById(R.id.registerPhone);
				EditText validateCode = (EditText)findViewById(R.id.registerValidateCode);
				boolean isTrue = true;
				if(!code.equals(validateCode.toString())){
					new AlertDialog.Builder(RegisterActivity.this)
									.setMessage("验证码不正确")
									.setPositiveButton("确定", null)
									.show();
					isTrue = false;
				}
				String url = "http://192.168.1.100:8080/webServer/user/register";
				if(isTrue){
					new HttpClientRegisterThread(url,
							username.getText().toString(),
							password.getText().toString(),
							email.getText().toString(),
							phone.getText().toString()).start();
					finish();
				}
			}
		});
		//返回按钮
		cancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
				
			}
		});
		
		//验证码按钮
		validate.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				validatePic = (ImageView) findViewById(R.id.registerValidatePic);
				validatePic.setImageBitmap(Code.getInstance().createBitmap());
			}
		});
	}
}
