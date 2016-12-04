package com.example.cieo233.unittest;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Cieo233 on 12/3/2016.
 */

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {
    @BindView(R.id.signup_title)
    TextView signup_title;
    @BindView(R.id.input_layout_id)
    TextInputLayout input_layout_id;
    @BindView(R.id.edit_id)
    EditText edit_id;
    @BindView(R.id.input_layout_password)
    TextInputLayout input_layout_password;
    @BindView(R.id.edit_password)
    EditText edit_password;
    @BindView(R.id.input_layout_confirm)
    TextInputLayout input_layout_confirm;
    @BindView(R.id.edit_confirm)
    EditText edit_confirm;
    @BindView(R.id.btn_signup)
    Button btn_signup;
    @BindView(R.id.text_login)
    TextView text_login;
    ProgressDialog progressDialog;
    String[] return_value = {"注册成功", "用户名已被使用", "未知错误"};
    private Handler handler;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.bind(this);
        init();
        setResponse();
    }

    void setResponse(){
        btn_signup.setOnClickListener(this);
        text_login.setOnClickListener(this);
    }

    void init() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                progressDialog.dismiss();
                showToast(return_value[msg.what]);
                switch (msg.what) {
                    case 0:
                        startActivity(new Intent(getApplicationContext(),LoginActivty.class));
                        finish();
                        break;
                }
            }
        };
    }

    boolean valid() {
        String username = edit_id.getText().toString();
        String password = edit_password.getText().toString();
        String confirm = edit_confirm.getText().toString();
        if (username.isEmpty()) {
            input_layout_id.setError("用户名不能为空");
            input_layout_id.setErrorEnabled(true);
        } else {
            input_layout_id.setErrorEnabled(false);
        }
        if (password.isEmpty()) {
            input_layout_password.setError("密码不能为空");
            input_layout_password.setErrorEnabled(true);
        } else {
            input_layout_password.setErrorEnabled(false);
        }
        if (confirm.isEmpty()) {
            input_layout_confirm.setError("请再次输入密码");
            input_layout_confirm.setErrorEnabled(true);
        } else if (!Objects.equals(confirm, password)){
            input_layout_confirm.setError("输入密码不一致");
            input_layout_confirm.setErrorEnabled(true);
        }else {
            input_layout_confirm.setErrorEnabled(false);
        }
        return !username.isEmpty() && !password.isEmpty() && !confirm.isEmpty() && Objects.equals(confirm, password);
    }

    void showToast(String content) {
        Toast.makeText(this, content, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_signup:
                if (valid()) {
                    progressDialog.show();
                    OkHttpClient mOkHttpClient = new OkHttpClient();
                    RequestBody formBody = new FormBody.Builder()
                            .add("username", edit_id.getText().toString())
                            .add("password", edit_password.getText().toString())
                            .build();
                    Request request = new Request.Builder()
                            .url("http://api.sysu.space/api/user/register")
                            .post(formBody)
                            .addHeader("Content-Type", "application/x-www-form-urlencoded")
                            .build();
                    Call call = mOkHttpClient.newCall(request);
                    call.enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            e.printStackTrace();
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            String json = response.body().string();
                            int result = 2;
                            Log.e("WOCAO", json);

                            try {
                                JSONObject jsonObject = new JSONObject(json);
                                if (jsonObject.getInt("ret") == StateCode.NAME_HAS_BEEN_USED) {
                                    result = 1;
                                } else if (jsonObject.getInt("ret") == StateCode.OK) {
                                    result = 0;
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            handler.sendEmptyMessage(result);
                        }

                    });
                }
                break;
            case R.id.text_login:
                startActivity(new Intent(this,LoginActivty.class));
                finish();
                break;
        }
    }
}
