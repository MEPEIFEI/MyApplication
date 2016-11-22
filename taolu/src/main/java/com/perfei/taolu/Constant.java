package com.perfei.taolu;


import android.graphics.Bitmap;
import android.os.Environment;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 常量类
 */
public class Constant {
    public static final String KEY ="1e86a65632769648c5b7c0af30a26de9";
    public static final String SECRET ="P93qn58x6m3YWu6h_EfYREpg6ORnJa_s";
    public static final String WX_APP_ID = "wx100bb38faa749fc1";
    public static final String HEAD_PATH = Environment.getExternalStorageDirectory().toString()+"/TAOLU/";
    public static void setPicToSd(Bitmap mBitmap) {
        String sdStatus = Environment.getExternalStorageState();
        if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用
            return;
        }
        FileOutputStream b = null;
        File file = new File(Constant.HEAD_PATH);
        File picfile = new File(Constant.HEAD_PATH + "head.jpg");
        if(!file.exists()){
            file.mkdirs();// 创建文件夹
        }
        if(picfile.exists()){
            picfile.delete();//如果文件存在，删除掉
        }
        String fileName =Constant.HEAD_PATH + "head.jpg";//图片名字

        try {
            b = new FileOutputStream(fileName);
            mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, b);// 把数据写入文件

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                //关闭流
                if (b != null){
                    b.flush();
                    b.close();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

}
