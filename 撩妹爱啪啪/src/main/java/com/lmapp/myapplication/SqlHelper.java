package com.lmapp.myapplication;

import android.content.Context;
import android.content.res.AssetManager;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 这个类就是实现从assets目录读取数据库文件然后写入SDcard中,如果在SDcard中存在，
 * 就打开数据库，不存在就从assets目录下复制过去
 *
 * @author PERFEI
 */
class SqlHelper {
    //数据库存储路径
    private static final String FILEPATH = "data/data/com.lmapp.myapplication/databases/stdinfo.db";
    //数据库存放的文件夹 data/data/com.main.jh 下面
    private static final String PATHSTR = "data/data/com.lmapp.myapplication/databases";

    public static SQLiteDatabase openDatabase(Context context) {
        System.out.println("filePath:" + FILEPATH);
        File jhPath = new File(FILEPATH);
        //查看数据库文件是否存在
        if (jhPath.exists()) {
            Log.i("test", "存在数据库");
            //存在则直接返回打开的数据库
            return SQLiteDatabase.openOrCreateDatabase(jhPath, null);
        } else {
            //不存在先创建文件夹
            File path = new File(PATHSTR);
            Log.i("test", "pathStr=" + path);
            if (path.mkdir()) {
                Log.i("test", "创建成功");
            } else {
                Log.i("test", "创建失败");
            }
            FileOutputStream fos = null;
            InputStream is = null;
            try {
                //得到资源
                AssetManager am = context.getAssets();
                //得到数据库的输入流
                is = am.open("stdinfo.db");
                Log.i("test", is + "");

                fos = new FileOutputStream(jhPath);
                Log.i("test", "fos=" + fos);
                Log.i("test", "jhPath=" + jhPath);
                //创建byte数组  用于1KB写一次
                byte[] buffer = new byte[1024];
                int count;
                while ((count = is.read(buffer)) > 0) {
                    Log.i("test", "得到");
                    fos.write(buffer, 0, count);
                }
                //最后关闭就可以了
                fos.flush();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            } finally {
                if (fos != null) {
                    try {
                        fos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (is != null) {
                    try {
                        is.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            //如果没有这个数据库  我们已经把他写到SD卡上了，然后在执行一次这个方法 就可以返回数据库了
            return openDatabase(context);
        }
    }

    static void creatTable(Context context) {
        SQLiteDatabase db = new SqlHelper().openDatabase(context);
        //建收藏的表
        if (db != null) {
            db.execSQL("CREATE TABLE IF NOT EXISTS tb_shouchang (_id integer primary key autoincrement NOT NULL,  student_num text NOT NULL, name text  NOT NULL,  used_name text  NOT NULL,  sex text  NOT NULL,  academy text  NOT NULL,  major text  NOT NULL,  xingzhenban text  NOT NULL,  short_num text  NOT NULL,  long_num text  NOT NULL,  grade text  NOT NULL,  dormitory text  NOT NULL,  dormitory_num text  NOT NULL,  bed_num text  NOT NULL,  birthday_num text  NOT NULL,  state text  NOT NULL,  nation text  NOT NULL,  place text  NOT NULL,  residence text  NOT NULL,  student_type text  NOT NULL,  education text  NOT NULL,  student_period text  NOT NULL,  student_status text  ,  major_direct text  NOT NULL,  enter_school_date text  NOT NULL,  middle_school text  NOT NULL,  phone text  NOT NULL,  examinee_number text  NOT NULL,  id_number text  NOT NULL,  major_number text  NOT NULL,  level text  NOT NULL,  examinee_type text  NOT NULL,  examinee_num text  NOT NULL,  home_place text  NOT NULL,  province text  NOT NULL,  deposit text  NOT NULL ,  native_num text  NOT NULL,  image text  NOT NULL)");
        }
    }
}
