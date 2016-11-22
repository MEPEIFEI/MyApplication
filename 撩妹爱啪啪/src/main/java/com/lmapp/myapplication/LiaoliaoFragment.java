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
import android.widget.Toast;

/**
 * Created by Administrator on 2016/7/1 0001.
 */
public class LiaoliaoFragment extends Fragment implements View.OnClickListener{

    private EditText et_xueyuan,et_nianji;
    private Button bt_jbzm,bt_nianji,bt_xueyuan,bt_jql;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.liaoliao_fragment, container, false);
        init_view(view);
        event_view();
        return view;
    }

    private void event_view() {
        bt_jbzm.setOnClickListener(this);
        bt_nianji.setOnClickListener(this);
        bt_xueyuan.setOnClickListener(this);
        bt_jql.setOnClickListener(this);
    }

    private void init_view(View view) {
        et_xueyuan = (EditText) view.findViewById(R.id.et_xueyuan);
        et_nianji = (EditText) view.findViewById(R.id.et_nianji);

        bt_jbzm = (Button) view.findViewById(R.id.bt_jbzm);
        bt_nianji = (Button) view.findViewById(R.id.bt_nianji);
        bt_xueyuan = (Button) view.findViewById(R.id.bt_xueyuan);
        bt_jql = (Button) view.findViewById(R.id.bt_jql);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_jbzm:
                String sql = "";
                sql = "select * from tb_students where sex=? order by RANDOM() limit 5 ";
                String[] Strs = {"女"};
                Intent intent = new Intent(getActivity(),ListActivity.class);
                intent.putExtra("sql",sql);
                intent.putExtra("strs",Strs);
                startActivity(intent);
                break;
            case R.id.bt_nianji:
                String nianji = et_nianji.getText().toString();
                if(!nianji.equals("")){
                    switch (nianji){
                        case "11" :
                        case "12" :
                        case "13" :
                        case "14" :
                            nianjiliao(nianji);
                            break;
                        default:
                            Toast.makeText(getActivity(),"请输入年级，11~14",Toast.LENGTH_LONG).show();
                            break;
                    }
                }
                break;
            case R.id.bt_xueyuan:
                String xueyuan = et_xueyuan.getText().toString();
                if(!xueyuan.equals("")){
                    xueyuanLiao(xueyuan);
                }else{
                    Toast.makeText(getActivity(),"请输入学院全称",Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.bt_jql:
                String nianji1 = et_nianji.getText().toString();
                String xueyuan1 = et_xueyuan.getText().toString();
                if(!nianji1.equals("")){
                    if(!xueyuan1.equals("")){
                        jqliao(nianji1,xueyuan1);
                    }else{
                        Toast.makeText(getActivity(),"请输入学院全称",Toast.LENGTH_LONG).show();
                    }
                }else{
                    Toast.makeText(getActivity(),"请输入年级，11~14",Toast.LENGTH_LONG).show();
                }
                break;
            default:
                break;
        }

    }

    private void jqliao(String nianji1, String xueyuan1) {
        String sql = "";
        sql = "select * from tb_students where sex=? and academy like ? and student_num like ? order by RANDOM() limit 5";
        String[] Strs = {"女","%"+xueyuan1+"%",nianji1+"%"};
        Intent intent = new Intent(getActivity(),ListActivity.class);
        intent.putExtra("sql",sql);
        intent.putExtra("strs",Strs);
        startActivity(intent);
    }

    private void xueyuanLiao(String xueyuan) {
        String sql = "";
        sql = "select * from tb_students where sex=? and academy like ? order by RANDOM() limit 5";
        String[] Strs = {"女","%"+xueyuan+"%"};
        Intent intent = new Intent(getActivity(),ListActivity.class);
        intent.putExtra("sql",sql);
        intent.putExtra("strs",Strs);
        startActivity(intent);
    }

    private void nianjiliao(String nianji) {
        String sql = "";
        sql = "select * from tb_students where sex=? and student_num like ? order by RANDOM() limit 5";
        String[] Strs = {"女",nianji+"%"};
        Intent intent = new Intent(getActivity(),ListActivity.class);
        intent.putExtra("sql",sql);
        intent.putExtra("strs",Strs);
        startActivity(intent);
    }
}
