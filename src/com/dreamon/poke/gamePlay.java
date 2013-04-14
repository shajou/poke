package com.dreamon.poke;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Queue;
import java.lang.Iterable;
import java.util.LinkedList;
import java.util.Stack;
import java.util.Timer;
import java.util.TimerTask;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.SparseArray;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.AbsoluteLayout;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

@SuppressWarnings("rawtypes")
@SuppressLint("NewApi")
public class gamePlay extends Activity {
	
	//螢幕寬高
	DisplayMetrics dm;
	int vH = 0;
	int vW = 0;
	
	//gameStatu
	int gameStart = 0;
	int gamePlay = 1;
	int gamePause = 2;
	int gameResume = 3;
	int gameOver = 4;
	int gameStatus;
	
	//場景佈置
	RelativeLayout gpl;
	RelativeLayout gsl;
	RelativeLayout gPauselayout;
	RelativeLayout gOverlayout;
	
	//線程
	private Thread thread = null;
	private int finish = 1;
	int tBaseTime = 1;
	int tMaxTime = 10;
	boolean threadPause = false;
	
	Thread icdThread = null;
	Thread itemThread = null;
	
	//計分
	TextView score;
	Timer timer;
	Boolean comboCheck = false;
	int timeFps = 100;
	int timeComboRage = 5;
	int c = 0;
	int scorePlus = 1;
	
	//連技計分
	TextView combo;
	TextView comboPlusShow;
	int comboPlus = 0;
	int regComboScore = 0;
	
	//連技標語
	TextView itemComboDesc;
	String itemComboDescStr;
	
	//動畫相關
	ScaleAnimation scaleAnime;
	ScaleAnimation cpsScaleAnime;
	AlphaAnimation cpsAlphaAnime;
	AnimationSet aSet;
	//im
	ScaleAnimation imScaleAnime;
	AlphaAnimation imAlphaAnime;
	AnimationSet imaSet;
	//ics
	AnimationSet icdAs;
	ScaleAnimation icdScale;
	AlphaAnimation icdAlpha;
	int icdDur = 1000;
	
	float sfX = 1.2f;
	float stX = 1.0f;
	float sfY = 1.2f;
	float stY = 1.0f;
	int dur = 500;
	
	//場景按鈕
	int btnCount = 30;
	ImageButton bub;
	int moveX = 100;
	Button pauseBtn;
	Button resumeBtn;
	
	//佇列存放
	ArrayList<View> st = new ArrayList<View>();
	int stStatu[] = new int[30];
	
	//遊戲計時
	Timer gTimer;
	int gameTimeHandler = 2;
	private Thread gTimeThread = null;
	int gTimeOut = 1000;
	String gTime = "1";
	public static TextView gTimeText;
	
	//功能按鈕
	Button restartBtn;
	
	//遊戲結束
	String levelStr;
	String name[];
	String email[];
	Button homeBtn;
	TextView gameOverScore;
	TextView gameOverCombo;
	TextView level;
	public static Button updateBtn;
	Button registerBtn; 
	TextView registerDesc;
	
	//道具
	TextView itemMsg;
	gameItem gameitem;
	int itemScorePlus = 1;
	int itemRandom = 15;
	
