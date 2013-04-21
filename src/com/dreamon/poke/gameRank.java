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
import android.app.Activity;
import android.app.AlertDialog;
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
public class gameRank extends Activity {
	
	//SQLite
	NewListDataSQL helper;
	
	//PHP Mysql
	getSqlData sqlData;
	
	int id[];
	String name[];
	int score[];
	int hits[];
	String level[];	
	String isUpdateScore[];
	String email[];
	int rows_num;
	
	
	
	//list view
	ListView list1;
	ListView list2;
	ArrayList<HashMap<String, String>> myList1;
	ArrayList<HashMap<String, String>> myList2;
	
	//Button
	Button globalBtn;
	Button localBtn;
	Button updLoclBestBtn;
	int gb = 0;
	int lb = 0;
	
	//net 
	ConnectivityManager conManager;
	NetworkInfo networInfo;
	boolean netBoo = false;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.game_rank);
		
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
		
		//ListView for SlidingDrawer
		myList1 = new ArrayList<HashMap<String, String>>();
		myList2 = new ArrayList<HashMap<String, String>>();
		
		//myList1.clear();
		
		
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
				break;
			case 2:
				
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
					isUpdateScore[n] = String.valueOf(obj2.get("isUpdateScore"));
				}
			}
			
			for(int i = 0; i < rows_num; i++)
			{
				HashMap<String, String> map = new HashMap<String, String>(); 
				//put( list_view.xml => itemTitle, "tittle text");
				//put( list_view.xml => itemDesc, "tittle text");
				//String[] code = codeArray[i].split("::");
				map.put("itemScore", String.valueOf(score[i]));
				map.put("itemDesc", String.valueOf("Name: " + name[i] + " Hits: " + hits[i] + " Level: " + level[i]) + " isUpdate: " + isUpdateScore[i]);
				myList2.add(map);
			}
			
			SimpleAdapter sa = new SimpleAdapter(gameRank.this, 
					myList2, 
									R.layout.rank_list, 
									new String[]{"itemScore", "itemDesc"}, 
									new int[] {R.id.itemScore, R.id.itemDesc} 
									);
			list2.setAdapter(sa);	
		
			
		
	}
	
	public void myNote(){
		
		if(helper.getAll("order by score desc") != null)
		{
			Cursor cursor = helper.getAll("order by score desc");
			int rows_num = cursor.getCount();	//取得資料表列數
			
			id = new int[rows_num];
			name = new String[rows_num];
			score = new int[rows_num];
			hits = new int[rows_num];
			level = new String[rows_num];
			//email = new String[rows_num];
			isUpdateScore = new String[rows_num];
			
			 //用陣列存資料
			
			String[] sNote = new String[cursor.getCount()];
							 
			if(rows_num != 0) {
				cursor.moveToFirst();			//將指標移至第一筆資料
				for(int i=0; i<rows_num; i++) {
					id[i] = cursor.getInt(0);	//取得第0欄的資料，根據欄位type使用適當語法
					name[i] = cursor.getString(1);
					score[i] = cursor.getInt(2);
					hits[i] = cursor.getInt(3);
					level[i] = cursor.getString(4);
					
					isUpdateScore[i] = cursor.getString(5);
	 
					cursor.moveToNext();		//將指標移至下一筆資料
					
					//System.out.println(id[i] + " " + name[i] + " " + score[i] + " " + hits[i] + " " + level[i] );
				}
			}
			cursor.close();
			
			for(int i = 0; i < id.length; i++)
			{
				HashMap<String, String> map = new HashMap<String, String>(); 
				//put( list_view.xml => itemTitle, "tittle text");
				//put( list_view.xml => itemDesc, "tittle text");
				//String[] code = codeArray[i].split("::");
				map.put("itemScore", String.valueOf(score[i]));
				map.put("itemDesc", String.valueOf("Name: " + name[i] + " Hits: " + hits[i] + " Level: " + level[i] + " isUpdateScore: " + isUpdateScore[i]));
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
		
		if(isUpdateScore[0] != "1")
		{
			//判斷是否有註冊
			
			memberDataSql mds = new memberDataSql(gameRank.this);		
			Cursor cursor = mds.getAll("");
			int rows_num = cursor.getCount();
			email = new String[rows_num];
			int localId[] = new int[rows_num];
			String localName[] = new String[rows_num];
			
			//取使用者資料
			if(rows_num != 0) {
				cursor.moveToFirst();			//將指標移至第一筆資料
				for(int i=0; i<rows_num; i++) {
					localId[i] = cursor.getInt(0);	//取得第0欄的資料，根據欄位type使用適當語法
					localName[i] = cursor.getString(1);
					email[i] = cursor.getString(2);
					cursor.moveToNext();		//將指標移至下一筆資料
					//System.out.println(id[i] + " " + name[i] + " " + score[i] + " " + hits[i] + " " + level[i] );
				}
			}
			cursor.close();
			
			//將第一筆高分 上傳資料至遠端
			urlLoad urlload = new urlLoad();
			urlload.setUrl("http://poke.grtimed.com/upd_rank.php");
			String keyAry[] = {"name", "email", "score", "hits", "level"};
			String valueAry[] = {localName[0], email[0], String.valueOf(score[0]), String.valueOf(hits[0]), level[0]};
			urlload.startThread(keyAry,valueAry);
			
			//isUpdateScore = 1;
			NewListDataSQL nld = new NewListDataSQL(gameRank.this);
			nld.update(id[0], "isUpdatesScore" , String.valueOf(isUpdateScore));
		}
		
		
	}

}
