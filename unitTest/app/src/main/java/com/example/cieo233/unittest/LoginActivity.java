package com.example.cieo233.unittest;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Cieo233 on 12/3/2016.
 */

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    @BindView(R.id.login_title)
    TextView login_title;
    @BindView(R.id.edit_id)
    MaterialEditText editId;
    @BindView(R.id.edit_password)
    MaterialEditText editPassword;
    @BindView(R.id.btn_login)
    Button btn_login;
    @BindView(R.id.text_create_account)
    TextView text_create_account;
    private Handler loginHandler, channelHandler, reminderHandler;
    private int result;
    private String username, password;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private ProgressDialog loginDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        init();
        checkToken();
        setResponse();
    }

    void onPreloadDone() {
        int DONE = 4;
        if (result == DONE) {
            result = 0;
            loginDialog.dismiss();
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
    }

    void init() {
        sharedPreferences = getSharedPreferences("currentUser", 0);
        editor = sharedPreferences.edit();
        loginDialog = new ProgressDialog(this);
        loginDialog.setMessage("Logining");
        result = 0;
        loginHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message message) {
                switch (message.what) {
                    case StateCode.USER_NAME_AND_PASSWORD_MISSMATCH:
                        Log.e("Login", "密码错误");
                        loginDialog.dismiss();
                        break;
                    case StateCode.OK:
                        Log.e("Login", "登陆成功");
                        editor.putString("password", CurrentUser.getInstance().getUser().getPassword());
                        editor.putString("token", CurrentUser.getInstance().getUser().getToken());
                        editor.putString("username", CurrentUser.getInstance().getUser().getUsername());
                        editor.putInt("id", CurrentUser.getInstance().getUser().getId());
                        editor.apply();
                        CodoAPI.getChannels(channelHandler);
                        CodoAPI.getReminders(reminderHandler);
                        break;
                }
                return false;
            }
        });
        channelHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message message) {
                switch (message.what) {
                    case StateCode.TOKEN_INVALID:
                        Log.e("GetChannel", "获取频道失败");
                        loginDialog.dismiss();
                        break;
                    case StateCode.OK:
                        Log.e("GetChannel", "获取频道成功");
                        result += 1;
                        onPreloadDone();
                        break;
                }
                return false;
            }
        });
        reminderHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message message) {
                switch (message.what) {
                    case StateCode.TOKEN_INVALID:
                        Log.e("GetReminder", "获取备忘录失败");
                        loginDialog.dismiss();
                        break;
                    case StateCode.OK:
                        Log.e("GetReminder", "获取备忘录成功");
                        result += 1;
                        onPreloadDone();
                        break;
                }
                return false;
            }
        });
    }

    void checkToken() {
        Log.e("CheckToken", "Inside CheckToken");

        Gson gson = new Gson();
        CurrentUser.getInstance().setUser(gson.fromJson(sharedPreferences.getString("user", ""), User.class));
        CurrentUser.getInstance().setReminders((List<Reminder>) gson.fromJson(sharedPreferences.getString("reminders", ""), new TypeToken<List<Reminder>>() {
        }.getType()));
        CurrentUser.getInstance().setSubscribeChannels((List<Channel>) gson.fromJson(sharedPreferences.getString("subscribeChannel", ""), new TypeToken<List<Channel>>() {
        }.getType()));
        CurrentUser.getInstance().setUnsubscribeChannels((List<Channel>) gson.fromJson(sharedPreferences.getString("unsubscribeChannel", ""), new TypeToken<List<Channel>>() {
        }.getType()));
        CurrentUser.getInstance().setCreatorChannels((List<Channel>) gson.fromJson(sharedPreferences.getString("creatorChannel", ""), new TypeToken<List<Channel>>() {
        }.getType()));
        if (CurrentUser.getInstance().getUser() != null) {
            if (CurrentUser.getInstance().getUser().getToken() != null) {
                loginDialog.show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        result = 4;
                        onPreloadDone();
                    }
                }, 150);

            }
        }

        Log.e("CheckToken", "Login through Token");
    }

    void setResponse() {
        btn_login.setOnClickListener(this);
        text_create_account.setOnClickListener(this);
    }

    boolean valid() {
        username = editId.getText().toString();
        password = editPassword.getText().toString();
        if (username.isEmpty()) {
            editId.setError("用户名不能为空");
        }
        if (password.isEmpty()) {
            editPassword.setError("密码不能为空");
        }
        return !username.isEmpty() && !password.isEmpty();
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
                if (valid()) {
                    loginDialog.show();
                    Log.e("LoginButton", "Login through Button");
                    CodoAPI.userLogin(new User(username, password), loginHandler);
                }
                break;
            case R.id.text_create_account:
                startActivity(new Intent(this, RegisterActivity.class));
                break;
        }
    }


}
