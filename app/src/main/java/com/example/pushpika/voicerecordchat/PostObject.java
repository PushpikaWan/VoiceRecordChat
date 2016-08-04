package com.example.pushpika.voicerecordchat;

import android.provider.ContactsContract;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pushpika on 8/4/16.
 */
public class PostObject {

    Integer Number_of_posts;
    List<String> Post_ID_List;
    List<String> Title_List ;
    List<String> User_ID_List ;
    List<String> User_Name_List ;
    List<Integer> Image_List ;
    List<String> Audio_Name_List;
    List<String> Date_Time_List;



    public PostObject(Integer Number_of_posts,List<String> Post_ID_List, List<String> Title_List,List<String> User_ID_List,List<String> User_Name_List,
                      List<Integer> Image_List,List<String> Audio_Name_List, List<String> Date_Time_List){

        this.Number_of_posts = Number_of_posts;
        this.Post_ID_List=Post_ID_List;
        this.Title_List = Title_List;
        this.User_ID_List = User_ID_List;
        this.User_Name_List = User_Name_List;
        this.Image_List = Image_List;
        this.Audio_Name_List = Audio_Name_List;
        this.Date_Time_List = Date_Time_List;
    }


}
