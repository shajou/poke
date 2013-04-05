package com.dreamon.poke;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Queue;
import java.lang.Iterable;
import java.util.LinkedList;
import java.util.Stack;
import java.util.Timer;
import java.util.TimerTask;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.util.Log;
import android.util.SparseArray;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
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
public class gameItem extends Activity {
	
	int timeBase = 5;
	int timeBtnBackground = R.drawable.item_time;
	
	public void gameItem() {
		
	}
	
	public int timePlus() {
		return this.timeBase;
	}

}
