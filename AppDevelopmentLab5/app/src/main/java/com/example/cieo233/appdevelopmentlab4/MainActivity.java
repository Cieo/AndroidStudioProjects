package com.example.cieo233.appdevelopmentlab4;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button btnDynamic, btnStatic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
        setResponse();
    }

    void init(){
        btnDynamic = (Button) findViewById(R.id.btnDynamic);
        btnStatic = (Button) findViewById(R.id.btnStatic);
    }

    void setResponse(){
        btnDynamic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,DynamicActivity.class);
                startActivity(intent);
            }
        });
        btnStatic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,StaticActivity.class);
                startActivity(intent);
            }
        });
    }
}
