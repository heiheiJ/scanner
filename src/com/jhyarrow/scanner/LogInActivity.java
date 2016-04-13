package com.jhyarrow.scanner;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.jhyarrow.scanner.http.HttpClientLoginThread;
import com.jhyarrow.scanner.util.Code;
import com.jhyarrow.scanner.util.IP;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
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
			boolean flag = false;
			try {
				JSONObject result = (JSONObject) jsonParser.nextValue();
				System.out.println(msg.obj);
				if(result.getString("username").equals("false")){
					new AlertDialog.Builder(LogInActivity.this)
					.setMessage("�û���������")
					.setPositiveButton("ȷ��", null)
					.show();
				}else if(result.getString("result").equals("true")){
					new AlertDialog.Builder(LogInActivity.this)
					.setMessage("��¼�ɹ�")
					.setPositiveButton("ȷ��", null)
					.show();
					flag = true;
				}else if(result.getString("result").equals("false")){
					new AlertDialog.Builder(LogInActivity.this)
					.setMessage("���벻��ȷ")
					.setPositiveButton("ȷ��", null)
					.show();
				}
				Intent intent = new Intent(mContext,FileViewActivity.class);
				EditText username = (EditText)findViewById(R.id.loginUsername);
				Bundle bundle = new Bundle();
				bundle.putString("username", username.getText().toString());
				intent.putExtras(bundle);
				startActivity(intent);
				finish();
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
		System.out.println("��֤��" + code);
		//�ύ��ť
		submit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				EditText username = (EditText)findViewById(R.id.loginUsername);
				EditText password = (EditText)findViewById(R.id.loginPassword);
				EditText validateCode = (EditText)findViewById(R.id.loginValidateCode);
				boolean isTrue = true;
				System.out.println(validateCode.getText().toString());
				if(!code.equals(validateCode.getText().toString())){
					new AlertDialog.Builder(LogInActivity.this)
									.setMessage("��֤�벻��ȷ")
									.setPositiveButton("ȷ��", null)
									.show();
					isTrue = false;
				}
				String url = "http://" + IP.getInstance().getIpAddress() + ":8080/webServer/user/login";
				if(isTrue){
					new HttpClientLoginThread(url,
							username.getText().toString(), 
							password.getText().toString(),
							handler).start();				
				}

			}
		});
		//���ذ�ť
		cancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();		
			}
		});
		
		//��֤�밴ť
		change.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				validatePic = (ImageView) findViewById(R.id.loginValidatePic);
				validatePic.setImageBitmap(Code.getInstance().createBitmap());
				
			}
		});
	}
}
