package com.example.pushpika.voicerecordchat;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.util.ByteArrayBuffer;
import org.w3c.dom.Text;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;



public class CardFragment extends Fragment {


    ArrayList<PostModule> listitems = new ArrayList<>();
    RecyclerView MyRecyclerView;




    //initialize our progress dialog/bar
    private ProgressDialog mProgressDialog;
    public static final int DIALOG_DOWNLOAD_PROGRESS = 0;

    //initialize root directory
    File rootDir = Environment.getExternalStorageDirectory();

    //defining file name and url
    public String fileName;
    public String fileURL;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initializeList();
        getActivity().setTitle(CategoryList.choose_category+" Posts");


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_card, container, false);
        MyRecyclerView = (RecyclerView) view.findViewById(R.id.cardView);
        MyRecyclerView.setHasFixedSize(true);
        LinearLayoutManager MyLayoutManager = new LinearLayoutManager(getActivity());
        MyLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        if (listitems.size() > 0 & MyRecyclerView != null) {
            MyRecyclerView.setAdapter(new MyAdapter(listitems));
        }
        MyRecyclerView.setLayoutManager(MyLayoutManager);

        return view;
    }




    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    public class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {
        private ArrayList<PostModule> list;

        public MyAdapter(ArrayList<PostModule> Data) {
            list = Data;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent,int viewType) {
            // create a new view
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.recycle_items, parent, false);
            MyViewHolder holder = new MyViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, int position) {
            holder.title.setText(list.get(position).getCardName());
            holder.titleTextView.setText(list.get(position).getCardName());
            holder.hiddenTextView.setText(list.get(position).getCardName2());
            holder.coverImageView.setImageResource(list.get(position).getImageResourceId());
            holder.coverImageView.setTag(list.get(position).getImageResourceId());
            holder.likeImageView.setTag(R.drawable.ic_like);

        }

        @Override
        public int getItemCount() {
            return list.size();
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView titleTextView;
        public TextView title;
        public TextView hiddenTextView;
        public ImageView coverImageView;
        public ImageView likeImageView;
        public ImageView shareImageView;


        public MyViewHolder(View v) {
            super(v);
            titleTextView = (TextView) v.findViewById(R.id.titleTextView);
            title = (TextView) v.findViewById(R.id.textView3);
            hiddenTextView = (TextView) v.findViewById(R.id.hiddenText);
            coverImageView = (ImageView) v.findViewById(R.id.coverImageView);
            likeImageView = (ImageView) v.findViewById(R.id.likeImageView);
            shareImageView = (ImageView) v.findViewById(R.id.shareImageView);
            likeImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    int id = (int)likeImageView.getTag();
                    if( id == R.drawable.ic_like){

                        likeImageView.setTag(R.drawable.ic_liked);
                        likeImageView.setImageResource(R.drawable.ic_liked);

                        Toast.makeText(getActivity(),titleTextView.getText()+" added to favourites",Toast.LENGTH_SHORT).show();

                    }else{

                        likeImageView.setTag(R.drawable.ic_like);
                        likeImageView.setImageResource(R.drawable.ic_like);
                        Toast.makeText(getActivity(),titleTextView.getText()+" removed from favourites",Toast.LENGTH_SHORT).show();


                    }

                }
            });



            shareImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    //making sure the download directory exists
                    checkAndCreateDirectory("/my_downloads");
                    fileName = (String) titleTextView.getText()+".mp3";
                    fileURL = MainActivity.WEB_SERVER+"uploads/"+hiddenTextView.getText()+".mp3";

                    //executing the asynctask
                    new DownloadFileAsync().execute(fileURL);
                    Toast.makeText(getActivity(), titleTextView.getText()+" downloading...", Toast.LENGTH_LONG).show();



/*
                    Uri imageUri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE +
                            "://" + getResources().getResourcePackageName(coverImageView.getId())
                            + '/' + "drawable" + '/' + getResources().getResourceEntryName((int)coverImageView.getTag()));


                    Intent shareIntent = new Intent();
                    shareIntent.setAction(Intent.ACTION_SEND);
                    shareIntent.putExtra(Intent.EXTRA_STREAM,imageUri);
                    shareIntent.setType("image/jpeg");
                    startActivity(Intent.createChooser(shareIntent, getResources().getText(R.string.send_to)));

*/

                }
            });



        }
    }



    public void initializeList() {
        listitems.clear();

        for(int i =0;i<CategoryList.current_post_object_set.Number_of_posts;i++){


            PostModule item = new PostModule();
            item.setCardName(CategoryList.current_post_object_set.Title_List.get(i));
            item.setImageResourceId(CategoryList.current_post_object_set.Image_List.get(i));
            item.setCardName2(CategoryList.current_post_object_set.Audio_Name_List.get(i));
            item.setIsfav(0);
            item.setIsturned(0);
            listitems.add(item);

        }
    }

    //download from server

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

