package com.dreamon.poke;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.ads.AdRequest;
import com.google.ads.AdSize;
import com.google.ads.AdView;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("NewApi")
public class ActivityPoke extends Activity {

	
	Button startBtn;
	Button rankBtn;
	Button testBtn;
	Button setBtn;
	Button aboutBtn;
	ImageView logo;
	TextView secretLogoText;
	
	AdView adView;
	Thread thread;
	
	int gameAniStatu = 0;
	DisplayMetrics dm;
	int vW = 0;
	
	int secretLogo = 0;
	Bundle bd;
	
	@SuppressWarnings("null")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.poke_main);
		
		ActionBar ab = getActionBar();
		ab.hide();
		
		bd = new Bundle();
		
		dm = new DisplayMetrics();
		this.getWindowManager().getDefaultDisplay().getMetrics(dm);
		vW = dm.widthPixels;
		//vH = dm.heightPixels - 40;
		
		/*
		Intent it = new Intent();
		it.setClass(this, gamePlay.class);
		ActivityPoke.this.startActivity(it);
		*/
		
		startBtn = (Button)findViewById(R.id.startBtn);
		rankBtn = (Button)findViewById(R.id.rankBtn);
		setBtn = (Button)findViewById(R.id.setBtn);
		testBtn = (Button)findViewById(R.id.testBtn);
		aboutBtn = (Button)findViewById(R.id.aboutBtn);
		logo = (ImageView)findViewById(R.id.logo);
		secretLogoText = (TextView)findViewById(R.id.secretLogoText);
		
		startBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent it = new Intent();
				it.setClass(ActivityPoke.this, gamePlay.class);
				
				it.putExtras(bd);
				ActivityPoke.this.startActivity(it);
				bd.putString("secretLogo", "0");
			
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
		
		aboutBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent it = new Intent();
				it.setClass(ActivityPoke.this, about.class);
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
		
		logo.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//Intent it = new Intent();
				//it.setClass(ActivityPoke.this, pokeSet.class);
				//ActivityPoke.this.startActivity(it);
				if(secretLogo == 3)
				{
					
					bd.putString("secretLogo", "1");
					secretLogo = 0;
					
					ScaleAnimation scaleAnime;
					AlphaAnimation AlphaAnime;
					AnimationSet as = new AnimationSet(false);
					AlphaAnime = new AlphaAnimation(0, 1);
					AlphaAnime.setDuration(2000);
					scaleAnime = new ScaleAnimation(0,2, 0, 2,Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
					scaleAnime.setDuration(2000);
					
					as.addAnimation(AlphaAnime);
					as.addAnimation(scaleAnime);
					secretLogoText.setAnimation(as);
					secretLogoText.setVisibility(View.VISIBLE);
					as.start();
					
					secretLogoAnimation();
				}
				else
				{
					secretLogo++;
				}
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
		
		
		//animation
		gameAnimation();
		
		//Ads
		//IMAdView imAdView = (IMAdView) findViewById(R.id.imAdview);
		//IMCommonUtil.setLogLevel(LOG_LEVEL.DEBUG);
		//imAdView.setRefreshInterval(IMAdView.REFRESH_INTERVAL_OFF);
		adView = new AdView(this, AdSize.BANNER, "a1518a249a59e00");
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
	
	private void gameAnimation() {
		Thread gameAnimate = new Thread(){
	    @Override
	    public void run() {
	        // TODO Auto-generated method stub 
	    	try {	    	   
		    		Thread.sleep(500);
					Message msg = new Message();
					msg.what = 2;
					uiMessageHandler.sendMessage(msg);
				}
			catch(Exception e){
				}
			finally {
				}
	    			        
	    	}    
		};
		gameAnimate.start();
	}
	
	private void secretLogoAnimation() {
		Thread secret = new Thread(){
	    @Override
	    public void run() {
	        // TODO Auto-generated method stub 
	    	try {	    	   
		    		Thread.sleep(2000);
					Message msg = new Message();
					msg.what = 3;
					uiMessageHandler.sendMessage(msg);
				}
			catch(Exception e){
				}
			finally {
				}
	    			        
	    	}    
		};
		secret.start();
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
				case 2:
					float x2 = (vW / 2);
					Animation am = new TranslateAnimation( vW, 0 , 0, 0);
					//AlphaAnimation alpha = new AlphaAnimation(0, 1);
					am.setDuration(1000);
					
					
					switch(gameAniStatu)
					{
						case 0:
							//gameOverScore.setAnimation(alpha);
							startBtn.setVisibility(View.VISIBLE);
							startBtn.setAnimation(am);
							am.startNow();
							gameAnimation();
							break;
						case 1:
							rankBtn.setVisibility(View.VISIBLE);
							rankBtn.setAnimation(am);
							am.startNow();
							gameAnimation();
							break;
						case 2:
							setBtn.setVisibility(View.VISIBLE);
							setBtn.setAnimation(am);
							am.startNow();
							gameAnimation();
							break;
						case 3:
							aboutBtn.setVisibility(View.VISIBLE);
							aboutBtn.setAnimation(am);
							am.startNow();
							gameAniStatu = 0;
							break;
						
						default:
							
							break;
					}
					
					gameAniStatu++;
					
					break;
				case 3: // secret
					secretLogoText.setVisibility(View.INVISIBLE);
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
