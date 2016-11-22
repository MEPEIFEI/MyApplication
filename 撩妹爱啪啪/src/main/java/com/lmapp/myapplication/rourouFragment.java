package com.lmapp.myapplication;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by Administrator on 2016/7/1 0001.
 */
public class rourouFragment extends Fragment implements View.OnClickListener {
    private EditText et_xuehao,et_xingming;
    private Button bt_xuehao,bt_xingming;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.rourou_fragment,container,false);
        init_view(view);
        event_view();
        return view;

    }

    private void event_view() {
        bt_xuehao.setOnClickListener(this);
        bt_xingming.setOnClickListener(this);
    }

    private void init_view(View view) {
        et_xuehao = (EditText) view.findViewById(R.id.et_xhrou);
        et_xingming = (EditText) view.findViewById(R.id.et_xmrou);
        bt_xingming = (Button) view.findViewById(R.id.bt_xmrou);
        bt_xuehao = (Button) view.findViewById(R.id.bt_xhrou);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_xmrou:
                String xm = et_xingming.getText().toString();
                xmRou(xm);
                break;
            case R.id.bt_xhrou:
                String xh = et_xuehao.getText().toString();
                xhRou(xh);
                break;
            default:
                break;
        }

    }

    private void xhRou(String xh) {
        String sql = "";
        sql = "select * from tb_students where student_num=?";
        String[] Strs = {xh};
        Intent intent = new Intent(getActivity(),ListActivity.class);
        intent.putExtra("sql",sql);
        intent.putExtra("strs",Strs);
        startActivity(intent);
    }

    private void xmRou(String xm) {
        String sql = "";
        sql = "select * from tb_students where name=?";
        String[] Strs = {xm};
        Intent intent = new Intent(getActivity(),ListActivity.class);
        intent.putExtra("sql",sql);
        intent.putExtra("strs",Strs);
        startActivity(intent);
    }
}
