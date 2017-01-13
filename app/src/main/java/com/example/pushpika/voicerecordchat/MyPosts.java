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

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;

public class MyPosts extends AppCompatActivity {


    private LoadMyPosts mAuthTask = null;
    public static final int CONNECTION_TIMEOUT = 1000*15;
    public static String My_Post_ID;
    List<String> Post_ID_List,Title_List,Like_Count_List,Comment_Count_List,My_Posts_list;
    ListView list = null;
    RelativeLayout rl;
    //use comment id for this activity also

     @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setTitle("My Posts");
            //Log.v(" record list","open View comment" );
            mAuthTask = new LoadMyPosts();
            mAuthTask.execute((Void) null);
            setContentView(R.layout.activity_my_posts);
            rl=(RelativeLayout) findViewById(R.id.rl);
            list=new ListView(this);
            final Intent intent = new Intent(this,MyPostTypes.class);
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
                    MyPosts.My_Post_ID = Post_ID_List.get(itemPosition);
                    startActivity(intent);
                    finish();
                }
            });

        }


        //get post details from db


        public class LoadMyPosts extends AsyncTask<Void, Void, Boolean> {
            public boolean stmt = true;

            LoadMyPosts() {

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
                HttpPost post = new HttpPost(MainActivity.WEB_SERVER + "get_myPosts.php");

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



                        Post_ID_List = new ArrayList<String>();
                        Title_List = new ArrayList<String>();
                        Like_Count_List = new ArrayList<String>();
                        Comment_Count_List = new ArrayList<String>();
                        My_Posts_list = new ArrayList<String>();

                        //cast.length();
                        Log.v(" record list",jObject.getString("records") );
                        JSONArray cast = jObject.getJSONArray("records");
                        for (int i=0; i<cast.length(); i++) {
                            JSONObject actor = cast.getJSONObject(i);

                            String post_id  = actor.getString("Post_ID");
                            String title = actor.getString("Title");
                            String like_count= actor.getString("Like_Count");
                            String comment_count= actor.getString("Comment_Count");

                            Post_ID_List.add(post_id);
                            Title_List.add(title);
                            Like_Count_List.add(like_count);
                            Comment_Count_List.add(comment_count);
                            My_Posts_list.add(title+"\n"+like_count+" - Likes\n"+comment_count+" - Comments");

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

    private void load_view() {

        ArrayAdapter<String> adp=new ArrayAdapter<String>(getBaseContext(),
                android.R.layout.simple_list_item_1,My_Posts_list);
        adp.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        list.setAdapter(adp);

        rl.addView(list);

    }


}