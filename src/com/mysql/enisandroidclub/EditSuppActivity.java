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

public class EditSuppActivity extends Activity {
	String col1, col2,col3,col4;
	EditText valCOL1,valCOL2,valCOL3,valCOL4;
	Button supp, edit, enregistrer;
	CustomProgressDialog  progressDialog;
	String urlSupp="http://192.168.1.5/enis_android_club/suppression_bd.php";
	String urlUpd="http://192.168.1.5/enis_android_club/update_bd.php";

	String message;
	int success;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_supp);
		progressDialog = new CustomProgressDialog(this, R.drawable.loading_throbber);
		progressDialog.setCancelable(true);
		Intent i=getIntent();
		 col1=i.getStringExtra("col1Value");
		 col2=i.getStringExtra("col2Value");
		 col3=i.getStringExtra("col3Value");
		 col4=i.getStringExtra("col4Value");
		 Log.i("verification", col1+"*"+col2+"*"+col3+"*"+col4);
		 valCOL1=(EditText)findViewById(R.id.col1);
		 valCOL2=(EditText)findViewById(R.id.col2);
		 valCOL3=(EditText)findViewById(R.id.col3);
		 valCOL4=(EditText)findViewById(R.id.col4);
		 
		 edit=(Button)findViewById(R.id.editer);
		 supp=(Button)findViewById(R.id.supp);
		 enregistrer=(Button)findViewById(R.id.save);
		 
		 enregistrer.setEnabled(false);
		 
		 valCOL1.setEnabled(false);
		 valCOL2.setEnabled(false);
		 valCOL3.setEnabled(false);
		 valCOL4.setEnabled(false);
		 
		 valCOL1.setText(col1);
		 valCOL2.setText(col2);
		 valCOL3.setText(col3);
		 valCOL4.setText(col4);
		 
		 edit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				enregistrer.setEnabled(true);
				supp.setEnabled(false);
				valCOL2.setEnabled(true);
				 valCOL3.setEnabled(true);
				 valCOL4.setEnabled(true);
				
			}
		});
		 enregistrer.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				new UpdateDataAsyncTask().execute();
			}
		});
		 supp.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				new SuppDataAsyncTask().execute();
			}
		});
		 
	}
	private class UpdateDataAsyncTask extends  AsyncTask<Void, Void, Void> {
		@Override
		protected void onPreExecute() {
			Log.i("apdate", "onPreExecute");
			super.onPreExecute();
			progressDialog.show();
		}
		
		@Override
		protected Void doInBackground(Void... params) {
			Log.i("update", " start doInBackground");
			// Creating service handler class instance
			ServiceHandler sh = new ServiceHandler();
			
			List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(1);
			nameValuePair.add(new BasicNameValuePair("col1",valCOL1.getText().toString()));
			nameValuePair.add(new BasicNameValuePair("col2",valCOL2.getText().toString()));
			nameValuePair.add(new BasicNameValuePair("col3",valCOL3.getText().toString()));
			nameValuePair.add(new BasicNameValuePair("col4",valCOL4.getText().toString()));
		
			// Making a request to url and getting response
			String jsonStr = sh.makeServiceCall(urlUpd, ServiceHandler.POST,nameValuePair);
			
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

			Log.i("update", " end doInBackground");
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {
			Log.i("update", "onPostExecute");
			super.onPostExecute(result);
			if (progressDialog.isShowing())
			{
				progressDialog.dismiss();
			}
			if(success==1)
			{
				Toast.makeText(getApplicationContext(), "Mise à jour avec succée  "+message, Toast.LENGTH_LONG).show();
			}
			else 
			{
				Toast.makeText(getApplicationContext(), "Erreur" +message, Toast.LENGTH_LONG).show();
			}
			Intent intent = new Intent(EditSuppActivity.this, ListDataActivity.class);
			startActivityForResult(intent, 100);
			finish();
      
		}
		
	}
	private class SuppDataAsyncTask extends  AsyncTask<Void, Void, Void> {
		@Override
		protected void onPreExecute() {
			Log.i("supp", "onPreExecute");
			super.onPreExecute();
			progressDialog.show();
		}
		
		@Override
		protected Void doInBackground(Void... params) {
			Log.i("supp", " start doInBackground");
			ServiceHandler sh = new ServiceHandler();
			List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(1);
			nameValuePair.add(new BasicNameValuePair("col1",valCOL1.getText().toString()));
			// Making a request to url and getting response
			String jsonStr = sh.makeServiceCall(urlSupp, ServiceHandler.POST,nameValuePair);	
			
		Log.d("Response: ",jsonStr);
		if (jsonStr != null) {
		try {
					
			JSONObject jsonObj = new JSONObject(jsonStr);
			// return value of success
			success=jsonObj.getInt("success");
			message = jsonObj.getString("message");
			Log.i("suucess", String.valueOf(success));
			Log.i("message", message);
				      
		} catch (JSONException e) {
			e.printStackTrace();
		}
		} else {
			Log.e("ServiceHandler", "Couldn't get any data from the url");
		}

		Log.i("supp", " end doInBackground");
		return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {
			Log.i("supp", "onPostExecute");
			super.onPostExecute(result);
			if (progressDialog.isShowing())
			{
				progressDialog.dismiss();
			}
			if(success==1)
			{
				Toast.makeText(getApplicationContext(), "Supprimé ", Toast.LENGTH_LONG).show();
			}
			else 
			{
				Toast.makeText(getApplicationContext(), "Erreur", Toast.LENGTH_LONG).show();
			}
			Intent intent = new Intent(EditSuppActivity.this, ListDataActivity.class);
			startActivityForResult(intent, 100);
			finish();
		}
	}
	

	

}
