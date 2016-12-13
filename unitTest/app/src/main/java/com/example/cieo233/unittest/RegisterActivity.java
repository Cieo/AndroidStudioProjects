package com.example.cieo233.unittest;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Cieo233 on 12/3/2016.
 */

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    @BindView(R.id.signup_title)
    TextView signup_title;
    @BindView(R.id.edit_id)
    EditText edit_id;
    @BindView(R.id.edit_password)
    EditText edit_password;
    @BindView(R.id.edit_confirm)
    EditText edit_confirm;
    @BindView(R.id.btn_signup)
    Button btn_signup;
    @BindView(R.id.text_login)
    TextView text_login;
    private String username, password, confirm;
    private Handler registerHandler;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        init();
        setResponse();
    }

    void setResponse(){
        btn_signup.setOnClickListener(this);
        text_login.setOnClickListener(this);
    }

    void onRegisterFinish(){
        startActivity(new Intent(this,LoginActivity.class));
        finish();
    }

    void init() {
        registerHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message message) {
                switch (message.what){
                    case StateCode.NAME_HAS_BEEN_USED:
                        Log.e("Register","用户名已被使用");
                        break;
                    case StateCode.OK:
                        Log.e("Register","注册成功");
                        onRegisterFinish();
                        break;
                }
                return false;
            }
        });
    }

    boolean valid() {
        username = edit_id.getText().toString();
        password = edit_password.getText().toString();
        confirm = edit_confirm.getText().toString();
        if (username.isEmpty()) {
            edit_id.setError("用户名不能为空");
        }
        if (password.isEmpty()) {
            edit_password.setError("密码不能为空");
        }
        if (confirm.isEmpty()) {
            edit_confirm.setError("请再次输入密码");
        } else if (!Objects.equals(confirm, password)){
            edit_confirm.setError("输入密码不一致");
        }
        return !username.isEmpty() && !password.isEmpty() && !confirm.isEmpty() && Objects.equals(confirm, password);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_signup:
                if (valid()) {
                    CodoAPI.userRegister(new User(username,password),registerHandler);
                }
                break;
            case R.id.text_login:
                finish();
                break;
        }
    }
}
