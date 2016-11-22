package com.perfei.shidai;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("时代");
    }

    public void clickTo(View view) {
        switch (view.getId()) {

            case R.id.yichang:
                startActivity(new Intent(MainActivity.this,YichangActivity.class));
                break;

            case R.id.erchang:
                startActivity(new Intent(MainActivity.this,ErchangActivity.class));
                break;

            case R.id.sanchang:
                startActivity(new Intent(MainActivity.this,EGActivity.class));
                break;
        }
    }
}
