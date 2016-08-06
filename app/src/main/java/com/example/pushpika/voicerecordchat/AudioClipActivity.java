package com.example.pushpika.voicerecordchat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class AudioClipActivity extends AppCompatActivity {
    //initialize our progress dialog/bar
    private ProgressDialog mProgressDialog;
    public static final int DIALOG_DOWNLOAD_PROGRESS = 0;

    //initialize root directory
    File rootDir = Environment.getExternalStorageDirectory();

    //defining file name and url
    public String fileName;
    public String fileURL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_clip);

    }

    public void Play(View view){
        Intent intent = new Intent(this,StreamingMp3Player.class);
        startActivity(intent);
    }

    public void Reply(View view){


    }

    public void Like(View view){

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
/*

 //our progress bar settings
    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DIALOG_DOWNLOAD_PROGRESS: //we set this to 0
                mProgressDialog = new ProgressDialog(this);
                mProgressDialog.setMessage("Downloading file");
                mProgressDialog.setIndeterminate(false);
                mProgressDialog.setMax(100);
                mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                mProgressDialog.setCancelable(true);
                mProgressDialog.show();
                return mProgressDialog;
            default:
                return null;
        }


 */


}
