package com.example.cieo233.unittest;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Cieo233 on 12/3/2016.
 */

public class LoginActivty extends AppCompatActivity implements View.OnClickListener {
    @BindView(R.id.login_title)
    TextView login_title;
    @BindView(R.id.input_layout_id)
    TextInputLayout input_layout_id;
    @BindView(R.id.edit_id)
    EditText edit_id;
    @BindView(R.id.input_layout_password)
    TextInputLayout input_layout_password;
    @BindView(R.id.edit_password)
    EditText edit_password;
    @BindView(R.id.btn_login)
    Button btn_login;
    @BindView(R.id.text_create_account)
    TextView text_create_account;
    ProgressDialog progressDialog;
    String[] return_value = {"登陆成功", "用户名或密码错误", "未知错误"};
    private Handler handler;
    private int count;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        init();
        setResponse();
    }

    void init() {
        count = 0;
        progressDialog = new ProgressDialog(this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 0:
                        getAllChannel();
                        getAllReminder();
                        break;
                    case 1:
                        showToast(return_value[1]);
                        break;
                    case 2:
                        showToast(return_value[2]);
                        break;
                    case 3:
                        count ++;
                        if (count == 2){
                            progressDialog.dismiss();
                            showToast(return_value[0]);
                            Log.e("WOCAO", String.valueOf(CurrentUser.getInstance().getChannels().size()));
                            Log.e("WOCAO", String.valueOf(CurrentUser.getInstance().getReminders().size()));
                            startActivity(new Intent(getApplicationContext(),MainActivity.class));
                            finish();
                            break;
                        }
                        break;
                }
            }
        };
    }

    void setResponse() {
        btn_login.setOnClickListener(this);
        text_create_account.setOnClickListener(this);
    }

    boolean valid() {
        String username = edit_id.getText().toString();
        String password = edit_password.getText().toString();
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
        return !username.isEmpty() && !password.isEmpty();
    }

    void showToast(String content) {
        Toast.makeText(this, content, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
                login();
                break;
            case R.id.text_create_account:
                startActivity(new Intent(this, SignUpActivity.class));
                finish();
                break;
        }
    }


    void login(){
        if (valid()) {
            progressDialog.show();
            OkHttpClient mOkHttpClient = new OkHttpClient();
            RequestBody formBody = new FormBody.Builder()
                    .add("username", edit_id.getText().toString())
                    .add("password", edit_password.getText().toString())
                    .build();
            Request request = new Request.Builder()
                    .url("http://api.sysu.space/api/user/login")
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
                        if (jsonObject.getInt("ret") == StateCode.USER_NAME_AND_PASSWORD_MISSMATCH) {
                            result = 1;
                        } else if (jsonObject.getInt("ret") == StateCode.OK) {
                            result = 0;
                            CurrentUser.getInstance().setToken(jsonObject.getString("token"));
                            CurrentUser.getInstance().setUser(new Gson().fromJson(jsonObject.getString("user"), User.class));
                            Log.e("WOCAO", CurrentUser.getInstance().getUser().getUsername());
                            Log.e("WOCAO", CurrentUser.getInstance().getToken());
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    handler.sendEmptyMessage(result);
                }

            });
        }
    }

    void getAllChannel(){
        OkHttpClient mOkHttpClient = new OkHttpClient();
        HttpUrl.Builder url_builder = HttpUrl.parse("http://api.sysu.space/api/channel").newBuilder();
        url_builder.addEncodedQueryParameter("token",CurrentUser.getInstance().getToken());
        Request request = new Request.Builder()
                .url(url_builder.build())
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
                    if (jsonObject.getInt("ret") == StateCode.TOKEN_INVALID) {
                        result = 1;
                    } else if (jsonObject.getInt("ret") == StateCode.OK) {
                        result = 3;
                        CurrentUser.getInstance().setChannels((List<Channel>) new Gson().fromJson(jsonObject.getString("channels"),new TypeToken<List<Channel>>(){}.getType()));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                handler.sendEmptyMessage(result);
            }

        });
    }

    void getAllReminder(){
        OkHttpClient mOkHttpClient = new OkHttpClient();
        HttpUrl.Builder url_builder = HttpUrl.parse("http://api.sysu.space/api/reminder").newBuilder();
        url_builder.addEncodedQueryParameter("token",CurrentUser.getInstance().getToken());
        Request request = new Request.Builder()
                .url(url_builder.build())
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
                    if (jsonObject.getInt("ret") == StateCode.TOKEN_INVALID) {
                        result = 1;
                    } else if (jsonObject.getInt("ret") == StateCode.OK) {
                        result = 3;
                        CurrentUser.getInstance().setReminders((List<Reminder>) new Gson().fromJson(jsonObject.getString("reminders"),new TypeToken<List<Reminder>>(){}.getType()));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                handler.sendEmptyMessage(result);
            }

        });
    }
}
