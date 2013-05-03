package com.dreamon.poke;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class memberDataSql extends SQLiteOpenHelper {

	private static final int VERSION = 1;//資料庫版本  
	private final static String name = "poke_member.db"; 
	
	private final static String _TableName = "poke_member";
	
	private SQLiteDatabase db;
	
    //建構子
	public memberDataSql(Context context) {
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
			     "create table if not exists poke_member  ("
			       + "_ID INTEGER PRIMARY KEY  AUTOINCREMENT,"
			             + "name TEXT,"
			             + "email TEXT"
			         + ")";
		 db.execSQL(DATABASE_CREATE_TABLE);


	}
	
	// 取得所有記錄
	public Cursor getAll(String order) {
		Cursor c;
		if(order == null)
		{
			order = "";
		}
		
		try{
			c = db.rawQuery("SELECT * FROM poke_member " + order , null);
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
		"poke_member",				//資料表名稱
		new String[] {"_ID", "name", "email"},	//欄位名稱
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
	public long create(String name, String email) {
		ContentValues args = new ContentValues();
		args.put("name", name);
		args.put("email", email);
 
		return db.insert("poke_member", null, args);
    }
	
	//刪除記錄，回傳成功刪除筆數
	public int delete(long rowId) {
		return db.delete("poke_member",	//資料表名稱
		"_ID=" + rowId,			//WHERE
		null				//WHERE的參數
		);
	}
	
	public int deleteAll() {
		return db.delete("poke_member",	//資料表名稱
		null,			//WHERE
		null				//WHERE的參數
		);
	}
	
	//修改記錄，回傳成功修改筆數
	public int update(long rowId, String name, String email) {
		ContentValues args = new ContentValues();
		args.put("name", name);
		args.put("email", email);
 
		return db.update("poke_member",	//資料表名稱
		args,				//VALUE
		"_ID=" + rowId,			//WHERE
		null				//WHERE的參數
		);
	}
	
	public void deleteTable() {
		db.execSQL("DROP TABLE IF EXISTS poke_member");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		db.execSQL("DROP TABLE IF EXISTS poke_member");
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
