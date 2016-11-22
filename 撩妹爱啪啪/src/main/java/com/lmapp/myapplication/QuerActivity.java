package com.lmapp.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/6/27 0027.
 */
public class QuerActivity extends BaseActivity {
    private Button bt;
    private Button bt1;
    private EditText et;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quer_activity);
        setUpToolbar();
        setTitle("撩妹爱啪啪");
        mActionBar.setDisplayHomeAsUpEnabled(false);
        bt = (Button) findViewById(R.id.button2);
        bt1 = (Button) findViewById(R.id.button3);
        et = (EditText) findViewById(R.id.editText2);
        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sql = "";
                String s = "";
                s = et.getText().toString();
                sql = "select * from tb_students where name=?";
                String[] Strs = {s};
                Intent intent = new Intent(QuerActivity.this,ListActivity.class);
                intent.putExtra("sql",sql);
                intent.putExtra("strs",Strs);
                startActivity(intent);
            }
        });
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sql = "";
                sql = "select * from tb_students where sex=? order by RANDOM() limit 5 ";
                String[] Strs = {"女"};
                Intent intent = new Intent(QuerActivity.this,ListActivity.class);
                intent.putExtra("sql",sql);
                intent.putExtra("strs",Strs);
                startActivity(intent);
            }
        });
    }
}
