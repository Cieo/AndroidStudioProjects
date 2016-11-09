package com.example.cieo233.appdevelopmentlab4;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Cieo233 on 10/14/2016.
 */

class Fruit{
    private String fruitText;
    private int resource;

    public Fruit(String fruitText, int resource) {
        this.fruitText = fruitText;
        this.resource = resource;
    }

    public String getFruitText() {
        return fruitText;
    }

    public int getResource() {
        return resource;
    }
}

public class StaticActivity extends AppCompatActivity {

    private final String ACTION = "com.cieo233.lab4.STATICACTION";
    private ListView fruitList;
    List<Fruit> fruits = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_static);
        init();
        setReponse();
    }

    void init(){
        fruitList = (ListView) findViewById(R.id.staticList);
        fruits.add(new Fruit("Apple",R.mipmap.apple));
        fruits.add(new Fruit("Banana",R.mipmap.banana));
        fruits.add(new Fruit("Cherry",R.mipmap.cherry));
        fruits.add(new Fruit("Coco",R.mipmap.coco));
        fruits.add(new Fruit("Kiwi",R.mipmap.kiwi));
        fruits.add(new Fruit("Orange",R.mipmap.orange));
        fruitList.setAdapter(new FruitAdapter(fruits,this));
    }

    void setReponse(){
        fruitList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Fruit selectedFruit  = (Fruit) adapterView.getAdapter().getItem(i);
                Intent intent = new Intent();
                intent.putExtra("fruitText",selectedFruit.getFruitText());
                intent.putExtra("resource",selectedFruit.getResource());
                intent.setAction(ACTION);
                sendBroadcast(intent);
            }
        });
    }

}
