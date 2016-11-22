package com.lmapp.myapplication;


import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import java.util.ArrayList;

/**
 * Created by Administrator on 2016/6/25 0025.
 */
public class ListActivity extends BaseActivity {

    private ListView mListView;
    private MyAdapter mAdapter;
    private ArrayList<Info> mLists;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_activity);
        setUpToolbar();
        setTitle("搜索结果");
        //初始化
        init();
        //Intent intent = new Intent();
        //intent.putExtra("info",mLists);

    }

    private void init() {
        mListView = (ListView) findViewById(R.id.listView);
        mLists = new ArrayList<Info>();
        Intent intent = getIntent();
        String sql = intent.getStringExtra("sql");
        String[] strs = intent.getStringArrayExtra("strs");
        new DbAsyncTask(strs).execute(sql);
    }

    class DbAsyncTask extends AsyncTask<String,Void,ArrayList<Info>>{

        private String[] selectionArgs;

        public DbAsyncTask(String[] selectionArgs){
            this.selectionArgs = selectionArgs;
        }

        @Override
        protected ArrayList<Info> doInBackground(String... params) {
            return getLists(params[0]);
        }

        @Override
        protected void onPostExecute(ArrayList<Info> infos) {
            super.onPostExecute(infos);
            mLists = infos;
            mAdapter = new MyAdapter(mContext,infos);
            mListView.setAdapter(mAdapter);
            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(mContext,InfoActivity.class);
                    intent.putExtra("info",mLists.get(position));
                    startActivity(intent);
                }
            });
        }

        private ArrayList<Info> getLists(String param) {
            ArrayList<Info> list = new ArrayList<Info>();
            //通过传入的数据库语句来查询 获得curson，解析curson放入Info，再add进list
           // SqlHelper sqlHelper = new SqlHelper();
          //  SQLiteDatabase db = sqlHelper.openDatabase(mContext);
           // Cursor c = db.rawQuery("select * from tb_students where student_num=?",new String[]{"131100115"});
            SqlHelper sqlhelper = new SqlHelper();
            SQLiteDatabase db = sqlhelper.openDatabase(ListActivity.this);
            //Cursor c = db.rawQuery("select * from tb_students where student_num=131100114",null);
            //Cursor c = db.query("tb_students",new String[]{"name","student_num"},
             //       "student_num=?",new String[]{"131100114"},null,null,null);
            Cursor c = db.rawQuery(param,selectionArgs);
            while (c.moveToNext()) {
                Info info = new Info();
                //info.infoString[0] = "";
                for(int i = 1; i < 38; i ++){
                    info.infoString[i] = c.getString(i);
                }
                list.add(info);
            }
            c.close();
            return list;
        }
    }




}