	//音效播放
	SoundPool sp;
	int sound;
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.game_play);
		
		//道具
		gameitem = new gameItem();
		itemMsg = (TextView)findViewById(R.id.itemMsg);
		
		//音效
		//mp = new MediaPlayer();
		//sp = new SoundPool();
		sp = new SoundPool( 30, AudioManager.STREAM_MUSIC, 0);
		sound = sp.load(this, R.raw.slap, 0);
		
		
		//連技標語
		itemComboDesc = (TextView)findViewById(R.id.itemComboDesc);
		
		//場景佈置		
		gpl = (RelativeLayout)findViewById(R.id.gamePlayLayout);
		gsl = (RelativeLayout)findViewById(R.id.gameStatuLayout);
		gPauselayout = (RelativeLayout)findViewById(R.id.gamePauseLayout);
		gOverlayout = (RelativeLayout)findViewById(R.id.gameOverLayout);
		score = (TextView)findViewById(R.id.score);
		restartBtn = (Button)findViewById(R.id.restartBtn);
		//連技計分
		combo = (TextView)findViewById(R.id.combo);
		comboPlusShow = (TextView)findViewById(R.id.comboPlus);
		//按鈕監聽
		pauseBtn = (Button)findViewById(R.id.pauseBtn);
		resumeBtn = (Button)findViewById(R.id.resumeBtn);
		//倒數計時開始
		gTimeText = (TextView)findViewById(R.id.timeout);
		//遊戲結束
		
		homeBtn = (Button)findViewById(R.id.homeBtn);
		gameOverScore = (TextView)findViewById(R.id.gameOverScore);
		gameOverCombo = (TextView)findViewById(R.id.gameOverCombo);
		level = (TextView)findViewById(R.id.level);
		updateBtn = (Button)findViewById(R.id.updateBtn);
		registerBtn = (Button)findViewById(R.id.registerBtn);
		registerDesc = (TextView)findViewById(R.id.registerDesc);
		
		
		gameInit();
		
	}
	
	public void gameInit() {
		
		//取得螢幕寬高
		dm = new DisplayMetrics();
		this.getWindowManager().getDefaultDisplay().getMetrics(dm);
		vW = dm.widthPixels;
		vH = dm.heightPixels - 40;
		
		System.out.println( "w:" + vW + " h:" + vH );
		
		gPauselayout.setVisibility(View.INVISIBLE);
		score.setText("0");
		combo.setTextColor(Color.argb(220, 255, 255, 255));
		comboPlusShow.setTextColor(Color.argb(220, 255, 255, 255));
		
		combo.setText("0");
		comboPlusShow.setText("0");
		
		//動畫初始
		scaleAnime = new ScaleAnimation(sfX, stX, sfY, stY);
		scaleAnime.setDuration(dur);
		
		cpsScaleAnime = new ScaleAnimation(sfX, stX, sfY, stY);
		cpsScaleAnime.setDuration(dur);
		
		cpsAlphaAnime = new AlphaAnimation(1, 0);
		cpsAlphaAnime.setDuration(dur);
		
		//icd
		icdAs = new AnimationSet(false);
		icdScale = new ScaleAnimation(sfX, stX, sfY, stY);
		icdAlpha = new AlphaAnimation(1,0);
		icdScale.setDuration(icdDur);
		icdAlpha.setDuration(icdDur);
		icdAs.addAnimation(icdScale);
		icdAs.addAnimation(icdAlpha);
		
		
		
		aSet = new AnimationSet(false);
		aSet.addAnimation(cpsScaleAnime);
		aSet.addAnimation(cpsAlphaAnime);
		
		//按鈕位置設置
		
		int x = ((vW / 5) - (int)(vW / 1.5)) / 2;
		
		int xPaddig = (int)(vW / 5);
		int yPadding = (int)(vH / 6);
		//int marginBottom = xyPaddig;
		int gsl_h = (int)(vH / 60) * 4;
		int y = 0;
		int yMargin = (int)(yPadding / 1.5 ) / 4; //18
		
		//combo 計算 有效時間為0.5秒
		comboTime();
		
		//重置泡泡 每1秒檢查一次
		threadResetBtn();
		
		//創建場景上按鈕
		for(int i = 0; i < btnCount ; i++) {
			
			bub = new ImageButton(this);
			
								
			if( i % 5 == 0)
			{
				y = (i * yMargin) + gsl_h;
				x = ((vW / 5) - (int)((vW / 5) / 1.5)) / 2;
			}			
			
			RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams((int)((vW / 5) / 1.5) , (int)((vW / 5) / 1.5) );  
			lp.setMargins(x , y, 0, 0); 
			bub.setLayoutParams(lp);  
			bub.setBackgroundResource(R.drawable.poke_icon);
			//id用來決定存活幾秒後恢復
			bub.setId(0);
			
			gpl.addView(bub);
			st.add(bub);
			
			x = x + vW / 5;
			
			bub.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					
					//音效
					pokeSoundEffect();
					
					//分數計算
					String str = score.getText().toString();
					int s = Integer.parseInt(str);
					
					//連技越多 加成分數越多
					//comboPlus = 0;
					
					if(scorePlus >= 30 && scorePlus < 50)
					{
						comboPlusShow.setAnimation(cpsScaleAnime);
						comboPlus = 100;
						
						
					}
					else if(scorePlus >= 50 && scorePlus < 70)
					{
						comboPlus = 200;
						
					}
					else if(scorePlus >= 70 && scorePlus < 90)
					{
						comboPlus = 300;
						
					}
					else if(scorePlus >= 90 && scorePlus < 110)
					{
						comboPlus = 400;
						
					}
					else if(scorePlus >= 110 && scorePlus < 130)
					{
						comboPlus = 550;
					}
					else if(scorePlus >= 130 && scorePlus < 150)
					{
						comboPlus = 700;
					}
					else if(scorePlus >= 150 && scorePlus < 170)
					{
						comboPlus = 900;
					}
					else if(scorePlus >= 170 && scorePlus < 190)
					{
						comboPlus = 1100;
					}
					else if(scorePlus >= 190 && scorePlus < 210)
					{
						comboPlus = 1400;
					}
					else if(scorePlus >= 210 && scorePlus < 230)
					{
						comboPlus = 1700;
						
					}
					else if(scorePlus >= 230 && scorePlus < 250)
					{
						comboPlus = 2000;
						
					}
					else if(scorePlus >= 250)
					{
						comboPlus = 3000;
						
					}
					
					//大於30後每次加成就跳一次動畫
					if(scorePlus >= 30)
					{
						comboPlusShow.setVisibility(View.VISIBLE);
						//comboPlusShow.setAnimation(aSet);
						comboPlusShow.startAnimation(aSet);
					}
					
					//連技加成顯示
					comboPlusShow.setText("+ " + String.valueOf(comboPlus ) + " !!" );
					
					//分數計算
					int p = s + 1 + scorePlus + (comboPlus * itemScorePlus);
					score.setText( String.valueOf(p) );
					
					//有戳下去就將c = 0; 讓有效combo 持續
					c = 0;
					
					//分數加成
					scorePlus++;
					
					//連技數顯示
					combo.setText( String.valueOf(scorePlus) + " hits!!");
					//大於十次連技才顯示
					if( scorePlus >= 10 ) 
					{
						combo.setAnimation(scaleAnime);
						combo.setVisibility(View.VISIBLE);
					}
					else
					{
						combo.setVisibility(View.INVISIBLE);
					}
					
					
					//戳下去時決定多久回復
					int recoverTime = (int)(Math.random() * tMaxTime) + tBaseTime;
					v.setId(recoverTime);
					
					//將v 加入佇列
					//st.add(v);
										
					v.setBackgroundResource(R.drawable.poke_press_icon);
					v.setClickable(false);					
					
					//記錄最大hits
					if( regComboScore < scorePlus )
					{
						regComboScore = scorePlus;
					}
					
					//道具效果
										
					//stStatu 記錄目前的btn是哪個 相等後再隨機決定是否要變為道具
					for(int i = 0; i < st.size(); i++)
					{
						
						
						if(st.get(i).equals(v))
						{
							
							//消耗道具
							switch(stStatu[i])
							{
								case 1:
									gTimeText.setText( 
											String.valueOf(
												Integer.parseInt(
													String.valueOf(
															gTimeText.getText()
															)
														) + 3
												)
											);
									itemMsgBox( "Time  +3 !!" );
									break;
								case 2:
									itemMsgBox( "Extra  X 2 !!" );
									itemScoreDouble();
									
								default:
									break;
							}
							
							
							//決定下一輪是否有道具
							if( 0 + (int)(Math.random() * itemRandom) < 1)
							{
								
								stStatu[i] = 1 + (int)(Math.random() * 2);
								//stStatu[i] = 2;
							}
							else
							{
								stStatu[i] = 0;
							}
						}
					}
					
					//連技標語
			    	
					if(scorePlus == 30)
					{
						itemComboDescStr = "不錯的連技";
						itemComboDescMsgBox(itemComboDescStr);
					} 
					else if(scorePlus == 70)
					{
						itemComboDescStr = "漂亮的連技";
						itemComboDescMsgBox(itemComboDescStr);
					}
					else if(scorePlus == 110)
					{
						itemComboDescStr = "你太瘋狂了";
						itemComboDescMsgBox(itemComboDescStr);
					}
					else if(scorePlus == 150)
					{
						itemComboDescStr = "有可能嗎!!";
						itemComboDescMsgBox(itemComboDescStr);
					}
					else if(scorePlus == 190)
					{
						itemComboDescStr = "這不科學!!";
						itemComboDescMsgBox(itemComboDescStr);
					}
					else if(scorePlus == 220)
					{
						itemComboDescStr = "已經沒有人能阻止你啦!!";
						itemComboDescMsgBox(itemComboDescStr);
					}
					else if(scorePlus == 250)
					{
						itemComboDescStr = "娘子快來見上帝";
						itemComboDescMsgBox(itemComboDescStr);
					}
					else if(scorePlus == 280)
					{
						itemComboDescStr = "神之領域";
						itemComboDescMsgBox(itemComboDescStr);
					}
					
					
				}
			});
			
			
		}
		
		//將狀態列的layout置前 使得hit及combo能在btn之前
		gsl.bringToFront();
				
		//暫停
		pauseBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				gamePause();
				thread.interrupt();
			}
		});
		
		//繼續
		resumeBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				gameResume();
			}
		});
		
		//重新開始
		
		restartBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				gameRestart();
			}
		});
		
		//倒數計時開始
		
		gTimeText.setText(gTime);
		threadGameTime();
	}
	
	
	private void threadResetBtn() {
		thread = new Thread(){
	    @Override
	    public void run() {
	    	while(true) {
	        // TODO Auto-generated method stub           
	    	
		    	try {
	        	   
			    		//設定多久後變回原樣
			    		//int time = tBaseTime + (int)(  Math.random() * tMaxTime );
		    	   
			    		//System.out.println(time);
			    		
			    		//每1秒檢查一次
			    		Thread.sleep(1000);
			    		if( !threadPause )
			    		{
			    			
			    			Message msg = new Message();
							msg.what = finish;
							uiMessageHandler.sendMessage(msg);
							
			    		}
			    		
		    		
					}
				catch(Exception e){
				    //  e.printStackTrace();
				    //結束線程
				    	//thread.interrupt();
					}
				finally {
				    	//System.out.println("finally");
						//thread.interrupt();
					}
			        
		    	}    
	    	}
		};
		thread.start();
	}
	
	
	
	private void threadGameTime() {
		gTimeThread = new Thread(){
	    @Override
	    public void run() {
	        // TODO Auto-generated method stub 
	    	try {
        	   	    		    	   
		    		Thread.sleep(gTimeOut);
					Message msg = new Message();
					msg.what = gameTimeHandler;
					uiMessageHandler.sendMessage(msg);
				        
				}
			catch(Exception e){
			    //  e.printStackTrace();
			    //結束線程
			    	//thread.interrupt();
				}
			finally {
			    	//System.out.println("finally");
					//thread.interrupt();
				}
	    			        
	    	}    
		};
		gTimeThread.start();
	}
	
	
	
	private Handler uiMessageHandler = new Handler()
	{
		@Override
		public void handleMessage(Message msg)
		{
			switch(msg.what)
			{
				case 1:
					//System.out.println("handler");
					
					//被按下後恢復按鈕
					for(int c =0 ; c < st.size() ;c++)
					{
						View v = (View)st.get(c);
						//stStatu[c] = 0;
						//System.out.println(String.valueOf( v.getId() ));
						if( v.getId() > 0)
						{
							v.setId( v.getId() - 1);
						}
						else
						{
							switch(stStatu[c])
							{
								case 1:
									v.setBackgroundResource(R.drawable.item_time);
									break;
								case 2:
									v.setBackgroundResource(R.drawable.item_double);
									break;
								default:
									v.setBackgroundResource(R.drawable.poke_icon);
									break;
							}
							
							v.setClickable(true);
						}
					}
					
					//當combo 小於10時
					if( scorePlus < 10 ) 
					{
						combo.setVisibility(View.INVISIBLE);
					}
					
					//連技小於30時
			    	if(scorePlus < 30)
					{
						comboPlus = 0;
						comboPlusShow.setVisibility(View.INVISIBLE);
					}
			    	
						
					break;
				case 2:
					//game play time
					gamePlayTime();
					
					break;
				case 3:
					;
					break;
			}
		}
	};
	
	
	/*
	 * combo 判斷
	 */
	//combo有效秒數判斷
	
	private void comboTime() {
		timer = new Timer();
		//每0.1秒跑一次
		timer.schedule(new checkTask() , timeFps);
		c = 0;
	}
	
	class checkTask extends TimerTask {
	    public void run() {
	    	//連技判斷
	    	//大於等於0.5秒時 
	    	if(c >= timeComboRage)
	    	{
	    		scorePlus = 0;
	    		c = 0;
	    	}
	    	
	    	c++;
	    		
	    	
	    	//System.out.println( "check task" );
	    	timer.schedule(new checkTask() , timeFps);
	    }
	}
	
	//遊戲時間倒數
	private void gamePlayTime() {
		int gt = Integer.parseInt( String.valueOf( gTimeText.getText() ) );
		
		if( gt > 0 )
		{
			gt = gt - 1;
			gTimeText.setText( String.valueOf(gt) );
			threadGameTime();
		}
		else
		{
			gameOver();
		}
		
		
	}
	
	private void gameRestart() {
		//gamePlay.this.startActivities(new Intent().setClass());
		//timer
		
		this.finish();
	}
	
	private void gamePause() {
		if(gameStatus != 4)
		{
			gPauselayout.setVisibility(View.VISIBLE);
			gPauselayout.bringToFront();
			
			timer.cancel();
			gTimeThread.interrupt();
			threadPause = true;
			
			for(int c =0 ; c < st.size();c++)
			{
				View v = (View)st.get(c);
				v.setClickable(false);
				
			}
		}
		
	}
	
	private void gameResume() {
		if(gameStatus != 4)
		{
			comboTime();
			threadGameTime();
			threadPause = false;
			
			gPauselayout.setVisibility(View.INVISIBLE);
			
			for(int c =0 ; c < st.size();c++)
			{
				View v = (View)st.get(c);
				v.setClickable(true);
				
			}
		}
		
		
	}
	
	private void gameOver() {
		gameStatus = gameOver;
		
		timer.cancel();
		gTimeThread.interrupt();
		threadPause = true;
		
		for(int c =0 ; c < st.size();c++)
		{
			View v = (View)st.get(c);
			v.setClickable(false);
			
		}
		
		gPauselayout.setVisibility(View.INVISIBLE);
		gOverlayout.setVisibility(View.VISIBLE);
		gOverlayout.bringToFront();
		
		homeBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				gameRestart();
			}
		});
		
		int scoreInt = Integer.parseInt( String.valueOf(score.getText()) );
		levelStr = "";
		if( scoreInt < 30000 )
		{
			levelStr = "F";
		}
		else if( scoreInt >= 30000 && scoreInt < 60000 )
		{
			levelStr = "E";
		}
		else if( scoreInt >= 60000 && scoreInt < 90000 )
		{
			levelStr = "D";
		}
		else if( scoreInt >= 90000 && scoreInt < 120000 )
		{
			levelStr = "C";
		}
		else if( scoreInt >= 120000 && scoreInt < 150000 )
		{
			levelStr = "B";
		}
		else if( scoreInt >= 150000 && scoreInt < 180000 )
		{
			levelStr = "A";
		}
		else if( scoreInt >= 180000 && scoreInt < 210000 )
		{
			levelStr = "S";
		}
		else if( scoreInt >= 210000)
		{
			levelStr = "SS";
		}
		
		level.setText(levelStr);
		gameOverScore.setText( score.getText() );
		gameOverCombo.setText( String.valueOf(regComboScore) + " hits" );
		
		//判斷是否有註冊
		String userNmae = "ghost";
		memberDataSql mds = new memberDataSql(gamePlay.this);
		//mds.create("ghost", "ghost@email.com");
		
		Cursor cursor = mds.getAll("");
		System.out.println("getCount: " + cursor.getCount());
		
		if(cursor.getCount() == 0)
		{
			System.out.println("getCount == 0");
			updateBtn.setVisibility(View.INVISIBLE);
			registerBtn.setVisibility(View.VISIBLE);
			registerDesc.setVisibility(View.VISIBLE);
			userNmae = "ghost";
			
			registerBtn.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent it = new Intent();
					it.setClass(gamePlay.this, register.class);
					gamePlay.this.startActivity(it);
				}
			});
		}
		else
		{
			updateBtn.setVisibility(View.VISIBLE);
			registerBtn.setVisibility(View.INVISIBLE);
			registerDesc.setVisibility(View.INVISIBLE);
			
			
			int rows_num = cursor.getCount();	//取得資料表列數
			int id[] = new int[rows_num];
			name = new String[rows_num];
			email = new String[rows_num];
			
			//取資料
			
			String[] str = new String[cursor.getCount()];
			 
			if(rows_num != 0) {
				cursor.moveToFirst();			//將指標移至第一筆資料
				for(int i=0; i<rows_num; i++) {
					id[i] = cursor.getInt(0);	//取得第0欄的資料，根據欄位type使用適當語法
					name[i] = cursor.getString(1);
					email[i] = cursor.getString(2);
					cursor.moveToNext();		//將指標移至下一筆資料
					//System.out.println(id[i] + " " + name[i] + " " + score[i] + " " + hits[i] + " " + level[i] );
				}
			}
			cursor.close();
			
			userNmae = name[0];
			
			updateBtn.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					
					//上傳資料至遠端
					urlLoad urlload = new urlLoad();
					urlload.setUrl("http://poke.grtimed.com/upd_rank.php");
					String keyAry[] = {"name", "email", "score", "hits", "level"};
					String valueAry[] = {name[0], email[0], String.valueOf(score.getText()), String.valueOf(regComboScore), levelStr};
					urlload.startThread(keyAry,valueAry);
				}
			});
		}
		
		//SQLite
		
		NewListDataSQL nld = new NewListDataSQL(gamePlay.this);
		nld.create(userNmae, Integer.parseInt(String.valueOf(score.getText())), regComboScore, levelStr);
		
		
		
	}
	
	//遊戲道具
	//訊息
	private void itemMsgBox(String str) {
		
		imaSet = new AnimationSet(false);
		imScaleAnime = new ScaleAnimation(sfX, stX, sfY, stY);
		imAlphaAnime = new AlphaAnimation(1,0);
		
		imaSet.addAnimation(imScaleAnime);
		imaSet.addAnimation(imAlphaAnime);
		imaSet.setDuration(500);
		itemMsg.setText( str );
		itemMsg.setVisibility(View.VISIBLE);
		itemMsg.startAnimation(imaSet);
		
		
		Thread viewThread = new Thread() {
			@Override
		    public void run() {
		    	while(true) {
		        // TODO Auto-generated method stub           
		    	
			    	try {
				    		Thread.sleep(500);
				    		itemMsg.setVisibility(View.INVISIBLE);
						}
					catch(Exception e){
					    //  e.printStackTrace();
					    //結束線程
						}
					finally {
						}
				        
			    	}    
		    	}
			};
			viewThread.start();
	}
	
	private void itemComboDescMsgBox(String str) {
				
		itemComboDesc.setText(String.valueOf( str) );
		itemComboDesc.setVisibility(View.VISIBLE);
		itemComboDesc.startAnimation(icdAs);
		
		//itemComboDescStr = str;
		icdThread = new Thread() {
			@Override
		    public void run() {
		    	while(true) {
		        // TODO Auto-generated method stub           
		    	
			    	try {
			    			Thread.sleep(icdDur);
				    		itemComboDesc.setVisibility(View.INVISIBLE);
						}
					catch(Exception e){
					    //  e.printStackTrace();
					    //結束線程
						}
					finally {
						}
				        
			    	}    
		    	}
			};
			icdThread.start();
			
	}
	
	private void itemScoreDouble() {
		itemScorePlus = 2;
		Thread itemThread = new Thread() {
			@Override
		    public void run() {
		    	while(true) {
		        // TODO Auto-generated method stub           
		    	
			    	try {
		        	   
			    			Thread.sleep(3000);
				    		itemScorePlus = 1;
				    		
			    		
						}
					catch(Exception e){
					    //  e.printStackTrace();
					    //結束線程
						}
					finally {
						}
				        
			    	}    
		    	}
			};
			itemThread.start();
	}
	
	//音效
	private void pokeSoundEffect() {
		
		sp.play(sound, 1, 1, 0, 0, 1);
		
	}
	
	//遊戲暫停
	@Override
	public void onPause() {
		super.onPause();
		
		gamePause();
		System.out.println("on pause");
	}
	
	@Override
	public void onResume() {
		super.onResume();
		
		//gamePause();
		System.out.println("on resume");
	}
	
	@Override
	public void onStart() {
		super.onStart();
	}
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_poke, menu);
		return true;
	}

}
