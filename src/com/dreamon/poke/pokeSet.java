package com.dreamon.poke;

import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class pokeSet extends Activity {

	
	//Button startBtn;
	CheckBox gameSound;
	setDataSql set;
	int isSetSound;
	Long setSoundLongId;
	
	@SuppressLint("NewApi")
	@SuppressWarnings("null")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.poke_set);
		
		ActionBar ab = getActionBar();
		ab.hide();
		
		gameSound = (CheckBox)findViewById(R.id.gameSound);
		
		set = new setDataSql(pokeSet.this);
		Cursor cu = set.getAll("");
		
		if(cu.getCount() != 0)
		{
			cu.moveToFirst();
			setSoundLongId = cu.getLong(0);
			isSetSound = cu.getInt(1);
			
			System.out.println("rowId " + cu.getLong(0) + " " + cu.getInt(1));
		}
		
		
		
		if(isSetSound == 1)
		{
			gameSound.setChecked(true);
		}
		else
		{
			gameSound.setChecked(false);
		}
		
		
		
		gameSound.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			public void onCheckedChanged(CompoundButton buttonView,
                    boolean isChecked) {
                 // TODO Auto-generated method stub
				
				System.out.println(buttonView.isChecked());
				
            	if (buttonView.isChecked()) {
            		buttonView.setChecked(true);
            		set.update(setSoundLongId, 1);
            	}
            	else
            	{
            		buttonView.setChecked(false);
            		set.update(setSoundLongId, 0);
            	}

            }
		});
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_poke, menu);
		return true;
	}

}
