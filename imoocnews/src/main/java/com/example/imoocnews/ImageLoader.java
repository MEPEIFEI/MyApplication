package com.example.imoocnews;


import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Message;
import android.util.LruCache;
import android.widget.ImageView;
import android.widget.ListView;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;


public class ImageLoader {
    private ImageView mImageView;
    private String mUrl;
    private ListView mListView;
    private Set<imageAsyncTask> mTask;
    //创建cache
    private LruCache<String,Bitmap> mCache;

    //创建构造方法，创建对象时初始化的动作放在构造方法里面
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
    public ImageLoader(ListView listview) {
        mListView = listview;
        mTask = new HashSet<>();
        //先设置cache的可用内存，我们将运行内存的 / 4  设置为cache的可用内存
        //获取最大可用内存；
        int maxMemory = (int) Runtime.getRuntime().maxMemory();
        int cacheSize = maxMemory / 4;
        //初始化lurcache
        mCache = new LruCache<String, Bitmap>(cacheSize){
            @Override
            protected int sizeOf(String key, Bitmap value) {
                //在每次存入缓存的时候调用
                return value.getByteCount();
            }
        };
    }
    //把bitmap 存入缓存
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
    public void addBitmapToCache(String url, Bitmap bitmap) {

        if(getBitmapFromCache(url) == null){
            mCache.put(url,bitmap);
        }
    }
    //通过url 从缓存中获得对应的bitmap
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
    public Bitmap getBitmapFromCache(String url){
        return mCache.get(url);
    }

    private android.os.Handler mHandler = new android.os.Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (mImageView.getTag().equals(mUrl))
            {
                mImageView.setImageBitmap((Bitmap) msg.obj);
            }
        }
    };

    public void showImageByThread (ImageView imageView, final String url) {
        mImageView = imageView;
        mUrl = url;
        new Thread(){
            @Override
            public void run() {
                super.run();
                Bitmap bitmap = getBitmapFromUrl(url);
                Message message = Message.obtain();
                message.obj = bitmap;
                mHandler.sendMessage(message);
            }
        }.start();
    }
    public Bitmap getBitmapFromUrl (String urlstring){
        Bitmap bitmap = null;
        InputStream is = null;
        BufferedInputStream isr = null;
        try {
            //URL url = new URL(urlstring);
            //HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            is = new URL(urlstring).openStream();
            isr = new BufferedInputStream(is);
            bitmap = BitmapFactory.decodeStream(isr);
            if(is != null){
                is.close();
            }
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }finally {
            try {
                if(isr != null){
                    isr.close();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return bitmap;
    }

    public void showImageByAsyncTask (ImageView imageView, final String url){
        //从缓存中取出对应的图片
        Bitmap bitmap = getBitmapFromCache(url);
        //如果缓存中没有，那么必须去下载
        if(bitmap == null){
            imageView.setImageResource(R.mipmap.ic_launcher);
        }else {
            imageView.setImageBitmap(bitmap);
        }
    }
    //停止所有tasks
    public void cancelAllTasks() {
        if(mTask != null){
            for (imageAsyncTask task : mTask){
                task.cancel(false);
            }
        }
    }
    //用来加载从start到end的所有图片
    public void loadImage(int start, int end){
        for (int i = start; i < end; i ++){
            String url = Newsadapter.URLS[i];
            //从缓存中取出对应的图片
            Bitmap bitmap = getBitmapFromCache(url);
            //如果缓存中没有，那么必须去下载
            if(bitmap == null){
                imageAsyncTask task = new imageAsyncTask(url);
                task.execute(url);
                mTask.add(task);
            }else {
                ImageView imageView = (ImageView) mListView.findViewWithTag(url);
                imageView.setImageBitmap(bitmap);
            }
        }

    }
    class imageAsyncTask extends AsyncTask<String,Void,Bitmap>{
        //private ImageView imageView1;
        private String url1;
        public imageAsyncTask (String url){
            //imageView1 = imageView;
            url1 = url;
        }
        @Override
        protected Bitmap doInBackground(String... params) {
            String url = params[0];
            Bitmap bitmap = getBitmapFromUrl(url);
            if(bitmap != null){
                addBitmapToCache(url,bitmap);
            }

            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            ImageView imageview = (ImageView) mListView.findViewWithTag(url1);
            if (imageview != null && bitmap != null)
            {
                imageview.setImageBitmap(bitmap);
            }
            mTask.remove(this);
        }
    }
}
