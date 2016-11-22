package com.example.imoocnews;


import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class Login_activity extends Activity implements View.OnClickListener{

    private EditText ed_username;
    private EditText ed_password;
    private CheckBox chkpass;
    private Button bt_login;
    private Button bt_cancel;

    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        init();
        mSharedPreferences = getSharedPreferences("Userinfo",MODE_PRIVATE );
        mEditor = mSharedPreferences.edit();
        String name = mSharedPreferences.getString("Username","");
        String pass = mSharedPreferences.getString("Password","");
        if(name != null){
            ed_username.setText(name);
        }
        if(pass == null){
            chkpass.setChecked(false);
        }
        else{
            chkpass.setChecked(true);
            ed_password.setText(pass);
        }
    }

    private void init() {
        ed_username = (EditText) findViewById(R.id.ed_username);
        ed_password = (EditText) findViewById(R.id.ed_pass);
        chkpass = (CheckBox) findViewById(R.id.checkBox);
        bt_login = (Button) findViewById(R.id.Login);
        bt_cancel = (Button) findViewById(R.id.cancel);
        bt_login.setOnClickListener(this);
        bt_cancel.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.Login :
                String name = ed_username.getText().toString().trim();
                String pass = ed_password.getText().toString().trim();
                if("admin".equals(name) && "123456".equals(pass)){
                    if(chkpass.isChecked()){
                        mEditor.putString("Username",name);
                        mEditor.putString("Password",pass);
                        mEditor.commit();
                    }else {
                        mEditor.remove("Username");
                        mEditor.remove("Password");
                        mEditor.commit();
                    }
                    Toast.makeText(this,"密码正确，正在登陆",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(this,MainActivity.class);
                    startActivity(intent);
                    finish();
                }
                else{
                    Toast.makeText(this,"密码错误，禁止登陆",Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.cancel :
                String name1 = ed_username.getText().toString().trim();
                String pass1 = ed_password.getText().toString().trim();

                if("".equals(name1) && "".equals(pass1)){
                    finish();
                }else{
                    ed_password.setText("");
                    ed_username.setText("");
                    Toast.makeText(this,"再按一次，退出程序",Toast.LENGTH_SHORT).show();
                }
                break;
                }

        }

    }

