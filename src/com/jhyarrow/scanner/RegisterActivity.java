package com.jhyarrow.scanner;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.jhyarrow.scanner.http.HttpClientRegisterThread;
import com.jhyarrow.scanner.util.Code;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
	private Handler handler = new Handler(){
		public void handleMessage(Message msg){
			JSONTokener jsonParser = new JSONTokener((String)msg.obj);
			try {
				JSONObject result = (JSONObject) jsonParser.nextValue();
				if(result.getString("email").equals("false")){
					new AlertDialog.Builder(RegisterActivity.this)
						.setMessage("�����ѱ�ע��")
						.setPositiveButton("ȷ��",null)
						.show();
				}else if(result.getString("phone").equals("false")){
					new AlertDialog.Builder(RegisterActivity.this)
					.setMessage("�ֻ������ѱ�ע��")
					.setPositiveButton("ȷ��",null)
					.show();
				}else if(result.getString("username").equals("false")){
					new AlertDialog.Builder(RegisterActivity.this)
					.setMessage("�û����ѱ�ע��")
					.setPositiveButton("ȷ��",null)
					.show();
				}else{
					new AlertDialog.Builder(RegisterActivity.this)
					.setMessage("ע��ɹ�")
					.setPositiveButton("ȷ��",null)
					.show();
					finish();
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	};
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

		
		//�ύ��ť
		submit.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				EditText username = (EditText)findViewById(R.id.registerUsername);
				EditText password = (EditText)findViewById(R.id.registerPassword);
				EditText email = (EditText)findViewById(R.id.registerEmail);
				EditText phone = (EditText)findViewById(R.id.registerPhone);
				EditText validateCode = (EditText)findViewById(R.id.registerValidateCode);
				boolean isTrue = true;
				if(!code.equals(validateCode.getText().toString())){
					new AlertDialog.Builder(RegisterActivity.this)
									.setMessage("��֤�벻��ȷ")
									.setPositiveButton("ȷ��", null)
									.show();
					isTrue = false;
				}
				String url = "http://192.168.1.100:8080/webServer/user/register";
				if(isTrue){
					new HttpClientRegisterThread(url,
							username.getText().toString(),
							password.getText().toString(),
							email.getText().toString(),
							phone.getText().toString(),
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
		validate.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				validatePic = (ImageView) findViewById(R.id.registerValidatePic);
				validatePic.setImageBitmap(Code.getInstance().createBitmap());
			}
		});
	}
}
