package com.dreamon.poke;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Handler;
import android.os.Message;

public class urlLoad extends Activity {

	String postUrl = "";
	String getUrl = "";
	String postResult = "";
	String getResult = "";
	//HttpClient client;
	//HttpPost post;
	//HttpResponse response;
	//HttpEntity resEntity;
	
    //«Øºc¤l
	public urlLoad() {
		//client = new DefaultHttpClient();
		//post = new HttpPost(url);
		//response = client.execute(post);
		//resEntity = response.getEntity();
		
	}
	
	
	
	
	public void setUrl(String url)
	{
		this.postUrl = url;
	}
	
	public void startThread(String[] key, String[] value) {
		Thread t = new Thread(new sendPostRunnable(key, value));
		t.start();
	}
	
	private String sendPostDataToIntenet(String[] key,String[] str)
	{
		//bulid http post connect
		HttpPost post = new HttpPost(postUrl);
		
		List<NameValuePair>params = new ArrayList<NameValuePair>();
		
		for(int i = 0; i < key.length; i++)
		{
			params.add(new BasicNameValuePair(key[i], str[i] ));
		}
		//params.add(new BasicNameValuePair(key, str ));
		
		try
		{
			//request http
			post.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
			
			//get http response
			HttpResponse response = new DefaultHttpClient().execute(post);
			
			if(response.getStatusLine().getStatusCode() == 200)
			{
				//get response string
				String result = EntityUtils.toString(response.getEntity());
				
				return result;
			}
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}
	
	class sendPostRunnable implements Runnable
	{
		String str[];
		String key[];
		
		public sendPostRunnable(String[] key, String[] str)
		{
			this.str = str;
			this.key = key;
		}
		
		@Override
		public void run() {
			//return result;
			String result = sendPostDataToIntenet(key, str);
			uiMessageHandler.obtainMessage(1, result).sendToTarget();
		}
	}
	
	private Handler uiMessageHandler = new Handler()
	{
		@Override
		public void handleMessage(Message msg)
		{
			switch(msg.what)
			{
				case 1:
					
					String result = "";
					
					if(msg.obj instanceof String)
					{
						result = (String)msg.obj;
					}
					
					if(result != null)
					{
						System.out.println(result);
					}
					
					break;
			}
		}
	};
	

}
