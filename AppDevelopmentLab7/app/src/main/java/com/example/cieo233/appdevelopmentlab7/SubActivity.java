package com.example.cieo233.appdevelopmentlab7;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Objects;

public class SubActivity extends AppCompatActivity implements View.OnClickListener {
    private File file;
    private Button save, load, clear;
    private EditText content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub);
        init();
        setResponse();
    }

    void init() {
        save = (Button) findViewById(R.id.save);
        load = (Button) findViewById(R.id.load);
        clear = (Button) findViewById(R.id.clear);
        content = (EditText) findViewById(R.id.content);
    }

    void setResponse() {
        save.setOnClickListener(this);
        load.setOnClickListener(this);
        clear.setOnClickListener(this);
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(Intent.ACTION_MAIN);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addCategory(Intent.CATEGORY_HOME);
        startActivity(i);
    }

    @Override
    public void onClick(View view) {
        file = new File("/storage/emulated/0/appDevelopment/file1.txt");
        try {
            switch (view.getId()) {
                case R.id.save:
                    if (!file.exists()) {
                        file.createNewFile();
                    }
                    FileWriter fileWriter = new FileWriter(file);
                    BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
                    bufferedWriter.write(content.getText().toString());
                    bufferedWriter.close();
                    fileWriter.close();
                    break;
                case R.id.load:
                    if (file.exists()) {
                        content.setText("");
                        FileReader fileReader = new FileReader(file);
                        BufferedReader bufferedReader = new BufferedReader(fileReader);
                        String temp;
                        while ((temp = bufferedReader.readLine()) != null) {
                            content.append(temp + "\n");
                        }
                        if (!content.getText().toString().isEmpty()){
                            content.getText().delete(content.length()-1,content.length());
                        }
                        bufferedReader.close();
                        fileReader.close();

                    } else {
                        showToast("Fail to load file");
                    }
                    break;
                case R.id.clear:
                    content.setText("");
                    break;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    void showToast(String content) {
        Toast.makeText(this, content, Toast.LENGTH_SHORT).show();
    }
}
