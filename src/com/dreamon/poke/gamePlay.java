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
	
	//�ù��e��
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
	
	//�����G�m
	RelativeLayout gpl;
	RelativeLayout gsl;
	RelativeLayout gPauselayout;
	RelativeLayout gOverlayout;
	
	//�u�{
	private Thread thread = null;
	private int finish = 1;
	int tBaseTime = 1;
	int tMaxTime = 10;
	boolean threadPause = false;
	
	Thread icdThread = null;
	Thread itemThread = null;
	
	//�p��
	TextView score;
	Timer timer;
	Boolean comboCheck = false;
	int timeFps = 100;
	int timeComboRage = 5;
	int c = 0;
	int scorePlus = 1;
	
	//�s�ޭp��
	TextView combo;
	TextView comboPlusShow;
	int comboPlus = 0;
	int regComboScore = 0;
	
	//�s�޼лy
	TextView itemComboDesc;
	String itemComboDescStr;
	
	//�ʵe����
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
	
	//�������s
	int btnCount = 30;
	ImageButton bub;
	int moveX = 100;
	Button pauseBtn;
	Button resumeBtn;
	
	//��C�s��
	ArrayList<View> st = new ArrayList<View>();
	int stStatu[] = new int[30];
	
	//�C���p��
	Timer gTimer;
	int gameTimeHandler = 2;
	private Thread gTimeThread = null;
	int gTimeOut = 1000;
	String gTime = "1";
	public static TextView gTimeText;
	
	//�\����s
	Button restartBtn;
	
	//�C������
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
	
	//�D��
	TextView itemMsg;
	gameItem gameitem;
	int itemScorePlus = 1;
	int itemRandom = 15;
	
	//���ļ���
	SoundPool sp;
	int sound;
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.game_play);
		
		//�D��
		gameitem = new gameItem();
		itemMsg = (TextView)findViewById(R.id.itemMsg);
		
		//����
		//mp = new MediaPlayer();
		//sp = new SoundPool();
		sp = new SoundPool( 30, AudioManager.STREAM_MUSIC, 0);
		sound = sp.load(this, R.raw.slap, 0);
		
		
		//�s�޼лy
		itemComboDesc = (TextView)findViewById(R.id.itemComboDesc);
		
		//�����G�m		
		gpl = (RelativeLayout)findViewById(R.id.gamePlayLayout);
		gsl = (RelativeLayout)findViewById(R.id.gameStatuLayout);
		gPauselayout = (RelativeLayout)findViewById(R.id.gamePauseLayout);
		gOverlayout = (RelativeLayout)findViewById(R.id.gameOverLayout);
		score = (TextView)findViewById(R.id.score);
		restartBtn = (Button)findViewById(R.id.restartBtn);
		//�s�ޭp��
		combo = (TextView)findViewById(R.id.combo);
		comboPlusShow = (TextView)findViewById(R.id.comboPlus);
		//���s��ť
		pauseBtn = (Button)findViewById(R.id.pauseBtn);
		resumeBtn = (Button)findViewById(R.id.resumeBtn);
		//�˼ƭp�ɶ}�l
		gTimeText = (TextView)findViewById(R.id.timeout);
		//�C������
		
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
		
		//���o�ù��e��
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
		
		//�ʵe��l
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
		
		//���s��m�]�m
		
		int x = ((vW / 5) - (int)(vW / 1.5)) / 2;
		
		int xPaddig = (int)(vW / 5);
		int yPadding = (int)(vH / 6);
		//int marginBottom = xyPaddig;
		int gsl_h = (int)(vH / 60) * 4;
		int y = 0;
		int yMargin = (int)(yPadding / 1.5 ) / 4; //18
		
		//combo �p�� ���Įɶ���0.5��
		comboTime();
		
		//���m�w�w �C1���ˬd�@��
		threadResetBtn();
		
		//�Ыس����W���s
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
			//id�ΨӨM�w�s���X����_
			bub.setId(0);
			
			gpl.addView(bub);
			st.add(bub);
			
			x = x + vW / 5;
			
			bub.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					
					//����
					pokeSoundEffect();
					
					//���ƭp��
					String str = score.getText().toString();
					int s = Integer.parseInt(str);
					
					//�s�޶V�h �[�����ƶV�h
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
					
					//�j��30��C���[���N���@���ʵe
					if(scorePlus >= 30)
					{
						comboPlusShow.setVisibility(View.VISIBLE);
						//comboPlusShow.setAnimation(aSet);
						comboPlusShow.startAnimation(aSet);
					}
					
					//�s�ޥ[�����
					comboPlusShow.setText("+ " + String.valueOf(comboPlus ) + " !!" );
					
					//���ƭp��
					int p = s + 1 + scorePlus + (comboPlus * itemScorePlus);
					score.setText( String.valueOf(p) );
					
					//���W�U�h�N�Nc = 0; ������combo ����
					c = 0;
					
					//���ƥ[��
					scorePlus++;
					
					//�s�޼����
					combo.setText( String.valueOf(scorePlus) + " hits!!");
					//�j��Q���s�ޤ~���
					if( scorePlus >= 10 ) 
					{
						combo.setAnimation(scaleAnime);
						combo.setVisibility(View.VISIBLE);
					}
					else
					{
						combo.setVisibility(View.INVISIBLE);
					}
					
					
					//�W�U�h�ɨM�w�h�[�^�_
					int recoverTime = (int)(Math.random() * tMaxTime) + tBaseTime;
					v.setId(recoverTime);
					
					//�Nv �[�J��C
					//st.add(v);
										
					v.setBackgroundResource(R.drawable.poke_press_icon);
					v.setClickable(false);					
					
					//�O���̤jhits
					if( regComboScore < scorePlus )
					{
						regComboScore = scorePlus;
					}
					
					//�D��ĪG
										
					//stStatu �O���ثe��btn�O���� �۵���A�H���M�w�O�_�n�ܬ��D��
					for(int i = 0; i < st.size(); i++)
					{
						
						
						if(st.get(i).equals(v))
						{
							
							//���ӹD��
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
							
							
							//�M�w�U�@���O�_���D��
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
					
					//�s�޼лy
			    	
					if(scorePlus == 30)
					{
						itemComboDescStr = "�������s��";
						itemComboDescMsgBox(itemComboDescStr);
					} 
					else if(scorePlus == 70)
					{
						itemComboDescStr = "�}�G���s��";
						itemComboDescMsgBox(itemComboDescStr);
					}
					else if(scorePlus == 110)
					{
						itemComboDescStr = "�A�Ӻƨg�F";
						itemComboDescMsgBox(itemComboDescStr);
					}
					else if(scorePlus == 150)
					{
						itemComboDescStr = "���i���!!";
						itemComboDescMsgBox(itemComboDescStr);
					}
					else if(scorePlus == 190)
					{
						itemComboDescStr = "�o�����!!";
						itemComboDescMsgBox(itemComboDescStr);
					}
					else if(scorePlus == 220)
					{
						itemComboDescStr = "�w�g�S���H�����A��!!";
						itemComboDescMsgBox(itemComboDescStr);
					}
					else if(scorePlus == 250)
					{
						itemComboDescStr = "�Q�l�֨Ө��W��";
						itemComboDescMsgBox(itemComboDescStr);
					}
					else if(scorePlus == 280)
					{
						itemComboDescStr = "�������";
						itemComboDescMsgBox(itemComboDescStr);
					}
					
					
				}
			});
			
			
		}
		
		//�N���A�C��layout�m�e �ϱohit��combo��bbtn���e
		gsl.bringToFront();
				
		//�Ȱ�
		pauseBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				gamePause();
				thread.interrupt();
			}
		});
		
		//�~��
		resumeBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				gameResume();
			}
		});
		
		//���s�}�l
		
		restartBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				gameRestart();
			}
		});
		
		//�˼ƭp�ɶ}�l
		
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
	        	   
			    		//�]�w�h�[���ܦ^���
			    		//int time = tBaseTime + (int)(  Math.random() * tMaxTime );
		    	   
			    		//System.out.println(time);
			    		
			    		//�C1���ˬd�@��
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
				    //�����u�{
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
			    //�����u�{
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
					
					//�Q���U���_���s
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
					
					//��combo �p��10��
					if( scorePlus < 10 ) 
					{
						combo.setVisibility(View.INVISIBLE);
					}
					
					//�s�ޤp��30��
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
	 * combo �P�_
	 */
	//combo���Ĭ�ƧP�_
	
	private void comboTime() {
		timer = new Timer();
		//�C0.1��]�@��
		timer.schedule(new checkTask() , timeFps);
		c = 0;
	}
	
	class checkTask extends TimerTask {
	    public void run() {
	    	//�s�ާP�_
	    	//�j�󵥩�0.5��� 
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
	
	//�C���ɶ��˼�
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
		
		//�P�_�O�_�����U
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
			
			
			int rows_num = cursor.getCount();	//���o��ƪ�C��
			int id[] = new int[rows_num];
			name = new String[rows_num];
			email = new String[rows_num];
			
			//�����
			
			String[] str = new String[cursor.getCount()];
			 
			if(rows_num != 0) {
				cursor.moveToFirst();			//�N���в��ܲĤ@�����
				for(int i=0; i<rows_num; i++) {
					id[i] = cursor.getInt(0);	//���o��0�檺��ơA�ھ����type�ϥξA��y�k
					name[i] = cursor.getString(1);
					email[i] = cursor.getString(2);
					cursor.moveToNext();		//�N���в��ܤU�@�����
					//System.out.println(id[i] + " " + name[i] + " " + score[i] + " " + hits[i] + " " + level[i] );
				}
			}
			cursor.close();
			
			userNmae = name[0];
			
			updateBtn.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					
					//�W�Ǹ�Ʀܻ���
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
	
	//�C���D��
	//�T��
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
					    //�����u�{
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
					    //�����u�{
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
					    //�����u�{
						}
					finally {
						}
				        
			    	}    
		    	}
			};
			itemThread.start();
	}
	
	//����
	private void pokeSoundEffect() {
		
		sp.play(sound, 1, 1, 0, 0, 1);
		
	}
	
	//�C���Ȱ�
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
