package com.example.pushpika.voicerecordchat;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class UserHomePage extends AppCompatActivity {

    public static String Current_Category;
    private Intent intent1;
    private Intent intent2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Categories");
        setContentView(R.layout.activity_user_home_page);


        //intent declaration
        intent1 = new Intent(this,SelectType.class);
        intent2 = new Intent(this,UserProfile.class);

    }

    public void sports_click(View view){
        Current_Category="Sports";
        startActivity(intent1);
    }

    public void educational_click(View view){
        Current_Category="Educational";
        startActivity(intent1);
    }

    public void entertainment_click(View view){
        Current_Category="Entertainment";
        startActivity(intent1);
    }

    public void political_click(View view){
        Current_Category="Political";
        startActivity(intent1);
    }

    public void innovations_click(View view){
        Current_Category="Innovations";
        startActivity(intent1);
    }

    public void others_click(View view){
        Current_Category="Others";
        startActivity(intent1);
    }

    public void profile_click(View view){
        Current_Category="Profile";
        startActivity(intent2);
    }


    @Override
    public void onBackPressed() {
        finish();
    }


}
