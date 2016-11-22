package com.perfei.taolu;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.maxleap.MLDataManager;
import com.maxleap.MLFile;
import com.maxleap.MLFileManager;
import com.maxleap.MLUser;
import com.maxleap.SaveCallback;
import com.maxleap.exception.MLException;
import com.perfei.taolu.utils.BaseActivity;
import com.perfei.taolu.utils.CircleImageView;

import java.io.ByteArrayOutputStream;

/**
 * Created by Administrator on 2016/5/24.
 */
public class PersonActivity extends BaseActivity {

    private EditText name,signature,age;
    private TextView sex ;
    private String mname,msex,msignature,mage;
    private CircleImageView mCircleImageView;
    private Bitmap head;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_info);
        init_view();
        setUpToolbar();
        setTitle("个人信息");//设置Toolbar标题
        mActionBar.setDisplayHomeAsUpEnabled(true);
        mActionBar.setHomeButtonEnabled(true);

    }
    public void updata_head(View view){
        //去相册选择图片
        switch (view.getId()){
            case R.id.hciv:
                Intent intent = new Intent(Intent.ACTION_PICK, null);
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(intent, 1);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 1 :
                if (resultCode == RESULT_OK) {
                    cropPhoto(data.getData());//裁剪图片
                }
                break;
            case 2 :
                if (data != null) {
                    MLUser mlUser = MLUser.getCurrentUser();
                    Bundle extras = data.getExtras();
                    head = extras.getParcelable("data");
                    if(head!=null){
                        Constant.setPicToSd(head);//保存在SD卡中
                        mCircleImageView.setImageBitmap(head);
                        //Constant.setPicToView(mContext,mCircleImageView);//circleImageView显示出来
                        /**
                         * 上传服务器代码
                         */
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        head.compress(Bitmap.CompressFormat.PNG, 100, stream);
                        byte[] image = stream.toByteArray();
                        // 创建MLFile对象
                        MLFile myFile = new MLFile(image);
                        // 上传
                        MLFileManager.saveInBackground(myFile, new SaveCallback() {
                            @Override
                            public void done(MLException e) {
                                if(e != null){
                                    Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_SHORT).show();
                                    return;
                                }
                            }
                        });
                        mlUser.put("head_photo",myFile);
                        MLDataManager.saveInBackground(mlUser, new SaveCallback() {
                            @Override
                            public void done(MLException e) {
                                if(e != null){
                                    Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_SHORT).show();
                                    return;

                                }

                            }
                        });


                    }
                }
                break;
        }
    }
    /**
     * 调用系统的裁剪
     * @param uri
     */
    public void cropPhoto(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 150);
        intent.putExtra("outputY", 150);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, 2);
    }
    //初始化控件
    private void init_view() {
        name = (EditText) findViewById(R.id.name);
        sex = (TextView) findViewById(R.id.sex);
        signature = (EditText) findViewById(R.id.signature);
        age = (EditText) findViewById(R.id.age);
        mCircleImageView = (CircleImageView) findViewById(R.id.hciv);
        SharedPreferences spf = getSharedPreferences("person_info",MODE_PRIVATE);
        name.setText(spf.getString("name",""));
        sex.setText(spf.getString("sex",""));
        signature.setText(spf.getString("signature",""));
        age.setText(spf.getString("age",""));
        Bitmap bitmap = BitmapFactory.decodeFile("/mnt/sdcard/TAOLU/head.jpg");
        mCircleImageView.setImageBitmap(bitmap);

        //Constant.setPicToView(this,mCircleImageView);
    }

    //optionMenus
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_person_info, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        //此处进行Item逻辑判断处理
        switch (item.getItemId()) {
            case R.id.action_save:
                mname = name.getText().toString();
                msex = sex.getText().toString();
                msignature = signature.getText().toString();
                mage = age.getText().toString();
                SharedPreferences spf = getSharedPreferences("person_info", MODE_PRIVATE);
                SharedPreferences.Editor editor = spf.edit();
                editor.putString("name",mname);
                editor.putString("sex",msex);
                editor.putString("age",mage);
                editor.putString("signature",msignature);
                editor.commit();
                //同步更新到服务器
                upData();
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    //设置性别
    public void setSex(View view){
        showSetSexDialog();
    }
     //设置并且显示dialog
    private void showSetSexDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("性别");
        builder.setItems(new String[]{"男", "女"}, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case 0 :
                        sex.setText("男");
                        dialog.dismiss();
                        break;
                    case 1 :
                        sex.setText("女");
                        dialog.dismiss();
                        break;

                }
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void upData() {
        MLUser user = MLUser.getCurrentUser();
        if (null != user) {
            user.put("name",mname);
            user.put("age",mage);
            user.put("sex",msex);
            user.put("signature",msignature);
            MLDataManager.saveInBackground(user);
        }
    }
}
