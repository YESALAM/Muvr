package com.example.muvr;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class AddressActivity extends Activity implements OnClickListener {

	TextView tv;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_address);
		Intent ri=getIntent();
		Bundle b=ri.getExtras();
		String rcity=b.getString("userdata");
		tv=(TextView)findViewById(R.id.muvr_cityName);
		tv.setText(rcity);
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
		
	}

	
}
