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

import com.google.ads.AdRequest;
import com.google.ads.AdSize;
import com.google.ads.AdView;

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
import android.widget.Toast;

@SuppressWarnings("rawtypes")
@SuppressLint("NewApi")
public class gameRank extends Activity {
	
	//SQLite
	NewListDataSQL helper;
	
	//PHP Mysql
	getSqlData sqlData;
	
	int id[];
	String name[];
	int score[];
	int hits[];
	String msg[];
	String globalEmail[];
	String level[];	
	String isUpdateScore[];
	String localIsUpdateScore[];
	String email[];
	int rows_num;
	
	int localId[];
	String localName[];
	int localScore[];
	int localHits[];
	String localLevel[];
	
	memberDataSql mds;
	
	//list view
	ListView list1;
	ListView list2;
	ArrayList<HashMap<String, String>> myList1;
	ArrayList<HashMap<String, String>> myList2;
	
	//Button
	Button globalBtn;
	Button localBtn;
	Button updLoclBestBtn;
	Button registerBtn;
	Button closeBtn;
	
	TextView registerDesc;
	TextView globalMyBestScore;
	
	int gb = 0;
	int lb = 0;
	
	//net 
	ConnectivityManager conManager;
	NetworkInfo networInfo;
	boolean netBoo = false;
	
	//layout
	RelativeLayout regLayout;
	
