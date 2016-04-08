package com.jhyarrow.scanner;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.jhyarrow.scanner.http.HttpClientLoginThread;
import com.jhyarrow.scanner.util.Code;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class LogInActivity extends Activity{
	private Button submit;
	private Button cancel;
	private Button change;
	private Context mContext;
	private ImageView validatePic;
	private String code;
	private Handler handler = new Handler(){
		public void handleMessage(Message msg) {
			JSONTokener jsonParser = new JSONTokener((String)msg.obj);   
			try {
				JSONObject result = (JSONObject) jsonParser.nextValue();
				System.out.println(msg.obj);
				if(result.getString("result").equals("true")){
					new AlertDialog.Builder(LogInActivity.this)
					.setMessage("登录成功")
					.setPositiveButton("确定", null)
					.show();
					finish();
				}else if(result.getString("result").equals("false")){
					new AlertDialog.Builder(LogInActivity.this)
					.setMessage("密码不正确")
					.setPositiveButton("确定", null)
					.show();
				}

			} catch (JSONException e) {
				
				e.printStackTrace();
			}
			
		};
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.log_in);
		mContext = this;
		submit = (Button) findViewById(R.id.loginSubmit);
		cancel = (Button) findViewById(R.id.loginCancel);
		change = (Button) findViewById(R.id.loginChange);
		validatePic = (ImageView)findViewById(R.id.loginValidatePic);
		validatePic.setImageBitmap(Code.getInstance().createBitmap());
		LayoutParams para = validatePic.getLayoutParams();
		para.height = 200;
		para.width = 500;
		validatePic.setLayoutParams(para);
		code = Code.getInstance().getCode();
		
		//提交按钮
		submit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				EditText username = (EditText)findViewById(R.id.loginUsername);
				EditText password = (EditText)findViewById(R.id.loginPassword);
				EditText validateCode = (EditText)findViewById(R.id.loginValidateCode);
				boolean isTrue = true;
//				if(!code.toLowerCase().equals(validateCode.toString().toLowerCase())){
//					new AlertDialog.Builder(LogInActivity.this)
//									.setMessage("验证码不正确")
//									.setPositiveButton("确定", null)
//									.show();
//					isTrue = false;
//				}
				String url = "http://192.168.1.100:8080/webServer/user/login";
				if(isTrue){
					new HttpClientLoginThread(url,
							username.getText().toString(), 
							password.getText().toString(),
							handler).start();				
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
		change.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				validatePic = (ImageView) findViewById(R.id.loginValidatePic);
				validatePic.setImageBitmap(Code.getInstance().createBitmap());
				
			}
		});
	}
}
