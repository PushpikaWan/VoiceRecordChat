package com.example.pushpika.voicerecordchat;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
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

public class CategoryList extends AppCompatActivity {
    ListView listView;
    //private View mProgressView;
    private LoadPosts mAuthTask = null;
    public static String choose_category=" ";
    public static final int CONNECTION_TIMEOUT = 1000*15;
    public static PostObject current_post_object_set;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_list);

        //destroy current post object
        current_post_object_set=null;

        // Get ListView object from xml
        listView = (ListView) findViewById(R.id.list1);

        // Defined Array values to show in ListView
        String[] values = new String[]{" Sport",
                "Art",
                "Other"
        };

        // Define a new Adapter
        // First parameter - Context
        // Second parameter - Layout for the row
        // Third parameter - ID of the TextView to which the data is written
        // Forth - the Array of data

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, values);


        // Assign adapter to ListView
        listView.setAdapter(adapter);

        // ListView Item Click Listener
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                // ListView Clicked item index
                int itemPosition = position;

                // ListView Clicked item value
                String itemValue = (String) listView.getItemAtPosition(position);


                if(itemPosition==0){
                    choose_category="Sport";
                    mAuthTask = new LoadPosts();
                    mAuthTask.execute((Void) null);


                }
                else if(itemPosition==1){
                    choose_category="Art";
                    mAuthTask = new LoadPosts();
                    mAuthTask.execute((Void) null);
                }
                else if(itemPosition==2){
                    choose_category="Other";
                    mAuthTask = new LoadPosts();
                    mAuthTask.execute((Void) null);
                }


            }

        });
    }


    //get post details from db


    public class LoadPosts extends AsyncTask<Void, Void, Boolean> {

        private final String User_ID;
        private final String category;
        public boolean stmt = true;

        LoadPosts() {
            User_ID= LoginActivity.user_ID;
            category=CategoryList.choose_category;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.


            ArrayList<NameValuePair> dataToSend = new ArrayList<>();
            dataToSend.add(new BasicNameValuePair("User_ID", User_ID));
            dataToSend.add(new BasicNameValuePair("Category",category ));
            Log.v("happened user id", User_ID);

            HttpParams httpRequestParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpRequestParams, CONNECTION_TIMEOUT);
            HttpConnectionParams.setSoTimeout(httpRequestParams, CONNECTION_TIMEOUT);

            HttpClient client = new DefaultHttpClient(httpRequestParams);
            HttpPost post = new HttpPost(MainActivity.WEB_SERVER + "get_posts.php");

            try {
                post.setEntity(new UrlEncodedFormEntity(dataToSend));
                HttpResponse httpResponse = client.execute(post);
                HttpEntity entity= httpResponse.getEntity();
                String result = EntityUtils.toString(entity);
                JSONObject jObject = new JSONObject(result);

                if(jObject.length()!=0){
                    Log.v("happened", "2");
                    //Log.v("ha records",jObject.getString("records[0].User_ID") );
                    //Log.v("ha records",jObject.getString("records[0].Post_ID") );
                    //Log.v("ha likes",jObject.getString("liked") );

                    List<String> Post_ID_List = new ArrayList<String>();
                    List<String> Title_List = new ArrayList<String>();
                    List<String> User_ID_List = new ArrayList<String>();
                    List<String> User_Name_List = new ArrayList<String>();
                    List<Integer> Image_List = new ArrayList<Integer>();
                    List<String> Audio_Name_List = new ArrayList<String>();
                    List<String> Date_Time_List = new ArrayList<String>();


                    JSONArray cast = jObject.getJSONArray("records");
                    for (int i=0; i<cast.length(); i++) {
                        JSONObject actor = cast.getJSONObject(i);
                        String post_id = actor.getString("Post_ID");
                        String type = actor.getString("Type");
                        String Title = actor.getString("Title");
                        String category = actor.getString("Category");
                        String user_id = actor.getString("User_ID");
                        String user_name = actor.getString("User_Name");
                        String audio_name = actor.getString("Audio_Name");
                        String data_time = actor.getString("Date_Time");

                        Post_ID_List.add(post_id);
                        Title_List.add(Title);
                        User_ID_List.add(user_id);
                        User_Name_List.add(user_name);
                        Image_List.add(R.drawable.mp3_1);
                        Audio_Name_List.add(audio_name);
                        Date_Time_List.add(data_time);
                    }

                    current_post_object_set = new PostObject(cast.length(),Post_ID_List,Title_List
                                ,User_ID_List,User_Name_List,Image_List,Audio_Name_List,Date_Time_List);

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
        final Intent intent1 = new Intent(this,ShowPosts.class);
        startActivity(intent1);
    }


    ////////////////////////////////////////////////////////////////////////////


}

