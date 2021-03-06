package com.example.cieo233.appdevelopmentlab7;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Objects;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button ok, clear;
    private EditText password, confirm;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private String realPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        setResponse();
    }

//    init the variables we are going to use
    void init() {
        sharedPreferences = getSharedPreferences("lab7", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        ok = (Button) findViewById(R.id.ok);
        clear = (Button) findViewById(R.id.clear);
        password = (EditText) findViewById(R.id.password);
        confirm = (EditText) findViewById(R.id.confirm);

        realPassword = sharedPreferences.getString("realPassword", "");
        if (!realPassword.isEmpty()) {
            confirm.setVisibility(View.GONE);
            password.setHint("Input Password");
        }
    }

    void setResponse() {
        ok.setOnClickListener(this);
        clear.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ok:
//                if we click ok , check if the password is empty or different with the confirmation
                if (!realPassword.isEmpty()) {
                    if (Objects.equals(realPassword, password.getText().toString())) {
                        Intent intent = new Intent(this, SubActivity.class);
                        startActivity(intent);
                    } else {
                        showToast("Invalid Password");
                    }
                } else {
                    if (password.getText().toString().isEmpty()) {
                        showToast("Password cannot be empty");
                    } else if (!Objects.equals(password.getText().toString(), confirm.getText().toString())) {
                        showToast("Password Mismatch");
                    } else {
                        realPassword = password.getText().toString();
                        editor.putString("realPassword", realPassword).commit();
                        Intent intent = new Intent(this, SubActivity.class);
                        startActivity(intent);
                    }
                }
                break;
            case R.id.clear:
                password.setText("");
                confirm.setText("");
                break;
        }
    }

    void showToast(String content) {
        Toast.makeText(this, content, Toast.LENGTH_SHORT).show();
    }
}
