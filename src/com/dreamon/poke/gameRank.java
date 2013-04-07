package com.dreamon.poke;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Queue;
import java.lang.Iterable;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Stack;
import java.util.Timer;
import java.util.TimerTask;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.annotation.SuppressLint;
import android.app.Activity;
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
	
	int id[];
	String name[];
	int score[];
	int hits[];
	String level[];	
	
	//list view
	ListView list;
	ArrayList<HashMap<String, String>> myList;
	SimpleAdapter sa;
	
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.game_rank);
		
		//db = helper.getReadableDatabase();
		helper = new NewListDataSQL(gameRank.this);
		myNote();
		
		//rank list
		list = (ListView)findViewById(R.id.listView1);
		
		//ListView for SlidingDrawer
		myList = new ArrayList<HashMap<String, String>>();

		//String[] codeArray = getApplicationContext().getResources().getStringArray(R.array.abbreviation_array);
		
		for(int i = 0; i < id.length; i++)
		{
			HashMap<String, String> map = new HashMap<String, String>(); 
			//put( list_view.xml => itemTitle, "tittle text");
			//put( list_view.xml => itemDesc, "tittle text");
			//String[] code = codeArray[i].split("::");
			map.put("itemScore", String.valueOf(score[i]));
			map.put("itemDesc", String.valueOf("Name: " + name[i] + " Hits: " + hits[i] + " Level: " + level[i]));
			myList.add(map);
		}
		
		sa = new SimpleAdapter(gameRank.this, 
								myList, 
								R.layout.rank_list, 
								new String[]{"itemScore", "itemDesc"}, 
								new int[] {R.id.itemScore, R.id.itemDesc} 
								);
		list.setAdapter(sa);
	}
	
	public void myNote(){
		
		
		
		Cursor cursor = helper.getAll("order by score desc");
		int rows_num = cursor.getCount();	//取得資料表列數
		
		id = new int[rows_num];
		name = new String[rows_num];
		score = new int[rows_num];
		hits = new int[rows_num];
		level = new String[rows_num];	
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
 
				cursor.moveToNext();		//將指標移至下一筆資料
				
				//System.out.println(id[i] + " " + name[i] + " " + score[i] + " " + hits[i] + " " + level[i] );
			}
		}
		cursor.close();
		//dbHelper.close();//關閉資料庫，釋放記憶體，還需使用時不要關閉
		  
		//System.out.println("num: " + rows_num);
		//return sNote;
	}

}
