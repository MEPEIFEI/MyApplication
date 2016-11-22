package com.lmapp.myapplication;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.stream.HttpUrlGlideUrlLoader;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Administrator on 2016/6/29 0029.
 */
public class ImageLoder {

    private ImageView mImageView;
    private String mImagePath;
    //private String mImageName;
    private Context mContext;
    private File mImageFile;
    private static final String SDCard= Environment.getExternalStorageDirectory()+"";
    public static final String dir =SDCard+"/lmapp/";//文件存储的文件夹

    public void showImage(Context context,ImageView imageView,String url,String imageName){
        mImageView = imageView;
        mContext = context;
        //mImageName = imageName;
        mImagePath = dir + imageName+".jpg";
        mImageFile = new File(mImagePath);
        //查看头像文件是否存在
        //如果存在直接加载
        //不存在则利用url下载图片到sd卡里面
        if(mImageFile.exists()){
            Glide.with(mContext).load(mImageFile).error(R.mipmap.ic_launcher).fitCenter().into(imageView);
        }else {
                //new ImageLoaderAsyc().execute(url);
            File dirFile = new File(dir);
            if(!dirFile.exists()){
                dirFile.mkdir();
            }
            HttpUtils httpUtils = new HttpUtils();
            httpUtils.download(url, mImagePath, new RequestCallBack<File>() {
                @Override
                public void onSuccess(ResponseInfo<File> responseInfo) {
                    Glide.with(mContext).load(mImageFile).error(R.mipmap.ic_launcher).fitCenter().into(mImageView);
                }

                @Override
                public void onFailure(HttpException e, String s) {

                }
            });
        }
    }


}
