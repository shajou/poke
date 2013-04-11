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

public class ActivityPoke extends Activity {

	
	Button startBtn;
	Button rankBtn;
	Button testBtn;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.poke_main);
		
		/*
		Intent it = new Intent();
		it.setClass(this, gamePlay.class);
		ActivityPoke.this.startActivity(it);
		*/
		
		startBtn = (Button)findViewById(R.id.startBtn);
		rankBtn = (Button)findViewById(R.id.rankBtn);
		testBtn = (Button)findViewById(R.id.testBtn);
		
		startBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent it = new Intent();
				it.setClass(ActivityPoke.this, gamePlay.class);
				ActivityPoke.this.startActivity(it);
			}
		});
		
		rankBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent it = new Intent();
				it.setClass(ActivityPoke.this, gameRank.class);
				ActivityPoke.this.startActivity(it);
			}
		});
		
		testBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				urlLoad urlload = new urlLoad();
				urlload.setUrl("http://poke.grtimed.com/upd_rank.php");
				
				String keyAry[] = {"name", "score", "hits", "level"};
				String valueAry[] = {"admin", "200000", "200", "S"};
				
				//ArrayList<String>key = ;
				//ArrayList<String>value = ;
				
				urlload.startThread(keyAry,valueAry);
				//urlLoadThread();
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
