package com.example.cieo233.appdevelopmentlab8;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Cieo233 on 11/17/2016.
 */

public class PersonAdapter extends BaseAdapter {
    private List<Person> people;
    private Context context;

    public PersonAdapter(List<Person> people, Context context) {
        this.people = people;
        this.context = context;
    }

    @Override
    public int getCount() {
        return people.size();
    }

    @Override
    public Object getItem(int i) {
        return people.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    public void setPeople(List<Person> people) {
        this.people = people;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null){
            view = View.inflate(context,R.layout.item_main,null);
        }
        if (view.getTag() == null){
            view.setTag(new ViewHolder(view));
        }
        ViewHolder mViewHolder = (ViewHolder) view.getTag();
        mViewHolder.setItem(people.get(i));
        return view;
    }

    private class ViewHolder{
        private TextView name, birthday, gift;

        ViewHolder(View view) {
            name = (TextView) view.findViewById(R.id.name);
            birthday = (TextView) view.findViewById(R.id.birthday);
            gift = (TextView) view.findViewById(R.id.gift);
        }

        void setItem(Person person){
            name.setText(person.getName());
            birthday.setText(person.getBirthday());
            gift.setText(person.getGift());
        }
    }
}
