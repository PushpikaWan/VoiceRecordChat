package com.example.pushpika.voicerecordchat;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class ReplyMethods extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reply_methods);
    }

    public void add_reply(View view){
        Intent intent = new Intent(this,AddComment.class);
        startActivity(intent);

    }

    public void view_reply(View view){
        Intent intent2 = new Intent(this,ViewComments.class);
        startActivity(intent2);
    }

}
