package com.lmapp.myapplication;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class MainActivity extends Activity {

    private TextView tv;
    private Button bt;
    private EditText et;
    private ImageView iv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //设置toolbar
        init();
        //setUpToolbar();
       // if（mActionBar.）{
            //mActionBar.setDisplayHomeAsUpEnabled(false);
        //}

    }

    private void init() {
        tv = (TextView) findViewById(R.id.text);
        bt = (Button) findViewById(R.id.button);
        et = (EditText) findViewById(R.id.editText);
        iv = (ImageView) findViewById(R.id.imageView);


        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //String sql = "select * from testbiao where student_num = '131100114'";
                //String num = et.getText().toString();
                //new Sqlascyn().execute(num);
                String url = "http://image.baidu.com/search/detail?ct=503316480&z=0&ipn=d&word=%E5%9B%BE%E7%89%87&pn=1&spn=0&di=29410147800&pi=&rn=1&tn=baiduimagedetail&ie=utf-8&oe=utf-8&cl=2&lm=-1&cs=3432927473%2C3545292754&os=801572895%2C478256987&simid=58340231%2C622669418&adpicid=0&ln=30&fr=ala&fm=&sme=&cg=&bdtype=0&oriquery=&objurl=http%3A%2F%2Fpic41.nipic.com%2F20140509%2F18285693_231156450339_2.jpg&fromurl=ippr_z2C%24qAzdH3FAzdH3Fooo_z%26e3Bgtrtv_z%26e3Bv54AzdH3Ffi5oAzdH3F8adml98d_z%26e3Bip4s&gsm=0";
                Glide.with(MainActivity.this)
                        .load(url)
                        //.error(R.mipmap.ic_launcher)
                        .into(iv);
            }
        });
    }
    class Sqlascyn extends AsyncTask<String,Void,String>{

        @Override
        protected String doInBackground(String... params) {

            SqlHelper sqlhelper = new SqlHelper();
            SQLiteDatabase db = sqlhelper.openDatabase(MainActivity.this);
            Cursor c = db.rawQuery("select * from tb_students where student_num=?",new String[]{params[0]});
            String name = "";

            //if(c.moveToFirst()){
            //    name = c.getString(3);
            //}
            while (c.moveToNext()) {
                Info info = new Info();
                //info.infoString[0] = "";
                for(int i = 1; i < 38; i ++){
                    info.infoString[i] = c.getString(i);
                }
                //list.add(info);
                name = info.infoString[2];
            }
            c.close();
            return name ;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            tv.setText(s);
        }
    }
}
