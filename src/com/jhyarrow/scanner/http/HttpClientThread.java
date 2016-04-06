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
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

public class HttpClientThread extends Thread {
	
	private String url;
	private String username;
	private String password;
	private String email;
	private String phone;
	public HttpClientThread(String url,String username,String password,String email,String phone){
		this.url = url;
		this.username = username;
		this.password = password;
		this.email = email;
		this.phone = phone;
	}
	
	public HttpClientThread(String url){
		this.url = url;
	}
	
	private void dohttpClientGet(){
		HttpGet httpGet = new HttpGet(url);
		HttpClient client = new DefaultHttpClient();
		try {
			HttpResponse response = client.execute(httpGet);
			if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
				String content = EntityUtils.toString(response.getEntity());
				System.out.println("content------->"+ content);
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void doHttpClientPost(){
		HttpClient client = new DefaultHttpClient();
		HttpPost post = new HttpPost(url);
		ArrayList<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("username", username));
		list.add(new BasicNameValuePair("password", password));
		list.add(new BasicNameValuePair("email", email));
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
		//dohttpClientGet();
		doHttpClientPost();
	}
}
