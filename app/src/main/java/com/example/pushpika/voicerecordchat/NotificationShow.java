package com.example.pushpika.voicerecordchat;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
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

public class NotificationShow extends AppCompatActivity {


        private LoadComments mAuthTask = null;
        public static final int CONNECTION_TIMEOUT = 1000*15;
        List<String> User_Name_List, Audio_Name_List,Title_List,Notification_Name_List;
        ListView list = null;
        RelativeLayout rl;
        //use comment id for this activity also


        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setTitle("Notifications");
            Log.v(" record list","open View comment" );
            mAuthTask = new LoadComments();
            mAuthTask.execute((Void) null);
            setContentView(R.layout.activity_notification_show);
            rl=(RelativeLayout) findViewById(R.id.rl);
            list=new ListView(this);
            final Intent intent = new Intent(this,StreamingMp3PlayerComments.class);
            // ListView Item Click Listener
            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    // ListView Clicked item index
                    int itemPosition = position;

                    // ListView Clicked item value
                    String itemValue = (String) list.getItemAtPosition(position);

                    //item position 0,1,2,...
                    ViewComments.current_comment_audio_id = Audio_Name_List.get(itemPosition);
                    startActivity(intent);
                }
            });

        }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_bar, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.action_home:
                finish();
                Intent intent = new Intent(this,UserHomePage.class);
                startActivity(intent);
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

        //get post details from db


        public class LoadComments extends AsyncTask<Void, Void, Boolean> {
            public boolean stmt = true;

            LoadComments() {

            }

            @Override
            protected Boolean doInBackground(Void... params) {
                // TODO: attempt authentication against a network service.


                ArrayList<NameValuePair> dataToSend = new ArrayList<>();
                dataToSend.add(new BasicNameValuePair("User_ID", MainActivity.User_ID));


                HttpParams httpRequestParams = new BasicHttpParams();
                HttpConnectionParams.setConnectionTimeout(httpRequestParams, CONNECTION_TIMEOUT);
                HttpConnectionParams.setSoTimeout(httpRequestParams, CONNECTION_TIMEOUT);

                HttpClient client = new DefaultHttpClient(httpRequestParams);
                HttpPost post = new HttpPost(MainActivity.WEB_SERVER + "get_notification.php");

                try {
                    post.setEntity(new UrlEncodedFormEntity(dataToSend));
                    HttpResponse httpResponse = client.execute(post);
                    HttpEntity entity= httpResponse.getEntity();
                    String result = EntityUtils.toString(entity);
                    JSONObject jObject = new JSONObject(result);

                    if(jObject.length()!=0){
                        Log.v(" record list",jObject.getString("records") );
                        Log.v("happened", "2");
                        //Log.v("ha records",jObject.getString("records[0].User_ID") );
                        //Log.v("ha records",jObject.getString("records[0].Post_ID") );
                        //Log.v("ha likes",jObject.getString("liked") );


                        User_Name_List = new ArrayList<String>();
                        Audio_Name_List = new ArrayList<String>();
                        Title_List = new ArrayList<String>();
                        Notification_Name_List = new ArrayList<String>();

                        //cast.length();
                        Log.v(" record list",jObject.getString("records") );
                        JSONArray cast = jObject.getJSONArray("records");
                        for (int i=0; i<cast.length(); i++) {
                            JSONObject actor = cast.getJSONObject(i);
                            String user_name = actor.getString("User_Name");
                            String title = actor.getString("Title");
                            String audio_name = actor.getString("Audio_Name");

                            Log.v("ha likes",user_name );

                            User_Name_List.add(user_name);
                            Audio_Name_List.add(audio_name);
                            Title_List.add(title);
                            Notification_Name_List.add(user_name+" commented on your post "+title);

                        }

                        //

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

                    load_view();

                } else {

                    Toast.makeText(getApplicationContext(), "Post loading error", Toast.LENGTH_LONG).show();

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

    private void load_view() {

        ArrayAdapter<String> adp=new ArrayAdapter<String>(getBaseContext(),
                android.R.layout.simple_list_item_1,Notification_Name_List);
        adp.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        list.setAdapter(adp);

        rl.addView(list);

    }


}

