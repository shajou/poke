package com.dreamon.poke;

import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import com.google.ads.AdRequest;
import com.google.ads.AdSize;
import com.google.ads.AdView;
import com.inmobi.androidsdk.IMAdView;
import com.inmobi.commons.IMCommonUtil;
import com.inmobi.commons.IMCommonUtil.LOG_LEVEL;

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
import android.widget.RelativeLayout;

@SuppressLint("NewApi")
public class ActivityPoke extends Activity {

	
	Button startBtn;
	Button rankBtn;
	Button testBtn;
	Button setBtn;
	
	AdView adView;
	Thread thread;
	
	@SuppressWarnings("null")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.poke_main);
		
		ActionBar ab = getActionBar();
		ab.hide();
		
		/*
		Intent it = new Intent();
		it.setClass(this, gamePlay.class);
		ActivityPoke.this.startActivity(it);
		*/
		
		startBtn = (Button)findViewById(R.id.startBtn);
		rankBtn = (Button)findViewById(R.id.rankBtn);
		setBtn = (Button)findViewById(R.id.setBtn);
		testBtn = (Button)findViewById(R.id.testBtn);
		
		startBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent it = new Intent();
				it.setClass(ActivityPoke.this, gamePlay.class);
				ActivityPoke.this.startActivity(it);
			}
		});
		
		rankBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent it = new Intent();
				it.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
				it.setClass(ActivityPoke.this, gameRank.class);
				ActivityPoke.this.startActivity(it);
			}
		});
		
		testBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent it = new Intent();
				it.setClass(ActivityPoke.this, sqlTest.class);
				ActivityPoke.this.startActivity(it);
			}
		});
		
		setBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent it = new Intent();
				it.setClass(ActivityPoke.this, pokeSet.class);
				ActivityPoke.this.startActivity(it);
			}
		});
		
		//判斷是否有註冊
		String userNmae = "ghost";
		memberDataSql mds = new memberDataSql(ActivityPoke.this);
		Cursor cursor = mds.getAll("");
		if(cursor.getCount() == 0)
		{
			mds.create("ghost", "ghost@email.com");
		}
		
		//建立設定
		setDataSql set = new setDataSql(ActivityPoke.this);
		Cursor setCursor = set.getAll("");
		System.out.println(setCursor);
		if(setCursor.getCount() == 0)
		{
			//set.onCreate();
			set.create(1);
		}
		//mds.create("ghost", "ghost@email.com");
		
		
		
	//	nld.onCreate(db);
		
		
		//Ads
		//IMAdView imAdView = (IMAdView) findViewById(R.id.imAdview);
		//IMCommonUtil.setLogLevel(LOG_LEVEL.DEBUG);
		//imAdView.setRefreshInterval(IMAdView.REFRESH_INTERVAL_OFF);
		adView = new AdView(this, AdSize.BANNER, "a15147df506928f");
		RelativeLayout ads = (RelativeLayout)findViewById(R.id.adsLayout);
		ads.addView(adView);
		threadResetBtn();
		adView.loadAd(new AdRequest());
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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_poke, menu);
		return true;
	}
	
	@Override
	public void onDestroy() {
		if (adView != null) {
			adView.destroy();
		}
		super.onDestroy();
	}

}
