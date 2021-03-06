package com.example.pushpika.voicerecordchat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
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

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class AddPost extends AppCompatActivity {


    private TextView timerValue;

    private long startTime = 0L;

    private Handler customHandler = new Handler();
    File rootDir = Environment.getExternalStorageDirectory();
    long timeInMilliseconds = 0L;
    long timeSwapBuff = 0L;
    long updatedTime = 0L;

    Button play, record,post,delete;
    TextView title;
    private MediaRecorder myAudioRecorder;
    private String outputFile = null;
    private String selectedFilePath;
    private EditText title_text;
    private String Title,Category,User_ID,User_Name,Date_Time;
    ProgressDialog dialog;
    private UserSendPost mAuthTask = null;
    public static final int CONNECTION_TIMEOUT = 1000*15;
    public String currentDateTimeString;
    public  String fileName ;
    private boolean isRecordStarted;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Add Post");
        setContentView(R.layout.activity_add_post);
        timerValue = (TextView) findViewById(R.id.timerValue);
        play = (Button) findViewById(R.id.play_btn);
        record = (Button) findViewById(R.id.record_btn);
        post = (Button)findViewById(R.id.post_btn);
        delete = (Button) findViewById(R.id.delete_btn);
        title_text = (EditText) findViewById(R.id.editText);
        title = (TextView) findViewById(R.id.title);

        play.setEnabled(false);
        post.setVisibility(View.GONE);
        delete.setVisibility(View.GONE);
        play.setVisibility(View.GONE);
        checkAndCreateDirectory("/Kawulu_audio");

        SimpleDateFormat s = new SimpleDateFormat("ddMMyyyyhhmmss");
        currentDateTimeString = s.format(new Date());
        fileName =MainActivity.User_ID+currentDateTimeString;
        outputFile = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Kawulu_audio/"+fileName+".mp3";
        selectedFilePath = outputFile;

        //
        myAudioRecorder = new MediaRecorder();
        myAudioRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        myAudioRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        myAudioRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        myAudioRecorder.setOutputFile(outputFile);
        isRecordStarted = false;

        record.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction() & MotionEvent.ACTION_MASK) {

                    case MotionEvent.ACTION_DOWN:
                        v.setPressed(true);
                        startRecord();
                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_OUTSIDE:
                    case MotionEvent.ACTION_CANCEL:
                        v.setPressed(false);
                        if(isRecordStarted){
                            stopRecord();
                        }
                        // Stop action ...
                        break;
                    case MotionEvent.ACTION_POINTER_DOWN:
                        break;
                    case MotionEvent.ACTION_POINTER_UP:
                        break;
                    case MotionEvent.ACTION_MOVE:
                        break;
                }

                return true;
            }
        });

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) throws IllegalArgumentException, SecurityException, IllegalStateException {
                MediaPlayer m = new MediaPlayer();

                try {
                    m.setDataSource(outputFile);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                try {
                    m.prepare();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                m.start();
                Toast.makeText(getApplicationContext(), "Playing audio", Toast.LENGTH_LONG).show();
            }
        });
    }


    //function to verify if directory exists
    public void checkAndCreateDirectory(String dirName){
        File new_dir = new File( rootDir + dirName );
        if( !new_dir.exists() ){
            new_dir.mkdirs();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_bar, menu);
        return true;

    }

    private Runnable updateTimerThread = new Runnable() {

        public void run() {

            timeInMilliseconds = SystemClock.uptimeMillis() - startTime;

            updatedTime = timeSwapBuff + timeInMilliseconds;

            int secs = (int) (updatedTime / 1000);
            int mins = secs / 60;
            secs = secs % 60;
            int milliseconds = (int) (updatedTime % 1000);
            timerValue.setText("" + mins + ":"
                    + String.format("%02d", secs) + ":"
                    + String.format("%03d", milliseconds));
            customHandler.postDelayed(this, 0);
        }

    };


    public void delete(View view){
        if(selectedFilePath != null){
            File file = new File(selectedFilePath);
            file.delete();
            finish();
            Intent intent = new Intent(this,AddPost.class);
            startActivity(intent);
        }
        else{
            Toast.makeText(AddPost.this,"File deleting error",Toast.LENGTH_SHORT).show();
        }
    }

    public void startRecord(){
        if(title_text.equals("") || TextUtils.isEmpty(title_text.getText().toString().trim())){
            title_text.setError(getString(R.string.error_field_required));
            Toast.makeText(AddPost.this,"Set a title before recording",Toast.LENGTH_SHORT).show();
            return;
        }
        isRecordStarted =true;
        try {
            myAudioRecorder.prepare();
            myAudioRecorder.start();
            startTime = SystemClock.uptimeMillis();
            customHandler.postDelayed(updateTimerThread, 0);
            try {
                Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
                r.play();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (IllegalStateException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        title_text.setVisibility(View.GONE);
        title.setVisibility(View.GONE);
        // Toast.makeText(getApplicationContext(), "Recording started", Toast.LENGTH_LONG).show();
    }

    public void stopRecord(){
        myAudioRecorder.stop();
        myAudioRecorder.release();
        myAudioRecorder = null;
        timeSwapBuff += timeInMilliseconds;
        customHandler.removeCallbacks(updateTimerThread);

        play.setEnabled(true);
        play.setVisibility(View.VISIBLE);
        delete.setVisibility(View.VISIBLE);
        post.setVisibility(View.VISIBLE);
        timerValue.setVisibility(View.GONE);
        record.setVisibility(View.GONE);

        try {
            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
            r.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //Toast.makeText(getApplicationContext(), "Audio recorded successfully", Toast.LENGTH_LONG).show();

    }

    public void post(View view){
        //on upload button Click
        if(selectedFilePath != null){
            dialog = ProgressDialog.show(AddPost.this,"","Uploading File...",true);

            new Thread(new Runnable() {
                @Override
                public void run() {
                    //creating new thread to handle Http Operations
                    uploadFile(selectedFilePath);
                }
            }).start();
        }else{
            Toast.makeText(AddPost.this,"Please choose a File First",Toast.LENGTH_SHORT).show();
        }

    }
    //android upload file to server
    public int uploadFile(final String selectedFilePath){

        int serverResponseCode = 0;

        HttpURLConnection connection;
        DataOutputStream dataOutputStream;
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";


        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1 * 1024 * 1024;
        File selectedFile = new File(selectedFilePath);


        String[] parts = selectedFilePath.split("/");
        final String fileName = parts[parts.length - 1];

        if (!selectedFile.isFile()) {
            dialog.dismiss();

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(), "Source File Doesn't Exist: " + selectedFilePath, Toast.LENGTH_LONG).show();
                }
            });
            return 0;
        } else {
            try {
                FileInputStream fileInputStream = new FileInputStream(selectedFile);
                String ur = MainActivity.WEB_SERVER+"UploadToServer.php";
                URL url = new URL(ur);
                connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);//Allow Inputs
                connection.setDoOutput(true);//Allow Outputs
                connection.setUseCaches(false);//Don't use a cached Copy
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Connection", "Keep-Alive");
                connection.setRequestProperty("ENCTYPE", "multipart/form-data");
                connection.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                connection.setRequestProperty("uploaded_file", selectedFilePath);

                //creating new dataoutputstream
                dataOutputStream = new DataOutputStream(connection.getOutputStream());

                //writing bytes to data outputstream
                dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);
                dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\";filename=\""
                        + selectedFilePath + "\"" + lineEnd);

                dataOutputStream.writeBytes(lineEnd);

                //returns no. of bytes present in fileInputStream
                bytesAvailable = fileInputStream.available();
                //selecting the buffer size as minimum of available bytes or 1 MB
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                //setting the buffer as byte array of size of bufferSize
                buffer = new byte[bufferSize];

                //reads bytes from FileInputStream(from 0th index of buffer to buffersize)
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                //loop repeats till bytesRead = -1, i.e., no bytes are left to read
                while (bytesRead > 0) {
                    //write the bytes read from inputstream
                    dataOutputStream.write(buffer, 0, bufferSize);
                    bytesAvailable = fileInputStream.available();
                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);
                }

                dataOutputStream.writeBytes(lineEnd);
                dataOutputStream.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

                serverResponseCode = connection.getResponseCode();
                String serverResponseMessage = connection.getResponseMessage();

                Log.i("New new", "Server Response is: " + serverResponseMessage + ": " + serverResponseCode);

                //response code of 200 indicates the server status OK
                if (serverResponseCode == 200) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "File Upload completed.", Toast.LENGTH_LONG).show();
                            send_post_data();
                        }
                    });
                }

                //closing the input and output streams
                fileInputStream.close();
                dataOutputStream.flush();
                dataOutputStream.close();


            } catch (FileNotFoundException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(AddPost.this, "File Not Found", Toast.LENGTH_SHORT).show();
                    }
                });
            } catch (MalformedURLException e) {
                e.printStackTrace();
                Toast.makeText(AddPost.this, "URL error!", Toast.LENGTH_SHORT).show();

            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(AddPost.this, "Cannot Read/Write File!", Toast.LENGTH_SHORT).show();
            }
            dialog.dismiss();
            return serverResponseCode;
        }
    }


    /////////////////////////////////////////////////////////////////////



    public void send_post_data() {

        if (mAuthTask != null) {
            return;
        }

        // Store values at the time of the login attempt.
        Title= title_text.getText().toString();
        //Category = "test";
        User_ID= MainActivity.User_ID;
        User_Name= MainActivity.User_Name;
        Date_Time = currentDateTimeString;
        //AudioName=filename


        boolean cancel = false;
        View focusView = null;


///contacts shouldn`t be empty
        // Check for valid first name.
        if (TextUtils.isEmpty(Title)) {
            title_text.setError(getString(R.string.error_field_required));
            focusView = title_text;
            cancel = true;
        }


        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            mAuthTask = new UserSendPost();
            mAuthTask.execute((Void) null);
            Intent intent = new Intent(this, UserHomePage.class);
            startActivity(intent);
            finish();
        }
    }


    public class UserSendPost extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            ArrayList<NameValuePair> dataToSend = new ArrayList<>();
            dataToSend.add(new BasicNameValuePair("Title",Title));
            dataToSend.add(new BasicNameValuePair("Category",UserHomePage.Current_Category));
            dataToSend.add(new BasicNameValuePair("User_ID",User_ID));
            dataToSend.add(new BasicNameValuePair("User_Name",User_Name));
            dataToSend.add(new BasicNameValuePair("Audio_Name",fileName));
            dataToSend.add(new BasicNameValuePair("Date_Time",Date_Time));

            //hard code


            //HttpParams httpRequestParams = new BasicHttpParams();
            //HttpConnectionParams.setConnectionTimeout(httpRequestParams, CONNECTION_TIMEOUT);
            //HttpConnectionParams.setSoTimeout(httpRequestParams, CONNECTION_TIMEOUT);

            HttpParams httpRequestParams = getHttpRequestParams();

            HttpClient client = new DefaultHttpClient(httpRequestParams);
            HttpPost post1 = new HttpPost(MainActivity.WEB_SERVER + "post_add.php");


            try {
                post1.setEntity(new UrlEncodedFormEntity(dataToSend));
                client.execute(post1);


            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(),"Error, Post failure", Toast.LENGTH_LONG).show();
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
            Toast.makeText(getApplicationContext(),"Post successfully", Toast.LENGTH_LONG).show();


//            progressDialog.dismiss();


        }

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

}