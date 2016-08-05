package com.example.pushpika.voicerecordchat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class FirstLoginActivity extends AppCompatActivity {

    private User_First_Login_Task mAuthTask = null;
    public static final int CONNECTION_TIMEOUT = 1000*15;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_login);
    }


    public void login_action(View view){
        mAuthTask = new User_First_Login_Task();
        mAuthTask.execute((Void) null);
    }



    //get post details from db


    public class User_First_Login_Task extends AsyncTask<Void, Void, Boolean> {
        public boolean stmt = true;

        User_First_Login_Task() {
            //
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.


            ArrayList<NameValuePair> dataToSend = new ArrayList<>();
            dataToSend.add(new BasicNameValuePair("Email_Address", EmailAddressEntryActivity.EmailAddress));
            //Log.v("happened user id", User_ID);

            HttpParams httpRequestParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpRequestParams, CONNECTION_TIMEOUT);
            HttpConnectionParams.setSoTimeout(httpRequestParams, CONNECTION_TIMEOUT);

            HttpClient client = new DefaultHttpClient(httpRequestParams);
            HttpPost post = new HttpPost(MainActivity.WEB_SERVER + "FetchType.php");

            try {
                post.setEntity(new UrlEncodedFormEntity(dataToSend));
                HttpResponse httpResponse = client.execute(post);
                HttpEntity entity= httpResponse.getEntity();
                String result = EntityUtils.toString(entity);
                JSONObject jObject = new JSONObject(result);

                if(jObject.length()!=0){
                    Log.v("happened", "2");
                    //user_id,user_name,email_address get and save.
                    String User_ID = jObject.getString("User_ID");
                    String User_Name =jObject.getString("User_Name");
                    String Email_Address = jObject.getString("Email_Address");

                    Log.v("happened_first",User_Name);
                    Log.v("happened_last", Email_Address);
                    Log.v("happened_first",User_ID);

                    SharedPreferences settings1 = getSharedPreferences("prefs", 0);
                    SharedPreferences.Editor editor = settings1.edit();
                    editor.putString("User_ID",User_ID);
                    editor.putString("User_Name",User_Name);
                    editor.putString("Email_Address",Email_Address);
                    editor.commit();

                    MainActivity.User_ID = User_ID;
                    MainActivity.User_Name = User_Name;
                    MainActivity.Email_Address =Email_Address;

                    //Log.v("ha records",jObject.getString("records[0].User_ID") );
                    //Log.v("ha records",jObject.getString("records[0].Post_ID") );

                }


            } catch (Exception e) {
                e.printStackTrace();
                Log.v("happened", "3");
                return false;

                //return false;
            }

            return stmt;
        }

        @Override
        protected void onPostExecute(final Boolean success) {

            mAuthTask = null;
            //showProgress(false);


            if (success) {

                after_data_get();

            } else {

                Toast.makeText(getApplicationContext(), "Login process error", Toast.LENGTH_LONG).show();

                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                ////////////////////////////////////////////////////////////////////////////////

            }
        }



        @Override
        protected void onCancelled() {
            mAuthTask = null;
            //showProgress(false);
        }
    }

    private void after_data_get() {
        final Intent intent1 = new Intent(this,UserHomePage.class);
        startActivity(intent1);
    }
}
