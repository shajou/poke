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
import android.database.sqlite.SQLiteDatabase;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

@SuppressLint("NewApi")
public class ActivityPoke extends Activity {

	
	Button startBtn;
	Button rankBtn;
	Button testBtn;
	Button setBtn;
	
	@SuppressWarnings("null")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.poke_main);
		
		ActionBar ab = getActionBar();
		ab.hide();
		
		/*
		Intent it = new Intent();
		it.setClass(this, gamePlay.class);
		ActivityPoke.this.startActivity(it);
		*/
		
		startBtn = (Button)findViewById(R.id.startBtn);
		rankBtn = (Button)findViewById(R.id.rankBtn);
		setBtn = (Button)findViewById(R.id.setBtn);
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
				it.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
				it.setClass(ActivityPoke.this, gameRank.class);
				ActivityPoke.this.startActivity(it);
			}
		});
		
		testBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent it = new Intent();
				it.setClass(ActivityPoke.this, sqlTest.class);
				ActivityPoke.this.startActivity(it);
			}
		});
		
		setBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent it = new Intent();
				it.setClass(ActivityPoke.this, pokeSet.class);
				ActivityPoke.this.startActivity(it);
			}
		});
		
		//判斷是否有註冊
		String userNmae = "ghost";
		memberDataSql mds = new memberDataSql(ActivityPoke.this);
		Cursor cursor = mds.getAll("");
		if(cursor.getCount() == 0)
		{
			mds.create("ghost", "ghost@email.com");
		}
		
		//建立設定
		setDataSql set = new setDataSql(ActivityPoke.this);
		Cursor setCursor = set.getAll("");
		System.out.println(setCursor);
		if(setCursor.getCount() == 0)
		{
			//set.onCreate();
			set.create(1);
		}
		//mds.create("ghost", "ghost@email.com");
		
		
		
	//	nld.onCreate(db);
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_poke, menu);
		return true;
	}

}
