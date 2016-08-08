package com.example.pushpika.voicerecordchat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    public static String WEB_SERVER="http://mywall.esy.es/";
    public static String User_ID,User_Name,Email_Address;
    File rootDir = Environment.getExternalStorageDirectory();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //making sure the download directory exists
        checkAndCreateDirectory("/Kawulu_audio");


        SharedPreferences settings = getSharedPreferences("prefs", 0);
        boolean firstRun = settings.getBoolean("firstRun", true);

        //final Intent intent = new Intent(this, LoginActivity.class);

        //set current class
        User_ID = settings.getString("User_ID","no");
        User_Name = settings.getString("User_Name","no");
        Email_Address = settings.getString("Email_Address","no");



        Log.v("Data check","successfully");
        Log.v("happened current share",String.valueOf(settings.getString("User_ID","DefaultClass")));
        Log.v("happened current share",String.valueOf(settings.getString("Email_Address","DefaultClass")));
        Log.v("happened current share",String.valueOf(settings.getString("User_Name","DefaultClass")));


        if (firstRun) {
            // here run your first-time instructions, for example :
            Intent intent = new Intent(this,EmailAddressEntryActivity.class);
            startActivity(intent);
            finish();

        }

        if(!firstRun){
            Intent intent = new Intent(this,UserHomePage.class);
            startActivity(intent);
            finish();
        }



    }


    //function to verify if directory exists
    public void checkAndCreateDirectory(String dirName){
        File new_dir = new File( rootDir + dirName );
        if( !new_dir.exists() ){
            new_dir.mkdirs();
        }
    }



    }

