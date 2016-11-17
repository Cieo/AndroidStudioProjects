package com.example.cieo233.appdevelopmentlab8;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Cieo233 on 11/16/2016.
 */

public class SubActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText inputGift, inputBirthday, inputName;
    private Button add;
    private DatabaseHelper mDatabaseHelper;
    private final String DEBUG = "WOCAOCAOCAOCAOCAO";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub);
        init();
        setResponse();
    }

    void init() {
        inputBirthday = (EditText) findViewById(R.id.inputBirthday);
        inputGift = (EditText) findViewById(R.id.inputGift);
        inputName = (EditText) findViewById(R.id.inputName);
        add = (Button) findViewById(R.id.add);
        mDatabaseHelper = new DatabaseHelper(openOrCreateDatabase("Person", MODE_PRIVATE, null));
    }


    void setResponse() {
        add.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (inputName.getText().length() == 0){
            showToast("名字为空，请完善");
            return;
        }
        if (mDatabaseHelper.insert(inputName.getText().toString(), inputBirthday.getText().toString(), inputGift.getText().toString())) {
            setResult(1);
            this.finish();
        } else {
            showToast("名字重复啦，请核查");
        }
    }

    void showToast(String content){
        Toast.makeText(this,content,Toast.LENGTH_SHORT).show();
    }
}

class DatabaseHelper {
    private SQLiteDatabase db;

    public DatabaseHelper(SQLiteDatabase db) {
        this.db = db;
        db.execSQL("CREATE TABLE IF NOT EXISTS people(name TEXT PRIMARY KEY, birthday TEXT, gift TEXT)");
    }

    public boolean insert(String name, String birthday, String gift) {
        ContentValues cv = new ContentValues();
        cv.put("name", name);
        cv.put("birthday", birthday);
        cv.put("gift", gift);
        return db.insert("people", null, cv) != -1;
    }

    public boolean delete(String name) {
        return db.delete("people", "name=?", new String[]{name}) != 0;
    }

    public boolean update(String name, String birthday, String gift) {
        ContentValues cv = new ContentValues();
        cv.put("birthday", birthday);
        cv.put("gift", gift);
        return db.update("people", cv, "name=?", new String[]{name}) != 0;
    }

    public List<Person> search() {
        Cursor cursor = db.query("people", null, null, null, null, null, null);
        List<Person> people = new ArrayList<>();
        while (cursor.moveToNext()) {
            people.add(new Person(cursor.getString(cursor.getColumnIndex("name")), cursor.getString(cursor.getColumnIndex("birthday")), cursor.getString(cursor.getColumnIndex("gift"))));
        }
        cursor.close();
        return people;
    }

    public void close() {
        db.close();
    }
}