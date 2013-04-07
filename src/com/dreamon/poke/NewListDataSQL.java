package com.dreamon.poke;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class NewListDataSQL extends SQLiteOpenHelper {

	private static final int VERSION = 1;//資料庫版本  
	private final static String name = "poke.db"; 
	
	private final static String _TableName = "poke_rank";
	
	private SQLiteDatabase db;
	
    //建構子
	public NewListDataSQL(Context context) {
		super(context, name, null, VERSION);
		db = this.getWritableDatabase();
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
			     "create table poke_rank  ("
			       + "_ID INTEGER PRIMARY KEY  AUTOINCREMENT,"
			             + "name TEXT,"
			             + "score INTEGER,"
			             + "hits INTEGER,"
			             + "level TEXT"
			         + ")";
		 db.execSQL(DATABASE_CREATE_TABLE);
	}
	
	// 取得所有記錄
	public Cursor getAll(String order) {
		if(order == null)
		{
			order = "";
		}
	    return db.rawQuery("SELECT * FROM poke_rank " + order , null);
	}
	
	// 取得一筆紀錄
	public Cursor get(long rowId) throws SQLException {
		Cursor cursor = db.query(true,
		"poke_rank",				//資料表名稱
		new String[] {"_ID", "name", "score", "hits", "level"},	//欄位名稱
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
	public long create(String name, int score, int hits, String level) {
		ContentValues args = new ContentValues();
		args.put("name", name);
		args.put("score", score);
		args.put("hits", hits);
		args.put("level", level);
 
		return db.insert("poke_rank", null, args);
    }
	
	//刪除記錄，回傳成功刪除筆數
	public int delete(long rowId) {
		return db.delete("poke_rank",	//資料表名稱
		"_ID=" + rowId,			//WHERE
		null				//WHERE的參數
		);
	}
	
	//修改記錄，回傳成功修改筆數
	public int update(long rowId, String value) {
		ContentValues args = new ContentValues();
		args.put("value", value);
 
		return db.update("poke_rank",	//資料表名稱
		args,				//VALUE
		"_ID=" + rowId,			//WHERE
		null				//WHERE的參數
		);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		db.execSQL("DROP TABLE IF EXISTS poke_rank");
		onCreate(db);
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
