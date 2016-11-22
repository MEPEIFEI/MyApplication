package com.example.imoocnews;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;


public class Newsadapter extends BaseAdapter implements AbsListView.OnScrollListener {

    private List<NewsBean> mNewsBean;
    private LayoutInflater mLayoutInflater;
    private ImageLoader mImageLoader;
    private int mStart,mEnd;
    public static String[] URLS;
    private boolean mFirst;

    public Newsadapter (List<NewsBean> mNewsBean, Context context, ListView listView){
        this.mNewsBean =mNewsBean;
        mLayoutInflater =  LayoutInflater.from(context);
        mImageLoader = new ImageLoader(listView);
        URLS = new String[mNewsBean.size()];
        for (int i = 0;i < mNewsBean.size();i ++){
            URLS[i] = mNewsBean.get(i).newsImageUrl;
        }
        listView.setOnScrollListener(this);
        mFirst = true;
    }
    @Override
    public int getCount() {
        return mNewsBean.size();
    }

    @Override
    public Object getItem(int position) {
        return mNewsBean.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder = null;
        if(convertView == null){
            viewHolder = new ViewHolder();
            convertView = mLayoutInflater.inflate(R.layout.item,null);
            viewHolder.imageView = (ImageView) convertView.findViewById(R.id.imageView);
            viewHolder.tv_Title = (TextView) convertView.findViewById(R.id.tv_title);
            viewHolder.tv_Content = (TextView) convertView.findViewById(R.id.tv_content);
            convertView.setTag(viewHolder);

        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.imageView.setImageResource(R.mipmap.ic_launcher);
        //new ImageLoader().showImageByThread(viewHolder.imageView,
        //        url);
        viewHolder.imageView.setTag(mNewsBean.get(position).newsImageUrl);
        mImageLoader.showImageByAsyncTask(viewHolder.imageView,
                mNewsBean.get(position).newsImageUrl);
        viewHolder.tv_Title.setText(mNewsBean.get(position).newsTitle);
        viewHolder.tv_Content.setText(mNewsBean.get(position).newsContent);
        return convertView;
    }
    //滑动时调用的接口
    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

        if(scrollState == SCROLL_STATE_IDLE)
        {
            //加载可见项
            mImageLoader.loadImage(mStart,mEnd);
        }else {
            //停止任务
            mImageLoader.cancelAllTasks();
        }

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        mStart = firstVisibleItem;
        mEnd = firstVisibleItem + visibleItemCount;

        //预加载，只进入一次
        if(mFirst && visibleItemCount > 0){
            mImageLoader.loadImage(mStart,mEnd);
            mFirst = false;
        }

    }

    class ViewHolder {
        public ImageView imageView;
        public TextView tv_Title;
        public TextView tv_Content;
    }
}
