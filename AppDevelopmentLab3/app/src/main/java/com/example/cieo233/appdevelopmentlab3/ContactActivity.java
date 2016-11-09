package com.example.cieo233.appdevelopmentlab3;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Created by Cieo233 on 10/8/2016.
 */

public class ContactActivity extends AppCompatActivity {
    private String[] operations;
    private TextView name, number, typeAndBelong, background;
    private ListView operationList;
    private Intent intent;
    private Contact contact;
    private ImageView star, back;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact_detial);
        init();
        setContent();
        this.setResult(1,intent);
    }

    void init() {
        intent = getIntent();
        background = (TextView) findViewById(R.id.background);
        contact = (Contact) intent.getSerializableExtra("contact");
        name = (TextView) findViewById(R.id.detial_name);
        number = (TextView) findViewById(R.id.number);
        typeAndBelong = (TextView) findViewById(R.id.typeAndBelong);
        star = (ImageView) findViewById(R.id.star);
        back = (ImageView) findViewById(R.id.back);
        operationList = (ListView) findViewById(R.id.contactOperation);
        operations = new String[]{"编辑联系人", "分享联系人", "加入黑名单", "删除联系人"};
    }

    void setContent() {
        name.setText(contact.getName());
        number.setText(contact.getNumber());
        background.setBackgroundColor(Color.parseColor("#"+contact.getColor()));
        typeAndBelong.setText(contact.getType() + " " + contact.getBelong());
        operationList.setAdapter(new OperationAdapter(operations, this));
        if (contact.isStared()) {
            star.setImageResource(R.mipmap.full_star);
        } else {
            star.setImageResource(R.mipmap.empty_star);
        }
        star.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                contact.setStared(!contact.isStared());
                if (contact.isStared()) {
                    star.setImageResource(R.mipmap.full_star);
                } else {
                    star.setImageResource(R.mipmap.empty_star);
                }
                intent.putExtra("contact",contact);

            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

}
