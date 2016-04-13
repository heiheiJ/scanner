package com.jhyarrow.scanner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.jhyarrow.scanner.http.HttpClientGetFilesThread;
import com.jhyarrow.scanner.util.IP;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class FileViewActivity extends Activity implements OnItemClickListener{
	private ListView listView;
	private SimpleAdapter adapter;
	private String username;
	private List<Map<String,Object>> dataList;
	private Context mContext;
	private Handler handler = new Handler(){
		public void handleMessage(Message msg){
			try {
				JSONObject json = new JSONObject((String)msg.obj);
				JSONArray fileData = json.getJSONArray("files");
				for(int i=0;i<fileData.length();i++){
					Map<String,Object> map = new HashMap<String, Object>();
					JSONObject file = fileData.getJSONObject(i);
					map.put("fileName", file.getString("fileName"));
					map.put("fileSize", file.getString("fileSize"));
					map.put("createTime", file.getString("createTime"));
					dataList.add(map);
				}
				listView.setAdapter(adapter);
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
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
		new HttpClientGetFilesThread(username, url, handler).start();
		
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
}
