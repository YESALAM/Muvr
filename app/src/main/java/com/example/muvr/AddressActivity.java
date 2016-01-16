package com.example.muvr;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.muvr.adapter.PlaceAutocompleteAdapter;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import static android.widget.Toast.LENGTH_LONG;

public class AddressActivity extends Activity implements OnClickListener , GoogleApiClient.OnConnectionFailedListener {

	TextView tv;
	AutoCompleteTextView actvStart ;
	AutoCompleteTextView actvEnd ;
    Button go ;

    private String startplaceid = null ;
    private String endplaceid = null ;
    private LatLng startlatlng ;
    private LatLng endlatlng ;
    private int turn  ;
	/**
	 * GoogleApiClient wraps our service connection to Google Play Services and provides access
	 * to the user's sign in state as well as the Google's APIs.
	 */
	protected GoogleApiClient mGoogleApiClient;

	private PlaceAutocompleteAdapter mAdapter;

	private static final LatLngBounds BOUNDS_AHAMADABAD = new LatLngBounds(
			new LatLng(22.934309, 72.484975), new LatLng(23.110625, 72.685476));
    private static final LatLngBounds BOUNDS_SURAT = new LatLngBounds(
            new LatLng(21.076884, 72.702603), new LatLng(21.262893, 72.944646));
    private static final LatLngBounds BOUNDS_VADODRA= new LatLngBounds(
            new LatLng(22.231226, 73.047771), new LatLng(22.404638, 73.261318));
    private static final LatLngBounds BOUNDS_GHANDHINAGAR= new LatLngBounds(
            new LatLng(23.178105, 72.594059), new LatLng(23.268182, 72.693795));

