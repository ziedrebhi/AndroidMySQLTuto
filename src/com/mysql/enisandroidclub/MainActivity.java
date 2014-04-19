package com.mysql.enisandroidclub;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {
	CustomProgressDialog  progressDialog;
	Button ajout,annuler,liste;
	EditText col2Valeur,col3Valeur,col4Valeur;
	String urlAdd="http://192.168.1.5/enis_android_club/ajout_bd.php";
	AddDataAsyncTask AddData;
	String message;
	int success;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		progressDialog = new CustomProgressDialog(this, R.drawable.loading_throbber);
		progressDialog.setCancelable(true);
		ajout=(Button)findViewById(R.id.ajout);
		liste=(Button)findViewById(R.id.list);
		annuler=(Button)findViewById(R.id.annuler);
		col2Valeur=(EditText)findViewById(R.id.col2);
		col3Valeur=(EditText)findViewById(R.id.col3);
		col4Valeur=(EditText)findViewById(R.id.col4);
		liste.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(MainActivity.this, ListDataActivity.class);
				startActivityForResult(intent, 100);
				finish();
			}
		});
		ajout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				AddData =new AddDataAsyncTask();
				AddData.execute();
			
			}
		});
	}
	
	
	private class AddDataAsyncTask extends  AsyncTask<Void, Void, Void> {
		@Override
		protected void onPreExecute() {
			Log.i("add", "onPreExecute");
			super.onPreExecute();
			progressDialog.show();
		}
		
		@Override
		protected Void doInBackground(Void... params) {
			Log.i("add", " start doInBackground");
			// Creating service handler class instance
			ServiceHandler sh = new ServiceHandler();
			
			List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(1);
			
			nameValuePair.add(new BasicNameValuePair("col2",col2Valeur.getText().toString()));
			nameValuePair.add(new BasicNameValuePair("col3",col3Valeur.getText().toString()));
			nameValuePair.add(new BasicNameValuePair("col4",col4Valeur.getText().toString()));
		
			// Making a request to url and getting response
			String jsonStr = sh.makeServiceCall(urlAdd, ServiceHandler.POST,nameValuePair);
			
			Log.d("Response: ",jsonStr);
			if (jsonStr != null) {
				try {
					
					JSONObject jsonObj = new JSONObject(jsonStr);
					success = jsonObj.getInt("success");
					message = jsonObj.getString("message");
					Log.i("suucess", String.valueOf(success));
					Log.i("message", message);
					
				} catch (JSONException e) {
					
					e.printStackTrace();
				}
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
				Toast.makeText(getApplicationContext(), "Succés :"+message, Toast.LENGTH_LONG).show();
			}
			else 
			{
				Toast.makeText(getApplicationContext(), "Erreur" +message, Toast.LENGTH_LONG).show();
			}
     
      
		}
		
	}
	

	

}
