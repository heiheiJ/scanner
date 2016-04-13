package com.jhyarrow.scanner;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.jhyarrow.scanner.http.HttpClientAddFilesThread;
import com.jhyarrow.scanner.http.HttpClientGetFilesThread;
import com.jhyarrow.scanner.util.IP;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class FileViewActivity extends Activity implements OnItemClickListener,OnScrollListener{
	private ListView listView;
	private SimpleAdapter adapter;
	private String username;
	private List<Map<String,Object>> dataList;
	private Context mContext;
	private Button addFile;
	private EditText editText;
	private Handler getFileHandler = new Handler(){
		public void handleMessage(Message msg){
			try {
				JSONObject json = new JSONObject((String)msg.obj);
				JSONArray fileData = json.getJSONArray("files");
				for(int i=0;i<fileData.length();i++){
					Map<String,Object> map = new HashMap<String, Object>();
					JSONObject file = fileData.getJSONObject(i);
					map.put("fileName", file.getString("fileName"));
					map.put("fileSize", file.getString("fileSize"));
					map.put("createTime", getDate(file.getString("createTime")));
					dataList.add(map);
				}
				listView.setAdapter(adapter);
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	};
	private Handler addFileHandler = new Handler(){
		public void handleMessage(Message msg) {
				try {
					JSONObject file = new JSONObject((String)msg.obj);
					Map<String,Object> map = new HashMap<String, Object>();
					map.put("fileName", file.getString("fileName"));
					map.put("fileSize", file.getString("fileSize"));
					map.put("createTime", getDate(file.getString("createTime")));
					dataList.add(map);
					listView.setAdapter(adapter);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			
		};
	};
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.file_view);
		mContext = this;
		listView =(ListView) findViewById(R.id.mainListView);
		dataList = new ArrayList<Map<String,Object>>();
		adapter = new SimpleAdapter(this, dataList, R.layout.file, 
				new String[]{"fileName","fileSize","createTime"}, 
				new int[]{R.id.mainFileName,R.id.mainFileSize,R.id.mainCreateTime});

		String url = "http://" + IP.getInstance().getIpAddress() + ":8080/webServer/user/getFiles";
		username = getIntent().getExtras().getString("username");
		System.out.println(username);
		listView.setOnItemClickListener(this);
		new HttpClientGetFilesThread(username, url, getFileHandler).start();
		editText = new EditText(this);
		addFile = (Button) findViewById(R.id.addFile);
		addFile.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				Builder builder = new AlertDialog.Builder(FileViewActivity.this);
				builder.setTitle("文件名");
				builder.setView(editText);
				builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						String fileName = editText.getText().toString();
						String url = "http://" + IP.getInstance().getIpAddress() + ":8080/webServer/file/addFile";
						new HttpClientAddFilesThread(username,fileName,url,addFileHandler).start();
					}
				});
				builder.setNegativeButton("取消", null);
				AlertDialog dialog = builder.create();
				dialog.show();
			}
		});
	}


	@Override
	public void onItemClick(AdapterView<?> parent,View view, int position, long id) {
		Intent intent = new Intent(mContext,PicViewActivity.class);
		Bundle bundle = new Bundle();
		bundle.putString("username", username);
		bundle.putInt("position", position);
		intent.putExtras(bundle);
		startActivity(intent);
	}


	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// TODO Auto-generated method stub
		
	}
	
	public String getDate(String tmp){
		BigInteger dateInt = new BigInteger(tmp);
		Date date = new Date(dateInt.longValue());
		return date.getYear()+1900 + "-" + date.getMonth() + "-" + date.getDay();
	}
}
