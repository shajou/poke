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

import com.facebook.FacebookException;
import com.facebook.FacebookOperationCanceledException;
import com.facebook.FacebookRequestError;
import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.RequestAsyncTask;
import com.facebook.Response;
import com.facebook.SessionState;
import com.facebook.LoggingBehavior;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.Settings;
import com.facebook.widget.WebDialog;
import com.facebook.widget.WebDialog.OnCompleteListener;
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
import android.view.animation.TranslateAnimation;
import android.widget.Button;
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
	
	AdView adView;
	Thread thread;
	
	int gameAniStatu = 0;
	DisplayMetrics dm;
	int vW = 0;
	
	//fb
	private Button shareButton;
	
	//Wprivate Session.StatusCallback statusCallback = new SessionStatusCallback();
	private static final List<String> PERMISSIONS = Arrays.asList("publish_actions");
	private static final String PENDING_PUBLISH_KEY = "pendingPublishReauthorization";
	private boolean pendingPublishReauthorization = false;
	
	private TextView textInstructionsOrLink;
    private Button buttonLoginLogout;
    private Session.StatusCallback statusCallback = new SessionStatusCallback();
	
	@SuppressWarnings("null")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.poke_main);
		
		ActionBar ab = getActionBar();
		ab.hide();
		
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
		adView = new AdView(this, AdSize.BANNER, "a15147df506928f");
		RelativeLayout ads = (RelativeLayout)findViewById(R.id.adsLayout);
		ads.addView(adView);
		threadResetBtn();
		adView.loadAd(new AdRequest());
		
		
		//fb

		
		shareButton = (Button)findViewById(R.id.shareButton);
		shareButton.setOnClickListener(new View.OnClickListener() {
		    @Override
		    public void onClick(View v) {
		        publishStory();
		    	//publishFeedDialog();
		    }
		});
		
		buttonLoginLogout = (Button)findViewById(R.id.buttonLoginLogout);
		Settings.addLoggingBehavior(LoggingBehavior.INCLUDE_ACCESS_TOKENS);
		Session session = Session.getActiveSession();
        if (session == null) {
        	System.out.println("session == null");
        	
            if (savedInstanceState != null) {
            	System.out.println("session save instance state != null");
            	
                session = Session.restoreSession(this, null, statusCallback, savedInstanceState);
                pendingPublishReauthorization = savedInstanceState.getBoolean(PENDING_PUBLISH_KEY, false);
                onSessionStateChange(session, session.getState());
            }
            if (session == null) {
                session = new Session(this);
            }
            Session.setActiveSession(session);
            if (session.getState().equals(SessionState.CREATED_TOKEN_LOADED)) {
                session.openForRead(new Session.OpenRequest(this).setCallback(statusCallback));
            }
        }
        
        onSessionStateChange(session, session.getState());

        updateView();
		
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
	
	//fb
	
	
	
	private void onSessionStateChange(Session session, SessionState state ) {
	    if (state.isOpened()) {
	        shareButton.setVisibility(View.VISIBLE);
	        if (pendingPublishReauthorization && 
	                state.equals(SessionState.OPENED_TOKEN_UPDATED)) {
	            pendingPublishReauthorization = false;
	            publishStory();
	          //  publishFeedDialog();
	        }
	    } else if (state.isClosed()) {
	        shareButton.setVisibility(View.INVISIBLE);
	    }
	}
	
	private void publishFeedDialog() {
	    Bundle params = new Bundle();
	    params.putString("name", "Facebook SDK for Android");
	    params.putString("caption", "Build great social apps and get more installs.");
	    params.putString("description", "The Facebook SDK for Android makes it easier and faster to develop Facebook integrated Android apps.");
	    params.putString("link", "https://developers.facebook.com/android");
	    params.putString("picture", "https://raw.github.com/fbsamples/ios-3.x-howtos/master/Images/iossdk_logo.png");

	    WebDialog feedDialog = (
	        new WebDialog.FeedDialogBuilder(getBaseContext(),
	            Session.getActiveSession(),
	            params))
	        .setOnCompleteListener(new WebDialog.OnCompleteListener() {
	           

				@Override
				public void onComplete(Bundle values, FacebookException error) {
					// TODO Auto-generated method stub
					if (error == null) {
	                    // When the story is posted, echo the success
	                    // and the post Id.
	                    final String postId = values.getString("post_id");
	                    if (postId != null) {
	                        Toast.makeText(getBaseContext(),
	                            "Posted story, id: "+postId,
	                            Toast.LENGTH_SHORT).show();
	                    } else {
	                        // User clicked the Cancel button
	                        Toast.makeText(getBaseContext(), 
	                            "Publish cancelled", 
	                            Toast.LENGTH_SHORT).show();
	                    }
	                } else if (error instanceof FacebookOperationCanceledException) {
	                    // User clicked the "x" button
	                    Toast.makeText(getBaseContext().getApplicationContext(), 
	                        "Publish cancelled", 
	                        Toast.LENGTH_SHORT).show();
	                } else {
	                    // Generic, ex: network error
	                    Toast.makeText(getBaseContext().getApplicationContext(), 
	                        "Error posting story", 
	                        Toast.LENGTH_SHORT).show();
	                }
					
				}

	        })
	        .build();
	    feedDialog.show();
	}
	
	private void publishStory() {
	    Session session = Session.getActiveSession();

	    if (session != null){

	        // Check for publish permissions    
	        List<String> permissions = session.getPermissions();
	        if (!isSubsetOf(PERMISSIONS, permissions)) {
	            pendingPublishReauthorization = true;
	            Session.NewPermissionsRequest newPermissionsRequest = new Session
	                    .NewPermissionsRequest(this, PERMISSIONS);
	            session.requestNewPublishPermissions(newPermissionsRequest);
	            return;
	        }
	        
	        

	        Bundle postParams = new Bundle();
	        postParams.putString("name", "Facebook SDK for Android");
	        postParams.putString("caption", "Build great social apps and get more installs.");
	        postParams.putString("description", "The Facebook SDK for Android makes it easier and faster to develop Facebook integrated Android apps.");
	        postParams.putString("link", "https://developers.facebook.com/android");
	        postParams.putString("picture", "https://raw.github.com/fbsamples/ios-3.x-howtos/master/Images/iossdk_logo.png");

	        Request.Callback callback= new Request.Callback() {
	            public void onCompleted(Response response) {
	                JSONObject graphResponse = response
	                                           .getGraphObject()
	                                           .getInnerJSONObject();
	                String postId = null;
	                try {
	                    postId = graphResponse.getString("id");
	                } catch (JSONException e) {
	                   /* Log.i(TAG,
	                        "JSON error "+ e.getMessage());*/
	                }
	                FacebookRequestError error = response.getError();
	                if (error != null) {
	                    Toast.makeText(getBaseContext(),
	                         error.getErrorMessage(),
	                         Toast.LENGTH_SHORT).show();
	                    } else {
	                        Toast.makeText(getBaseContext(), 
	                             postId,
	                             Toast.LENGTH_LONG).show();
	                }
	            }
	        };

	        Request request = new Request(session, "me/feed", postParams, 
	                              HttpMethod.POST, callback);

	        RequestAsyncTask task = new RequestAsyncTask(request);
	        task.execute();
	    }

	}
	
	private boolean isSubsetOf(Collection<String> subset, Collection<String> superset) {
	    for (String string : subset) {
	        if (!superset.contains(string)) {
	            return false;
	        }
	    }
	    return true;
	}
