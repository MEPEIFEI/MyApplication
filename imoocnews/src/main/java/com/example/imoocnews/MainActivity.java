package com.example.imoocnews;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity implements AdapterView.OnItemClickListener {

    private ListView mListView;
    private ProgressBar mProgressBar;
    private List<NewsBean> mNewsBeanList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mListView = (ListView) findViewById(R.id.listView);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        //执行NewAsyncTask的线程
        String url = "http://www.imooc.com/api/teacher?type=4&num=30";
        new NewsAsyncTask().execute(url);
        mListView.setOnItemClickListener(this);
    }

    //传入url  通过new URL(url).openStream()传入字节流，readstring（）方法 获得jsonstring，
    //再解析jsonstring数据。。。。
    private List<NewsBean> getJson(String url) {
        List<NewsBean> newsBeanList = new ArrayList<>();
        try {
            String jsstr = readstream(new URL(url).openStream());
            JSONObject mJSONObject;
            NewsBean mNewsBean ;
            try {
                //解析json字符，加入mNewsBeanList
                mJSONObject = new JSONObject(jsstr);
                JSONArray jsonArray = mJSONObject.getJSONArray("data");
                for (int i=0;i < jsonArray.length();i++){
                    mJSONObject = jsonArray.getJSONObject(i);
                    mNewsBean = new NewsBean();
                    mNewsBean.newsImageUrl = mJSONObject.getString("picSmall");
                    mNewsBean.newsContent = mJSONObject.getString("description");
                    mNewsBean.newsTitle = mJSONObject.getString("name");
                    mNewsBean.newsBigImageUrl = mJSONObject.getString("picBig");
                    newsBeanList.add(mNewsBean);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
        return newsBeanList;
    }


    // 传入inputstring 字节流转成isr字符流
    // 再通过BufferedReader.readline（）读取json字符数据 返回result
    private String readstream(InputStream is){
        InputStreamReader isr;
        String result = "";
        try {
            String line ;
            isr = new InputStreamReader(is,"utf-8");
            BufferedReader br = new BufferedReader(isr);
            while((line = br.readLine())!=null){
                result += line;
            }
            br.close();
            isr.close();
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this,Image_activity.class);
        intent.putExtra("url",mNewsBeanList.get(position).newsBigImageUrl);
        intent.putExtra("title",mNewsBeanList.get(position).newsTitle);
        intent.putExtra("content",mNewsBeanList.get(position).newsContent);
        startActivity(intent);
    }


    class NewsAsyncTask extends AsyncTask<String,Void,List<NewsBean>>{


        @Override
        protected List<NewsBean> doInBackground(String... params) {
            //传入一个String 类型的url  通过getJson（）方法 返回list<NewsBean> 类型
            return getJson(params[0]);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(List<NewsBean> newsBeen) {
            super.onPostExecute(newsBeen);
            mProgressBar.setVisibility(View.GONE);
            mNewsBeanList = newsBeen;
            Newsadapter adapter = new Newsadapter(mNewsBeanList,MainActivity.this,mListView);
            mListView.setAdapter(adapter);
        }
    }

}

