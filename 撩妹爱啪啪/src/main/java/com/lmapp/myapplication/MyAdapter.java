package com.lmapp.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/6/25 0025.
 */
public class MyAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Info> lists;
    private LayoutInflater inflater;

    public MyAdapter (Context context,ArrayList<Info> lists){
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
        ImageLoder imageLoder = new ImageLoder();
        ViewHolder viewHolder;
        if(convertView == null){
            convertView = inflater.inflate(R.layout.item_list,null);
            viewHolder = new ViewHolder();
            viewHolder.pic = (ImageView) convertView.findViewById(R.id.pic);
            viewHolder.tv_name = (TextView)convertView.findViewById(R.id.name);
            viewHolder.tv_xueyuan = (TextView)convertView.findViewById(R.id.xueyuan);
            viewHolder.tv_xuehao = (TextView)convertView.findViewById(R.id.xuehao);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.pic.setImageResource(R.mipmap.ic_launcher);
        String url = lists.get(position).infoString[37];
        String imageName = lists.get(position).infoString[1];
        imageLoder.showImage(context,viewHolder.pic,url,imageName);
        viewHolder.tv_name.setText(lists.get(position).infoString[2]);
        viewHolder.tv_xueyuan.setText(lists.get(position).infoString[5]);
        viewHolder.tv_xuehao.setText(lists.get(position).infoString[1]);

        return convertView;
    }
    class ViewHolder {

        public ImageView pic;
        public TextView tv_name, tv_xuehao,tv_xueyuan;
    }
}
