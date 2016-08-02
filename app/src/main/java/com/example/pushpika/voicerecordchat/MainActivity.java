package com.example.pushpika.voicerecordchat;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {



    public static String WEB_SERVER="http://mywall.esy.es/";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Handler handler = new Handler();
        final Intent intent = new Intent(this, LoginActivity.class);
        handler.postDelayed(new Runnable() {
            public void run() {
                startActivity(intent);
                finish();
            }
        }, 3000);
    }


    public void record(View view){
        //Intent intent = new Intent(this,voiceCapture.class);
        Intent intent = new Intent(this,UploadToServer.class);
        startActivity(intent);
    }

    public void send(View view){
        Intent intent = new Intent(this,voiceCapture.class);
        startActivity(intent);
    }

    public void recive(View view){
        Intent intent = new Intent(this,DownloadFromServer.class);
        startActivity(intent);
    }

}
