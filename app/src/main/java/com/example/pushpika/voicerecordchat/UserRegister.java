package com.example.pushpika.voicerecordchat;

import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import java.util.ArrayList;


public class UserRegister extends AppCompatActivity{

    private Button bRegister;
    private String user_name;
    private ProgressDialog progressDialog;
    private UserLoginTask mAuthTask = null;
    public static final int CONNECTION_TIMEOUT = 1000*15;
    EditText user_name_entry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_register);
        user_name_entry = (EditText)findViewById(R.id.user_name);
        bRegister = (Button) findViewById(R.id.bRegister);
    }

    @Override
    public void onBackPressed() {
        //
    }

    protected boolean isOnLine(){
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if(netInfo != null && netInfo.isConnected()){
            return true;
        }
        else
            return false;

    }

    public void send_data(View view) {
        if(!isOnLine()){
            Toast.makeText(this, "Internet is not available", Toast.LENGTH_LONG).show();
            return;
        }
        attemptLogin();
    }


    ///////////////////////////////////////////////////////////////

    public void attemptLogin() {

        if (mAuthTask != null) {
            return;
        }


        // Store values at the time of the login attempt.
        user_name = user_name_entry.getText().toString();



        boolean cancel = false;
        View focusView = null;


        if (TextUtils.isEmpty(user_name)) {
            user_name_entry.setError(getString(R.string.error_field_required));
            focusView = user_name_entry;
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            mAuthTask = new UserLoginTask();
            mAuthTask.execute((Void) null);
            Intent intent = new Intent(this, FirstLoginActivity.class);
            startActivity(intent);
            finish();
        }
    }



    public class UserLoginTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            ArrayList<NameValuePair> dataToSend = new ArrayList<>();
            dataToSend.add(new BasicNameValuePair("User_Name",user_name));
            dataToSend.add(new BasicNameValuePair("Email_Address", EmailAddressEntryActivity.EmailAddress));

            HttpParams httpRequestParams = getHttpRequestParams();

            HttpClient client = new DefaultHttpClient(httpRequestParams);
            HttpPost post1 = new HttpPost(MainActivity.WEB_SERVER + "ConsumerType.php");


            try {
                post1.setEntity(new UrlEncodedFormEntity(dataToSend));
                client.execute(post1);


            } catch (Exception e) {
                e.printStackTrace();
                //Toast.makeText(getApplicationContext(),"Error, Registration failure", Toast.LENGTH_LONG).show();
            }


            return null;
        }
        private HttpParams getHttpRequestParams() {
            HttpParams httpRequestParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpRequestParams,
                    CONNECTION_TIMEOUT);
            HttpConnectionParams.setSoTimeout(httpRequestParams,
                    CONNECTION_TIMEOUT);
            return httpRequestParams;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            Toast.makeText(getApplicationContext(),"Registration successfully", Toast.LENGTH_LONG).show();


//            progressDialog.dismiss();


        }

    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////





}