package com.lmapp.myapplication;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.File;
import java.util.ArrayList;

/**
 *
 * Created by Administrator on 2016/6/27 0027.
 */
public class jiyouInfoActivity extends BaseActivity {

    private ImageView mImageView;
    private ListView mListview;
    private Menu mMenu;
    private Handler mHandle = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0:
                    Toast.makeText(mContext,"删除成功",Toast.LENGTH_LONG).show();
                    break;
                case 1:
                    Toast.makeText(mContext,"已经删除",Toast.LENGTH_LONG).show();
                    break;
            }
        }
    };
    //private ArrayList<String> mLists;
    private Info mInfo;
    public String[] Strings = {"学号:","姓名:","曾用名:","性别:","学院:",
            "专业:","行政班:","短号:","长号:","年级:",
            "宿舍楼:","宿舍号:","床号:","出生日期:","政治面貌:",
            "民族:","籍贯:","户口所在地:","学生类别:","学制:",
            "学习年限:","学籍状态:","专业方向:","入学日期:","毕业中学:",
            "联系电话:","准考证号:","身份证号:","专业代码:","层次:",
            "考生类别:","考生号:","家庭所在地:","来源省:","托管学院:",
            "国家统编专业代码:",};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.info_activity);
        setUpToolbar();
        setTitle("妹(汉)纸详情");
        init();
        Intent intent = getIntent();
        mInfo = (Info) intent.getSerializableExtra("info");
        //url = mInfo.infoString[37];
        String imageName = mInfo.infoString[1];
        String imagePath = Environment.getExternalStorageDirectory().toString()+"/lmapp/"+imageName+".jpg";
        Glide.with(mContext).load(new File(imagePath)).into(mImageView);
        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext,ImageActivity.class);
                intent.putExtra("infoimage",mInfo);
                startActivity(intent);
            }
        });
        new MyAscy().execute(mInfo);

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        mMenu = menu;
        getMenuInflater().inflate(R.menu.menu_main1, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //此处进行Item逻辑判断处理
        switch (item.getItemId()) {
            case R.id.action_delete:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        SQLiteDatabase db = new SqlHelper().openDatabase(mContext);
                        //String sql = "delete from tb_shouchang where  " ;
                        int delete = db.delete("tb_shouchang", "student_num = ?", new String[]{mInfo.infoString[1]});
                        //.execSQL(sql);
                        if(delete<=0){
                            db.close();
                            mHandle.sendEmptyMessage(1);
                        }
                        db.close();
                        mHandle.sendEmptyMessage(0);

                    }
                }).start();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    private void init() {
        mImageView = (ImageView) findViewById(R.id.touxiang);
        mListview = (ListView) findViewById(R.id.listView2);
        //mLists = new ArrayList<String>();
    }

    class MyAscy extends AsyncTask<Info,Void,ArrayList<String>>{

        @Override
        protected ArrayList<String> doInBackground(Info... params) {
            ArrayList<String> lists = new ArrayList<String>();
            String string = "";
            for(int i = 0; i < 36; i++){
                string = Strings[i] +"  "+ params[0].infoString[i+1];
                lists.add(string);
            }
            return lists;
        }

        @Override
        protected void onPostExecute(ArrayList<String> strings) {
            super.onPostExecute(strings);
            InfoAdapter infoAdapter = new InfoAdapter(mContext,strings);
            mListview.setAdapter(infoAdapter);
        }
    }
}
