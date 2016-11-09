package com.example.cieo233.appdevelopmentlab3;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Cieo233 on 10/7/2016.
 */

public class ContactAdapter extends BaseAdapter {

    private List<Contact> contacts = new ArrayList<>();
    private Context context;

    public ContactAdapter(List<Contact> contacts, Context context) {
        this.contacts = contacts;
        this.context = context;
    }

    @Override
    public int getCount() {
        return contacts.size();
    }

    @Override
    public Object getItem(int i) {
        return contacts.get(i);
    }



    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        mViewHolder mViewHolder;
        if (view == null){
            mViewHolder = new mViewHolder();
            view = View.inflate(context,R.layout.list_item,null);
            mViewHolder.contactFirst = (TextView) view.findViewById(R.id.contactFirst);
            mViewHolder.contactName = (TextView) view.findViewById(R.id.contactName);
            view.setTag(mViewHolder);
        } else {
            mViewHolder = (ContactAdapter.mViewHolder) view.getTag();
        }

        mViewHolder.contactName.setText(contacts.get(i).getName());
        mViewHolder.contactFirst.setText(contacts.get(i).getName().substring(0,1));

        return view;
    }

    class mViewHolder{
        TextView contactFirst;
        TextView contactName;
    }
}