    private  LatLngBounds BOUNDS_CITY  ;
    private static final String TAG = new String("AddressActivity");

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_address);
		Intent ri=getIntent();
		Bundle b=ri.getExtras();
		String rcity=b.getString("userdata");
		tv=(TextView)findViewById(R.id.muvr_cityName);
		tv.setText(rcity);
        turn = 0 ;
		actvStart = (AutoCompleteTextView) findViewById(R.id.muvr_editStart);
		actvEnd = (AutoCompleteTextView) findViewById(R.id.muvr_editEnd);
        go = (Button) findViewById(R.id.muvr_go);
        go.setOnClickListener(this);

        switch (rcity){
            case "Ahmedabad" :
                BOUNDS_CITY = BOUNDS_AHAMADABAD ;
                break;
            case "Surat" :
                BOUNDS_CITY = BOUNDS_SURAT ;
                break;
            case "Vadodra" :
                BOUNDS_CITY = BOUNDS_VADODRA ;
                break;
            case "Gandhinagar" :
                BOUNDS_CITY = BOUNDS_GHANDHINAGAR ;
                break;
        }

        //Implements the listener to extract the place ID for the selected location .
        actvStart.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                 /*
             Retrieve the place ID of the selected item from the Adapter.
             The adapter stores each Place suggestion in a AutocompletePrediction from which we
             read the place ID and title.
              */
                final AutocompletePrediction item = mAdapter.getItem(position);
               // final String placeId = item.getPlaceId();
                startplaceid = item.getPlaceId();
            }
        });
        actvEnd.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                 /*
             Retrieve the place ID of the selected item from the Adapter.
             The adapter stores each Place suggestion in a AutocompletePrediction from which we
             read the place ID and title.
              */
                final AutocompletePrediction item = mAdapter.getItem(position);
                //final String placeId = item.getPlaceId();
                endplaceid = item.getPlaceId();
            }
        });
		// Construct a GoogleApiClient for the {@link Places#GEO_DATA_API} using AutoManage
		// functionality, which automatically sets up the API client to handle Activity lifecycle
		// events. If your activity does not extend FragmentActivity, make sure to call connect()
		// and disconnect() explicitly.
		mGoogleApiClient = new GoogleApiClient.Builder(this)
				.addApi(Places.GEO_DATA_API)
                .addOnConnectionFailedListener(this)
				.build();

        mGoogleApiClient.connect();

        // Set up the adapter that will retrieve suggestions from the Places Geo Data API that cover
        // the entire world.
        mAdapter = new PlaceAutocompleteAdapter(this, mGoogleApiClient, BOUNDS_CITY,
                new  AutocompleteFilter.Builder().setTypeFilter(AutocompleteFilter.TYPE_FILTER_ADDRESS).build()
                );
        actvStart.setAdapter(mAdapter);
        actvEnd.setAdapter(mAdapter);




	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		String start = actvStart.getText().toString();
        String end = actvEnd.getText().toString();
        Log.e(TAG,start);
        Log.e(TAG,end);
		if(isinputValid(start,end)){
            new GetDistance().execute(start,end);

        }
	}


    /**
     * To check if the input string is valid or not after clicking go button .
     */
    private boolean isinputValid(String start , String end){
        if(start.isEmpty() || end.isEmpty()){
            Toast.makeText(this,"You cant leave black these field ", Toast.LENGTH_SHORT).show();
            return false ;
        } else if(start.equalsIgnoreCase(end)){
            Toast.makeText(this,"Start and End are same",Toast.LENGTH_SHORT).show();
            return false ;
        } else if(startplaceid == null || endplaceid == null ){
            Toast.makeText(this,"Please select address from suggestion", LENGTH_LONG).show();
            return false ;
        }
        return  true;
    }



    /**
     * Called when the Activity could not connect to Google Play services and the auto manager
     * could resolve the error automatically.
     * In this case the API is not available and notify the user.
     *
     * @param connectionResult can be inspected to determine the cause of the failure
     */
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

        Log.e(TAG, "onConnectionFailed: ConnectionResult.getErrorCode() = "
                + connectionResult.getErrorCode());

        // TODO(Developer): Check error code and notify the user of error state and resolution.
        Toast.makeText(this,
                "Could not connect to Google API Client: Error " + connectionResult.getErrorCode(),
                Toast.LENGTH_SHORT).show();
    }

    private class GetDistance extends AsyncTask<String,Void,String> {

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            String distance = parseJson(s);
            Log.e(TAG,"distance is "+distance);
        }

        @Override
        protected String doInBackground(String... strings) {
            // These two need to be declared outside the try/catch
            // so that they can be closed in the finally block.
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            String distanceMatrixJson = null ;

            try{
                final String BASE_URL = "https://maps.googleapis.com/maps/api/distancematrix/json?" ;
                final String ORIGIN = "origins";
                final String DESTINATION = "destinations";
                final String API_KEY = "";

                Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                        .appendQueryParameter(ORIGIN, strings[0])
                        .appendQueryParameter(DESTINATION, strings[1])
                        .appendQueryParameter(API_KEY, "AIzaSyBrZmxD_RSkIJYn_OJmJTxU_MPcYJ1POQ4")
                        .build();

                URL url = new URL(builtUri.toString());

                // Create the request to OpenWeatherMap, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                distanceMatrixJson = buffer.toString();

            } catch (IOException e) {
                Log.e(TAG, "Error ", e);
                // If the code didn't successfully get the weather data, there's no point in attempting
                // to parse it.
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(TAG, "Error closing stream", e);
                    }
                }
            }
            return distanceMatrixJson;
        }

        /**
         * Parse the recieved JSON and extract the distance as String .
         * @param distanceMatrixJson JSON to parse
         * @return distnace in meter as String object
         */
        private String parseJson(String distanceMatrixJson){
            try{
                JSONObject json = new JSONObject(distanceMatrixJson);
                if(json.getString("status").equalsIgnoreCase("OK")){
                    JSONArray rowarray = json.getJSONArray("rows") ;
                    JSONObject firstrow = rowarray.getJSONObject(0);
                    JSONArray elementsarray = firstrow.getJSONArray("elements");
                    JSONObject firstelement = elementsarray.getJSONObject(0);
                    JSONObject distanceobject = firstelement.getJSONObject("distance");
                    String distance = distanceobject.getString("value");
                    return distance ;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null ;
        }


    }


}