	Thread thread;
	AdView adView;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.game_rank);
		
		ActionBar ab = getActionBar();
		ab.hide();
		
		//SQLite
		helper = new NewListDataSQL(gameRank.this);
		
		//MySql
		sqlData = new getSqlData();
		sqlData.setUrl("http://poke.grtimed.com/download_rank.php");
		
		
		//System.out.println(sqlData.result());
		
		//rank list
		list1 = (ListView)findViewById(R.id.listView1);
		list2 = (ListView)findViewById(R.id.listView2);
		list2.setVisibility(View.INVISIBLE);
		
		//reg
		registerBtn = (Button)findViewById(R.id.registerBtn);
		registerDesc = (TextView)findViewById(R.id.registerDesc);
		regLayout = (RelativeLayout)findViewById(R.id.regLayout);
		closeBtn = (Button)findViewById(R.id.closeBtn);
		
		//ListView for SlidingDrawer
		myList1 = new ArrayList<HashMap<String, String>>();
		myList2 = new ArrayList<HashMap<String, String>>();
		
		//myList1.clear();
		globalMyBestScore = (TextView)findViewById(R.id.globalMyBestScore);
		
		//btn
		localBtn = (Button)findViewById(R.id.localBtn);
		localBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				switchRank(1);
			}
		});
		
		//btn
		globalBtn = (Button)findViewById(R.id.globalBtn);
		globalBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				switchRank(2);
			}
		});
		
		//updLoclBestBtn
		updLoclBestBtn = (Button)findViewById(R.id.updLoclBestBtn);
		updLoclBestBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				switchRank(3);
			}
		});
		
		
		
		//switchRank(2);
		
		//net
		conManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);//先取得此service

		networInfo = conManager.getActiveNetworkInfo();       //在取得相關資訊

		if (networInfo == null || !networInfo.isAvailable()){ //判斷是否有網路
			netBoo = false;
		}else{
			netBoo = true;
			sqlData.startThread();
		}
		
		//開啟本機記錄
		switchRank(1);
		
		//ads
		adView = new AdView(this, AdSize.BANNER, "a15147df506928f");
		RelativeLayout ads = (RelativeLayout)findViewById(R.id.adsLayout);
		ads.addView(adView);
		threadResetBtn();
		adView.loadAd(new AdRequest());
		
	}
	
	private void switchRank(int s){
						
		switch(s)
		{
			case 1:
				if(lb == 0)
				{
					lb++;
					myNote();
				}
				
				list1.setVisibility(View.VISIBLE);
				list2.setVisibility(View.INVISIBLE);
				
				updLoclBestBtn.setVisibility(View.VISIBLE);
				globalMyBestScore.setVisibility(View.INVISIBLE);
				break;
			case 2:
				updLoclBestBtn.setVisibility(View.INVISIBLE);
				globalMyBestScore.setVisibility(View.VISIBLE);
				//System.out.println(netBoo);
				if(netBoo == false)
				{
					conManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);//先取得此service
					networInfo = conManager.getActiveNetworkInfo();       //在取得相關資訊
					
					gb = 0;
					if (networInfo == null || !networInfo.isAvailable()){ //判斷是否有網路
						netBoo = false;
					}else{
						netBoo = true;
						sqlData.startThread();
					}
				}
				else
				{
					if(gb == 0)
					{
						gb++;
						addGlobalRank();
					}
					
					
					System.out.println(gb);
					
					list1.setVisibility(View.INVISIBLE);
					list2.setVisibility(View.VISIBLE);
				}
				
				
				
				break;
			case 3:
				String userName = "ghost";
				memberDataSql mds = new memberDataSql(gameRank.this);
				//
				Cursor cursor = mds.getAll("");				
				cursor.moveToFirst();
				
				if(cursor.getString(1).equals(userName))
				{
					
					System.out.println("getCount: " + cursor.getCount());
					//updateBtn.setVisibility(View.INVISIBLE);
					regLayout.setVisibility(View.VISIBLE);
					//registerBtn.setVisibility(View.VISIBLE);
					//registerDesc.setVisibility(View.VISIBLE);
					//userNmae = "ghost";
					
					registerBtn.setOnClickListener(new View.OnClickListener() {
						
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							Intent it = new Intent();
							it.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY );
							it.setClass(gameRank.this, register.class);
							gameRank.this.startActivity(it);
						}
					});
					
					closeBtn.setOnClickListener(new View.OnClickListener() {
						
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							regLayout.setVisibility(View.INVISIBLE);
						}
					});
				}
				else
				{
					updBestLocalScore();
				}
				
				
				
				break;
		}
	}
	
	private void addGlobalRank() {
		
		
			
		String str = sqlData.result();
		
		
			JSONArray arr = (JSONArray) JSONValue.parse(str);
			
			int rows_num = arr.size();
			
			id = new int[rows_num];
			name = new String[rows_num];
			score = new int[rows_num];
			hits = new int[rows_num];
			level = new String[rows_num];	
			msg = new String[rows_num];	
			globalEmail = new String[rows_num];	
			isUpdateScore = new String[rows_num];
			
			for(int n = 0; n < rows_num;n++)
			{
				JSONObject obj2=(JSONObject)arr.get(n);
				
				for(int m = 0; m < obj2.size();m++)
				{
					score[n] = Integer.parseInt(String.valueOf(obj2.get("score")));
					name[n] = String.valueOf(obj2.get("name"));
					hits[n] = Integer.parseInt(String.valueOf(obj2.get("hits")));
					level[n] = String.valueOf(obj2.get("level"));
					msg[n] = String.valueOf(obj2.get("msg"));
					isUpdateScore[n] = String.valueOf(obj2.get("isUpdateScore"));
					globalEmail[n] = String.valueOf(obj2.get("email"));
					
				}
			}
			
			for(int i = 0; i < rows_num; i++)
			{
				HashMap<String, String> map = new HashMap<String, String>(); 
				//put( list_view.xml => itemTitle, "tittle text");
				//put( list_view.xml => itemDesc, "tittle text");
				//String[] code = codeArray[i].split("::");
				map.put("itemScore", String.valueOf(score[i]));
				map.put("itemMsg", String.valueOf(msg[i]));
				map.put("itemDesc", String.valueOf("Name: " + name[i] + " Hits: " + hits[i] + " Level: " + level[i]));
				myList2.add(map);
			}
			
			SimpleAdapter sa = new SimpleAdapter(gameRank.this, 
					myList2, 
									R.layout.rank_list, 
									new String[]{"itemScore", "itemMsg", "itemDesc"}, 
									new int[] {R.id.itemScore, R.id.itemMsg,R.id.itemDesc} 
									);
			list2.setAdapter(sa);	
		
			//local member data
			mds = new memberDataSql(gameRank.this);		
			Cursor cursor = mds.getAll("");
			int num = cursor.getCount();
			String email[] = new String[num];
			int mId[] = new int[num];
			String localName[] = new String[num];
			
			//取使用者資料
			if(num != 0) {
				cursor.moveToFirst();			//將指標移至第一筆資料
				for(int i=0; i<num; i++) {
					mId[i] = cursor.getInt(0);	//取得第0欄的資料，根據欄位type使用適當語法
					localName[i] = cursor.getString(1);
					email[i] = cursor.getString(2);
					cursor.moveToNext();		//將指標移至下一筆資料
					//System.out.println(id[i] + " " + name[i] + " " + score[i] + " " + hits[i] + " " + level[i] );
				}
			}
			cursor.close();
			
			//全球最佳排名
			for(int n = 0; n < score.length;n++)
			{
				if(name[n].equals(localName[0]) && globalEmail[n].equals(email[0]))
				{
					globalMyBestScore.setText("排名 " + (n+1) + " 分數 " + score[n] + "\n" + name[n] + " | " +  hits[n] + " hits | " + level[n] + "\n" + msg[n]);
					break;
				}
				else
				{
					globalMyBestScore.setText("尚未上傳成績");
				}
				
			}
			
		
	}
	
	public void myNote(){
		
		if(helper.getAll("order by score desc") != null)
		{
			Cursor cursor = helper.getAll("order by score desc");
			int rows_num = cursor.getCount();	//取得資料表列數
			
			localId = new int[rows_num];
			localName = new String[rows_num];
			localScore = new int[rows_num];
			localHits = new int[rows_num];
			localLevel = new String[rows_num];
			//email = new String[rows_num];
			localIsUpdateScore = new String[rows_num];
			
			 //用陣列存資料
			
			String[] sNote = new String[cursor.getCount()];
							 
			if(rows_num != 0) {
				cursor.moveToFirst();			//將指標移至第一筆資料
				for(int i=0; i<rows_num; i++) {
					localId[i] = cursor.getInt(0);	//取得第0欄的資料，根據欄位type使用適當語法
					localName[i] = cursor.getString(1);
					localScore[i] = cursor.getInt(2);
					localHits[i] = cursor.getInt(3);
					localLevel[i] = cursor.getString(4);
					
					localIsUpdateScore[i] = cursor.getString(5);
	 
					cursor.moveToNext();		//將指標移至下一筆資料
					
					//System.out.println(id[i] + " " + name[i] + " " + score[i] + " " + hits[i] + " " + level[i] );
				}
			}
			cursor.close();
			
			for(int i = 0; i < localId.length; i++)
			{
				HashMap<String, String> map = new HashMap<String, String>(); 
				//put( list_view.xml => itemTitle, "tittle text");
				//put( list_view.xml => itemDesc, "tittle text");
				//String[] code = codeArray[i].split("::");
				map.put("itemScore", String.valueOf(localScore[i]));
				map.put("itemDesc", String.valueOf("Name: " + localName[i] + " Hits: " + localHits[i] + " Level: " + localLevel[i]));
				myList1.add(map);
			}
			
			SimpleAdapter lsa = new SimpleAdapter(gameRank.this, 
					myList1, 
									R.layout.rank_list, 
									new String[]{"itemScore", "itemDesc"}, 
									new int[] {R.id.itemScore, R.id.itemDesc} 
									);
			list1.setAdapter(lsa);
			
			//System.out.println("1");
		}
		else
		{
			//System.out.println("0");
		}
		
		
		//dbHelper.close();//關閉資料庫，釋放記憶體，還需使用時不要關閉
		  
		//System.out.println("num: " + rows_num);
		//return sNote;
	}
	
	public void updBestLocalScore() {
		
		if(!localIsUpdateScore[0].equals("1"))
		{
			
			
			memberDataSql mds = new memberDataSql(gameRank.this);		
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
			
			/*
			//將第一筆高分 上傳資料至遠端
			urlLoad urlload = new urlLoad();
			urlload.act = 1;
			urlload.setUrl("http://poke.grtimed.com/upd_rank.php");
			String keyAry[] = {"name", "email", "score", "hits", "level"};
			String valueAry[] = {localName[0], email[0], String.valueOf(localScore[0]), String.valueOf(localHits[0]), localLevel[0]};
			urlload.startThread(keyAry,valueAry);
			
			//isUpdateScore = 1;
			NewListDataSQL nld = new NewListDataSQL(gameRank.this);
			nld.update(localId[0], "isUpdatesScore" , "1");
			
			
			//Intent it = new Intent(getBaseContext(), gameRank.class);
			//it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
			//gameRank.this.startActivity(it);
			gameRank.this.finish();*/
			Bundle bd = new Bundle();
			bd.putString("_id", String.valueOf(localId[0]));
			bd.putString("name", localName[0]);
			bd.putString("email", email[0]);
			bd.putString("score", String.valueOf(localScore[0]));
			bd.putString("hits", String.valueOf(localHits[0]));
			bd.putString("level", String.valueOf(localLevel[0]));
			
			Intent it = new Intent(getBaseContext(), scoreUpdate.class);
			it.putExtras(bd);
			
			it.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY );
			gameRank.this.startActivity(it);
		}
		else
		{
			Toast.makeText(getBaseContext(), "最佳分數已上傳過了 <:3", Toast.LENGTH_LONG).show();
		}
		
		
	}
	
	private void threadResetBtn() {
		thread = new Thread(){
	    @Override
	    public void run() {
	        // TODO Auto-generated method stub           
	    	
	    	try {
		    		Thread.sleep(15000);
	    			Message msg = new Message();
					msg.what = 1;
					uiMessageHandler.sendMessage(msg);
				}
			catch(Exception e){
				}
			finally {
				}
		        
	    	}    
	    	
		};
		thread.start();
	}

	private Handler uiMessageHandler = new Handler()
	{
		@Override
		public void handleMessage(Message msg)
		{
			switch(msg.what)
			{
				case 1:
					System.out.println("new request");
					//adView.destroy();
					adView.loadAd(new AdRequest());
					
					threadResetBtn();
					break;
			}
		}
	};

}