/*
	@Override
	public void onSaveInstanceState(Bundle outState) {
	    super.onSaveInstanceState(outState);
	    outState.putBoolean(PENDING_PUBLISH_KEY, pendingPublishReauthorization);
	    uiHelper.onSaveInstanceState(outState);
	}*/
	
	 @Override
	    public void onStart() {
	        super.onStart();
	        Session.getActiveSession().addCallback(statusCallback);
	    }

	    @Override
	    public void onStop() {
	        super.onStop();
	        Session.getActiveSession().removeCallback(statusCallback);
	    }

	    @Override
	    public void onActivityResult(int requestCode, int resultCode, Intent data) {
	        super.onActivityResult(requestCode, resultCode, data);
	        Session.getActiveSession().onActivityResult(this, requestCode, resultCode, data);
	    }

	    
	    @Override
	    protected void onSaveInstanceState(Bundle outState) {
	    	System.out.println("on save instance state");
	    	
	    	
	    	
	        super.onSaveInstanceState(outState);
	        Session session = Session.getActiveSession();
	        Session.saveSession(session, outState);
	        outState.putBoolean(PENDING_PUBLISH_KEY, pendingPublishReauthorization);
	        
	        session = Session.restoreSession(this, null, statusCallback, outState);
            pendingPublishReauthorization = outState.getBoolean(PENDING_PUBLISH_KEY, false);
            onSessionStateChange(session, session.getState());
	    	//uiHelper.onSaveInstanceState(outState);
            
            System.out.println("session: " + session.isOpened());
            System.out.println("session: " + session.getState());
            System.out.println("session: " + session.getApplicationId());
	    }

	    private void updateView() {
	        Session session = Session.getActiveSession();
	        if (session.isOpened()) {
	           // textInstructionsOrLink.setText(URL_PREFIX_FRIENDS + session.getAccessToken());
	            //buttonLoginLogout.setText(R.string.logout);
	            buttonLoginLogout.setOnClickListener(new OnClickListener() {
	                public void onClick(View view) { onClickLogout(); }
	            });
	        } else {
	            //textInstructionsOrLink.setText(R.string.instructions);
	            //buttonLoginLogout.setText(R.string.login);
	            buttonLoginLogout.setOnClickListener(new OnClickListener() {
	                public void onClick(View view) { 
	                	onClickLogin(); 
	                	}
	            });
	        }
	    }

	    private void onClickLogin() {
	        Session session = Session.getActiveSession();
	        if (!session.isOpened() && !session.isClosed()) {
	            session.openForRead(new Session.OpenRequest(this).setCallback(statusCallback));
	        } else {
	            Session.openActiveSession(this, true, statusCallback);
	        }
	        
	        
	    }

	    private void onClickLogout() {
	        Session session = Session.getActiveSession();
	        if (!session.isClosed()) {
	            session.closeAndClearTokenInformation();
	        }
	    }

	    private class SessionStatusCallback implements Session.StatusCallback {
	        @Override
	        public void call(Session session, SessionState state, Exception exception) {
	        	onSessionStateChange(session, state);
        	 System.out.println("call session: " + session.isOpened());
             System.out.println("call session: " + session.getState());
             System.out.println("call session: " + session.getApplicationId());
	            updateView();
	        }
	    }

}
