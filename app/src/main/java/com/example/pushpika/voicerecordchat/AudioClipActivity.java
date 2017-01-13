package com.example.pushpika.voicerecordchat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class AudioClipActivity extends AppCompatActivity {
    //initialize our progress dialog/bar
    private ProgressDialog mProgressDialog;
    public static final int DIALOG_DOWNLOAD_PROGRESS = 0;
    public static final int CONNECTION_TIMEOUT = 1000*15;
    private UserLikeTask mAuthTask = null;
    //initialize root directory
    File rootDir = Environment.getExternalStorageDirectory();

    //defining file name and url
    public String fileName;
    public String fileURL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_clip);
        setTitle(CardFragment.audioClipObject.Title +" audio clip");

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

    public void Play(View view){
        Intent intent = new Intent(this,StreamingMp3Player.class);
        startActivity(intent);
    }

    public void Reply(View view){
        Intent intent2 = new Intent(this,ReplyMethods.class);
        startActivity(intent2);
        finish();
    }

    public void Like(View view){
        mAuthTask = new UserLikeTask();
        mAuthTask.execute((Void) null);
        //Toast.makeText(getApplicationContext(),"Like button clicked", Toast.LENGTH_LONG).show();
    }

    public void Download(View view){

        //making sure the download directory exists
        checkAndCreateDirectory("/my_downloads");
        fileName = CardFragment.audioClipObject.Title+".mp3";
        fileURL = MainActivity.WEB_SERVER+"uploads/"+CardFragment.audioClipObject.Audio_Name+".mp3";

        //executing the asynctask
        new DownloadFileAsync().execute(fileURL);
        Toast.makeText(getApplicationContext(), fileName+" downloading...", Toast.LENGTH_LONG).show();

    }



    //this is our download file asynctask
    class DownloadFileAsync extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            try {
                Log.v("like activated"," yeah");
                //connecting to url
                URL u = new URL(fileURL);
                HttpURLConnection c = (HttpURLConnection) u.openConnection();
                c.setRequestMethod("GET");
                c.setDoOutput(true);
                c.connect();

                //lenghtOfFile is used for calculating download progress
                int lenghtOfFile = c.getContentLength();

                //this is where the file will be seen after the download
                FileOutputStream f = new FileOutputStream(new File(rootDir + "/my_downloads/", fileName));
                //file input is from the url
                InputStream in = c.getInputStream();

                //here’s the download code
                byte[] buffer = new byte[1024];
                int len1 = 0;
                long total = 0;

                while ((len1 = in.read(buffer)) > 0) {
                    total += len1; //total = total + len1
                    publishProgress("" + (int)((total*100)/lenghtOfFile));
                    f.write(buffer, 0, len1);
                }
                f.close();

            } catch (Exception e) {
                Log.d("log tag", e.getMessage());
            }

            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //showDialog(DIALOG_DOWNLOAD_PROGRESS);
        }


        /* protected void onProgressUpdate(String… progress) {
             Log.d("Log tag",progress[0]);
             mProgressDialog.setProgress(Integer.parseInt(progress[0]));
         }
 */
        @Override
        protected void onPostExecute(String unused) {
            //dismiss the dialog after the file was downloaded
            // dismissDialog(DIALOG_DOWNLOAD_PROGRESS);
        }
    }

    //function to verify if directory exists
    public void checkAndCreateDirectory(String dirName){
        File new_dir = new File( rootDir + dirName );
        if( !new_dir.exists() ){
            new_dir.mkdirs();
        }
    }



    public class UserLikeTask extends AsyncTask<Void, Void, Boolean> {


        public boolean stmt = true;

        UserLikeTask() {

        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.


            ArrayList<NameValuePair> dataToSend = new ArrayList<>();
            dataToSend.add(new BasicNameValuePair("User_ID", MainActivity.User_ID));
            dataToSend.add(new BasicNameValuePair("User_Name", MainActivity.User_Name));
            dataToSend.add(new BasicNameValuePair("Post_ID",CardFragment.audioClipObject.Post_ID));


            HttpParams httpRequestParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpRequestParams, CONNECTION_TIMEOUT);
            HttpConnectionParams.setSoTimeout(httpRequestParams, CONNECTION_TIMEOUT);

            HttpClient client = new DefaultHttpClient(httpRequestParams);
            HttpPost post = new HttpPost(MainActivity.WEB_SERVER + "like_add.php");

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

            Toast.makeText(getApplicationContext(), "Your like will be shown after refresh posts", Toast.LENGTH_LONG).show();


            if (success) {

                load_view();

            } else {

               // Toast.makeText(getApplicationContext(), "data sending error.. already liked", Toast.LENGTH_LONG).show();

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
        //nothing load only
    }



}
