package com.lmapp.myapplication;


import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.io.File;

/**
 *
 * Created by Administrator on 2016/7/2 0002.
 */
public class ImageActivity extends BaseActivity {

    private ImageView img;
    private Info mInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_big);
        setUpToolbar();
        mActionBar.setDisplayHomeAsUpEnabled(true);
        setTitle("");
        img= (ImageView) findViewById(R.id.bigImage);
        Intent intent = getIntent();
        mInfo = (Info) intent.getSerializableExtra("infoimage");
        String imageName = mInfo.infoString[1];
        String imagePath = Environment.getExternalStorageDirectory().toString()+"/lmapp/"+imageName+".jpg";
        Glide.with(mContext).load(new File(imagePath)).into(img);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
