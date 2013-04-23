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

public class sqlTest extends Activity {

	memberDataSql mds;
	NewListDataSQL rds;
	
	Button deleteMemberTableBtn;
	Button deleteRankTableBtn;
	Button deleteMemberDataBtn;
	Button deleteRankDataBtn;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sql_test);
		
		mds = new memberDataSql(sqlTest.this);
		rds = new NewListDataSQL(sqlTest.this);
		
		deleteMemberTableBtn = (Button)findViewById(R.id.deleteMemberTableBtn);
		deleteMemberDataBtn = (Button)findViewById(R.id.deleteMemberDataBtn);
		deleteRankTableBtn = (Button)findViewById(R.id.deleteRankTableBtn);
		deleteRankDataBtn = (Button)findViewById(R.id.deleteRankDataBtn);
		
		deleteMemberTableBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mds.deleteTable();
			}
		});
		
		deleteMemberDataBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mds.deleteAll();
			}
		});
		
		
		
		deleteRankTableBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				rds.deleteTable();
			}
		});
		
		deleteRankDataBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				rds.deleteAll();
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
