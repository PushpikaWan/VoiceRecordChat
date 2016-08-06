package com.example.pushpika.voicerecordchat;

import android.view.animation.PathInterpolator;

/**
 * Created by pushpika on 8/6/16.
 */
public class AudioClipObject {

    String Title,Category,Post_ID,Audio_Name;

   public AudioClipObject(String Title,String Category,String Post_ID,String Audio_Name){
       this.Title= Title;
       this.Category = Category;
       this.Post_ID = Post_ID;
       this.Audio_Name = Audio_Name;
   }

}
