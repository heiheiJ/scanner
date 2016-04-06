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

public class HttpClientRegisterThread extends Thread{
	private String url;
	private String username;
	private String password;
	private String email;
	private String phone;
	public HttpClientRegisterThread(String url,String username,String password,String email,String phone){
		this.url = url;
		this.username = username;
		this.password = password;
		this.email = email;
		this.phone = phone;
	}
	
	private void doHttpClientPost(){
		HttpClient client = new DefaultHttpClient();
		HttpPost post = new HttpPost(url);
		ArrayList<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("username", username));
		list.add(new BasicNameValuePair("password", password));
		list.add(new BasicNameValuePair("email", email));
		list.add(new BasicNameValuePair("phone", phone));
		try {
			 post.setEntity(new UrlEncodedFormEntity(list));
			try {
				HttpResponse response = client.execute(post);
				if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
					String content = EntityUtils.toString(response.getEntity());
					System.out.println("content------->"+ content);
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
