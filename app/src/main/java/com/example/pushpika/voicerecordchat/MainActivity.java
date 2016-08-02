package com.example.pushpika.voicerecordchat;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
}
