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

	private static final int VERSION = 1;//��Ʈw����  
	private final static String name = "poke_member.db"; 
	
	private final static String _TableName = "poke_member";
	
	private SQLiteDatabase db;
	
    //�غc�l
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
	
	// ���o�Ҧ��O��
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
	
	// ���o�@������
	public Cursor get(long rowId) throws SQLException {
		Cursor cursor = db.query(true,
		"poke_member",				//��ƪ�W��
		new String[] {"_ID", "name", "email"},	//���W��
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
	public long create(String name, String email) {
		ContentValues args = new ContentValues();
		args.put("name", name);
		args.put("email", email);
 
		return db.insert("poke_member", null, args);
    }
	
	//�R���O���A�^�Ǧ��\�R������
	public int delete(long rowId) {
		return db.delete("poke_member",	//��ƪ�W��
		"_ID=" + rowId,			//WHERE
		null				//WHERE���Ѽ�
		);
	}
	
	public int deleteAll() {
		return db.delete("poke_member",	//��ƪ�W��
		null,			//WHERE
		null				//WHERE���Ѽ�
		);
	}
	
	//�ק�O���A�^�Ǧ��\�קﵧ��
	public int update(long rowId, String name, String email) {
		ContentValues args = new ContentValues();
		args.put("name", name);
		args.put("email", email);
 
		return db.update("poke_member",	//��ƪ�W��
		args,				//VALUE
		"_ID=" + rowId,			//WHERE
		null				//WHERE���Ѽ�
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
	           // TODO �C�����\���}�ƾڮw�᭺���Q����     
	       } 
	 
	@Override
	public synchronized void close() {
		super.close();
 	}

}
