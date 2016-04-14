package com.jhyarrow.scanner.http;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import android.os.Handler;
import android.os.Message;

public class HttpClientChangeFileNameThread {
	private String url;
	private String fileName;
	private Handler handler;
	private String fileId;
	
	public HttpClientChangeFileNameThread(String fileId,String fileName,String url,Handler handler){
		this.url = url;
		this.fileName = fileName;
		this.handler = handler;
		this.fileId = fileId;
	}
	
	private void doHttpClientPost(){
		HttpClient client = new DefaultHttpClient();
		HttpPost post = new HttpPost(url);
		ArrayList<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("fileName", fileName));
		try {
			 post.setEntity(new UrlEncodedFormEntity(list));
			try {
				HttpResponse response = client.execute(post);
				if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
					String content = EntityUtils.toString(response.getEntity());
					System.out.println("content------->"+ content);
					Message message = new Message();
					message.obj = content;
					handler.sendMessage(message);
				}
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
	
	public void run() {
		doHttpClientPost();
	}
}
