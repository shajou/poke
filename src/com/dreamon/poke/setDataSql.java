package com.dreamon.poke;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class setDataSql extends SQLiteOpenHelper {

	private static final int VERSION = 3;//資料庫版本  
	private final static String name = "poke_set.db"; 
	
	private final static String _TableName = "poke_setting";
	
	private SQLiteDatabase db;
	
    //建構子
	public setDataSql(Context context) {
		super(context, name, null, VERSION);
		db = this.getWritableDatabase();
		//db.execSQL("DROP TABLE IF EXISTS poke_setting");
	}
	
	/*
	public NewListDataSQL(Context context,String name) { 
		this(context, name, null, VERSION); 
	} 
	
	public NewListDataSQL(Context context, String name, int version) {  
		this(context, name, null, version);  
	}  */
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		 String DATABASE_CREATE_TABLE =
			     "CREATE TABLE if not exists poke_setting  ("
			       + "_ID INTEGER PRIMARY KEY  AUTOINCREMENT,"
			             + "isChecked INTEGER"
			         + ")";
		 db.execSQL(DATABASE_CREATE_TABLE);

		 System.out.println("onCreate");
	}
	
	// 取得所有記錄
	public Cursor getAll(String order) {
		Cursor c;
		if(order == null)
		{
			order = "";
		}
		
		try{
			c = db.rawQuery("SELECT * FROM poke_setting " + order , null);
			return c;
		}
		catch (SQLiteException e)
		{
			c = null;
			return c;
		}
		//System.out.println(db.rawQuery("SELECT * FROM poke_member " + order , null));
		
		//System.out.println("getCount: " + c.getCount());
	   // return db.rawQuery("SELECT * FROM poke_member " + order , null);
	}
	
	// 取得一筆紀錄
	public Cursor get(long rowId) throws SQLException {
		Cursor cursor = db.query(true,
		"poke_setting",				//資料表名稱
		new String[] {"_ID", "isChecked"},	//欄位名稱
		"_ID=" + rowId,				//WHERE
		null, // WHERE 的參數
		null, // GROUP BY
		null, // HAVING
		null, // ORDOR BY
		null  // 限制回傳的rows數量
		);
 
		// 注意：不寫會出錯
		if (cursor != null) {
			cursor.moveToFirst();	//將指標移到第一筆資料
		}
		return cursor;
	}
	
	//新增一筆記錄，成功回傳rowID，失敗回傳-1
	public long create(int isChecked) {
		ContentValues args = new ContentValues();
		args.put("isChecked", isChecked);
 
		return db.insert("poke_setting", null, args);
    }
	
	//刪除記錄，回傳成功刪除筆數
	public int delete(long rowId) {
		return db.delete("poke_setting",	//資料表名稱
		"_ID=" + rowId,			//WHERE
		null				//WHERE的參數
		);
	}
	
	public int deleteAll() {
		return db.delete("poke_setting",	//資料表名稱
		null,			//WHERE
		null				//WHERE的參數
		);
	}
	
	//修改記錄，回傳成功修改筆數
	public int update(long rowId, int isChecked) {
		ContentValues args = new ContentValues();
		args.put("isChecked", isChecked);
 
		return db.update("poke_setting",	//資料表名稱
		args,				//VALUE
		"_ID=" + rowId,			//WHERE
		null				//WHERE的參數
		);
	}
	
	public void deleteTable() {
		db.execSQL("DROP TABLE IF EXISTS poke_setting");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
		System.out.println("onUpgrade");
		
		if (newVersion > oldVersion) {
		   db.beginTransaction();//建立交易
		     
		    boolean success = false;//判斷參數
		        
		    //由之前不用的版本，可做不同的動作     
		    switch (oldVersion) {
		    case 1:           
		     // db.execSQL("ALTER TABLE poke_setting ADD COLUMN reminder integer DEFAULT 0");
		     // db.execSQL("ALTER TABLE poke_setting ADD COLUMN type VARCHAR");
		     // db.execSQL("ALTER TABLE poke_setting ADD COLUMN memo VARCHAR");
		     // oldVersion++;
		             
		     success = true;
		     break;
		    }
		                
		     if (success) {
		       db.setTransactionSuccessful();//正確交易才成功
		      }
		    db.endTransaction();
		  }
		  else {
		    onCreate(db);
		  }  
		//db.execSQL("DROP TABLE IF EXISTS poke_setting");
		//onCreate(db);
	}
	
	@Override   
	public void onOpen(SQLiteDatabase db) {     
	           super.onOpen(db);       
	           // TODO 每次成功打開數據庫後首先被執行     
	       } 
	 
	@Override
	public synchronized void close() {
		super.close();
 	}

}
