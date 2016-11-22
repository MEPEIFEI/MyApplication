package com.lmapp.myapplication;


import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

/**
 *  Created by perfei on 2016/7/1 0001.
 */
public class jiyouFragment extends Fragment {
    private ListView mListView;
    private MyAdapter mAdapter;
    private ArrayList<Info> mLists;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.jiyou_fragment, container, false);
        mListView = (ListView) view.findViewById(R.id.listView3);
        mLists = new ArrayList<>();
        String sql = "select * from tb_shouchang ";
        //String[] strs = new String[];
        new DbAsyncTask().execute(sql);
        return view;
    }

    class DbAsyncTask extends AsyncTask<String, Void, ArrayList<Info>> {

        /*private String[] selectionArgs;

        public DbAsyncTask(String[] selectionArgs) {
            this.selectionArgs = selectionArgs;
        }*/

        @Override
        protected ArrayList<Info> doInBackground(String... params) {
            return getLists(params[0]);
        }

        @Override
        protected void onPostExecute(ArrayList<Info> infos) {
            super.onPostExecute(infos);
            mLists = infos;
            mAdapter = new MyAdapter(getActivity(), infos);
            mListView.setAdapter(mAdapter);
            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(getActivity(), jiyouInfoActivity.class);
                    intent.putExtra("info", mLists.get(position));
                    startActivity(intent);
                }
            });
        }

        private ArrayList<Info> getLists(String param) {
            ArrayList<Info> list = new ArrayList<>();
            //通过传入的数据库语句来查询 获得curson，解析curson放入Info，再add进list
            // SqlHelper sqlHelper = new SqlHelper();
            //  SQLiteDatabase db = sqlHelper.openDatabase(mContext);
            // Cursor c = db.rawQuery("select * from tb_students where student_num=?",new String[]{"131100115"});
            //SqlHelper sqlhelper = new SqlHelper();
            SQLiteDatabase db = SqlHelper.openDatabase(getActivity());
            //Cursor c = db.rawQuery("select * from tb_students where student_num=131100114",null);
            //Cursor c = db.query("tb_students",new String[]{"name","student_num"},
            //       "student_num=?",new String[]{"131100114"},null,null,null);
            Cursor c = null;
            if (db != null) {
                c = db.rawQuery(param, null);
            }
            if (c != null) {
                while (c.moveToNext()) {
                    Info info = new Info();
                    //info.infoString[0] = "";
                    for (int i = 1; i < 38; i++) {
                        info.infoString[i] = c.getString(i);
                    }
                    list.add(info);
                }
                c.close();
            }
            return list;
        }

    }
}