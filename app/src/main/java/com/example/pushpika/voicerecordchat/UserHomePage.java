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

    ListView listView;
    public static String Current_Category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home_page);


        //intent declaration
        final Intent intent1 = new Intent(this,SelectType.class);
        final Intent intent2 = new Intent(this,UserProfile.class);

        // Get ListView object from xml
        listView = (ListView) findViewById(R.id.list);

        // Defined Array values to show in ListView
        String[] values = new String[]{"Sport",
                "Educational",
                "Entertainment",
                "Political",
                "Profile"
        };



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
                    Current_Category="Sport";
                    startActivity(intent1);

                }
                else if(itemPosition==1){
                    Current_Category="Educational";
                    startActivity(intent1);
                }
                else if (itemPosition==2){
                    Current_Category="Entertainment";
                    startActivity(intent1);
                }
                else if (itemPosition==3){
                    Current_Category="Political";
                    startActivity(intent1);
                }
                else if (itemPosition==4){
                    Current_Category="Profile";
                    startActivity(intent2);
                }

            }

        });
    }

    @Override
    public void onBackPressed() {
        finish();
    }


}
