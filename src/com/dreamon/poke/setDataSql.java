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

	private static final int VERSION = 3;//��Ʈw����  
	private final static String name = "poke_set.db"; 
	
	private final static String _TableName = "poke_setting";
	
	private SQLiteDatabase db;
	
    //�غc�l
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
	
	// ���o�Ҧ��O��
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
	
	// ���o�@������
	public Cursor get(long rowId) throws SQLException {
		Cursor cursor = db.query(true,
		"poke_setting",				//��ƪ�W��
		new String[] {"_ID", "isChecked"},	//���W��
		"_ID=" + rowId,				//WHERE
		null, // WHERE ���Ѽ�
		null, // GROUP BY
		null, // HAVING
		null, // ORDOR BY
		null  // ����^�Ǫ�rows�ƶq
		);
 
		// �`�N�G���g�|�X��
		if (cursor != null) {
			cursor.moveToFirst();	//�N���в���Ĥ@�����
		}
		return cursor;
	}
	
	//�s�W�@���O���A���\�^��rowID�A���Ѧ^��-1
	public long create(int isChecked) {
		ContentValues args = new ContentValues();
		args.put("isChecked", isChecked);
 
		return db.insert("poke_setting", null, args);
    }
	
	//�R���O���A�^�Ǧ��\�R������
	public int delete(long rowId) {
		return db.delete("poke_setting",	//��ƪ�W��
		"_ID=" + rowId,			//WHERE
		null				//WHERE���Ѽ�
		);
	}
	
	public int deleteAll() {
		return db.delete("poke_setting",	//��ƪ�W��
		null,			//WHERE
		null				//WHERE���Ѽ�
		);
	}
	
	//�ק�O���A�^�Ǧ��\�קﵧ��
	public int update(long rowId, int isChecked) {
		ContentValues args = new ContentValues();
		args.put("isChecked", isChecked);
 
		return db.update("poke_setting",	//��ƪ�W��
		args,				//VALUE
		"_ID=" + rowId,			//WHERE
		null				//WHERE���Ѽ�
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
		   db.beginTransaction();//�إߥ��
		     
		    boolean success = false;//�P�_�Ѽ�
		        
		    //�Ѥ��e���Ϊ������A�i�����P���ʧ@     
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
		       db.setTransactionSuccessful();//���T����~���\
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
	           // TODO �C�����\���}�ƾڮw�᭺���Q����     
	       } 
	 
	@Override
	public synchronized void close() {
		super.close();
 	}

}
