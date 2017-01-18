package com.example.cieo233.notetest;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Cieo233 on 1/19/2017.
 */

public class DrawerRecyclerViewAdapter extends RecyclerView.Adapter {

    private List<String> buttonTextList, badgeTextList;
    private Context context;

    public DrawerRecyclerViewAdapter(Context context, List<String> buttonTextList, List<String> badgeTextList) {
        this.context = context;
        this.buttonTextList = buttonTextList;
        this.badgeTextList = badgeTextList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.list_item,parent,false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MyViewHolder myViewHolder = (MyViewHolder) holder;
        myViewHolder.setBadgeText(badgeTextList.get(position));
        myViewHolder.setButtonText(buttonTextList.get(position));
    }

    @Override
    public int getItemCount() {
        return buttonTextList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        private Button button;
        private TextView badge;

        public MyViewHolder(View view) {
            super(view);
            button = (Button) view.findViewById(R.id.button);
            badge = (TextView) view.findViewById(R.id.badge);
        }

        void setButtonText(String content){
            button.setText(content);
        }

        void setBadgeText(String content){
            badge.setText(content);
        }
    }
}
