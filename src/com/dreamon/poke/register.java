package com.dreamon.poke;

import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class register extends Activity {

	
	Button registerBtn;
	TextView nameText;
	TextView emailText;
	TextView responseText;
	
	memberDataSql mds;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register);
		
		mds = new memberDataSql(register.this);
		
		/*
		Intent it = new Intent();
		it.setClass(this, gamePlay.class);
		ActivityPoke.this.startActivity(it);
		*/
		
		registerBtn = (Button)findViewById(R.id.registerBtn);
		nameText = (TextView)findViewById(R.id.nameText);
		emailText = (TextView)findViewById(R.id.emailText);
		responseText = (TextView)findViewById(R.id.responseText);
		
		registerBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				if( mds.create(String.valueOf(nameText.getText()), String.valueOf(emailText.getText())) != -1 )
				{
					register.this.finish();
				}
				else
				{
					responseText.setText("註冊失敗，請重新再試一次");
				}
				
				
				/*
				Intent it = new Intent();
				it.setClass(register.this, gamePlay.class);
				register.this.startActivity(it);
				*/
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
