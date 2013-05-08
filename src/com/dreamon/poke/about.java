package com.dreamon.poke;

import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class about extends Activity {

	TextView app1;
	
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about);
		
		ActionBar ab = getActionBar();
		ab.hide();
		
		
		/*
		Intent it = new Intent();
		it.setClass(this, gamePlay.class);
		ActivityPoke.this.startActivity(it);
		*/
		
		//app1 = (TextView)findViewById(R.id.app1);
		//app1.setText(Html.fromHtml("<a href=\"https://play.google.com/store/apps/details?id=com.dreamon.morse\">SOS Morse Code ¼¯´µ¹q½X 123</a> " 
		//		));
		//app1.setMovementMethod(LinkMovementMethod.getInstance());
		
		app1 = (TextView)findViewById(R.id.app1);
		
		app1.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.dreamon.morse"));
				startActivity(browserIntent);
				
			}
		});
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_poke, menu);
		return true;
	}

}
