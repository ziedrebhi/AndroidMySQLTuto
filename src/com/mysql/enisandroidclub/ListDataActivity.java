package com.mysql.enisandroidclub;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class ListDataActivity extends Activity {
	CustomProgressDialog  progressDialog;
	String urlGet="http://192.168.1.5/enis_android_club/affichage_bd.php";
	GetDataAsyncTask getData;
	String message;
	int success;
	ListView lv;
	List<String> myListofData ;
	ArrayAdapter arrayadp; 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list_data);
		progressDialog = new CustomProgressDialog(this, R.drawable.loading_throbber);
		progressDialog.setCancelable(true);
		lv=(ListView)findViewById(R.id.listView1);
		myListofData = new ArrayList<String>();
		getData=new GetDataAsyncTask();
		getData.execute();	
		lv.setOnItemClickListener(new OnItemClickListener() {         
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,long arg3)
			{
				// s= value of seleted row
				String s=(String) (lv.getItemAtPosition(arg2)); 
				// on each row , I have save all of data separted by '-' : col1-col2-col3-col4
				String[] patrs = s.split(" - ");
				//parts[0] contains value of col1 , parts [1] contains value of col2 of each row
				Intent intent = new Intent(ListDataActivity.this, EditSuppActivity.class);
				//send data to the next activity
				intent.putExtra("col1Value", patrs[0]);
				intent.putExtra("col2Value", patrs[1]);
				intent.putExtra("col3Value", patrs[2]);
				intent.putExtra("col4Value", patrs[3]);
				startActivityForResult(intent, 100);
				finish();
	    	                
			}                                                              
		});
	}

	
	private class GetDataAsyncTask extends  AsyncTask<Void, Void, Void> {
		@Override
		protected void onPreExecute() {
			Log.i("add", "onPreExecute");
			super.onPreExecute();
			progressDialog.show();
		}
		
		@Override
		protected Void doInBackground(Void... params) {
			Log.i("add", " start doInBackground");
			ServiceHandler sh = new ServiceHandler();
			
			// Making a request to url and getting response
		String jsonStr = sh.makeServiceCall(urlGet, ServiceHandler.GET);

		Log.d("Response: ",jsonStr);
			
		if (jsonStr != null) {
		try {
			JSONObject jsonObj = new JSONObject(jsonStr);
			// return value of success
			success=jsonObj.getInt("success");
			Log.i("success", String.valueOf(success));
			if (success==0)
			{
				// success=0 ==> there is a string = message
				message=jsonObj.getString("message");
				Log.i("message", message);
			}
			else if (success==1)
			{
		    	// success=1 ==> there is an array of data = valeurs
				JSONArray dataValues = jsonObj.getJSONArray("valeurs");
				// loop each row in the array 
				for(int j=0;j<dataValues.length();j++)
				{
					JSONObject values = dataValues.getJSONObject(j);
					// return values of col1 in valCol1
					String valCol1= values.getString("col1");
					// return values of col2 in valCol2
					String valCol2= values.getString("col2");
					String valCol3= values.getString("col3");
					String valCol4= values.getString("col4");
					//add a string witch contains all of data getted from the response
					myListofData.add(valCol1+" - "+valCol2+" - "+valCol3+" - "+valCol4);
					Log.i("Row "+(j+1), valCol1+" - "+valCol2+" - "+valCol3+" - "+valCol4);
				}
			}
				      
		} catch (JSONException e) {
			e.printStackTrace();
		}
		} else {
			Log.e("ServiceHandler", "Couldn't get any data from the url");
		}

		Log.i("add", " end doInBackground");
		return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {
			Log.i("add", "onPostExecute");
			super.onPostExecute(result);
			if (progressDialog.isShowing())
			{
				progressDialog.dismiss();
			}
			if(success==1)
			{
				Toast.makeText(getApplicationContext(), "Bien récues ", Toast.LENGTH_LONG).show();
				// show the list view contains the data
				arrayadp=new ArrayAdapter(getApplicationContext(),  android.R.layout.simple_list_item_1, myListofData);                                    
				lv.setAdapter(arrayadp);  
			}
			else 
			{
				Toast.makeText(getApplicationContext(), "Erreur", Toast.LENGTH_LONG).show();
			}
     
      
		}
		
	}
	

	

}