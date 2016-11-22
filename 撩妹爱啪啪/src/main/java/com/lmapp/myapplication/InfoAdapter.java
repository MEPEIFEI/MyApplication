package com.lmapp.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/6/25 0025.
 */
public class InfoAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<String> lists;
    private LayoutInflater inflater;

    public InfoAdapter(Context context, ArrayList<String> lists){
        this.context = context;
        this.lists = lists;
        inflater = LayoutInflater.from(context);
    }
    @Override
    public int getCount() {

        return lists.size();
    }

    @Override
    public Object getItem(int position) {
        return lists.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if(convertView == null){
            convertView = inflater.inflate(R.layout.info_item,null);
            viewHolder = new ViewHolder();
            viewHolder.tv = (TextView)convertView.findViewById(R.id.itemtv);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tv.setText(lists.get(position));
        return convertView;
    }
    class ViewHolder {

        public TextView tv;
    }
}
