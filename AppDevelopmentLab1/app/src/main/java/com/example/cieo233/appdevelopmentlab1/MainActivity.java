package com.example.cieo233.appdevelopmentlab1;

import android.content.DialogInterface;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.Objects;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnFocusChangeListener {
    private EditText inputUsername, inputPassword;
    private RadioGroup radioGroup;
    private Button btnLogin, btnRegister;
    private String user, userName, password;
    private TextInputLayout userNameLayout, passwordLayout;
    private View rootView;
    private final String USERNAME = "Android", PASSWORD = "123456";
    private Toast toastUnit;
    private boolean isFirstTimeU, isFirstTimeP;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        setResponse();
    }

    void init() {
        inputPassword = (EditText) findViewById(R.id.inputPassword);
        inputUsername = (EditText) findViewById(R.id.inputUsername);
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnRegister = (Button) findViewById(R.id.btnRegister);
        rootView = findViewById(R.id.activity_main);
        userNameLayout = (TextInputLayout) findViewById(R.id.inputUsernameLayout);
        passwordLayout = (TextInputLayout) findViewById(R.id.inputPasswordLayout);
        user = "Student";
        userName = "";
        password = "";
        toastUnit = null;
        isFirstTimeP = true;
        isFirstTimeU = true;
    }

    void setResponse() {
        btnLogin.setOnClickListener(this);
        btnRegister.setOnClickListener(this);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.tobeCheck0:
                        user = "Student";
                        showToast("学生身份被选中");
                        showSnackbar("学生身份被选中");
                        break;
                    case R.id.tobeCheck1:
                        user = "Teacher";
                        showToast("教师身份被选中");
                        showSnackbar("教师身份被选中");
                        break;
                    case R.id.tobeCheck2:
                        user = "Club";
                        showToast("社团身份被选中");
                        showSnackbar("社团身份被选中");
                        break;
                    case R.id.tobeCheck3:
                        user = "Administrator";
                        showToast("管理者身份被选中");
                        showSnackbar("管理者身份被选中");
                        break;
                }
            }
        });
        inputUsername.setOnFocusChangeListener(this);
        inputPassword.setOnFocusChangeListener(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onClick(View view) {
        userName = inputUsername.getText().toString();
        password = inputPassword.getText().toString();
        inputPassword.clearFocus();
        inputUsername.clearFocus();
        switch (view.getId()) {
            case R.id.btnLogin:
                if (Objects.equals(userName, "")) {
                    showToast("用户名不能为空");
                    showSnackbar("用户名不能为空");
                } else if (Objects.equals(password, "")) {
                    showToast("密码不能为空");
                    showSnackbar("密码不能为空");
                } else if (Objects.equals(userName, USERNAME) && Objects.equals(password, PASSWORD)) {
                    showDialog("登陆成功");
                } else {
                    showDialog("登录失败");
                }
                break;
            case R.id.btnRegister:
                switch (user) {
                    case "Student":
                        showToast("学生身份注册功能未开启");
                        showSnackbar("学生身份注册功能未开启");
                        break;
                    case "Teacher":
                        showToast("教师身份注册功能未开启");
                        showSnackbar("教师身份注册功能未开启");
                        break;
                    case "Administrator":
                        showToast("社团身份注册功能未开启");
                        showSnackbar("社团身份注册功能未开启");
                        break;
                    case "Club":
                        showToast("管理者身份注册功能未开启");
                        showSnackbar("管理者身份注册功能未开启");
                        break;
                }
                break;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onFocusChange(View view, boolean b) {
        userName = inputUsername.getText().toString();
        password = inputPassword.getText().toString();
        switch (view.getId()){
            case R.id.inputUsername:
                if (Objects.equals(userName, "") && !isFirstTimeU){
                    userNameLayout.setError("用户名不能为空");
                }else {
                    userNameLayout.setErrorEnabled(false);
                }
                isFirstTimeU = false;
                break;
            case R.id.inputPassword:
                if (Objects.equals(password, "") && !isFirstTimeP){
                    passwordLayout.setError("密码不能为空");
                }else {
                    passwordLayout.setErrorEnabled(false);
                }
                isFirstTimeP = false;
                break;
        }
    }

    void showDialog(String content) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        DialogInterface.OnClickListener onClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                switch (i) {
                    case AlertDialog.BUTTON_POSITIVE:
                        showToast("对话框“确定”按钮被点击");
                        showSnackbar("对话框“确定”按钮被点击");
                        break;
                    case AlertDialog.BUTTON_NEGATIVE:
                        showToast("对话框“取消”按钮被点击");
                        showSnackbar("对话框“取消”按钮被点击");
                        break;
                }
            }
        };

        builder.setTitle("提示").setMessage(content).setPositiveButton("确定", onClickListener).setNegativeButton("取消", onClickListener).create().show();
    }

    void showToast(String content) {
        if (toastUnit != null) {
            toastUnit.cancel();
        }
        toastUnit = Toast.makeText(this, content, Toast.LENGTH_SHORT);
        toastUnit.show();
    }

    void showSnackbar(String content) {
        Snackbar.make(rootView, content, Snackbar.LENGTH_SHORT).setAction("确定", new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        }).show();
    }
}
