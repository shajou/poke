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
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
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
	TextView alert;
	
	memberDataSql mds;
	NewListDataSQL nld;
	
	Long id;
	
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register);
		
		ActionBar ab = getActionBar();
		ab.hide();
		
		mds = new memberDataSql(register.this);
		
		/*
		Intent it = new Intent();
		it.setClass(this, gamePlay.class);
		ActivityPoke.this.startActivity(it);
		*/
		
		Cursor cursor = mds.getAll("");
		cursor.moveToFirst();
		id = cursor.getLong(0);
		
		registerBtn = (Button)findViewById(R.id.registerBtn);
		nameText = (TextView)findViewById(R.id.nameText);
		emailText = (TextView)findViewById(R.id.emailText);
		responseText = (TextView)findViewById(R.id.responseText);
		alert = (TextView)findViewById(R.id.alert);
		
		registerBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				if(
						( !String.valueOf(nameText.getText()).isEmpty() && 
						!String.valueOf(emailText.getText()).isEmpty() ) 
				)
				{
					if( mds.update(id, String.valueOf(nameText.getText()), String.valueOf(emailText.getText())) != -1 )
					{
						//replace name
						nld = new NewListDataSQL(register.this);
						Cursor cursor = nld.getAll("");
						cursor.moveToFirst();
						for(int j = 0; j < cursor.getCount(); j++)
						{
							nld.update(cursor.getLong(0), "name", String.valueOf(nameText.getText()) );
							//nld.update(cursor.getLong(0), "email", String.valueOf(emailText.getText()) );
							cursor.moveToNext();
						}
						
						Intent it = new Intent();
						it.setClass(register.this, gameRank.class);
						it.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
						register.this.startActivity(it);
						register.this.finish();
					}
					else
					{
						responseText.setText("註冊失敗，請重新再試一次");
					}
				}
				else
				{
					alert.setText("請確實輸入name及E-mail");
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
