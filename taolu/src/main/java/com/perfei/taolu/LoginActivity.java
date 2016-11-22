package com.perfei.taolu;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.TextInputLayout;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.maxleap.GetDataCallback;
import com.maxleap.LogInCallback;
import com.maxleap.MLFile;
import com.maxleap.MLFileManager;
import com.maxleap.MLLog;
import com.maxleap.MLUser;
import com.maxleap.MLUserManager;
import com.maxleap.SignUpCallback;
import com.maxleap.exception.MLException;
import com.perfei.taolu.utils.BaseActivity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


public class LoginActivity extends BaseActivity {

    public static final String TAG = LoginActivity.class.getName();

    private EditText mEmailEditText;
    private EditText mPasswordEditText;
    private TextInputLayout mEmailTextInputLayout;
    private TextInputLayout mPasswordTextInputLayout;
    private String mname,msex,msignature,mage;
    private MLFile mlFile ;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        MLUser user = MLUser.getCurrentUser();
        if (null != user) {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        }

        setUpToolbar();
        mActionBar.setDisplayHomeAsUpEnabled(false);
        mEmailEditText = (EditText) findViewById(R.id.email_edit_text);
        mEmailTextInputLayout = (TextInputLayout) findViewById(R.id.email_input_layout);
        mPasswordEditText = (EditText) findViewById(R.id.password_edit_text);
        mPasswordTextInputLayout = (TextInputLayout) findViewById(R.id.password_input_layout);
        mEmailTextInputLayout.setErrorEnabled(true);
        mPasswordTextInputLayout.setErrorEnabled(true);

        findViewById(R.id.login_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                if (validate()) {
                    MLUserManager.logInInBackground(mEmailEditText.getText().toString(),
                            mPasswordEditText.getText().toString(),
                            new LogInCallback() {
                                @Override
                                public void done(final MLUser mlUser, final MLException e) {
                                    if (e != null) {
                                        MLLog.e(TAG, e);
                                        Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                        return;
                                    }
                                    //取出个人信息和头像
                                    mname = mlUser.getString("name");
                                    mage = mlUser.getString("age");
                                    msex = mlUser.getString("sex");
                                    msignature = mlUser.getString("signature");
                                    mlFile = mlUser.getMLFile("head_photo");
                                    //服务器取用户头像，下载到sd卡里面
                                    if(mlFile == null){
                                        //把默认头像写到sd卡里面
                                        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.touxiang);
                                        Constant.setPicToSd(bitmap);
                                    }else{
                                        //取出服务器的头像写到sd里面
                                        MLFileManager.getDataInBackground(mlFile, new GetDataCallback() {
                                            @Override
                                            public void done(byte[] bytes, MLException e) {
                                                //把二进制文件写到sd卡
                                                if (e != null) {
                                                    MLLog.e(TAG, e);
                                                    Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                                    return;
                                                }
                                                Bitmap mBitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                                                Constant.setPicToSd(mBitmap);
                                            }
                                        });
                                    }
                                    //创建sharedpreferences 并初始化sharedpreferences
                                    SharedPreferences spf = getSharedPreferences("person_info", MODE_PRIVATE);
                                    SharedPreferences.Editor editor = spf.edit();
                                    if(mname != ""){
                                        editor.putString("name",mname);
                                    }else{
                                        editor.putString("name",mEmailEditText.getText().toString());
                                    }
                                    editor.putString("sex",msex);
                                    editor.putString("age",mage);
                                    editor.putString("signature",msignature);
                                    editor.commit();
                                    Toast.makeText(LoginActivity.this, "登陆成功", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                    finish();
                                }
                            });

                }
            }
        });

        findViewById(R.id.signup_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                startActivity(new Intent(LoginActivity.this,SignupActivity.class));
                //finish();
            }
        });
        //findViewById(R.id.reset_button).setOnClickListener(new View.OnClickListener() {
        //    @Override
        //    public void onClick(final View v) {
                //startActivity(new Intent(mContext, MainActivity.class));
        //    }
       // });
    }

    private boolean validate() {
        mEmailTextInputLayout.setError("");
        mPasswordTextInputLayout.setError("");
        if (TextUtils.isEmpty(mEmailEditText.getText().toString())) {
            mEmailTextInputLayout.setError(getString(R.string.err_require_email));
            return false;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(mEmailEditText.getText().toString()).matches()) {
            mEmailTextInputLayout.setError(getString(R.string.err_invalid_email));
            return false;
        }
        if (TextUtils.isEmpty(mPasswordEditText.getText().toString())) {
            mPasswordTextInputLayout.setError(getString(R.string.err_require_password));
            return false;
        }
        return true;
    }

}
