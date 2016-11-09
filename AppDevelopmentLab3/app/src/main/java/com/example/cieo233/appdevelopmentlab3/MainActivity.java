package com.example.cieo233.appdevelopmentlab3;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

class Contact implements Serializable{

    private static final long serialVersionUID = -1L;
    private boolean stared;
    private String[] contact_info;

    public Contact(String info) {
        contact_info = info.split("\t");
        stared = false;
    }

    public boolean isStared() {
        return stared;
    }

    public void setStared(boolean stared) {
        this.stared = stared;
    }

    public String getName() {
        return contact_info[0];
    }

    public String getNumber() {
        return contact_info[1];
    }

    public String getType() {
        return contact_info[2];
    }

    public String getBelong() {
        return contact_info[3];
    }

    public String getColor() {
        return contact_info[4];
    }
}

public class MainActivity extends AppCompatActivity {

    private List<Contact> contacts = new ArrayList<>();
    private ListView listView;
    private ContactAdapter mContactAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        setResponse();
    }

    void init() {
        listView = (ListView) findViewById(R.id.contactList);
        contacts.add(new Contact("Aaron\t17715523654\t手机\t江苏苏州电信\tBB4C3B"));
        contacts.add(new Contact("Elvis\t18825653224\t手机\t广东揭阳移动\tc48d30"));
        contacts.add(new Contact("David\t15052116654\t手机\t江苏无锡移动\t4469b0"));
        contacts.add(new Contact("Edwin\t18854211875\t手机\t山东青岛移动\t20A17B"));
        contacts.add(new Contact("Frank\t13955188541\t手机\t安徽合肥移动\tBB4C3B"));
        contacts.add(new Contact("Joshua\t13621574410\t手机\t江苏苏州移动\tc48d30"));
        contacts.add(new Contact("Ivan\t15684122771\t手机\t山东烟台联通\t4469b0"));
        contacts.add(new Contact("Mark\t17765213579\t手机\t广东珠海电信\t20A17B"));
        contacts.add(new Contact("Joseph\t13315466578\t手机\t河北石家庄电信\tBB4C3B"));
        contacts.add(new Contact("Phoebe\t17895466428\t手机\t山东东营移动\tc48d30"));
        mContactAdapter = new ContactAdapter(contacts, this);
        listView.setAdapter(mContactAdapter);
    }

    void setResponse() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Contact contact = (Contact) adapterView.getAdapter().getItem(i);
                Intent intent = new Intent(MainActivity.this,ContactActivity.class);
                intent.putExtra("id",i);
                intent.putExtra("contact",contact);
                startActivityForResult(intent,1);
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                showDialog1(i);
                return true;
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        int id = data.getIntExtra("id",0);
        Contact recived = (Contact) data.getSerializableExtra("contact");
        contacts.set(id,recived);
        mContactAdapter.notifyDataSetChanged();
    }

    void showDialog1(final int id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        DialogInterface.OnClickListener onClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                switch (i) {
                    case AlertDialog.BUTTON_POSITIVE:
                        contacts.remove(id);
                        mContactAdapter.notifyDataSetChanged();
                        break;
                    case AlertDialog.BUTTON_NEGATIVE:
                        break;
                }
            }
        };
        builder.setTitle("提示").setMessage("确定删除联系人" + contacts.get(id).getName() + "?").setTitle("删除联系人").setPositiveButton("确定", onClickListener).setNegativeButton("取消", onClickListener).create().show();
    }

}

