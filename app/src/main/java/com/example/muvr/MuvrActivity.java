package com.example.muvr;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class MuvrActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_muvr);
		Thread timer=new Thread(){
			public void run(){
				try{
					sleep(1000);
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
				finally
				{
					Intent i =new Intent("com.example.muvr.MAINACTIVITY");
					startActivity(i);
					finish();
				}
			}
		};
		timer.start();
	}

}
