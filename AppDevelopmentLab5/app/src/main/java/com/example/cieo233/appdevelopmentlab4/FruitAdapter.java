package com.example.cieo233.appdevelopmentlab4;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

/**
 * Created by Cieo233 on 10/14/2016.
 */

public class FruitAdapter extends BaseAdapter{

    List<Fruit> fruits = new ArrayList<>();
    Context context;

    public FruitAdapter(List<Fruit> fruits, Context context) {
        this.fruits = fruits;
        this.context = context;
    }

    @Override
    public int getCount() {
        return fruits.size();
    }

    @Override
    public Object getItem(int i) {
        return fruits.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        mViewHolder viewHolder;
        if (view == null){
            view = LayoutInflater.from(context).inflate(R.layout.item_static,null);
            viewHolder = new mViewHolder();
            viewHolder.fruitImage = (ImageView) view.findViewById(R.id.itemImage);
            viewHolder.fruitText = (TextView) view.findViewById(R.id.itemText);
            view.setTag(viewHolder);
        } else {
            viewHolder = (mViewHolder) view.getTag();
        }
        viewHolder.fruitImage.setImageResource(fruits.get(i).getResource());
        viewHolder.fruitText.setText(fruits.get(i).getFruitText());
        return view;
    }

    class mViewHolder{
        ImageView fruitImage;
        TextView fruitText;
    }

}
