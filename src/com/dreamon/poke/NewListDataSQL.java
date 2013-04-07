package com.dreamon.poke;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class NewListDataSQL extends SQLiteOpenHelper {

	private static final int VERSION = 1;//��Ʈw����  
	private final static String name = "poke.db"; 
	
	private final static String _TableName = "poke_rank";
	
	private SQLiteDatabase db;
	
    //�غc�l
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
	
	// ���o�Ҧ��O��
	public Cursor getAll(String order) {
		if(order == null)
		{
			order = "";
		}
	    return db.rawQuery("SELECT * FROM poke_rank " + order , null);
	}
	
	// ���o�@������
	public Cursor get(long rowId) throws SQLException {
		Cursor cursor = db.query(true,
		"poke_rank",				//��ƪ�W��
		new String[] {"_ID", "name", "score", "hits", "level"},	//���W��
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
	public long create(String name, int score, int hits, String level) {
		ContentValues args = new ContentValues();
		args.put("name", name);
		args.put("score", score);
		args.put("hits", hits);
		args.put("level", level);
 
		return db.insert("poke_rank", null, args);
    }
	
	//�R���O���A�^�Ǧ��\�R������
	public int delete(long rowId) {
		return db.delete("poke_rank",	//��ƪ�W��
		"_ID=" + rowId,			//WHERE
		null				//WHERE���Ѽ�
		);
	}
	
	//�ק�O���A�^�Ǧ��\�קﵧ��
	public int update(long rowId, String value) {
		ContentValues args = new ContentValues();
		args.put("value", value);
 
		return db.update("poke_rank",	//��ƪ�W��
		args,				//VALUE
		"_ID=" + rowId,			//WHERE
		null				//WHERE���Ѽ�
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
	           // TODO �C�����\���}�ƾڮw�᭺���Q����     
	       } 
	 
	@Override
	public synchronized void close() {
		super.close();
 	}

}
