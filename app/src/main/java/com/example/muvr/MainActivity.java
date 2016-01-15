package com.example.muvr;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

public class MainActivity extends Activity implements OnItemSelectedListener,OnClickListener {
	String[] city = { "Ahmedabad", "Surat", "Vadodra", "Gandhinagar"};
	Button b;
	String cityName="Ahmedabad";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Spinner spin = (Spinner) findViewById(R.id.spinner1);  
        spin.setOnItemSelectedListener(this);  
          
        //Creating the ArrayAdapter instance having the city list  
        ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_spinner_item,city);  
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);  
        //Setting the ArrayAdapter data on the Spinner  
        spin.setAdapter(aa); 
        b=(Button)findViewById(R.id.muvr_address);
        b.setOnClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
		// TODO Auto-generated method stub
		  //Toast.makeText(getApplicationContext(),city[position] ,Toast.LENGTH_LONG).show();
		 cityName= city[position];
		
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent i=new Intent(this, AddressActivity.class);
		i.putExtra("userdata", cityName);
		startActivity(i);
		
	}
}
