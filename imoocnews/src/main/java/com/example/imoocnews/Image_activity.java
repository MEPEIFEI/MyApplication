package com.example.imoocnews;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;


public class Image_activity extends Activity {

    private TextView tv_title;
    private TextView tv_content;
    private ImageView bigimage;

    private String title;
    private String content;
    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image);
        init();
        getData();
        tv_title.setText(title);
        tv_content.setText(content);
        new MyAsyncTask().execute(url);
    }

    private void init() {
        tv_title = (TextView) findViewById(R.id.textView_title);
        tv_content = (TextView) findViewById(R.id.textView_content);
        bigimage = (ImageView) findViewById(R.id.big_image);
    }

    public Bitmap getBitmapFromUrl (String urlstring){
        Bitmap bitmap = null;
        InputStream is = null;
        try {
            URL url = new URL(urlstring);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            is = new BufferedInputStream(connection.getInputStream());
            bitmap = BitmapFactory.decodeStream(is);
            connection.disconnect();
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }finally {
            try {
                if(is != null){
                    is.close();
                }
                
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return bitmap;
    }
    private void getData() {
        Intent intent = getIntent();
        title = intent.getStringExtra("title");
        content = intent.getStringExtra("content");
        url = intent.getStringExtra("url");
    }
    class MyAsyncTask extends AsyncTask<String,Void,Bitmap> {


        @Override
        protected Bitmap doInBackground(String... params) {

            return getBitmapFromUrl(params[0]);
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            bigimage.setImageBitmap(bitmap);
        }
    }
}
