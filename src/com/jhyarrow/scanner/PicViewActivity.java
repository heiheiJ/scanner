package com.jhyarrow.scanner;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.jhyarrow.scanner.http.HttpClientAddFilesThread;
import com.jhyarrow.scanner.http.HttpClientGetPicsThread;
import com.jhyarrow.scanner.util.IP;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Base64;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.SimpleAdapter.ViewBinder;

public class PicViewActivity extends Activity implements OnScrollListener{
	private ListView listView;
	private Context mContext;
	private List<Map<String,Object>> dataList;
	private SimpleAdapter adapter;
	private Button cancel;
	private Button addPic;
	private Handler handler = new Handler(){
		public void handleMessage(Message msg) {
			try {
				JSONObject json = new JSONObject((String)msg.obj);
				JSONArray picData = json.getJSONArray("pics");
				for (int i=0;i<picData.length();i++){
					Map<String,Object> map = new HashMap<String,Object>();
					JSONObject pic = picData.getJSONObject(i);
					byte[] img = Base64.decode(pic.getString("picContent"),Base64.DEFAULT);
					BitmapFactory.Options bfOptions = new BitmapFactory.Options();
					bfOptions.inSampleSize = 2;	
					InputStream input = new ByteArrayInputStream(img);
					SoftReference softRef = new SoftReference(BitmapFactory.decodeStream(input,null,bfOptions));
					Bitmap bitMap = (Bitmap)softRef.get();
					map.put("picViewContent", bitMap);
					dataList.add(map);
					if(input!=null){
						try {
							input.close();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
				ViewGroup.LayoutParams params = listView.getLayoutParams();
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
		setContentView(R.layout.pic_view);
		mContext = this;
		listView = (ListView) findViewById(R.id.picListView);
		String position = String.valueOf(getIntent().getExtras().getInt("position"));
		String username = getIntent().getExtras().getString("username");
		dataList = new ArrayList<Map<String,Object>>();
		addPic = (Button) findViewById(R.id.addPic);
		addPic.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Builder builder = new AlertDialog.Builder(PicViewActivity.this);
				builder.setTitle("选择文件来源");
				builder.setPositiveButton("拍照", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						
					}
				});
				builder.setNeutralButton("本地图片", null);
				AlertDialog dialog = builder.create();
				dialog.show();
			}
		});
		cancel = (Button) findViewById(R.id.picReturn);
		cancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
				
			}
		});
		
		adapter = new SimpleAdapter(this,dataList,R.layout.pic,
				new String[]{"picViewContent"},
				new int[]{R.id.picViewContent});
		adapter.setViewBinder(new ListViewBinder());
		String url = "http://" + IP.getInstance().getIpAddress() + ":8080/webServer/user/getPics";
		new HttpClientGetPicsThread(url,position,username,handler).start();
	}
	
	private class ListViewBinder implements ViewBinder {

		@Override
		public boolean setViewValue(View view, Object data,
				String textRepresentation) {
			// TODO Auto-generated method stub
			if((view instanceof ImageView) && (data instanceof Bitmap)) {
				ImageView imageView = (ImageView) view;
				Bitmap bmp = (Bitmap) data;
				imageView.setImageBitmap(bmp);
				return true;
			}
			return false;
		}
		
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// TODO Auto-generated method stub
		
	} 
}
