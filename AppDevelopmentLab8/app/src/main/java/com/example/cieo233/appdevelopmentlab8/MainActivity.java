package com.example.cieo233.appdevelopmentlab8;

import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private DatabaseHelper mDatabaseHelper;
    private ListView peopleList;
    private Button addItem;
    private List<Person> people;
    private PersonAdapter personAdapter;
    private ContentResolver contentResolver;
    private final String DEBUG = "WOCAOCAOCAOCAO";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        setResponse();
    }

    void init() {
        peopleList = (ListView) findViewById(R.id.peopleList);
        addItem = (Button) findViewById(R.id.addItem);
        mDatabaseHelper = new DatabaseHelper(openOrCreateDatabase("Person", MODE_PRIVATE, null));
        people = mDatabaseHelper.search();
        personAdapter = new PersonAdapter(people, this);
        peopleList.setAdapter(personAdapter);
        contentResolver = getContentResolver();
    }

    void setResponse() {
        addItem.setOnClickListener(this);
        peopleList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                showDetial((Person) personAdapter.getItem(i));
            }
        });
        peopleList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                showDelete((Person) personAdapter.getItem(i));
                return true;
            }
        });
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.addItem:
                Intent intent = new Intent(this, SubActivity.class);
                startActivityForResult(intent, 0);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == 1) {
            people = mDatabaseHelper.search();
            personAdapter.setPeople(people);
            personAdapter.notifyDataSetChanged();
        }
    }

    String getPhone(String name) {
        Cursor cursorPhone = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER}, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + "=?", new String[]{name}, null);
        if (cursorPhone.moveToNext()) {
            String temp = cursorPhone.getString(0);
            cursorPhone.close();
            return temp;
        }
        cursorPhone.close();
        return "";
    }

    void showDetial(final Person person) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("o(*￣▽￣*)ブ");
        LinearLayout customDialog = (LinearLayout) getLayoutInflater().inflate(R.layout.dialog_main, null);
        TextView dialogName = (TextView) customDialog.findViewById(R.id.dialogName);
        final EditText dialogBirthday = (EditText) customDialog.findViewById(R.id.dialogBirthday);
        final EditText dialogGift = (EditText) customDialog.findViewById(R.id.dialogGift);
        TextView dialogPhone = (TextView) customDialog.findViewById(R.id.dialogPhone);
        dialogName.setText(person.getName());
        dialogBirthday.setText(person.getBirthday());
        dialogGift.setText(person.getGift());
        dialogPhone.setText(getPhone(person.getName()));
        builder.setView(customDialog);
        builder.setNegativeButton("放弃修改", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.setPositiveButton("保存修改", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mDatabaseHelper.update(person.getName(), dialogBirthday.getText().toString(), dialogGift.getText().toString());
                people = mDatabaseHelper.search();
                personAdapter.setPeople(people);
                personAdapter.notifyDataSetChanged();
            }
        });
        builder.create().show();
    }

    void showDelete(final Person person) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("是否删除?");
        builder.setNegativeButton("否", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mDatabaseHelper.delete(person.getName());
                people = mDatabaseHelper.search();
                personAdapter.setPeople(people);
                personAdapter.notifyDataSetChanged();
            }
        });
        builder.create().show();
    }
}

class Person {

    private String name, gift, birthday;

    public Person(String name, String birthday, String gift) {
        this.name = name;
        this.gift = gift;
        this.birthday = birthday;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setGift(String gift) {
        this.gift = gift;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getName() {
        return name;
    }

    public String getGift() {
        return gift;
    }

    public String getBirthday() {
        return birthday;
    }
}