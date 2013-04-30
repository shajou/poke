package com.dreamon.poke;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Queue;
import java.lang.Iterable;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Stack;
import java.util.Timer;
import java.util.TimerTask;


import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.util.Log;
import android.util.SparseArray;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.AbsoluteLayout;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

@SuppressWarnings("rawtypes")
@SuppressLint("NewApi")
public class scoreUpdate extends Activity {
	
	//SQLite
	NewListDataSQL helper;
	
	//PHP Mysql
	getSqlData sqlData;
	
	
	//Button
	Button scoreUpdateBtn;
	
	TextView scoreDataText;
	TextView updMsgTv;
	
	//net 
	ConnectivityManager conManager;
	NetworkInfo networInfo;
	boolean netBoo = false;
	
	Bundle bd;
	
	//layout
	RelativeLayout regLayout;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.score_update);
		
		ActionBar ab = getActionBar();
		ab.hide();
		
		//bundle
		bd = new Bundle();
		bd = this.getIntent().getExtras();
		
		//SQLite
		helper = new NewListDataSQL(scoreUpdate.this);
		
		//MySql
		sqlData = new getSqlData();
		sqlData.setUrl("http://poke.grtimed.com/download_rank.php");
		
		scoreUpdateBtn = (Button)findViewById(R.id.scoreUpdateBtn);
		updMsgTv = (TextView)findViewById(R.id.updMsgTv);
		scoreDataText = (TextView)findViewById(R.id.scoreDataText);
		scoreDataText.setText(bd.getString("name") + "\n" +
				bd.getString("score") + "\n" + 
				bd.getString("hits") + " hits" + "\n" + 
				bd.getString("level")
				);
				
		conManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);//先取得此service

		networInfo = conManager.getActiveNetworkInfo();       //在取得相關資訊

		if (networInfo == null || !networInfo.isAvailable()){ //判斷是否有網路
			netBoo = false;
		}else{
			scoreUpdateBtn.setVisibility(View.VISIBLE);
			netBoo = true;
			sqlData.startThread();
			//btn
			
			scoreUpdateBtn.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					updBestLocalScore();
				}
			});
		}
		
		
	}
	
	
	public void updBestLocalScore() {
		
		
			/*
			memberDataSql mds = new memberDataSql(scoreUpdate.this);		
			Cursor cursor = mds.getAll("");
			int rows_num = cursor.getCount();
			email = new String[rows_num];
			int mId[] = new int[rows_num];
			String localName[] = new String[rows_num];
			
			//取使用者資料
			if(rows_num != 0) {
				cursor.moveToFirst();			//將指標移至第一筆資料
				for(int i=0; i<rows_num; i++) {
					mId[i] = cursor.getInt(0);	//取得第0欄的資料，根據欄位type使用適當語法
					localName[i] = cursor.getString(1);
					email[i] = cursor.getString(2);
					cursor.moveToNext();		//將指標移至下一筆資料
					//System.out.println(id[i] + " " + name[i] + " " + score[i] + " " + hits[i] + " " + level[i] );
				}
			}
			cursor.close();
			*/
			//bundle
			
			
			//將第一筆高分 上傳資料至遠端
			urlLoad urlload = new urlLoad();
			urlload.act = 1;
			urlload.setUrl("http://poke.grtimed.com/upd_rank.php");
			String keyAry[] = {"name", "email", "score", "hits", "level", "msg"};
			String valueAry[] = {bd.getString("name"), bd.getString("email"), bd.getString("score"), bd.getString("hits"), bd.getString("level"), String.valueOf(updMsgTv.getText())};
			urlload.startThread(keyAry,valueAry);
			
			//isUpdateScore = 1;
			NewListDataSQL nld = new NewListDataSQL(scoreUpdate.this);
			nld.update(Long.valueOf(bd.getString("_id")), "isUpdatesScore" , "1");
			
			System.out.println("msg: " + String.valueOf(updMsgTv.getText()));
			
			Intent it = new Intent();
			it.setClass(scoreUpdate.this, gameRank.class);
			it.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
			scoreUpdate.this.startActivity(it);
			scoreUpdate.this.finish();
		
		
		
	}
}
